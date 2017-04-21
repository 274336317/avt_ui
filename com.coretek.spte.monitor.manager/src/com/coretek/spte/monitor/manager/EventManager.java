/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * 事件管理器。负责监听每个监控视图的变化，并将监听到的事件分发给其它的监控视图。
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class EventManager extends Observable implements Observer
{

	private static final Logger	logger	= LoggingPlugin.getLogger(EventManager.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
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
			case Event.EVENT_LOCK:
			case Event.EVENT_UNLOCK:
			case Event.EVENT_START:
			case Event.EVENT_STOP:
			case Event.EVENT_TIME:
			case Event.EVENT_TIME_GENERATED:
			case Event.EVENT_RECV_FIRST_MSG:
			case Event.EVENT_TIME_SELECTED:
			{
				this.setChanged();
				// 将监听到的消息广播到其它的监控视图
				this.notifyObservers(event);
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

	/**
	 * 发送停止监控事件
	 */
	public void fireShutDownEvent()
	{
		logger.info("发送Event.EVENT_STOP 事件。");
		Event event = new Event(Event.EVENT_STOP);
		this.setChanged();
		this.notifyObservers(event);
		this.clearChanged();
	}

	/**
	 * 发送开始运行事件
	 */
	public void fireStartEvent()
	{
		Event event = new Event(Event.EVENT_START);
		this.setChanged();
		this.notifyObservers(event);
	}
}