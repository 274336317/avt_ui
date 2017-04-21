/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * 事件
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class Event
{

	/** 锁住界面事件 */
	public final static int	EVENT_LOCK				= 0x1;

	/** 解锁界面事件 */
	public final static int	EVENT_UNLOCK			= 0x2;

	/** 停止监控事件 */
	public final static int	EVENT_STOP				= 0x3;

	/** 开始监控事件 */
	public final static int	EVENT_START				= 0x4;

	/** 用户选中某个时间点事件 */
	public final static int	EVENT_TIME_SELECTED		= 0x5;

	/** 拖动滚动条事件 */
	public final static int	EVENT_TIME				= 0x6;

	/** 时钟事件 */
	public final static int	EVENT_TIME_GENERATED	= 0x7;

	/** 加载监控历史记录事件 */
	public final static int	EVENT_LOAD				= 0x8;

	/** 接收到执行器发送的第一条消息 */
	public final static int	EVENT_RECV_FIRST_MSG	= 0x9;

	// 监控当前时间戳
	private long			currentTime;

	// 数据库路径
	private String			dbPath;

	// 监控开始时间戳
	private long			startTime;

	// 监控结束时间戳
	private long			endTime;

	// 事件类型
	private int				eventType;

	private SPTEMsg[]		spteMsgs;

	/**
	 * 事件类型
	 * 
	 * @param eventType 事件类型
	 */
	public Event(int eventType)
	{
		this.eventType = eventType;
	}

	public Event(int eventType, long startTime, long endTime, long currentTime)
	{
		this(eventType);
		this.startTime = startTime;
		this.endTime = endTime;
		this.currentTime = currentTime;
	}

	public Event(int eventType, String dbPath)
	{
		this.eventType = eventType;
		this.dbPath = dbPath;
	}

	/**
	 * 获得当前时间戳
	 * 
	 * @return 当前时间戳
	 */
	public long getCurrentTime()
	{
		return currentTime;
	}

	/**
	 * 数据库路径
	 * 
	 * @return 数据库路径
	 */
	public String getDbPath()
	{
		return dbPath;
	}

	/**
	 * 设置当前时间戳
	 * 
	 * @param currentTime 当前时间戳
	 */
	public void setCurrentTime(long currentTime)
	{
		this.currentTime = currentTime;
	}

	public Event(int eventType, int startTime, int endTime)
	{
		this(eventType);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * 获取事件类型
	 * 
	 * @return the eventType <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-10
	 */
	public int getEventType()
	{
		return eventType;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public void setSpteMsgs(SPTEMsg[] spteMsgs)
	{
		this.spteMsgs = spteMsgs;
	}

	public SPTEMsg[] getSpteMsgs()
	{
		return spteMsgs;
	}

	@Override
	public String toString()
	{
		String type = "";
		switch (getEventType())
		{
			case 0x1:
				type = "锁住界面事件";
				break;
			case 0x2:
				type = "解锁界面事件";
				break;
			case 0x3:
				type = "停止监控事件";
				break;
			case 0x4:
				type = "开始监控事件";
				break;
			case 0x5:
				type = "用户选中某个时间点事件";
				break;
			case 0x6:
				type = "拖动滚动条事件";
				break;
			case 0x7:
				type = "时钟事件";
				break;
			case 0x8:
				type = "加载监控历史记录事件";
				break;
			case 0x9:
				type = "接收到第一条消息事件";
				break;

			default:
				type = "未知事件";
				break;
		}
		return StringUtils.concat("Event [", type, " at ", getCurrentTime(), "]");
	}

}