/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * 节点元素管理器
 * 
 * @author 尹军 2012-3-14
 */

public final class NodeElementSetManager
{

	// 一页的最大时间戳长度
	private long						cachePageTimeLength;

	// 功能节点列表
	private ArrayList<NodeElementSet>	fieldSets	= new ArrayList<NodeElementSet>();

	// 消息ID列表
	private List<String>				messageIds	= new ArrayList<String>();

	// 最大页的长度
	private long						pageItemLength;

	// 页的最大子项数
	private long						pageSubItemLength;

	/**
	 * @param cachePageTimeLength 一页的最大时间戳长度
	 * @param pageItemLength 最大页的长度
	 * @param pageSubItemLength 页的最大子项数
	 */
	public NodeElementSetManager(long cachePageTimeLength, long pageItemLength, long pageSubItemLength)
	{
		super();
		this.cachePageTimeLength = cachePageTimeLength;
		this.pageItemLength = pageItemLength;
		this.pageSubItemLength = pageSubItemLength;
	}

	/**
	 * 向功能节点集合列表添加功能节点集合
	 * 
	 * @param cfb 功能节点集合</br>
	 */
	public synchronized void addField(NodeElementSet cfb)
	{
		this.fieldSets.add(cfb);
	}

	/**
	 * 清理功能节点列表 </br>
	 */
	public synchronized void clear()
	{
		for (NodeElementSet field : fieldSets)
		{
			field.getMsgs().clear();
		}
		this.fieldSets.clear();
	}

	/**
	 * 清理功能节点列表的数据</br>
	 */
	public synchronized void clearData()
	{
		for (NodeElementSet field : fieldSets)
		{
			field.getMsgs().clear();
		}
	}

	/**
	 * 获得功能节点列表
	 * 
	 * @return 功能节点列表</br>
	 */
	public synchronized List<NodeElementSet> getAllFields()
	{
		List<NodeElementSet> fields = new ArrayList<NodeElementSet>();
		fields.addAll(this.fieldSets);
		return fields;
	}

	/**
	 * 获得缓存页的时间戳长度
	 * 
	 * @return 缓存页的时间戳长度</br>
	 */
	public long getCachePageTimeLength()
	{
		return cachePageTimeLength;
	}

	/**
	 * 获得用户配置的消息ID列表
	 * 
	 * @return 消息ID列表 </br>
	 */
	public List<String> getMessageIds()
	{
		if (messageIds != null && messageIds.size() > 0)
		{
			return messageIds;
		} else
		{
			List<NodeElementSet> fields = getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (NodeElementSet field : fields)
				{
					if (field != null)
					{
						SPTEMsg msg = field.getMonitorMsgNode();
						if (msg instanceof SPTEMsg)
						{ // 功能单元消息

							if (!messageIds.contains(msg.getMsg().getId()))
							{
								messageIds.add(msg.getMsg().getId());
							}
						}
					}
				}
				if (messageIds != null && messageIds.size() > 0)
				{
					return messageIds;
				}
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * 获得一个视图能支持的最大页数
	 * 
	 * @return 一个视图能支持的最大页数 </br>
	 */
	public long getPageItemLength()
	{
		return pageItemLength;
	}

	/**
	 * 获得一个视图能支持的信号页的最大子项数
	 * 
	 * @return 获得视图能支持的信号页的最大子项数</br>
	 */
	public long getPageSubItemLength()
	{
		return pageSubItemLength;
	}

	/**
	 * 判断功能节点集合列表是否为空
	 * 
	 * @return 功能节点集合列表是否为空</br>
	 */
	public synchronized boolean isEmpty()
	{
		return this.fieldSets.isEmpty();
	}

	/**
	 * 向功能节点集合列表添加功能节点集合列表
	 * 
	 * @param fields 功能节点集合列表 </br>
	 */
	public synchronized void setFields(List<NodeElementSet> fields)
	{
		this.fieldSets.addAll(fields);
	}
}