/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.preference.TimeStampPreferenceManager;

/**
 * 时间戳管理器
 * 
 * @author 尹军 2012-3-13
 */
public class TimeStampManager extends Observable implements Observer
{

	private final static Logger		logger	= LoggingPlugin.getLogger(TimeStampManager.class);

	/**
	 * 时间戳管理器
	 */
	public static TimeStampManager	manager;

	/**
	 * 获得时间戳管理器
	 * 
	 * @return 时间戳管理器</br>
	 */
	public synchronized static TimeStampManager getTimeStampManager()
	{
		if (manager == null)
		{
			manager = new TimeStampManager();
		}
		return manager;

	}

	/**
	 * 当前时间戳
	 */
	private long						currentTimeStamp	= 0;

	/**
	 * 是否启动监控
	 */
	private volatile boolean			isStartMonitor		= false;

	// 首选项管理器
	private TimeStampPreferenceManager	preferenceManager;

	/**
	 * 时间戳产生线程
	 */
	private MonitorTimer				thread				= null;

	/**
	 * </br>
	 */
	TimeStampManager()
	{
		super();
		init();
		// 将事件管理器作为监听器监听当前对象所发生的事件
		this.addObserver(MonitorEventManager.getMonitorEventManager());
		MonitorEventManager.getMonitorEventManager().addObserver(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeStampManager other = (TimeStampManager) obj;
		if (currentTimeStamp != other.currentTimeStamp)
			return false;

		if (isStartMonitor != other.isStartMonitor)
			return false;

		if (preferenceManager == null)
		{
			if (other.preferenceManager != null)
				return false;
		} else if (!preferenceManager.equals(other.preferenceManager))
			return false;
		if (thread == null)
		{
			if (other.thread != null)
				return false;
		} else if (!thread.equals(other.thread))
			return false;
		return true;
	}

	/**
	 * 获得当前时间戳
	 * 
	 * @return 当前时间戳</br>
	 */
	public synchronized long getCurrentTimeStamp()
	{
		return currentTimeStamp;
	}

	/**
	 * 获得首选项配置管理器
	 * 
	 * @return 首选项配置管理器 @return </br>
	 */
	public TimeStampPreferenceManager getPreferenceManager()
	{
		return preferenceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (currentTimeStamp ^ (currentTimeStamp >>> 32));
		result = prime * result + (isStartMonitor ? 1231 : 1237);
		result = prime * result + ((preferenceManager == null) ? 0 : preferenceManager.hashCode());
		result = prime * result + ((thread == null) ? 0 : thread.hashCode());
		return result;
	}

	/**
	 * 初始化配置管理器 </br>
	 */
	public void init()
	{
		preferenceManager = new TimeStampPreferenceManager();
		preferenceManager.init();
	}

	/**
	 * 判断是否启动监控
	 * 
	 * @return 是否启动监控 </br>
	 */
	public boolean isStartMonitor()
	{
		return isStartMonitor;
	}

	/**
	 * 设置当前时间戳
	 * 
	 * @param currentTimeStamp 当前时间戳</br>
	 */
	public synchronized void setCurrentTimeStamp(long currentTimeStamp)
	{
		this.currentTimeStamp = currentTimeStamp;
	}

	/**
	 * 设置是否启动监控
	 * 
	 * @param isStartMonitor 是否启动监控</br>
	 */
	public void setStartMonitor(boolean isStartMonitor)
	{
		this.isStartMonitor = isStartMonitor;
	}

	/**
	 * 启动监控</br>
	 */
	public void startMonitor()
	{
		setStartMonitor(true);
		setCurrentTimeStamp(0);
		if (thread == null || !thread.isAlive())
		{
			thread = new MonitorTimer(Messages.getString("TimeStampManager_Thread_MoniterTimer"), this);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}

	/**
	 * 停止监控 </br>
	 */
	public void stopMonitor()
	{
		logger.info("停止监控，并停止时间戳线程。");
		setStartMonitor(false);
	}

	public void notifyObservers(Object arg)
	{
		this.setChanged();
		super.notifyObservers(arg);
		this.clearChanged();
	}

	/*
	 * (non-Javadoc) 通过事件更新监听者
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 * <br/>
	 */
	@Override
	public void update(Observable obs, Object arg)
	{
		Event event = (Event) arg;
		switch (event.getEventType())
		{
			case Event.EVENT_LOCK:
				break;

			case Event.EVENT_UNLOCK:
				break;

			// 接受启动监控事件并进行处理
			case Event.EVENT_START:
			{
				break;
			}
				// 接收到执行器上传的第一条消息，告知时间管理器，开始计时
			case Event.EVENT_RECV_FIRST_MSG:
			{
				logger.info("Recevied the first message from executor. The timer starts to work.");
				startMonitor();
				break;
			}

				// 接受加载监控历史记录事件并进行处理
			case Event.EVENT_LOAD:
			{

				break;
			}

				// 接受停止监控事件并进行处理
			case Event.EVENT_STOP:
			{
				logger.info("接收到执行模块传来的停止监控事件！");
				stopMonitor();
				break;
			}

				// 接受用户选择某个时间戳事件并进行处理
			case Event.EVENT_TIME_SELECTED:
			{
				break;
			}

			default:
				break;
		}
	}
}
