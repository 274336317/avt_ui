/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制命令工厂
 * 
 * @author 孙大巍
 * @date 2012-1-13
 */
public class CommandFactory
{

	/**
	 * 获取运行(run)命令
	 * 
	 * @param testCase 测试用例
	 * @param startMsg 开始消息
	 * @param endMsg 结束消息
	 * @param prjPath 工程路径
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public static AbstractCommand getRunCommand(String testCase, String startMsg, String endMsg, String prjPath)
	{

		return new RunCommand(testCase, startMsg, endMsg, prjPath);
	}

	/**
	 * 获取设置环境(setEnv)命令
	 * 
	 * @param endian
	 * @param port 通信端口
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public static AbstractCommand getSetEnvCommand(String endian, String port)
	{

		return new SetEnvCommand(endian, port);
	}

	/**
	 * 获取设置监控对象(setObjs)命令
	 * 
	 * @param topicIds 主题以及引用主题的节点号列表
	 * @param monitorNodeId 监控节点的编号
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public static AbstractCommand getSetObjsCommand(Map<String, List<String>> topicIds, String monitorNodeId)
	{
		// FIXME 为了方便调试使用了硬编码
		monitorNodeId = "16847115";
		topicIds = new HashMap<String, List<String>>();
		List<String> nodes = new ArrayList<String>();
		nodes.add("16842762");
		topicIds.put("65", nodes);
		return new SetObjsCommand(topicIds, monitorNodeId);
	}

	/**
	 * 获取开始监控(startMonitor)命令
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public static AbstractCommand getStartMonitorCommand()
	{

		return new StartMonitorCommand();
	}

	/**
	 * 获取停止监控(stopMonitor)命令
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public static AbstractCommand getStopMonitorCommand()
	{

		return new StopMonitorCommand();
	}

	/**
	 * 获取退出执行器(exit)的命令
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-17
	 */
	public static AbstractCommand getExitCommand()
	{

		return new ExitCommand();
	}

}