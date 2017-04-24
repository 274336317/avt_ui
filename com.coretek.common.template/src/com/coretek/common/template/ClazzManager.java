/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 对象管理器，管理被解析出来并被组装好的对象。
 * 
 * @author 孙大巍 2011-12-25
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
	 * 将组装好的entity注册到管理器中
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

		logger.fine("注册了解析出来的对象。对象的类型为:" + entity.getClass().getName() + "对象内容\n" + entity.toString());

	}

	/**
	 * 获取主题对象
	 * 
	 * @param topicSymbol 主题标识符
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
		logger.warning("根据传入的参数topicSymbol=" + topicSymbol + " 找不到对应的Topic对象。");

		return null;
	}

	/**
	 * 获取ICD文件中的所有topic对象。
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
	 * 根据主题编号获取主题对象
	 * 
	 * @param topicId 主题编号
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
		logger.warning("根据传入的参数topicId=" + topicId + " 找不到对应的主题。");

		return null;
	}

	/**
	 * 清空管理器
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
	 * 获取管理器中的所有对象
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
	 * 获取管理器中的测试用例对象
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
	 * 获取管理器中的机型对象
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
	 * 获取所有的功能域
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
		logger.fine("获取所有的功能域个数为:" + list.size());

		return list;
	}

	/**
	 * 获取所有的功能子域对象
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
		logger.fine("获取所有的功能域个数为:" + list.size());

		return list;
	}

	/**
	 * 获取所有的功能子域消息
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
	 * 获取所有的功能节点消息
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
	 * 获取所有的功能节点，但是要过滤掉所有的做为被测对象的功能节点
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
	 * 获取功能节点的名字
	 * 
	 * @param nodeId 功能节点号
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
	 * 获取所有的功能节点
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
	 * 获取所有的功能单元消息
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
	 * 根据信号标识符获取信号
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
		logger.warning("根据传入的信号标识符signalSymbol=" + signalSymbol + " 找不到对应的信号对象。");

		return null;
	}

	/**
	 * 获取单位类型，前提是当前的类管理器是单位管理器。
	 * 
	 * @param unitCode 单位代码
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
		logger.warning("根据传入的单位代码unitCode=" + unitCode + " 找不到对应的单位类型对象。");

		return null;
	}

	/**
	 * 根据消息的源ID与主题ID获取对应的功能节点消息。
	 * 
	 * @param srcId 消息源ID
	 * @param topicId 主题ID
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
		logger.warning("根据传入的srcId=" + srcId + " topicId=" + topicId + " 找不到对应的功能节点消息。");

		return null;
	}

	/**
	 * 根据节点消息的ID获取对应的节点消息对象
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
		logger.warning("根据传入的msgId=" + msgId + " 找不到对应的功能节点消息");

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
		logger.warning("根据传入的msgId=" + msgId + " 找不到对应的功能节点消息");

		return null;
	}

	/**
	 * 根据消息的源ID与主题ID获取对应的功能单元消息。
	 * 
	 * @param srcId 消息源ID
	 * @param topicId 主题ID
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
		logger.warning("根据传入的srcId=" + srcId + " topicId=" + topicId + " 找不到对应的功能单元消息。");

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
	 * 获取ICD文件中的所有功能域消息
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
	 * 根据UUID获取消息
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