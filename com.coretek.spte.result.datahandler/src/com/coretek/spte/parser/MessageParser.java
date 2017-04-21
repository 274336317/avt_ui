package com.coretek.spte.parser;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.utils.StringUtils;

/**
 * 消息解析器，将消息从二进制数据根据消息编码规则文件(ICD)解码后写入消息流中 在使用该类时需要先设置输入、输出流，以及消息编码规则文件。调用过程如下:
 * setInDataStream(istream); setOutDataStream(ostream); start();
 * 
 * @author Administrator
 * 
 */
public class MessageParser implements IMessageParser
{

	/** 消息传输过程中的长度, 消息在解码前以固定长度的字节流进行传输 */
	public static final int							MSG_LEN			= 2 * 1024;

	private static final Logger						logger			= LoggingPlugin.getLogger(MessageParser.class.getName());

	private IStreamReader							reader			= null;

	private IMessageWriter							writer			= null;

	private IDecoder								decoder			= null;

	private Observer								observer;

	private volatile boolean						inputFinished	= false;													// 标识是否数据接收完毕

	private volatile LinkedBlockingQueue<byte[]>	blockingQueue	= new LinkedBlockingQueue<byte[]>();

	/**
	 * @param isLittle </br> true:小端 / false:大端
	 * @param observer 当收到第一条消息后，会通知观察者
	 */
	public MessageParser(boolean isLittle, Observer observer)
	{
		decoder = new BinaryMessageDecoder(isLittle);
		this.observer = observer;
	}

	@Override
	public boolean start()
	{
		if (reader == null || writer == null)
		{
			return false;
		}

		// 开启一个线程，读取接收到的数据
		new Thread(new BinaryReader(), "Reading thread").start();
		logger.info("启动读取线程...");

		BinaryDecoder decoder = new BinaryDecoder();
		decoder.addObserver(this.observer);
		// 开启一个线程，解析读取到的数据
		new Thread(decoder, "Decoding thread").start();
		logger.info("启动数据解析线程...");

		return true;
	}

	@Override
	public void setInDataStream(IStreamReader istream)
	{
		reader = istream;
	}

	@Override
	public void setOutDataStream(IMessageWriter ostream)
	{
		writer = ostream;
	}

	@Override
	public boolean isFinished()
	{
		return inputFinished && this.blockingQueue.isEmpty();
	}

	public void setIcdPath(String icdPath)
	{
		decoder.setIcdManager(TemplateEngine.getEngine().parseICD(new File(icdPath)));
	}

	public void setCasePath(String casePath)
	{
		if (StringUtils.isNotNull(casePath))
			decoder.setCaseManager(TemplateEngine.getEngine().parseCase(new File(casePath)));
	}

	/**
	 * 二进制读取器
	 * 
	 * @author 孙大巍 2012-3-20
	 */
	private class BinaryReader implements Runnable
	{

		@Override
		public void run()
		{

			while (!inputFinished)
			{
				// 如果消息输入流不可读, 则等待直到其可读状态
				if (!reader.canRead())
				{
					try
					{
						logger.info("输入流中还没有数据，BinaryReader线程等待...");
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e)
					{
						LoggingPlugin.logException(logger, e);
						logger.warning("The sleeping status of BinaryReader was interrupted, the BinaryReader will exit.");
						return;
					}
				}
				// 从输入流中读取消息内容
				byte[] data = new byte[MSG_LEN];
				int readlen = reader.read(data, MSG_LEN);

				// 返回－1表示流已经结束
				if (readlen == -1)
				{
					inputFinished = true;
					logger.info("接收到从执行器输出流返回的 -1。");
					reader.shutDown();
					return;
				}

				blockingQueue.add(data);
				// 判断是不是结束符
				if (data[0] == 0 && data[1] == 0 && data[2] == 0 && data[3] == 0)
				{
					inputFinished = true;
					logger.info("从执行器输出流中接收到推出命令。");
					reader.shutDown();
				}
			}
		}
	}

	/**
	 * 二进制解码器
	 * 
	 * @author 孙大巍 2012-3-20
	 */
	private class BinaryDecoder extends Observable implements Runnable
	{
		// 用于标识是否为接收到的第一条消息
		private volatile boolean	isFirstMsg	= true;

		@Override
		public void run()
		{
			while (!isFinished())
			{
				try
				{
					byte[] contents = blockingQueue.take();
					// 判断是不是结束符
					if (contents[0] == 0 && contents[1] == 0 && contents[2] == 0 && contents[3] == 0)
					{
						SPTEMsg msg = new SPTEMsg();
						msg.setTimeStamp(-1);
						writer.write(msg);
						logger.info("BinaryDecoder线程接收到推出命令, 线程退出！");
						break;
					}
					// 调用解码器解码后写入输出流中
					SPTEMsg msg = (SPTEMsg) decoder.decode(contents);
					writer.write(msg);
					if (this.isFirstMsg)
					{
						logger.info("从执行器接收到第一条消息！");
						// 通知观察者，已经收到第一条消息
						this.setChanged();
						this.notifyObservers();
						this.isFirstMsg = false;
						this.clearChanged();
						this.deleteObserver(observer);
					}
					logger.config(new StringBuilder("解析出来的消息:\n").append(msg.getMsg()).toString());
				} catch (InterruptedException e)
				{
					LoggingPlugin.logException(logger, e);
					logger.warning("The BlockingQueue was interruptted, the BinaryDecoder thread will exit.");
					break;
				}
			}

		}
	}
}