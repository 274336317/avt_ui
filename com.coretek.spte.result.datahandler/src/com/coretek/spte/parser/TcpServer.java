package com.coretek.spte.parser;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * TCPSocket通信
 * 
 * @author Sim.Wang 2012-3-20
 */
public class TcpServer implements IServer
{
	private final static Logger	logger		= LoggingPlugin.getLogger(TcpServer.class);

	private ServerSocket		serverSocket;

	private Socket				socket;

	private DataInputStream		dataInput;

	private BufferedInputStream	bufferedInput;

	private volatile boolean	finished	= false;									// 用于标识服务器是否被关闭

	/**
	 * 创建Socket连接
	 * 
	 * @param port
	 * @return
	 */
	public boolean start(int port, int cacheSize)
	{
		try
		{
			serverSocket = new ServerSocket(port);
		} catch (UnknownHostException e)
		{
			LoggingPlugin.logException(logger, e);
			return false;
		} catch (IOException e)
		{
			LoggingPlugin.logException(logger, e);
			return false;
		}
		new Thread(new SocketListener(cacheSize), "Server Listenning Thread").start();

		logger.info(new StringBuilder("成功启动SocketServer, 监听端口").append(port).append("，等待执行器连接...").toString());
		return true;
	}

	@Override
	public boolean canRead()
	{
		return (bufferedInput != null);
	}

	@Override
	public int read(byte[] buffer, int size)
	{
		if (dataInput == null)
			return 0;
		int counter = 0;
		int start = 0;// 已经读取的数量
		int remain = size;// 剩余的未读数量
		while (size != counter)
		{
			try
			{
				if (this.finished)
					return -1;
				int length = this.bufferedInput.read(buffer, start, remain);
				if (length == -1)
				{
					logger.info("执行器输出流返回 EOF, 读取操作将退出！ ");
					break;
				}
				counter = counter + length;
				start = counter;
				remain = size - start;
			} catch (IOException e)
			{
				LoggingPlugin.logException(logger, e);
				return -1;
			}
		}
		logger.config(new StringBuilder("从执行器输出流中读取长度").append(counter).toString());
		if (buffer[0] == 0 && buffer[1] == 0 && buffer[2] == 0 && buffer[3] == 0)
		{
			logger.info("TcpServer从执行器输出流中接收到推出命令。");
			this.finished = true;
		}
		if (size == counter)
		{
			return size;
		}

		return -1;
	}

	/**
	 * 监听客户端连接
	 * 
	 * @author SunDawei 2012-5-29
	 */
	private class SocketListener implements Runnable
	{

		private int	cacheSize;

		public SocketListener(int cacheSize)
		{
			this.cacheSize = cacheSize;
		}

		public void run()
		{
			try
			{
				socket = serverSocket.accept();
				socket.setReceiveBufferSize(this.cacheSize);
				dataInput = new DataInputStream(socket.getInputStream());
				bufferedInput = new BufferedInputStream(dataInput, MessageParser.MSG_LEN);
				logger.info("与执行器建立连接。");
			} catch (IOException e)
			{
				LoggingPlugin.logException(logger, e);
				logger.severe("Exception was thrown, Can not open Socket。");
			}
		}

	}

	@Override
	public void shutDown()
	{
		this.finished = true;
		try
		{
			if (socket != null)
			{
				socket.close();
				socket = null;
			}
			if (bufferedInput != null)
			{
				bufferedInput.close();
				bufferedInput = null;
				dataInput.close();
				dataInput = null;
			}
			if (serverSocket != null)
			{
				serverSocket.close();
				serverSocket = null;
			}
		} catch (IOException e)
		{
			LoggingPlugin.logException(logger, e);
		}
		logger.info("关闭了SocketServer!");

	}
}