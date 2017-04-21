/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.manager;

import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 产生时间戳线程
 * 
 * @author 尹军 2012-3-14
 */
public class MonitorTimer extends Thread
{

	/**
	 * 时间戳管理器
	 */
	private TimeStampManager	manager;

	private static final Logger	logger	= LoggingPlugin.getLogger(MonitorTimer.class.getName());

	public static boolean		flag	= false;

	private int					sleepTime;
	private int					intervalTime;

	/**
	 * @param name 时间戳线程名
	 * @param manager 时间戳管理器
	 */
	public MonitorTimer(String name, TimeStampManager manager)
	{
		super(name);
		this.manager = manager;
		sleepTime = getManager().getPreferenceManager().getSleepIntervalNum();
		intervalTime = getManager().getPreferenceManager().getTimestampIntervalNum();
	}

	/**
	 * @return 时间戳管理器
	 */
	public TimeStampManager getManager()
	{
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		while (getManager().isStartMonitor())
		{
			flag = false;
			generateTime(intervalTime, true);
		}
	}

	@Override
	public String toString()
	{
		return StringUtils.concat("TimeStampGenerationThread [manager=", manager, "]");
	}

	private void generateTime(int interval, boolean sleep)
	{
		long time = getManager().getCurrentTimeStamp();
		try
		{

			if (getManager() != null && ExecutorSession.getInstance() != null)
			{
				logger.config("MoniterTimer生成时间戳，并查询数据，时间段为：" + time + "-" + interval);
				SPTEMsg[] msgs = ExecutorSession.getInstance().querySPTEMsgs(time, interval);
				logger.config("查询的数量为：" + msgs.length);
				if (msgs.length > 0)
				{ // 将当前时间设置为所查询消息的最大时间戳+1
					for (SPTEMsg msg : msgs)
					{
						if (msg.getTimeStamp() > getManager().getCurrentTimeStamp())
						{
							getManager().setCurrentTimeStamp(msg.getTimeStamp() + 1);
						}
					}
				}
				if (getManager().countObservers() > 0 && msgs.length > 0)
				{
					// 通知观察者对象发生了监控定时事件，并通知当前时间戳产生器的开始时间戳、结束时间戳、当前时间戳。
					Event event = new Event(Event.EVENT_TIME_GENERATED, time, getManager().getCurrentTimeStamp(), getManager().getCurrentTimeStamp());
					event.setSpteMsgs(msgs);
					getManager().notifyObservers(event);
				}
			}

			// 休眠监控定时间隔时间
			Thread.sleep(sleepTime);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}