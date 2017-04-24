/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.Fighter;
import com.coretek.spte.FunctionCell;
import com.coretek.spte.FunctionCellMsg;
import com.coretek.spte.FunctionDomain;
import com.coretek.spte.FunctionDomainMsg;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.FunctionNodeMsg;
import com.coretek.spte.FunctionSubDomain;
import com.coretek.spte.FunctionSubDomainMsg;
import com.coretek.spte.Signal;
import com.coretek.spte.Topic;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TestedObjects;
import com.coretek.spte.testcase.TimeSpan;
import com.coretek.spte.unit.Unit;
import com.coretek.spte.unit.UnitType;

/**
 * �����������������������������װ�õĶ���
 * 
 * @author ���Ρ 2011-12-25
 */
public final class ClazzManager implements Serializable
{
	private static final long			serialVersionUID	= 6240926265118355738L;

	private static final Logger			logger				= LoggingPlugin.getLogger(ClazzManager.class.getName());

	private Map<String, List<Entity>>	map					= new HashMap<String, List<Entity>>();

	public ClazzManager()
	{

	}

	/**
	 * ����װ�õ�entityע�ᵽ��������
	 * 
	 * @param entity
	 */
	public void register(Entity entity)
	{
		List<Entity> list = this.map.get(entity.getClass().getName());
		if (list == null)
		{
			list = new ArrayList<Entity>();
			this.map.put(entity.getClass().getName(), list);

		}

		list.add(entity);

		logger.fine("ע���˽��������Ķ��󡣶��������Ϊ:" + entity.getClass().getName() + "��������\n" + entity.toString());

	}

