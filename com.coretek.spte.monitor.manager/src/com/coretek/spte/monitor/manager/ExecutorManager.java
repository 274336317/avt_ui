/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 执行器管理，负责与执行器进行通信。
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class ExecutorManager extends Observable implements IContextManager, Observer
{
	private static final Logger	logger	= LoggingPlugin.getLogger(ExecutorManager.class.getName());

	private ExecutorControl		commandControl;

	private IExecutorContext	parserContext;

	private String				casePath;															// 测试用例文件路径

	private boolean				needCache;

	public ExecutorManager(boolean needCache)
	{
		super();
		this.commandControl = new ExecutorControl();
		// 注册处理器
		ResultHandler handler = new DoneHandler(this.commandControl.getCommandCache());
		HandlerRegistries.registerHandler(handler);
		handler = new LogicErrorHandler(this.commandControl.getCommandCache());
		HandlerRegistries.registerHandler(handler);
		handler = new InfoHandler();
		HandlerRegistries.registerHandler(handler);
		this.needCache = needCache;
		logger.info(StringUtils.concat("本次执行是否需要将消息添加到缓存里:", needCache));
	}

	public ExecutorManager(Observer observer, boolean needCache)
	{
		this(needCache);
		this.addObserver(observer);
	}

	/**
	 * 设置测试用例文件的路径
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
	 * 开始消息解析
	 */
	public void startParing()
	{
		this.parserContext.startParsing();
	}

	/**
	 * 关闭执行器
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
	 * 当执行器出现错误的时候停止与执行器的输入输出线程，并将所有与执行器的操作停止。
	 */
	public void shutDownOnError()
	{
		this.commandControl.shutDown();

		// 重置处理器工厂中注册的处理器
		HandlerRegistries.reset();
		// 清空命令缓存
		this.commandControl.getCommandCache().clear();
		this.commandControl.killExecutor();
		this.parserContext.shutDown();
	}

	/**
	 * 查询现在能否关闭执行器。
	 * 
	 * @return 如果现在就可以关闭则返回true，否则返回false
	 */
	public boolean canShutDown()
	{

		return this.parserContext.isFinished();
	}

	/**
	 * 启动执行器并启动监控。启动的步骤如下：<br/>
	 * 1.启动执行器进程；<br/>
	 * 2.设置环境变量；<br/>
	 * 3.设置监控对象；<br/>
	 * 4.发送startMonitor命令；<br/>
	 * 5.发送run命令。
	 * 
	 * @param cmd 启动执行器进程的命令行命令
	 * @param testCase 测试用例
	 * @param startMsg 开始消息
	 * @param endMsg 结束消息
	 * @param prjPath 工程绝对路径
	 * @param endian 大小端
	 * @param tempDb 数据库名字
	 * @return
	 * @throws TimeoutException
	 */
	public boolean launchBoth(String cmd, String testCase, String startMsg, String endMsg, String prjPath, String endian, Map<String, List<String>> topicIds, String monitorNodeId) throws TimeoutException
	{
		// 启动执行器进程
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// 设置环境变量
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand(endian, this.getPort()), true);
			if (mr.isStatus())
			{
				// 设置监控对象
				mr = this.commandControl.addCommand(CommandFactory.getSetObjsCommand(topicIds, monitorNodeId), true);
				if (mr.isStatus())
				{
					// 发送startMonitor命令
					mr = this.commandControl.addCommand(CommandFactory.getStartMonitorCommand(), true);
					if (mr.isStatus())
					{
						// 发送运行命令给执行器
						mr = this.commandControl.addCommand(CommandFactory.getRunCommand(testCase, startMsg, endMsg, prjPath), false);

						return mr.isStatus();
					} else
					{
						// 处理发送运行命令失败:直接结束进程。
						this.commandControl.killExecutor();
					}
				} else
				{
					// 处理设置监控对象失败:直接结束进程。
					this.commandControl.killExecutor();
				}

			} else
			{
				// 处理设置环境变量失败:直接结束进程。
				this.commandControl.killExecutor();
			}
		}

		return false;

	}

	/**
	 * 启动纯监控。启动的步骤如下：<BR> 
	 * 1.启动执行器线程；<BR>
	 * 2.设置环境变量；<BR> 
	 * 3.设置监控对象；<BR> 
	 * 4.发送startMonitor命令。
	 * 
	 * @param cmd 启动执行器进程的命令行命令
	 * @param endian 大小端
	 * @param tempDb 监控数据库
	 * @param topicIds 主题号集合
	 * @param monitorNodeId 监控节点号
	 * @return 启动成功则返回true，否则返回false
	 * @throws TimeoutException 当执行器在执行命令超时时会抛出此异常
	 */
	public boolean launchMonitor(String cmd, String endian, String tempDb, Map<String, List<String>> topicIds, String monitorNodeId) throws TimeoutException
	{
		// 启动执行器进程
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// 设置环境变量
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand(endian, this.getPort()), true);
			if (mr.isStatus())
			{
				// 设置监控对象
				mr = this.commandControl.addCommand(CommandFactory.getSetObjsCommand(/* topicIds */null, /* monitorNodeId */""), true);
				if (mr.isStatus())
				{
					// 发送startMonitor命令
					this.commandControl.addCommand(CommandFactory.getStartMonitorCommand(), true);
				} else
				{
					// 处理设置监控对象失败:直接结束进程。
					this.commandControl.killExecutor();
				}
			} else
			{
				// 处理设置环境变量失败:直接结束进程。
				this.commandControl.killExecutor();
			}
		}

		return true;
	}

	/**
	 * 获取设置执行器的端口号
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
	 * 启动执行器，但是不启动监控。启动的步骤如下：</br> 1.启动执行器线程；</br> 2.设置环境变量；</br>
	 * 3.发送run命令。</br>
	 * 
	 * @param cmd 启动执行器进程的命令行命令
	 * @param testCase 测试用例
	 * @param startMsg 开始消息
	 * @param endMsg 结束消息
	 * @param endian 大小端
	 * @param tempDb 二进制数据库
	 * @return
	 * @throws TimeoutException
	 */
	public boolean launchExecutor(String cmd, String testCase, String startMsg, String endMsg, String prjPath, String endian) throws TimeoutException
	{
		// 启动执行器进程
		if (this.commandControl.startExcutorProcess(cmd))
		{
			// 设置环境变量
			MethodResult mr = this.commandControl.addCommand(CommandFactory.getSetEnvCommand("little", this.getPort()), true);
			if (mr.isStatus())
			{
				// 发送运行命令给执行器
				mr = this.commandControl.addCommand(CommandFactory.getRunCommand(testCase, startMsg, endMsg, prjPath), false);
				return mr.isStatus();

			} else
			{
				// 处理设置环境变量失败:直接结束进程。
				this.commandControl.killExecutor();
			}
		}
		return false;
	}

	/**
	 * 改变监控主题
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
			// 接收到第一条消息，向外广播
			Event event = new Event(Event.EVENT_RECV_FIRST_MSG);
			this.setChanged();
			this.notifyObservers(event);
			this.clearChanged();
		}

	}

}