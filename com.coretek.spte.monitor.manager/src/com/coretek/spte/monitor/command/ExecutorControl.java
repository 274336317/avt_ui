/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.MethodResult;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.ResultHandlerThread;

/**
 * ��������������������������ִ�����ķ�����Ϣ��
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class ExecutorControl
{
	private static final Logger						logger		= LoggingPlugin.getLogger(ExecutorControl.class.getName());

	// ���ڱ�ʶ�û��Ƿ�������
	private volatile boolean						shutDown;

	// ���������߳�
	private TxThread								txThread;

	// ��ִ��������߳�
	private RxThread								rxThread;

	// ִ��������
	private Process									process;

	// �������
	private final BlockingQueue<AbstractCommand>	fTxCommands	= new LinkedBlockingQueue<AbstractCommand>();

	// �����
	private CommandCache							commandCache;

	private ExecutorService							executorService;

	public ExecutorControl()
	{
		this.commandCache = new CommandCache();
		this.shutDown = false;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	/**
	 * ���������Ͷ����С�������blockΪtrueʱ���˺�����ͬ��ִ������ֱ�����յ�����ִ�еķ��ؽṹ�������ʱ��
	 * 
	 * @param command ����
	 * @param block �Ƿ���Ҫ�ȴ�ִ��������ִ�н��
	 * @return </br>
	 * @throws TimeoutException ��block������Ϊtrueʱ�����������10��֮�ڶ�û�б�ִ������׳����쳣
	 */
	public MethodResult addCommand(AbstractCommand command, boolean block) throws TimeoutException
	{
		if (block)
		{
			logger.info(StringUtils.concat("The execution of command [", command.getCommand(), "] will be blocked until the result will be returned."));
			final CommandDecorator cd = this.commandCache.addCommand(command);
			this.fTxCommands.add(cd);
			FutureTask<CommandDecorator> ft = new FutureTask<CommandDecorator>(new Callable<CommandDecorator>()
			{

				@Override
				public CommandDecorator call() throws Exception
				{
					while (!cd.isExecuted())
						try
						{
							Thread.sleep(1000);
						} catch (InterruptedException e)
						{
							LoggingPlugin.logException(logger, e);
						}

					return cd;
				}

			});

			Future<?> future = this.executorService.submit(ft);
			try
			{
				future.get(10000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e)
			{
				LoggingPlugin.logException(logger, e);
				return MethodResult.getFailedInstance(e);
			} catch (ExecutionException e)
			{
				LoggingPlugin.logException(logger, e);
				throw new TimeoutException(StringUtils.concat("After 10 seconds, The system still not get the execution result. command=", command.getCommand()));
			} finally
			{
				if (future != null)
					future.cancel(true);
			}

			if (cd.isExecuted())
			{
				if (!cd.isSucceed())
				{
					// ����ִ�г���
					return MethodResult.getSuccInstance(false);
				} else
				{
					// ����ִ�гɹ�
					return MethodResult.getSuccInstance(true);
				}
			} else
			{
				// ����û�б�ִ��
				return MethodResult.getFailedInstance(false);
			}

		} else if (this.fTxCommands.add(this.commandCache.addCommand(command)))
		{

			return MethodResult.getSuccInstance(true);
		} else
		{

			return MethodResult.getFailedInstance(false);
		}

	}

	/**
	 * ����ִ��������
	 * 
	 * @param cmd ��������
	 * @return �����ɹ��򷵻�true�����򷵻�false
	 */
	public boolean startExcutorProcess(String cmd)
	{
		try
		{
			this.process = Runtime.getRuntime().exec(cmd);
			if (this.process == null)
			{
				logger.severe(StringUtils.concat("Launching executor failed.cmd=", cmd));
				return false;
			}
			logger.info("Launching the exector process successfully.");
			this.txThread = new TxThread(this.process.getOutputStream());
			this.rxThread = new RxThread(this.process.getInputStream());
			this.txThread.start();
			logger.info("Launched Sending thread...");
			this.rxThread.start();
			logger.info("Launched reading thread....");
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			return false;
		}

		return true;
	}

	/**
	 * �ر�ִ���� </br>
	 */
	public void shutDown()
	{
		this.shutDown = true;
		this.fTxCommands.add(new EndCommand());
		logger.info("The user turned off executor.");
	}

	/**
	 * ֱ�ӽ���ִ��������
	 * 
	 * @return </br>
	 */
	public void killExecutor()
	{
		try
		{
			this.process.getInputStream().close();
			this.process.getOutputStream().close();
		} catch (IOException e)
		{
			LoggingPlugin.logException(logger, e);
		}

		this.process.destroy();
		this.process = null;
		this.shutDown = true;
		logger.warning("Terminated the executor process forcely...");
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return the commandCache <br/>
	 * 
	 */
	public CommandCache getCommandCache()
	{
		return commandCache;
	}

	/**
	 * ���������߳�
	 * 
	 * @author ���Ρ
	 * @date 2012-1-14
	 */
	private class TxThread extends Thread
	{
		private OutputStream		out;

		private OutputStreamWriter	osw;

		private PrintWriter			writer;

		public TxThread(OutputStream out)
		{
			this.out = out;
			this.osw = new OutputStreamWriter(this.out);
			this.writer = new PrintWriter(this.osw);
			this.setName("Sending command thread");

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run() 2012-1-13
		 */
		@Override
		public void run()
		{
			while (!ExecutorControl.this.shutDown)
			{
				try
				{
					AbstractCommand command = ExecutorControl.this.fTxCommands.take();
					if (command instanceof EndCommand)
					{
						logger.info("Received the 'end' signal from system, the sending command thread will exit.");
						break;
					}
					this.writer.println(command.getCommand());
					this.writer.flush();
					logger.info(StringUtils.concat("Send>>", command.getCommand()));
				} catch (InterruptedException e)
				{
					LoggingPlugin.logException(logger, e);
					break;
				}
			}
			this.writer.close();
			try
			{
				this.osw.close();
				this.out.close();
			} catch (IOException e)
			{
				LoggingPlugin.logException(logger, e);
			}
			logger.info("Sending thread exited.");
		}

	}

	/**
	 * ���ڸ��߷����߳��˳�ִ��
	 * 
	 * @author ���Ρ
	 * @date 2012-2-8
	 */
	private static class EndCommand extends AbstractCommand
	{
		/**
		 * @param command
		 */
		public EndCommand()
		{
			super("end");
		}
	}

	/**
	 * ��ȡ����߳�
	 * 
	 * @author ���Ρ
	 * @date 2012-1-14
	 */
	private class RxThread extends Thread
	{
		private InputStream			input;

		private InputStreamReader	isr;

		private BufferedReader		reader;

		public RxThread(InputStream input)
		{
			this.input = input;
			this.isr = new InputStreamReader(this.input);
			this.reader = new BufferedReader(this.isr);
			this.setName("Reading thread");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run() 2012-1-13
		 */
		@Override
		public void run()
		{
			String line;
			try
			{
				while (!ExecutorControl.this.shutDown)
				{
					while ((line = this.reader.readLine()) != null)
					{
						logger.info(StringUtils.concat("Read>>", line));
						executorService.execute(new ResultHandlerThread(line));
					}
				}
				logger.info("The user shutted down the monitor, the reading thread will exit.");
				this.reader.close();
				this.isr.close();
				this.input.close();
				if (process != null)
				{
					process.destroy();
					logger.info("The executor process was destroyed.");
				}

			} catch (IOException e)
			{
				LoggingPlugin.logException(logger, e);
			} finally
			{
				// �ر��߳�ִ�з���
				executorService.shutdown();
			}
		}
	}

}