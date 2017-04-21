/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.MethodResult;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ExecutionPreferencePage;
import com.coretek.spte.monitor.command.CommandFactory;
import com.coretek.spte.monitor.command.ExecutorControl;
import com.coretek.spte.monitor.command.handler.DoneHandler;
import com.coretek.spte.monitor.command.handler.HandlerRegistries;
import com.coretek.spte.monitor.command.handler.InfoHandler;
import com.coretek.spte.monitor.command.handler.LogicErrorHandler;
import com.coretek.spte.monitor.command.handler.ResultHandler;
import com.coretek.spte.parser.ExecutionContext;
import com.coretek.spte.parser.IExecutorContext;

/**
 * ִ��������������ִ��������ͨ�š�
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class ExecutorManager extends Observable implements IContextManager, Observer
{
	private static final Logger	logger	= LoggingPlugin.getLogger(ExecutorManager.class.getName());

	private ExecutorControl		commandControl;

	private IExecutorContext	parserContext;

	private String				casePath;															// ���������ļ�·��

	private boolean				needCache;

	public ExecutorManager(boolean needCache)
	{
		super();
		this.commandControl = new ExecutorControl();
		// ע�ᴦ����
		ResultHandler handler = new DoneHandler(this.commandControl.getCommandCache());
		HandlerRegistries.registerHandler(handler);
		handler = new LogicErrorHandler(this.commandControl.getCommandCache());
		HandlerRegistries.registerHandler(handler);
		handler = new InfoHandler();
		HandlerRegistries.registerHandler(handler);
		this.needCache = needCache;
		logger.info(StringUtils.concat("����ִ���Ƿ���Ҫ����Ϣ��ӵ�������:", needCache));
	}

	public ExecutorManager(Observer observer, boolean needCache)
	{
		this(needCache);
		this.addObserver(observer);
	}

	/**
	 * ���ò��������ļ���·��
	 * 
	 * @param casePath
	 */
	public void setCasePath(String casePath)
	{
		this.casePath = casePath;
	}

	@Override
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{

		return this.parserContext.querySPTEMsgs(startTime, length);
	}

	@Override
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{

		return this.parserContext.getMsgLimitByMsgId(time, length, range);
	}

	@Override
	public void initParser(String icdPath, String msgDBPath)
	{
		this.parserContext = new ExecutionContext(this, needCache);
		this.parserContext.init(icdPath, msgDBPath);
		((ExecutionContext) this.parserContext).getMessageParser().setCasePath(this.casePath);
	}

	/**
	 * ��ʼ��Ϣ����
	 */
	public void startParing()
	{
		this.parserContext.startParsing();
	}

	/**
	 * �ر�ִ����
	 * 
	 * @return
	 * @throws TimeoutException
	 * @throws TimeoutException
	 */
	public boolean shutDown() throws TimeoutException
	{
		try
		{
			this.commandControl.addCommand(CommandFactory.getExitCommand(), true);
			this.commandControl.shutDown();
		} catch (TimeoutException e)
		{
			LoggingPlugin.logException(logger, e);
			this.shutDownOnError();
			throw e;
		}

		return true;
	}

	/**
	 * ��ִ�������ִ����ʱ��ֹͣ��ִ��������������̣߳�����������ִ�����Ĳ���ֹͣ��
	 */
	public void shutDownOnError()
	{
		this.commandControl.shutDown();

		// ���ô�����������ע��Ĵ�����
		HandlerRegistries.reset();
		// ��������
		this.commandControl.getCommandCache().clear();
		this.commandControl.killExecutor();
		this.parserContext.shutDown();
	}

	/**
	 * ��ѯ�����ܷ�ر�ִ������
	 * 
	 * @return ������ھͿ��Թر��򷵻�true�����򷵻�false
	 */
	public boolean canShutDown()
	{

		return this.parserContext.isFinished();
	}

	/**
	 * ����ִ������������ء������Ĳ������£�<br/>
	 * 1.����ִ�������̣�<br/>
	 * 2.���û���������<br/>
	 * 3.���ü�ض���<br/>
	 * 4.����startMonitor���<br/>
	 * 5.����run���
	 * 
	 * @param cmd ����ִ�������̵�����������
	 * @param testCase ��������
	 * @param startMsg ��ʼ��Ϣ
	 * @param endMsg ������Ϣ
	 * @param prjPath ���̾���·��
	 * @param endian ��С��
	 * @param tempDb ���ݿ�����
	 * @return
	 * @throws TimeoutException
	 */
	public boolean launchBoth(String cmd, String testCase, String startMsg, String endMsg, String prjPath, String endian, Map<String, List<String>> topicIds, String monitorNodeId) throws TimeoutException
	{
		// ����ִ��������
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// ���û�������
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand(endian, this.getPort()), true);
			if (mr.isStatus())
			{
				// ���ü�ض���
				mr = this.commandControl.addCommand(CommandFactory.getSetObjsCommand(topicIds, monitorNodeId), true);
				if (mr.isStatus())
				{
					// ����startMonitor����
					mr = this.commandControl.addCommand(CommandFactory.getStartMonitorCommand(), true);
					if (mr.isStatus())
					{
						// �������������ִ����
						mr = this.commandControl.addCommand(CommandFactory.getRunCommand(testCase, startMsg, endMsg, prjPath), false);

						return mr.isStatus();
					} else
					{
						// ��������������ʧ��:ֱ�ӽ������̡�
						this.commandControl.killExecutor();
					}
				} else
				{
					// �������ü�ض���ʧ��:ֱ�ӽ������̡�
					this.commandControl.killExecutor();
				}

			} else
			{
				// �������û�������ʧ��:ֱ�ӽ������̡�
				this.commandControl.killExecutor();
			}
		}

		return false;

	}

	/**
	 * ��������ء������Ĳ������£�<BR> 
	 * 1.����ִ�����̣߳�<BR>
	 * 2.���û���������<BR> 
	 * 3.���ü�ض���<BR> 
	 * 4.����startMonitor���
	 * 
	 * @param cmd ����ִ�������̵�����������
	 * @param endian ��С��
	 * @param tempDb ������ݿ�
	 * @param topicIds ����ż���
	 * @param monitorNodeId ��ؽڵ��
	 * @return �����ɹ��򷵻�true�����򷵻�false
	 * @throws TimeoutException ��ִ������ִ�����ʱʱ���׳����쳣
	 */
	public boolean launchMonitor(String cmd, String endian, String tempDb, Map<String, List<String>> topicIds, String monitorNodeId) throws TimeoutException
	{
		// ����ִ��������
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// ���û�������
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand(endian, this.getPort()), true);
			if (mr.isStatus())
			{
				// ���ü�ض���
				mr = this.commandControl.addCommand(CommandFactory.getSetObjsCommand(/* topicIds */null, /* monitorNodeId */""), true);
				if (mr.isStatus())
				{
					// ����startMonitor����
					this.commandControl.addCommand(CommandFactory.getStartMonitorCommand(), true);
				} else
				{
					// �������ü�ض���ʧ��:ֱ�ӽ������̡�
					this.commandControl.killExecutor();
				}
			} else
			{
				// �������û�������ʧ��:ֱ�ӽ������̡�
				this.commandControl.killExecutor();
			}
		}

		return true;
	}

	/**
	 * ��ȡ����ִ�����Ķ˿ں�
	 * 
	 * @return
	 */
	private String getPort()
	{
		String port = CfgPlugin.getDefault().getPreferenceStore().getString(ExecutionPreferencePage.PORT_NAME);
		if (StringUtils.isNull(port))
		{
			port = String.valueOf(ExecutionPreferencePage.PORT_DEFAULT_VALUE);
		}

		return port;
	}

	/**
	 * ����ִ���������ǲ�������ء������Ĳ������£�</br> 1.����ִ�����̣߳�</br> 2.���û���������</br>
	 * 3.����run���</br>
	 * 
	 * @param cmd ����ִ�������̵�����������
	 * @param testCase ��������
	 * @param startMsg ��ʼ��Ϣ
	 * @param endMsg ������Ϣ
	 * @param endian ��С��
	 * @param tempDb ���������ݿ�
	 * @return
	 * @throws TimeoutException
	 */
	public boolean launchExecutor(String cmd, String testCase, String startMsg, String endMsg, String prjPath, String endian) throws TimeoutException
	{
		// ����ִ��������
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// ���û�������
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand("little", this.getPort()), true);
			if (mr.isStatus())
			{
				// �������������ִ����
				mr = this.commandControl.addCommand(CommandFactory.getRunCommand(testCase, startMsg, endMsg, prjPath), false);
				return mr.isStatus();

			} else
			{
				// �������û�������ʧ��:ֱ�ӽ������̡�
				this.commandControl.killExecutor();
			}
		}
		return false;
	}

	/**
	 * �ı�������
	 * 
	 * @param cmd
	 * @return </br>
	 * @throws TimeoutException
	 */
	public boolean changeMonitoredTopics(Map<String, List<String>> map, String monitorNodeId) throws TimeoutException
	{
		return this.commandControl.addCommand(CommandFactory.getSetObjsCommand(map, monitorNodeId), true).isStatus();
	}

	@Override
	public void clearCache()
	{
		if (this.parserContext != null)
			this.parserContext.clearCache();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 * <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-29
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		if (o instanceof ExecutionContext)
		{
			logger.info("Received the first message from executor.");
			// ���յ���һ����Ϣ������㲥
			Event event = new Event(Event.EVENT_RECV_FIRST_MSG);
			this.setChanged();
			this.notifyObservers(event);
			this.clearChanged();
		}

	}

}