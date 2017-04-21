/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ���������
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class CommandFactory
{

	/**
	 * ��ȡ����(run)����
	 * 
	 * @param testCase ��������
	 * @param startMsg ��ʼ��Ϣ
	 * @param endMsg ������Ϣ
	 * @param prjPath ����·��
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public static AbstractCommand getRunCommand(String testCase, String startMsg, String endMsg, String prjPath)
	{

		return new RunCommand(testCase, startMsg, endMsg, prjPath);
	}

	/**
	 * ��ȡ���û���(setEnv)����
	 * 
	 * @param endian
	 * @param port ͨ�Ŷ˿�
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public static AbstractCommand getSetEnvCommand(String endian, String port)
	{

		return new SetEnvCommand(endian, port);
	}

	/**
	 * ��ȡ���ü�ض���(setObjs)����
	 * 
	 * @param topicIds �����Լ���������Ľڵ���б�
	 * @param monitorNodeId ��ؽڵ�ı��
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public static AbstractCommand getSetObjsCommand(Map<String, List<String>> topicIds, String monitorNodeId)
	{
		// FIXME Ϊ�˷������ʹ����Ӳ����
		monitorNodeId = "16847115";
		topicIds = new HashMap<String, List<String>>();
		List<String> nodes = new ArrayList<String>();
		nodes.add("16842762");
		topicIds.put("65", nodes);
		return new SetObjsCommand(topicIds, monitorNodeId);
	}

	/**
	 * ��ȡ��ʼ���(startMonitor)����
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public static AbstractCommand getStartMonitorCommand()
	{

		return new StartMonitorCommand();
	}

	/**
	 * ��ȡֹͣ���(stopMonitor)����
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public static AbstractCommand getStopMonitorCommand()
	{

		return new StopMonitorCommand();
	}

	/**
	 * ��ȡ�˳�ִ����(exit)������
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-17
	 */
	public static AbstractCommand getExitCommand()
	{

		return new ExitCommand();
	}

}