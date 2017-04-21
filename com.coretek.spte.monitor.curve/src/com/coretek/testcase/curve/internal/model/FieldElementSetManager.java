/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * 信号元素集合管理器
 * 
 * @author 尹军 2012-3-14
 */
public class FieldElementSetManager
{
	// 一个曲线视图一次能显示的最大信号长度
	private long						cachePageTimeLength;

	// 一个曲线视图同时支持的最大信号数
	private int							fieldElementSetLength;

	// 信号集合列表
	private ArrayList<FieldElementSet>	fieldSets	= new ArrayList<FieldElementSet>();

	// 用户配置的消息ID
	private List<String>				messageIds	= new ArrayList<String>();

	// 一个曲线视图能支持的最大信号页
	private long						pageItemLength;

	// 一个曲线视图能支持的信号页的最大子项数
	private long						pageSubItemLength;

	public FieldElementSetManager(int fieldElementSetLength, long cachePageTimeLength, long pageItemLength, long pageSubItemLength)
	{
		super();
		this.fieldElementSetLength = fieldElementSetLength;
		this.cachePageTimeLength = cachePageTimeLength;
		this.pageItemLength = pageItemLength;
		this.pageSubItemLength = pageSubItemLength;
	}

	/**
	 * 向缓存页中添加信号项
	 * 
	 * @param element 信号集合
	 * @param field 信号项
	 */
	public synchronized void addElement(FieldElement element, FieldElementSet field)
	{
		field.addElement(element);
	}

	/**
	 * 向信号集合列表添加信号集合
	 * 
	 */
	public synchronized void addField(FieldElementSet cfb)
	{
		this.fieldSets.add(cfb);
	}

	/**
	 * 清理信号集合列表
	 */
	public synchronized void clear()
	{
		for (FieldElementSet field : fieldSets)
		{
			field.getMap().keySet().clear();
		}
		this.fieldSets.clear();
	}

	/**
	 * 清理信号集合列表中的数据
	 */
	public synchronized void clearData()
	{
		for (FieldElementSet field : fieldSets)
		{
			field.getMap().keySet().clear();
		}
	}

	/**
	 * 获得信号集合列表
	 * 
	 * @return 信号集合列表
	 */
	public synchronized List<FieldElementSet> getAllFields()
	{
		List<FieldElementSet> fields = new ArrayList<FieldElementSet>();
		fields.addAll(this.fieldSets);
		return fields;
	}

	/**
	 * 获得曲线视图一次能显示的最大信号长度
	 * 
	 * @return 曲线视图一次能显示的最大信号长度
	 */
	public long getCachePageTimeLength()
	{
		return cachePageTimeLength;
	}

	/**
	 * 获得一个曲线视图同时支持的最大信号数
	 * 
	 * @return 一个曲线视图同时支持的最大信号数
	 */
	public int getFieldElementSetLength()
	{
		return fieldElementSetLength;
	}

	/**
	 * 获得用户配置的消息ID列表
	 * 
	 * @return 消息ID列表
	 */
	public List<String> getMessageIds()
	{
		if (messageIds != null && messageIds.size() > 0)
		{
			return messageIds;
		} else
		{
			List<FieldElementSet> fields = getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (FieldElementSet field : fields)
				{
					if (field != null)
					{
						SPTEMsg msg = field.getMonitorMsgNode();
						if (!messageIds.contains(msg.getMsg().getId()))
						{
							messageIds.add(msg.getMsg().getId());
						}
					}
				}
				if (messageIds != null && messageIds.size() > 0)
				{
					return messageIds;
				}
			}
			return new ArrayList<String>(0);
		}
	}

	/**
	 * 获得一个曲线视图能支持的最大信号页
	 * 
	 * @return 一个曲线视图能支持的最大信号页
	 */
	public long getPageItemLength()
	{
		return pageItemLength;
	}

	/**
	 * 获得一个曲线视图能支持的信号页的最大子项数
	 * 
	 * @return 一个曲线视图能支持的信号页的最大子项数
	 */
	public long getPageSubItemLength()
	{
		return pageSubItemLength;
	}

	/**
	 * 判断信号集合列表是否为空
	 * 
	 * @return 信号集合列表是否为空
	 */
	public synchronized boolean isEmpty()
	{
		return this.fieldSets.isEmpty();
	}

	/**
	 * 判断当前页号是否缓存
	 * 
	 * @param pageNum 缓存的页号
	 * @return 当前页号是否缓存
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		List<FieldElementSet> fields = getAllFields();
		if (fields.size() > 0)
		{
			return fields.get(0).isPageCache(pageNum);
		}
		return false;
	}

	/**
	 * 判断开始时间戳到结束时间戳是否缓存过
	 * 
	 * @param startTimeStamp 开始时间戳
	 * @param endTimeStamp 结束时间戳
	 * @return 开始时间戳到结束时间戳是否缓存过
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		List<FieldElementSet> fields = getAllFields();
		if (fields.size() > 0)
		{
			return fields.get(0).isPageCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * * 设置曲线视图一次能显示的最大信号长度
	 * 
	 * @param cachePageTimeLength 曲线视图一次能显示的最大信号长度
	 * @param cachePageTimeLength
	 */
	public void setCachePageTimeLength(long cachePageTimeLength)
	{
		this.cachePageTimeLength = cachePageTimeLength;
	}

	/**
	 * * 设置一个曲线视图同时支持的最大信号数
	 * 
	 * @param fieldElementSetLength 一个曲线视图同时支持的最大信号数
	 * @param fieldElementSetLength
	 */
	public void setFieldElementSetLength(int fieldElementSetLength)
	{
		this.fieldElementSetLength = fieldElementSetLength;
	}

	/**
	 * 设置信号集合列表
	 * 
	 * @param fields 信号集合列表
	 */
	public synchronized void setFields(List<FieldElementSet> fields)
	{
		this.fieldSets.addAll(fields);
	}

	/**
	 * * 设置 一个曲线视图能支持的最大信号页
	 * 
	 * @param pageItemLength 一个曲线视图能支持的最大信号页
	 * @param pageItemLength
	 */
	public void setPageItemLength(long pageItemLength)
	{
		this.pageItemLength = pageItemLength;
	}

	/**
	 * 设置一个曲线视图能支持的信号页的最大子项数
	 * 
	 * @param pageSubItemLength 一个曲线视图能支持的信号页的最大子项数
	 */
	public void setPageSubItemLength(long pageSubItemLength)
	{
		this.pageSubItemLength = pageSubItemLength;
	}
}