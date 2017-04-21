/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * 节点元素集合
 * 
 * @author 尹军 2012-3-14
 */
public class NodeElementSet
{

	private static final long		serialVersionUID	= -378150288233272831L;

	// 消息节点
	private SPTEMsg					monitorMsgNode;

	// 节点元素管理器
	private NodeElementSetManager	manager;

	// 消息页号、缓存页键值对
	private Map<Long, CachePage>	map					= new HashMap<Long, CachePage>();

	// 消息列表
	private int						maxElementLength;

	// 消息列表
	private ArrayList<SPTEMsg>		msgs				= new ArrayList<SPTEMsg>();

	// 消息列表
	private String					uuid;

	public NodeElementSet()
	{
		this.uuid = StringUtils.getUUID();
	}

	/**
	 * 功能节点集合的自构函数
	 * 
	 * @param manager 功能节点集合管理器
	 */
	public NodeElementSet(NodeElementSetManager manager)
	{
		this();
		this.manager = manager;
	}

	/**
	 * 向缓存页中添加消息元素
	 * 
	 * @param element 消息元素
	 */
	public synchronized void addSPTEMsgElement(SPTEMsg element)
	{
		long time = element.getTimeStamp();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getSpteMsgs().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addSPTEMsgElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				if (iterator.hasNext())
				{
					CachePage cachePage = getMap().remove(iterator.next());
					cachePage.clearSPTEMsgElement();
				}
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addSPTEMsgElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	/**
	 * 获得功能节点集合管理器
	 * 
	 * @return 功能节点集合管理器
	 */
	public NodeElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * 获得页号与缓存页的键值对
	 * 
	 * @return 页号与缓存页的键值对
	 */
	public synchronized Map<Long, CachePage> getMap()
	{
		return map;
	}

	/**
	 * 获得功能节点集合的最大元素长度
	 * 
	 * @return 功能节点集合的最大元素长度
	 */
	public int getMaxElementLength()
	{
		return maxElementLength;
	}

	/**
	 * 获得消息列表
	 * 
	 * @return 消息列表
	 */
	public ArrayList<SPTEMsg> getMsgs()
	{
		return msgs;
	}

	/**
	 * 从缓存页中获取用于显示的消息元素列表
	 * 
	 * @param currentTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 用于显示的消息元素列表
	 */
	public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long currentTimeStamp, long endTimeStamp)
	{
		List<SPTEMsg> list = new ArrayList<SPTEMsg>();

		int startPageNum = (int) (currentTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getSPTEMsgElementsToShow(currentTimeStamp, endTimeStamp));
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() 2012-3-14
	 */
	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.uuid.hashCode();
		return result;
	}

	/**
	 * 判断当前页号是否缓存
	 * 
	 * @param pageNum 页号
	 * @return 是否缓存
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		return getMap().containsKey(Long.valueOf(pageNum));
	}

	/**
	 * 判断当前时间戳段是否缓存
	 * 
	 * @param startTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 是否缓存
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		if (getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())) != null)
		{
			return getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())).isSPTEMsgElementCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * 设置功能节点集合管理器
	 * 
	 * @param map 功能节点集合管理器
	 */
	public synchronized void setMap(Map<Long, CachePage> map)
	{
		this.map = map;
	}

	/**
	 * 设置功能节点集合的最大元素长度
	 * 
	 * @param maxElementLength 功能节点集合的最大元素长度
	 */
	public void setMaxElementLength(int maxElementLength)
	{
		this.maxElementLength = maxElementLength;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<field");

		sb.append(" >");
		sb.append("\n");

		sb.append("</field>");
		sb.append("\n");
		return sb.toString();
	}

	public SPTEMsg getMonitorMsgNode()
	{
		return monitorMsgNode;
	}

	public void setMonitorMsgNode(SPTEMsg monitorMsgNode)
	{
		this.monitorMsgNode = monitorMsgNode;
	}

	/**
	 * @author 尹军 2012-3-14
	 */
	private static class CachePage
	{

		// 结束时间戳
		private long			endTime		= -1;

		// 消息列表
		private List<SPTEMsg>	spteMsgs	= new ArrayList<SPTEMsg>();

		// 开始时间戳
		private long			startTime	= -1;

		/**
		 * 缓存页的自构函数
		 * 
		 * @param startTime 开始时间戳
		 * @param endTime 结束时间戳
		 */
		public CachePage(long startTime, long endTime)
		{
			this.startTime = startTime;
			this.endTime = endTime;
		}

		/**
		 * 向缓存页中添加消息元素
		 * 
		 * @param spteMsg 消息元素
		 */
		public synchronized void addSPTEMsgElement(SPTEMsg spteMsg)
		{
			if (spteMsg.getTimeStamp() > getCurrentPageEndTime() && spteMsg.getTimeStamp() < getEndTime())
			{
				spteMsgs.add(spteMsg);
			}
		}

		/**
		 * 从缓存页中清理消息元素列表
		 */
		public synchronized void clearSPTEMsgElement()
		{
			if (spteMsgs.size() > 0)
			{
				spteMsgs.clear();
			}
		}

		/**
		 * @return 结束时间戳
		 */
		private long getCurrentPageEndTime()
		{
			if (spteMsgs.size() > 0)
			{
				return spteMsgs.get(spteMsgs.size() - 1).getTimeStamp();
			} else
			{
				return getStartTime();
			}
		}

		/**
		 * 结束时间戳
		 * 
		 * @return 结束时间戳
		 */
		public long getEndTime()
		{
			return endTime;
		}

		/**
		 * 从缓存页中获取用于显示的消息元素列表
		 * 
		 * @param startTimeStamp 开始时间戳
		 * @param endTimeStamp 结束时间戳
		 * @return 用于显示的消息元素列表
		 */
		public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<SPTEMsg> list = new ArrayList<SPTEMsg>();
			long current = startTimeStamp;

			for (SPTEMsg msg : spteMsgs)
			{
				long timeStamp = msg.getTimeStamp();

				// 获取大于开始时间戳，小于结束时间戳，并大于当前时间戳的消息元素
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp) && (timeStamp > current))
				{
					list.add(msg);
					current = timeStamp;
				} else if (timeStamp > endTimeStamp)
				{
					break;
				}
			}

			return list;
		}

		/**
		 * 获得缓存页中的消息列表
		 * 
		 * @return 消息列表
		 */
		public List<SPTEMsg> getSpteMsgs()
		{
			return spteMsgs;
		}

		/**
		 * 开始时间戳
		 * 
		 * @return 开始时间戳
		 */
		public long getStartTime()
		{
			return startTime;
		}

		/**
		 * 判断当前时间戳段是否缓存
		 * 
		 * @param startTimeStamp 开始时间戳
		 * @param endTimeStamp 结束时间戳
		 * @return 是否缓存
		 */
		public synchronized boolean isSPTEMsgElementCache(long startTimeStamp, long endTimeStamp)
		{
			for (SPTEMsg field : spteMsgs)
			{
				long timeStamp = field.getTimeStamp();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp))
				{
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof NodeElementSet)
		{
			NodeElementSet set = (NodeElementSet) obj;
			return set.uuid.equals(this.uuid);
		}
		return false;
	}
}
