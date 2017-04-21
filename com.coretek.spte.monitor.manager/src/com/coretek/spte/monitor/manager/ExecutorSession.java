/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.Constants;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.FileUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.data.DataPlugin;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.command.handler.HandlerRegistries;

/**
 * ִ�����Ự�������ṩ���ִ̨�����Ľ����ӿڡ�ִ�����������Լ��������ڹ���ӿڡ�
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class ExecutorSession
{
	private final static Logger		logger				= LoggingPlugin.getLogger(ExecutorSession.class.getName());

	/** ���״̬ */
	public final static int			MONITROING			= 1;

	/** ����״̬ */
	public final static int			LOADING				= 2;

	/** ִ��״̬ */
	public final static int			EXECUTING			= 3;

	/** δ�н��κβ��� */
	public final static int			NONE_STATUS			= -1;

	private static ExecutorSession	executorSession;

	private static volatile boolean	isRunning			= false;													// ��ʶ����Ƿ�����

	private CfgManager				cfgManager;

	private IContextManager			excutorManager;

	private EventManager			eventManager;

	private String					msgDBPath;																		// ��Ϣ���ݿ�·��

	private String					icdPath;																		// icd�ļ��ľ���·��

	private volatile boolean		executorRunning		= false;													// ��ʶִ�����Ƿ�����

	private volatile boolean		monitorRunning		= false;													// ��ʶ�Ƿ������˼��

	private volatile boolean		needLaunchMonitor	= false;													// ��ʶ����ִ�в��������Ƿ���Ҫ�������

	private int						status				= NONE_STATUS;

	private String					endian;																			// ��С��
																													// :big��little

	private ExecutorSession(String icdPath)
	{
		this();
		this.icdPath = icdPath;
	}

	private ExecutorSession()
	{
		this.cfgManager = new CfgManager();
		this.eventManager = new EventManager();

	}

	/**
	 * ��ȡ��ǰ��״̬��1��ʾ����ִ������2��ʾ������ʷ��¼����Ϊ-1ʱ����ʾδ�н��κβ�����
	 * 
	 * @return </br>
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * ���ص�ǰ��MonitorManager�����Ƿ���Ҫ�������
	 * 
	 * @return
	 */
	public boolean isNeedLaunchMonitor()
	{
		return needLaunchMonitor;
	}

	/**
	 * ע����������¼���������
	 * 
	 * @param observer
	 */
	public void registerListener(Observer observer)
	{
		this.eventManager.addObserver(observer);
	}

	/**
	 * ע��������
	 * 
	 * @param observer </br>
	 */
	public void unRegisterListener(Observer observer)
	{
		this.eventManager.deleteObserver(observer);
	}

	/**
	 * ���¼���������Ϊ��������ӵ�observable�ļ�����������
	 * 
	 * @param observable </br>
	 */
	public void addToEventManager(Observable observable)
	{
		observable.addObserver(this.eventManager);
	}

	/**
	 * ע��MonitorManager����ͬʱ����Ϣ������ա����¼���������� 2012-3-13
	 */
	public synchronized static void dispose()
	{
		if (executorSession == null)
		{
			return;
		}

		executorSession.eventManager.deleteObservers();
		executorSession.excutorManager.clearCache();
		executorSession = null;
		HandlerRegistries.reset();
		System.gc();
		logger.info("Destroyed MonitorManager object.");
	}

	/**
	 * ��������״̬
	 */
	public static void reset()
	{
		isRunning = false;
	}

	/**
	 * ��������ػ�ʼִ��ʱ������Ҫ���ô˺����Գ�ʼ��MonitorManager���� 
	 */
	private void init()
	{
		// ������Ϣ���ݿ⵽.metadata/db�ļ�������
		String path = EclipseUtils.getWorkspacePath().toOSString();
		String uuid = UUID.randomUUID().toString();
		// ��Ϣ���ݿ�
		String msgDBName = new StringBuilder(uuid).append("_msg").append(".db").toString();
		StringBuilder sb = new StringBuilder();
		sb.append(EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath());
		sb.append(File.separator).append("db").append(File.separator).append("msglog.db");
		String srcPath = sb.toString();

		sb = new StringBuilder();
		sb.append(path).append(File.separator).append(".metadata").append(File.separator);
		sb.append("db");
		this.msgDBPath = sb.toString();
		File dbFolder = new File(this.msgDBPath);
		if (!dbFolder.exists())
		{
			if (!dbFolder.mkdirs())
			{
				logger.severe("Can not create " + this.msgDBPath);
				return;
			}
		}

		this.msgDBPath = new StringBuilder(this.msgDBPath).append(File.separator).append(msgDBName).toString();
		this.copyDB(new File(srcPath), new File(msgDBPath));
	}

	/**
	 * ��ȡ�û����õļ����Ϣ
	 * 
	 * @param monitoredMsgs
	 * @return
	 */
	private Map<String, List<String>> getMonitorInfo(List<SPTEMsg> monitoredMsgs)
	{
		Map<String, List<String>> topicAndNodes = new HashMap<String, List<String>>();
		for (SPTEMsg spteMsg : monitoredMsgs)
		{
			String topicId = spteMsg.getICDMsg().getAttribute(Constants.ICD_TOPIC_ID).getValue().toString();
			String sourceId = spteMsg.getICDMsg().getAttribute("sourceFunctionID").getValue().toString();
			List<String> nodeIds = topicAndNodes.get(topicId);
			if (nodeIds != null)
			{
				if (nodeIds.indexOf(sourceId) == -1)
				{
					nodeIds.add(sourceId);
				}
			} else
			{
				List<String> nodeIds2 = new ArrayList<String>();
				nodeIds2.add(sourceId);
				topicAndNodes.put(topicId, nodeIds2);
			}
		}
		return topicAndNodes;

	}

	/**
	 * ����ִ������������ء�
	 * 
	 * @param cmd ����ִ�������̵�����������
	 * @param testCase ��������
	 * @param startMsg ��ʼ��Ϣ
	 * @param endMsg ������Ϣ
	 * @param tempDb ���ݿ�����
	 * @param icdPath icd�ļ��ľ���·��
	 * @throws TimeoutException
	 */
	public boolean launchBoth(String cmd, String testCase, String startMsg, String endMsg, String prjPath) throws TimeoutException
	{
		this.status = MONITROING;
		Map<String, List<String>> topicAndNodes = this.getMonitorInfo(this.cfgManager.getSpteMsgs());
		((ExecutorManager) this.excutorManager).setCasePath(testCase);
		this.excutorManager.initParser(this.icdPath, this.msgDBPath);
		if (((ExecutorManager) this.excutorManager).launchBoth(cmd, testCase, startMsg, endMsg, prjPath, this.getEndian(), topicAndNodes, this.cfgManager.getMonitorNodeId()))
		{
			executorRunning = true;
			this.monitorRunning = true;
			// ��ʼ��������
			((ExecutorManager) this.excutorManager).startParing();
			// ֪ͨ�����
			this.eventManager.fireStartEvent();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}
	}

	/**
	 * ����ִ���������������
	 * 
	 * @param cmd ����ִ����������
	 * @param testCase ��������
	 * @param startMsg ��ʼ��Ϣ��UUID
	 * @param endMsg ������Ϣ��UUID
	 * @param icdPath icd�ļ��ľ���·��
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-18
	 * @throws TimeoutException
	 */
	public boolean launchExecutor(String cmd, String testCase, String startMsg, String endMsg, String prjPath) throws TimeoutException
	{
		((ExecutorManager) this.excutorManager).setCasePath(testCase);
		this.status = EXECUTING;
		this.excutorManager.initParser(this.icdPath, this.msgDBPath);

		if (((ExecutorManager) this.excutorManager).launchExecutor(cmd, testCase, startMsg, endMsg, prjPath, this.getEndian()))
		{
			executorRunning = true;
			// ��ʼ��������
			((ExecutorManager) this.excutorManager).startParing();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}

	}

	/**
	 * �������
	 * 
	 * @param cmd ����ִ����������
	 * @param tempDb ���ݿ�
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-4
	 * @throws TimeoutException
	 */
	public boolean launchMonitor(String cmd, String tempDb) throws TimeoutException
	{
		this.status = MONITROING;
		Map<String, List<String>> topicAndNodes = this.getMonitorInfo(this.cfgManager.getSpteMsgs());

		this.excutorManager.initParser(this.icdPath, this.msgDBPath);
		if (((ExecutorManager) this.excutorManager).launchMonitor(cmd, this.getEndian(), tempDb, topicAndNodes, this.cfgManager.getMonitorNodeId()))
		{
			executorRunning = true;
			this.monitorRunning = true;
			// ��ʼ��������
			((ExecutorManager) this.excutorManager).startParing();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}
	}

	/**
	 * ��ȡ��Ϣ���ݿ��·��
	 * 
	 * @return the dbPath <br/>
	 * 
	 */
	public String getMsgDBPath()
	{

		return msgDBPath;
	}

	/**
	 * ����������ʷ��¼
	 * 
	 * @param msgDBPath
	 */
	public void loadHistory(String msgDBPath)
	{
		this.status = LOADING;
		this.excutorManager = new LoadHistoryContextManager();
		this.msgDBPath = msgDBPath;
		this.excutorManager.initParser(this.icdPath, this.msgDBPath);
		this.icdPath = ((LoadHistoryContextManager) this.excutorManager).getIcdPath();
	}

	/**
	 * ��ȡICD�ļ���·��
	 * 
	 * @return
	 */
	public String getICDPath()
	{
		return this.icdPath;
	}

	/**
	 * �������ݿ��ļ���ָ����Ŀ¼��
	 * 
	 * @param srcPath Դ·��
	 * @param destPath Ŀ��·��
	 * @return </br>
	 */
	private void copyDB(File srcPath, File destPath)
	{
		try
		{
			FileUtils.copyFile(srcPath, destPath);
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			return;
		}

		logger.info("Copied the database to " + destPath);
	}

	/**
	 * @return the isRunning <br/>
	 * 
	 */
	public synchronized static boolean isRunning()
	{
		return isRunning;
	}

	public synchronized static void setRunning(boolean running)
	{
		isRunning = running;
	}

	/**
	 * ��ȡ����ִ�С���صĻỰʵ��
	 * 
	 * @param icdPath
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForBoth(String icdPath)
	{
		if (!isRunning)// ���ִ����û�������������ȡʵ������
			return null;
		if (StringUtils.isNull(icdPath))
		{
			throw new IllegalArgumentException("ICD should not be null.");
		}
		if (executorSession != null)
		{
			logger.warning("Found the MonitorManager was already initialized, it needs to be destroyed!");
			if (executorSession.executorRunning)
			{
				logger.warning("Found the executor is running, it should be colsed now.");
				try
				{
					shutDown();
				} catch (TimeoutException e)
				{
					LoggingPlugin.logException(logger, e);
					executorSession.shutDownOnError();
				}
			}
			// ������ǰ�Ķ���
			dispose();
		}
		executorSession = new ExecutorSession(icdPath);
		executorSession.needLaunchMonitor = true;
		executorSession.init();
		executorSession.excutorManager = new ExecutorManager(executorSession.eventManager, true);
		return executorSession;
	}

	/**
	 * ��ȡ���ڽ�ִ�в��������ĻỰʵ��
	 * 
	 * @param icdPath
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForExecutionOnly(String icdPath)
	{
		if (!isRunning)
		{// ���ִ����û�������������ȡʵ������
			logger.warning("ִ����û�����У���ȡExecutorSessionʵ��ʧ�ܣ�");
			return null;
		}
		if (StringUtils.isNull(icdPath))
		{
			throw new IllegalArgumentException("ICD·������Ϊ��");
		}
		if (executorSession != null)
		{
			logger.warning("Found the MonitorManager was already initialized, it needs to be destroyed!");
			if (executorSession.executorRunning)
			{
				logger.warning("Found the executor is running, it should be colsed now.");
				try
				{
					shutDown();
				} catch (TimeoutException e)
				{
					LoggingPlugin.logException(logger, e);
					executorSession.shutDownOnError();
				}
			}
			// ������ǰ�Ķ���
			dispose();
		}
		executorSession = new ExecutorSession(icdPath);
		executorSession.needLaunchMonitor = false;
		executorSession.init();
		executorSession.excutorManager = new ExecutorManager(false);

		return executorSession;
	}

	/**
	 * ���û�ִ�м�ػطŵ�ʱ����Ҫ���ô˺����Ի�ȡMonitorManager���� ������Ӧ������
	 * {@link ExecutorSession#setMsgDBPath(String)}���� �������ݿ��ļ���·����
	 * 
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForReplay()
	{
		if (!isRunning)
		{
			return null;
		}

		if (executorSession != null)
		{
			logger.warning("Found the MonitorManager was already initialized, it needs to be destroyed!");
			if (executorSession.executorRunning)
			{
				logger.warning("Found the executor is running, it should be colsed now.");
				try
				{
					shutDown();
				} catch (TimeoutException e)
				{
					LoggingPlugin.logException(logger, e);
					executorSession.shutDownOnError();
				}
			}

			dispose();
		}

		executorSession = new ExecutorSession();

		return executorSession;
	}

	/**
	 * ��������ء���ع��������Զ�����һ���µ����ݲ���ʼ����ִ������������ �û�ֻ��Ҫ��
	 * {@link ExecutorSession#launchMonitor(String, String, String, List, String)}
	 * ���� ����������ء�
	 * 
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForMonitor(SPTEMsg[] spteMsgs)
	{
		if (!isRunning)
		{
			return null;
		}
		if (executorSession != null)
		{
			logger.warning("Found the MonitorManager was already initialized, it needs to be destroyed!");
			if (executorSession.executorRunning)
			{
				logger.warning("Found the executor is running, it should be colsed now.");
				try
				{
					shutDown();
				} catch (TimeoutException e)
				{
					e.printStackTrace();
					executorSession.shutDownOnError();
				}
			}

			dispose();
		}
		executorSession = new ExecutorSession();
		executorSession.needLaunchMonitor = true;
		executorSession.init();
		executorSession.excutorManager = new ExecutorManager(executorSession.eventManager, true);

		return executorSession;
	}

	/**
	 * ��ȡ��ع�������ʵ����ע�⣬�˺���ֻ���ڳ�ʼ����MonitorManager֮�󣬷�������� �����Ѿ�����ʼ����MonitorManager����
	 * 
	 * @return ����ع�����û������ʱ����null</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-6
	 */
	public synchronized static ExecutorSession getInstance()
	{

		return executorSession;
	}

	/**
	 * ���ü�ز���
	 * 
	 * @param spteMsgs
	 * @param icdPath </br>
	 */
	public void setMonitorCfg(List<SPTEMsg> spteMsgs, String icdPath, String endian)
	{
		this.cfgManager.setIcdPath(icdPath);
		// this.cfgManager.addSPTEMsgs(spteMsgs);
		cfgManager.getSpteMsgs().clear(); // ���������ǰ�Ժ�ļ����Ϣ
		cfgManager.addSPTEMsgs(spteMsgs);
		this.icdPath = icdPath;
		this.endian = endian;
	}

	/**
	 * ��ѯ���ݿ��б������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-10
	 */
	public List<Result> queryResult(long startTime, long length)
	{
		List<Result> resultList = new ArrayList<Result>();
		SPTEMsg[] msgs = this.excutorManager.querySPTEMsgs(startTime, length);
		for (SPTEMsg msg : msgs)
		{
			resultList.add(new Result(new ArrayList<ErrorTypesEnum>(0), null, msg, null));
		}

		return resultList;
	}

	/**
	 * ��ѯ���ݿ��б������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{

		return this.excutorManager.querySPTEMsgs(startTime, length);
	}

	/**
	 * ��ѯ���ݿ��б������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @param msgIDs ��ϢIDs
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, int length, String[] msgIDs)
	{

		return this.excutorManager.getMsgLimitByMsgId(startTime, length, msgIDs);
	}

	/**
	 * ��ȡ���ݿ��е�������Ϣ������
	 * 
	 * @return </br>
	 */
	public SPTEMsg[] getAllKindOfSPTEMsgs()
	{
		if (this.excutorManager instanceof ExecutorManager)
		{
			return new SPTEMsg[0];
		}

		return ((LoadHistoryContextManager) this.excutorManager).getAllKindsOfMsgs();
	}

	/**
	 * �ر�ִ����
	 * 
	 * @return �����ڲ��ܹر�ִ����ʱ����false�����򷵻�true
	 * @throws TimeoutException </br>
	 */
	public synchronized static boolean shutDown() throws TimeoutException
	{
		logger.info("Try to invoke MonitorManager.shutDown() method to close executor.");
		if (!executorSession.excutorManager.shutDown())
		{
			logger.warning("Invoking ExecutorManager.shutDown() method fialed.");
			return false;
		}

		isRunning = false;
		executorSession.eventManager.fireShutDownEvent();
		return true;
	}

	/**
	 * ��ִ�������ִ���ʱֹͣ��ִ������ص����в�����
	 */
	public void shutDownOnError()
	{
		isRunning = false;
		dispose();
		((ExecutorManager) this.excutorManager).shutDownOnError();
		this.eventManager.fireShutDownEvent();
	}

	/**
	 * �������в����������ص���ż����ѯ�Ƿ���Թر�ִ������ ������Թر�ִ�������򷵻�true�����򷵻�false��
	 * 
	 * @return true/false</br>
	 */
	public boolean canShutDown()
	{

		return ((ExecutorManager) this.excutorManager).canShutDown();
	}

	/**
	 * ��ȡ���õļ����Ϣ
	 * 
	 * @return </br>
	 */
	public SPTEMsg[] getCfgSPTEMsgs()
	{
		if (!this.monitorRunning)
			return new SPTEMsg[0];
		return this.cfgManager.getSpteMsgs().toArray(new SPTEMsg[this.cfgManager.getSpteMsgs().size()]);
	}

	/**
	 * ��ȡ������õ�icd����·��������û�û�п�����أ� �򷵻�null
	 * 
	 * @return </br>
	 */
	public String getCfgICDPath()
	{
		if (!this.monitorRunning)
			return null;
		return this.cfgManager.getIcdPath();
	}

	/**
	 * �ı��ض���
	 * 
	 * @param topics
	 * @return </br>
	 */
	public boolean changeMonitoredTopics(List<SPTEMsg> topics)
	{
		if (!this.monitorRunning)
		{
			logger.warning("Wrong operation! It was not allowed to do operation for Changing Monitoring topic(s) without launching monitor.");
			return false;
		}
		try
		{
			Map<String, List<String>> map = this.getMonitorInfo(topics);
			return ((ExecutorManager) this.excutorManager).changeMonitoredTopics(map, this.cfgManager.getMonitorNodeId());
		} catch (TimeoutException e)
		{
			LoggingPlugin.logException(logger, e);
		}

		return false;
	}

	public String getEndian()
	{
		return endian;
	}

}