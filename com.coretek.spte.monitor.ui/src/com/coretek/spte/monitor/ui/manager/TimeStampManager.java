/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ʱ���������
 * 
 * @author ���� 2012-3-13
 */
public class TimeStampManager extends Observable implements Observer
{

	private final static Logger		logger	= LoggingPlugin.getLogger(TimeStampManager.class);

	/**
	 * ʱ���������
	 */
	public static TimeStampManager	manager;

	/**
	 * ���ʱ���������
	 * 
	 * @return ʱ���������</br>
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
	 * ��ǰʱ���
	 */
	private long						currentTimeStamp	= 0;

	/**
	 * �Ƿ��������
	 */
	private volatile boolean			isStartMonitor		= false;

	// ��ѡ�������
	private TimeStampPreferenceManager	preferenceManager;

	/**
	 * ʱ��������߳�
	 */
	private MonitorTimer				thread				= null;

	/**
	 * </br>
	 */
	TimeStampManager()
	{
		super();
		init();
		// ���¼���������Ϊ������������ǰ�������������¼�
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
	 * ��õ�ǰʱ���
	 * 
	 * @return ��ǰʱ���</br>
	 */
	public synchronized long getCurrentTimeStamp()
	{
		return currentTimeStamp;
	}

	/**
	 * �����ѡ�����ù�����
	 * 
	 * @return ��ѡ�����ù����� @return </br>
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
	 * ��ʼ�����ù����� </br>
	 */
	public void init()
	{
		preferenceManager = new TimeStampPreferenceManager();
		preferenceManager.init();
	}

	/**
	 * �ж��Ƿ��������
	 * 
	 * @return �Ƿ�������� </br>
	 */
	public boolean isStartMonitor()
	{
		return isStartMonitor;
	}

	/**
	 * ���õ�ǰʱ���
	 * 
	 * @param currentTimeStamp ��ǰʱ���</br>
	 */
	public synchronized void setCurrentTimeStamp(long currentTimeStamp)
	{
		this.currentTimeStamp = currentTimeStamp;
	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param isStartMonitor �Ƿ��������</br>
	 */
	public void setStartMonitor(boolean isStartMonitor)
	{
		this.isStartMonitor = isStartMonitor;
	}

	/**
	 * �������</br>
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
	 * ֹͣ��� </br>
	 */
	public void stopMonitor()
	{
		logger.info("ֹͣ��أ���ֹͣʱ����̡߳�");
		setStartMonitor(false);
	}

	public void notifyObservers(Object arg)
	{
		this.setChanged();
		super.notifyObservers(arg);
		this.clearChanged();
	}

	/*
	 * (non-Javadoc) ͨ���¼����¼�����
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

			// ������������¼������д���
			case Event.EVENT_START:
			{
				break;
			}
				// ���յ�ִ�����ϴ��ĵ�һ����Ϣ����֪ʱ�����������ʼ��ʱ
			case Event.EVENT_RECV_FIRST_MSG:
			{
				logger.info("Recevied the first message from executor. The timer starts to work.");
				startMonitor();
				break;
			}

				// ���ܼ��ؼ����ʷ��¼�¼������д���
			case Event.EVENT_LOAD:
			{

				break;
			}

				// ����ֹͣ����¼������д���
			case Event.EVENT_STOP:
			{
				logger.info("���յ�ִ��ģ�鴫����ֹͣ����¼���");
				stopMonitor();
				break;
			}

				// �����û�ѡ��ĳ��ʱ����¼������д���
			case Event.EVENT_TIME_SELECTED:
			{
				break;
			}

			default:
				break;
		}
	}
}
