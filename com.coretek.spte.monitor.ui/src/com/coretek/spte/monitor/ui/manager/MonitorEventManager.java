/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.ui.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 事件管理器。负责监听每个监控视图的变化，并将监听到的事件分发给其它的监控视图。
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class MonitorEventManager extends Observable implements Observer
{
	private static final Logger			logger	= LoggingPlugin.getLogger(MonitorEventManager.class.getName());

	/**
	 * 时间戳管理器
	 */
	public static MonitorEventManager	manager;

	/**
	 * 获得时间戳管理器
	 * 
	 * @return 时间戳管理器</br>
	 */
	public synchronized static MonitorEventManager getMonitorEventManager()
	{
		if (manager == null)
		{
			manager = new MonitorEventManager();
		}
		return manager;

	}

	public void registerObserver()
	{
		if (ExecutorSession.getInstance() != null)
		{
			ExecutorSession.getInstance().registerListener(this);
			ExecutorSession.getInstance().addToEventManager(this);
			TimeStampManager.getTimeStampManager();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 * <br/>
	 */
	@Override
	public void update(Observable obs, Object arg)
	{
		if (obs instanceof Observer)
		{
			// 删除掉事件源
			this.deleteObserver((Observer) obs);
		}
		Event event = (Event) arg;
		switch (event.getEventType())
		{

			case Event.EVENT_STOP:
			{
				logger.info("接收到执行模块发送的Event.EVENT_STOP事件。");
				// 将监听到的消息广播到其它的监控视图
				this.setChanged();
				this.notifyObservers(event);
				this.clearChanged();
				break;
			}
			case Event.EVENT_LOAD:
			case Event.EVENT_LOCK:
			case Event.EVENT_UNLOCK:
			case Event.EVENT_START:
			case Event.EVENT_TIME:
			case Event.EVENT_TIME_GENERATED:
			case Event.EVENT_RECV_FIRST_MSG:
			case Event.EVENT_TIME_SELECTED:
			{
				// 将监听到的消息广播到其它的监控视图
				this.setChanged();
				this.notifyObservers(event);
				this.clearChanged();
				break;
			}
			default:
			{
				logger.warning("未知的事件类型。");
			}
		}
		if (obs instanceof Observer)
		{
			// 重新将事件源添加到队列中
			this.addObserver((Observer) obs);
		}
	}
}