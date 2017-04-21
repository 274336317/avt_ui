/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 执行器会话，负责提供与后台执行器的交互接口、执行器上下文以及生命周期管理接口。
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class ExecutorSession
{
	private final static Logger		logger				= LoggingPlugin.getLogger(ExecutorSession.class.getName());

	/** 监控状态 */
	public final static int			MONITROING			= 1;

	/** 加载状态 */
	public final static int			LOADING				= 2;

	/** 执行状态 */
	public final static int			EXECUTING			= 3;

	/** 未行进任何操作 */
	public final static int			NONE_STATUS			= -1;

	private static ExecutorSession	executorSession;

	private static volatile boolean	isRunning			= false;													// 标识监控是否运行

	private CfgManager				cfgManager;

	private IContextManager			excutorManager;

	private EventManager			eventManager;

	private String					msgDBPath;																		// 消息数据库路径

	private String					icdPath;																		// icd文件的绝对路径

	private volatile boolean		executorRunning		= false;													// 标识执行器是否运行

	private volatile boolean		monitorRunning		= false;													// 标识是否启动了监控

	private volatile boolean		needLaunchMonitor	= false;													// 标识本次执行测试用例是否需要启动监控

	private int						status				= NONE_STATUS;

	private String					endian;																			// 大小端
																													// :big、little

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
	 * 获取当前的状态，1表示运行执行器，2表示加载历史记录，当为-1时，表示未行进任何操作。
	 * 
	 * @return </br>
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * 返回当前的MonitorManager对象是否需要启动监控
	 * 
	 * @return
	 */
	public boolean isNeedLaunchMonitor()
	{
		return needLaunchMonitor;
	}

	/**
	 * 注册监听器到事件管理器上
	 * 
	 * @param observer
	 */
	public void registerListener(Observer observer)
	{
		this.eventManager.addObserver(observer);
	}

	/**
	 * 注销监听器
	 * 
	 * @param observer </br>
	 */
	public void unRegisterListener(Observer observer)
	{
		this.eventManager.deleteObserver(observer);
	}

	/**
	 * 将事件管理器作为监听器添加到observable的监听器队列中
	 * 
	 * @param observable </br>
	 */
	public void addToEventManager(Observable observable)
	{
		observable.addObserver(this.eventManager);
	}

	/**
	 * 注销MonitorManager对象，同时将消息缓存清空、将事件管理器清空 2012-3-13
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
	 * 重置运行状态
	 */
	public static void reset()
	{
		isRunning = false;
	}

	/**
	 * 当启动监控或开始执行时，必须要调用此函数以初始化MonitorManager对象 
	 */
	private void init()
	{
		// 拷贝消息数据库到.metadata/db文件夹下面
		String path = EclipseUtils.getWorkspacePath().toOSString();
		String uuid = UUID.randomUUID().toString();
		// 消息数据库
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
	 * 获取用户设置的监控信息
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
	 * 启动执行器并启动监控。
	 * 
	 * @param cmd 启动执行器进程的命令行命令
	 * @param testCase 测试用例
	 * @param startMsg 开始消息
	 * @param endMsg 结束消息
	 * @param tempDb 数据库名字
	 * @param icdPath icd文件的绝对路径
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
			// 开始解析操作
			((ExecutorManager) this.excutorManager).startParing();
			// 通知监控器
			this.eventManager.fireStartEvent();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}
	}

	/**
	 * 启动执行器而不启动监控
	 * 
	 * @param cmd 启动执行器的命令
	 * @param testCase 测试用例
	 * @param startMsg 开始消息的UUID
	 * @param endMsg 结束消息的UUID
	 * @param icdPath icd文件的绝对路径
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-18
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
			// 开始解析操作
			((ExecutorManager) this.excutorManager).startParing();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}

	}

	/**
	 * 启动监控
	 * 
	 * @param cmd 启动执行器的命令
	 * @param tempDb 数据库
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-4
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
			// 开始解析操作
			((ExecutorManager) this.excutorManager).startParing();
			return true;
		} else
		{
			isRunning = false;
			return false;
		}
	}

	/**
	 * 获取消息数据库的路径
	 * 
	 * @return the dbPath <br/>
	 * 
	 */
	public String getMsgDBPath()
	{

		return msgDBPath;
	}

	/**
	 * 开启加载历史记录
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
	 * 获取ICD文件的路径
	 * 
	 * @return
	 */
	public String getICDPath()
	{
		return this.icdPath;
	}

	/**
	 * 拷贝数据库文件到指定的目录下
	 * 
	 * @param srcPath 源路径
	 * @param destPath 目的路径
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
	 * 获取用于执行、监控的会话实例
	 * 
	 * @param icdPath
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForBoth(String icdPath)
	{
		if (!isRunning)// 如果执行器没有运行则不允许获取实例对象
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
			// 销毁以前的对象
			dispose();
		}
		executorSession = new ExecutorSession(icdPath);
		executorSession.needLaunchMonitor = true;
		executorSession.init();
		executorSession.excutorManager = new ExecutorManager(executorSession.eventManager, true);
		return executorSession;
	}

	/**
	 * 获取用于仅执行测试用例的会话实例
	 * 
	 * @param icdPath
	 * @return </br>
	 */
	public synchronized static ExecutorSession getInstanceForExecutionOnly(String icdPath)
	{
		if (!isRunning)
		{// 如果执行器没有运行则不允许获取实例对象
			logger.warning("执行器没有运行，获取ExecutorSession实例失败！");
			return null;
		}
		if (StringUtils.isNull(icdPath))
		{
			throw new IllegalArgumentException("ICD路径不能为空");
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
			// 销毁以前的对象
			dispose();
		}
		executorSession = new ExecutorSession(icdPath);
		executorSession.needLaunchMonitor = false;
		executorSession.init();
		executorSession.excutorManager = new ExecutorManager(false);

		return executorSession;
	}

	/**
	 * 当用户执行监控回放的时候，需要调用此函数以获取MonitorManager对象。 调用者应当调用
	 * {@link ExecutorSession#setMsgDBPath(String)}函数 设置数据库文件的路径。
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
	 * 启动纯监控。监控管理器会自动拷贝一个新的数据并初始化好执行器管理器。 用户只需要调
	 * {@link ExecutorSession#launchMonitor(String, String, String, List, String)}
	 * 方法 即可启动监控。
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
	 * 获取监控管理器的实例。注意，此函数只是在初始化了MonitorManager之后，方便调用者 调用已经被初始化的MonitorManager对象。
	 * 
	 * @return 当监控管理器没有运行时返回null</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-6
	 */
	public synchronized static ExecutorSession getInstance()
	{

		return executorSession;
	}

	/**
	 * 设置监控参数
	 * 
	 * @param spteMsgs
	 * @param icdPath </br>
	 */
	public void setMonitorCfg(List<SPTEMsg> spteMsgs, String icdPath, String endian)
	{
		this.cfgManager.setIcdPath(icdPath);
		// this.cfgManager.addSPTEMsgs(spteMsgs);
		cfgManager.getSpteMsgs().clear(); // 先清除掉以前以后的监控消息
		cfgManager.addSPTEMsgs(spteMsgs);
		this.icdPath = icdPath;
		this.endian = endian;
	}

	/**
	 * 查询数据库中保存的消息
	 * 
	 * @param startTime 起始时间
	 * @param length 时间长度
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-10
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
	 * 查询数据库中保存的消息
	 * 
	 * @param startTime 起始时间
	 * @param length 时间长度
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{

		return this.excutorManager.querySPTEMsgs(startTime, length);
	}

	/**
	 * 查询数据库中保存的消息
	 * 
	 * @param startTime 起始时间
	 * @param length 时间长度
	 * @param msgIDs 消息IDs
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, int length, String[] msgIDs)
	{

		return this.excutorManager.getMsgLimitByMsgId(startTime, length, msgIDs);
	}

	/**
	 * 获取数据库中的所有消息的种类
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
	 * 关闭执行器
	 * 
	 * @return 当现在不能关闭执行器时返回false，否则返回true
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
	 * 当执行器出现错误时停止与执行器相关的所有操作。
	 */
	public void shutDownOnError()
	{
		isRunning = false;
		dispose();
		((ExecutorManager) this.excutorManager).shutDownOnError();
		this.eventManager.fireShutDownEvent();
	}

	/**
	 * 当在运行测试用例与监控的是偶，查询是否可以关闭执行器。 如果可以关闭执行器，则返回true，否则返回false。
	 * 
	 * @return true/false</br>
	 */
	public boolean canShutDown()
	{

		return ((ExecutorManager) this.excutorManager).canShutDown();
	}

	/**
	 * 获取配置的监控消息
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
	 * 获取监控配置的icd绝对路径。如果用户没有开启监控， 则返回null
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
	 * 改变监控对象
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