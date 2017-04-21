/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import com.coretek.common.template.ICDField;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * 信号元素集合
 * 
 * @author 尹军 2012-3-14
 */
public class FieldElementSet
{

	private static final long		serialVersionUID	= -378150288233272831L;

	// 信号颜色
	private RGB						color;

	// 信号的ICDField对象
	private ICDField				field;

	// 信号的父类对象
	private SPTEMsg					monitorMsgNode;

	// 信号连线类型
	private int						lineType;

	// 信号集合管理器
	private FieldElementSetManager	manager;

	// 页号与缓存页的键值对
	private Map<Long, CachePage>	map					= new HashMap<Long, CachePage>();

	// 信号最小值
	private int						maxValue;

	// 信号最小值
	private int						minValue;

	// 信号的uuid
	private String					uuid;

	// 信号是否可见
	private boolean					visible;

	/**
	 * 信号集合管理器
	 * 
	 * @param manager 信号集合管理器
	 */
	public FieldElementSet(FieldElementSetManager manager)
	{
		this.manager = manager;
		this.uuid = StringUtils.getUUID();
	}

	/**
	 * 向信号列表中添加信号对象
	 * 
	 * @param element 信号对象
	 */
	public synchronized void addElement(FieldElement element)
	{
		long time = element.getTime();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getFields().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addFieldElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				long minCachePage = 0;
				if (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					minCachePage = curCachePage;
				}
				while (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					if (minCachePage > curCachePage)
					{
						minCachePage = curCachePage;
					}
				}

				CachePage cachePage = getMap().remove(minCachePage);
				cachePage.clearFieldElement();
				cachePage.clearSPTEMsgElement();
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addFieldElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	/**
	 * 向消息列表中添加消息对象
	 * 
	 * @param element 消息对象
	 */
	public synchronized void addSPTEMsgElement(SPTEMsg element)
	{
		long time = element.getTimeStamp();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getFields().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addSPTEMsgElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				long minCachePage = 0;
				if (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					minCachePage = curCachePage;
				}
				while (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					if (minCachePage > curCachePage)
					{
						minCachePage = curCachePage;
					}
				}

				CachePage cachePage = getMap().remove(minCachePage);
				cachePage.clearFieldElement();
				cachePage.clearSPTEMsgElement();
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addSPTEMsgElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
			return true;
		if (this.hashCode() == obj.hashCode())
		{
			return true;
		}
		return false;
	}

	/**
	 * 获得信号的颜色
	 * 
	 * @return 信号的颜色
	 */
	public synchronized RGB getColor()
	{
		return color;
	}

	/**
	 * 获得时间戳之间的信号列表
	 * 
	 * @param startTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 信号列表
	 */
	public synchronized List<FieldElement> getElementsToShow(long startTimeStamp, long endTimeStamp)
	{
		List<FieldElement> list = new ArrayList<FieldElement>();

		int startPageNum = (int) (startTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getElementsToShow(startTimeStamp, endTimeStamp));
			}
		}