	/**
	 * ��ȡ�������
	 * 
	 * @param topicSymbol �����ʶ��
	 * @return
	 */
	public Entity getTopic(String topicSymbol)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof Topic)
			{
				Topic topic = (Topic) entity;
				if (topic.getTopicSymbol().equals(topicSymbol))
					return topic;
			}
		}
		logger.warning("���ݴ���Ĳ���topicSymbol=" + topicSymbol + " �Ҳ�����Ӧ��Topic����");

		return null;
	}

	/**
	 * ��ȡICD�ļ��е�����topic����
	 * 
	 * @return
	 */
	public List<Entity> getAllTopics()
	{
		List<Entity> topics = new ArrayList<Entity>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof Topic)
			{
				topics.add(entity);
			}
		}

		return topics;
	}

	/**
	 * ���������Ż�ȡ�������
	 * 
	 * @param topicId ������
	 * @return
	 */
	public Topic getTopicById(Integer topicId)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof Topic)
			{
				Topic topic = (Topic) entity;
				if (topic.getTopicID().equals(topicId))
					return topic;
			}
		}
		logger.warning("���ݴ���Ĳ���topicId=" + topicId + " �Ҳ�����Ӧ�����⡣");

		return null;
	}

	/**
	 * ��չ�����
	 */
	public void clear()
	{
		this.map.clear();
	}

	@Override
	public String toString()
	{
		Set<Entry<String, List<Entity>>> set = this.map.entrySet();
		for (Entry<String, List<Entity>> entry : set)
		{
			List<Entity> list = entry.getValue();
			for (Entity entity : list)
			{
				this.getString(entity);
			}

		}

		return super.toString();
	}

	/**
	 * ��ȡ�������е����ж���
	 * 
	 * @return
	 */
	public List<Entity> getAllEntities()
	{
		List<Entity> list = new ArrayList<Entity>();
		Set<Entry<String, List<Entity>>> set = this.map.entrySet();
		for (Entry<String, List<Entity>> entry : set)
		{
			list.addAll(entry.getValue());
		}

		return list;
	}

	/**
	 * ��ȡ�������еĲ�����������
	 * 
	 * @return
	 */
	public Entity getTestCase()
	{
		List<Entity> list = getAllEntities();
		Entity testCase = null;
		for (Entity entity : list)
		{
			if (entity instanceof TestCase)
			{
				testCase = entity;
				break;
			}
		}

		return testCase;
	}

	/**
	 * ��ȡ�������еĻ��Ͷ���
	 * 
	 * @return
	 */
	public Entity getFighter()
	{
		List<Entity> list = getAllEntities();
		Entity fighter = null;
		for (Entity entity : list)
		{
			if (entity instanceof Fighter)
			{
				fighter = entity;
				break;
			}
		}

		return fighter;
	}

	/**
	 * ��ȡ���еĹ�����
	 * 
	 * @return
	 */
	public List<Entity> getAllFunctionDomains()
	{
		List<Entity> entities = this.getAllEntities();
		List<Entity> list = new ArrayList<Entity>();
		for (Entity entity : entities)
		{
			if (entity instanceof FunctionDomain)
			{
				list.add((FunctionDomain) entity);
			}
		}
		logger.fine("��ȡ���еĹ��������Ϊ:" + list.size());

		return list;
	}

	/**
	 * ��ȡ���еĹ����������
	 * 
	 * @return
	 */
	public List<Entity> getAllFunctionSubDomains()
	{
		List<Entity> entities = this.getAllEntities();
		List<Entity> list = new ArrayList<Entity>();
		for (Entity entity : entities)
		{
			if (entity instanceof FunctionSubDomain)
			{
				list.add((FunctionSubDomain) entity);
			}
		}
		logger.fine("��ȡ���еĹ��������Ϊ:" + list.size());

		return list;
	}

	/**
	 * ��ȡ���еĹ���������Ϣ
	 * 
	 * @return
	 */
	public List<FunctionSubDomainMsg> getAllFunctionSubDomainMsgs()
	{
		List<FunctionSubDomainMsg> subDomainMsgs = new ArrayList<FunctionSubDomainMsg>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionSubDomainMsg)
			{
				subDomainMsgs.add((FunctionSubDomainMsg) entity);
			}
		}

		return subDomainMsgs;
	}

	/**
	 * ��ȡ���еĹ��ܽڵ���Ϣ
	 * 
	 * @return
	 */
	public List<FunctionNodeMsg> getAllFunctionNodeMsgs()
	{
		List<FunctionNodeMsg> subDomainMsgs = new ArrayList<FunctionNodeMsg>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionNodeMsg)
			{
				subDomainMsgs.add((FunctionNodeMsg) entity);
			}
		}

		return subDomainMsgs;
	}

	/**
	 * ��ȡ���еĹ��ܽڵ㣬����Ҫ���˵����е���Ϊ�������Ĺ��ܽڵ�
	 * 
	 * @return
	 */
	public List<FunctionNode> getAllFunctionNodes()
	{
		List<FunctionNode> nodes = new ArrayList<FunctionNode>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionNode)
			{
				nodes.add((FunctionNode) entity);
			}
		}

		return nodes;
	}

	/**
	 * ��ȡ���ܽڵ������
	 * 
	 * @param nodeId ���ܽڵ��
	 * @return
	 */
	public String getFunctionNodeName(String nodeId)
	{

		List<FunctionNode> nodes = this.getAllFunctionNodes();

		for (FunctionNode node : nodes)
		{
			if (nodeId.equals(String.valueOf(node.getID())))
			{
				return node.getName();
			}
		}

		return null;
	}

	/**
	 * ��ȡ���еĹ��ܽڵ�
	 * 
	 * @return
	 */
	public List<FunctionCell> getAllFunctionCells()
	{
		List<FunctionCell> nodes = new ArrayList<FunctionCell>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionCell)
			{
				nodes.add((FunctionCell) entity);
			}
		}

		return nodes;
	}

	/**
	 * ��ȡ���еĹ��ܵ�Ԫ��Ϣ
	 * 
	 * @return
	 */
	public List<FunctionCellMsg> getAllFunctionCellMsgs()
	{
		List<FunctionCellMsg> subDomainMsgs = new ArrayList<FunctionCellMsg>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionCellMsg)
			{
				subDomainMsgs.add((FunctionCellMsg) entity);
			}
		}

		return subDomainMsgs;
	}

	/**
	 * �����źű�ʶ����ȡ�ź�
	 * 
	 * @param signalSymbol
	 * @return
	 */
	protected Signal getSingal(String signalSymbol)
	{
		List<Entity> entyties = getAllEntities();
		for (Entity entity : entyties)
		{
			if (entity instanceof Signal)
			{
				Signal signal = (Signal) entity;
				if (signal.getSignalSymbol().equals(signalSymbol))
					return signal;
			}
		}
		logger.warning("���ݴ�����źű�ʶ��signalSymbol=" + signalSymbol + " �Ҳ�����Ӧ���źŶ���");

		return null;
	}

	/**
	 * ��ȡ��λ���ͣ�ǰ���ǵ�ǰ����������ǵ�λ��������
	 * 
	 * @param unitCode ��λ����
	 * @return
	 */
	public UnitType getUnitType(Integer unitCode)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof UnitType)
			{
				UnitType unitType = (UnitType) entity;
				for (Entity entity2 : unitType.getChildren())
				{
					if (entity2 instanceof Unit)
					{
						Unit unit = (Unit) entity2;
						if (unit.getID().equals(unitCode))
							return unitType;
					}
				}
			}
		}
		logger.warning("���ݴ���ĵ�λ����unitCode=" + unitCode + " �Ҳ�����Ӧ�ĵ�λ���Ͷ���");

		return null;
	}

	/**
	 * ������Ϣ��ԴID������ID��ȡ��Ӧ�Ĺ��ܽڵ���Ϣ��
	 * 
	 * @param srcId ��ϢԴID
	 * @param topicId ����ID
	 * @return
	 */
	protected FunctionNodeMsg getFunctionNodeMsg(Integer srcId, Integer topicId)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionNodeMsg)
			{
				FunctionNodeMsg nodeMsg = (FunctionNodeMsg) entity;
				if (nodeMsg.getSourceFunctionID().equals(srcId))
				{
					Topic topic = getTopicById(topicId);
					if (topic != null && nodeMsg.getTopic().getTopicID().equals(topicId))
						return nodeMsg;
				}
			}
		}
		logger.warning("���ݴ����srcId=" + srcId + " topicId=" + topicId + " �Ҳ�����Ӧ�Ĺ��ܽڵ���Ϣ��");

		return null;
	}

	/**
	 * ���ݽڵ���Ϣ��ID��ȡ��Ӧ�Ľڵ���Ϣ����
	 * 
	 * @param msgId
	 * @return
	 */
	public FunctionNodeMsg getFunctionNodeMsg(String msgId)
	{
		List<FunctionNodeMsg> nodeMsgs = this.getAllFunctionNodeMsgs();
		for (FunctionNodeMsg nodeMsg : nodeMsgs)
		{
			if (nodeMsg.getMsgID().equals(msgId))
			{
				return nodeMsg;
			}
		}
		logger.warning("���ݴ����msgId=" + msgId + " �Ҳ�����Ӧ�Ĺ��ܽڵ���Ϣ");

		return null;
	}
	public FunctionCellMsg getFunctionCellMsg(String msgId) {
		List<FunctionCellMsg> cellMsgs = getAllFunctionCellMsgs();
		for (FunctionCellMsg cellMsg : cellMsgs)
		{
			if (cellMsg.getMsgID().equals(msgId))
			{
				return cellMsg;
			}
		}
		logger.warning("���ݴ����msgId=" + msgId + " �Ҳ�����Ӧ�Ĺ��ܽڵ���Ϣ");

		return null;
	}

	/**
	 * ������Ϣ��ԴID������ID��ȡ��Ӧ�Ĺ��ܵ�Ԫ��Ϣ��
	 * 
	 * @param srcId ��ϢԴID
	 * @param topicId ����ID
	 * @return
	 */
	protected FunctionCellMsg getFunctionCellMsg(Integer srcId, Integer topicId)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionCellMsg)
			{
				FunctionCellMsg nodeMsg = (FunctionCellMsg) entity;
				if (nodeMsg.getSourceFunctionID().equals(srcId))
				{
					Topic topic = getTopicById(topicId);
					if (topic != null && nodeMsg.getTopic().getTopicID().equals(topicId))
						return nodeMsg;
				}
			}
		}
		logger.warning("���ݴ����srcId=" + srcId + " topicId=" + topicId + " �Ҳ�����Ӧ�Ĺ��ܵ�Ԫ��Ϣ��");

		return null;
	}

	private void getString(Entity entity)
	{
		if (entity.getChildren() != null)
		{
			List<Entity> list = entity.getChildren();
			for (Entity child : list)
				this.getString(child);
		}
	}

	/**
	 * ��ȡICD�ļ��е����й�������Ϣ
	 * 
	 * @return
	 */
	public List<FunctionDomainMsg> getAllFunctionDomainMsgs()
	{
		List<FunctionDomainMsg> msgs = new ArrayList<FunctionDomainMsg>();
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof FunctionDomainMsg)
			{
				msgs.add((FunctionDomainMsg) entity);
			}
		}

		return msgs;
	}

	/**
	 * ����UUID��ȡ��Ϣ
	 * 
	 * @param uuid
	 * @return
	 */
	public XMLBean getByUUID(String uuid)
	{
		for (Entity entity : getAllEntities())
		{
			if (entity instanceof TimeSpan)
			{
				TimeSpan span = (TimeSpan) entity;
				if (span.getUuid().equals(uuid))
				{
					return span;
				}
			} else if (entity instanceof Message)
			{
				Message msg = (Message) entity;
				if (msg.getUuid().equals(uuid))
					return msg;
			}
		}

		return null;

	}
	
	
	public String getTestObjectsLevel() {
		for (Entity entity : getAllEntities()) {
			if(entity instanceof TestedObjects) {
				return ((TestedObjects)entity).getLevel();
			}
		}
		return "";
	}
}