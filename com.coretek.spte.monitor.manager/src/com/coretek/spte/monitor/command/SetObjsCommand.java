/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ���ü�ض���(setObjs)���� ��ʽ�����,setObjs,{topicId=123,nodeId=145;69
 * 'topicId=321,nodeId=25;96'monitorId=36}
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class SetObjsCommand extends AbstractCommand
{
	/**
	 * ��������
	 * 
	 * @param topicIdAndNodeIds �����Լ���������Ľڵ���б�
	 * @param monitorNodeId ��ؽڵ�ı��
	 */
	public SetObjsCommand(Map<String, List<String>> topicIdAndNodeIds, String monitorNodeId)
	{
		super("setObjs");
		// setObjectCommand2(topicIdAndNodeIds, monitorNodeId); //����ģ����Ի�������������
		setObjectCommand1(topicIdAndNodeIds, monitorNodeId); // ������ʽ���Ի�������������
	}

	/**
	 * ������ʽ��ִ�л�������������
	 * 
	 * @param topicIdAndNodeIds
	 * @param monitorNodeId
	 */
	protected void setObjectCommand1(Map<String, List<String>> topicIdAndNodeIds, String monitorNodeId)
	{
		StringBuilder sb = new StringBuilder(this.command).append(",{");
		Set<Map.Entry<String, List<String>>> set = topicIdAndNodeIds.entrySet();
		int k = 0;
		for (Map.Entry<String, List<String>> entry : set)
		{
			String topicId = entry.getKey();
			sb.append("topicId=").append(topicId).append(",");
			sb.append("nodeId=");
			int j = 0;
			for (String nodeId : entry.getValue())
			{
				sb.append(nodeId);
				if (j != entry.getValue().size() - 1)
				{
					sb.append(";");
				}

				++j;
			}

			if (k != set.size() - 1)
			{
				sb.append("'");
			}

			++k;

		}
		sb.append("'monitorId=").append(monitorNodeId);
		sb.append("}");
		this.command = sb.toString();
	}

	/**
	 * ����ģ����Ի�������������
	 * 
	 * @param topicIdAndNodeIds
	 * @param monitorNodeId
	 */
	protected void setObjectCommand2(Map<String, List<String>> topicIdAndNodeIds, String monitorNodeId)
	{
		StringBuilder sb = new StringBuilder(this.command + ", {");
		Set<String> keys = topicIdAndNodeIds.keySet();
		List<String> list = new ArrayList<String>(keys);
		for (int i = 0; i < list.size() - 1; i++)
		{
			String topicId = list.get(i);
			sb.append("topicId=\"").append(topicId).append("\", ");
		}

		sb.append("topicId=\"").append(list.get(list.size() - 1)).append("\"}");
		this.command = sb.toString();
	}

}