		return list;
	}

	/**
	 * 获得信号
	 * 
	 * @return 信号
	 */
	public ICDField getField()
	{
		return field;
	}

	/**
	 * 获得连线类型
	 * 
	 * @return 连线类型
	 */
	public int getLineType()
	{
		return lineType;
	}

	/**
	 * 获得信号集合管理器
	 * 
	 * @return 信号集合管理器
	 */
	public FieldElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * 获得页号与缓存页键值对
	 * 
	 * @return 页号与缓存页键值对
	 */
	public synchronized Map<Long, CachePage> getMap()
	{
		return map;
	}

	/**
	 * 获得最小值
	 * 
	 * @return 最小值
	 */
	public int getMaxValue()
	{
		return maxValue;
	}

	/**
	 * 获得最小值
	 * 
	 * @return 最小值
	 */
	public int getMinValue()
	{
		return minValue;
	}

	/**
	 * 获得时间戳之间的消息列表
	 * 
	 * @param startTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 消息列表
	 */
	public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
	{
		List<SPTEMsg> list = new ArrayList<SPTEMsg>();

		int startPageNum = (int) (startTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getSPTEMsgElementsToShow(startTimeStamp, endTimeStamp));
			}
		}
		return list;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.uuid.hashCode();
		return result;
	}

	/**
	 * 当前页是否缓存
	 * 
	 * @param pageNum 页号
	 * @return 是否缓存
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		return getMap().containsKey(Long.valueOf(pageNum));
	}

	/**
	 * 当前页是否缓存
	 * 
	 * @param startTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 当前页是否缓存
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		if (getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())) != null)
		{
			return getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())).isFieldElementCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * 判断信号是否可见
	 * 
	 * @return 信号是否可见
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * 设置信号的颜色
	 * 
	 * @param color 信号的颜色
	 */
	public synchronized void setColor(RGB color)
	{
		this.color = color;
	}

	/**
	 * 设置信号
	 * 
	 * @param field 信号
	 */
	public void setField(ICDField field)
	{
		this.field = field;
	}

	/**
	 * 设置连线类型
	 * 
	 * @param lineType 连线类型
	 */
	public void setLineType(int lineType)
	{
		this.lineType = lineType;
	}

	/**
	 * 设置页号与缓存页键值对
	 * 
	 * @param map 页号与缓存页键值对
	 */
	public synchronized void setMap(Map<Long, CachePage> map)
	{
		this.map = map;
	}

	/**
	 * 设置最大值
	 * 
	 * @param maxValue 最大值
	 */
	public void setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
	}

	/**
	 * 设置最小值
	 * 
	 * @param minValue 最小值
	 */
	public void setMinValue(int minValue)
	{
		this.minValue = minValue;
	}

	/**
	 * 设置信号是否可见
	 * 
	 * @param visible 信号是否可见
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
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
		private long				endTime		= -1;

		// 信号列表
		private List<FieldElement>	fields		= new ArrayList<FieldElement>();

		// 消息列表
		private List<SPTEMsg>		spteMsgs	= new ArrayList<SPTEMsg>();

		// 开始时间戳
		private long				startTime	= -1;

		public CachePage(long startTime, long endTime)
		{
			this.startTime = startTime;
			this.endTime = endTime;
		}

		/**
		 * 向信号列表中添加信号对象
		 * 
		 * @param element 信号对象
		 */
		public synchronized void addFieldElement(FieldElement element)
		{
			if (element.getTime() > getCurrentPageFieldEndTime() && element.getTime() < getEndTime())
			{
				fields.add(element);
			}
		}

		/**
		 * 项消息列表中添加消息对象
		 * 
		 * @param spteMsg 消息对象
		 */
		public synchronized void addSPTEMsgElement(SPTEMsg spteMsg)
		{
			if (spteMsg.getTimeStamp() > getCurrentPageEndTime() && spteMsg.getTimeStamp() < getEndTime())
			{
				spteMsgs.add(spteMsg);
			}
		}

		/**
		 * 清理信号列表对象
		 */
		public synchronized void clearFieldElement()
		{
			if (fields.size() > 0)
			{
				fields.clear();
			}
		}

		/**
		 * 清理消息列表
		 */
		public synchronized void clearSPTEMsgElement()
		{
			if (spteMsgs.size() > 0)
			{
				spteMsgs.clear();
			}
		}

		/**
		 * 获得当前页的结束时间戳
		 * 
		 * @return 当前页的结束时间戳
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
		 * 获得当前页的结束时间戳
		 */
		private long getCurrentPageFieldEndTime()
		{
			if (fields.size() > 0)
			{
				return fields.get(fields.size() - 1).getTime();
			} else
			{
				return getStartTime();
			}
		}

		/**
		 * 获得在开始时间戳与结束时间戳之间的信号列表
		 * 
		 * @param startTimeStamp 开始时间戳
		 * @param endTimeStamp 结束时间戳
		 * @return 在时间戳之间的信号列表
		 */
		public synchronized List<FieldElement> getElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<FieldElement> list = new ArrayList<FieldElement>();
			long current = startTimeStamp;

			for (FieldElement field : fields)
			{
				long timeStamp = field.getTime();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp) && (timeStamp > current))
				{
					list.add(field);
					current = timeStamp;
				} else if (timeStamp > endTimeStamp)
				{
					break;
				}
			}

			return list;
		}

		/**
		 * 获得结束时间戳
		 */
		public long getEndTime()
		{
			return endTime;
		}

		/**
		 * 获得信号列表
		 * 
		 * @return 信号列表
		 */
		public List<FieldElement> getFields()
		{
			return fields;
		}

		/**
		 * 获得在开始时间戳与结束时间戳之间的消息列表
		 * 
		 * @param startTimeStamp 开始时间戳
		 * @param endTimeStamp 结束时间戳
		 * @return 在时间戳之间的消息列表
		 */
		public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<SPTEMsg> list = new ArrayList<SPTEMsg>();
			long current = startTimeStamp;

			for (SPTEMsg msg : spteMsgs)
			{
				long timeStamp = msg.getTimeStamp();
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
		 * 获得开始时间戳
		 * 
		 * @return 开始时间戳
		 */
		public long getStartTime()
		{
			return startTime;
		}

		/**
		 * 开始时间戳到结束时间戳的信号是否被缓存
		 * 
		 * @param startTimeStamp 开始时间戳
		 * @param endTimeStamp 结束时间戳
		 * @return 当前信号是否被缓存
		 */
		public synchronized boolean isFieldElementCache(long startTimeStamp, long endTimeStamp)
		{
			for (FieldElement field : fields)
			{
				long timeStamp = field.getTime();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp))
				{
					return true;
				}
			}

			return false;
		}
	}
}
