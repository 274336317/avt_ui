/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �¼����������������ÿ�������ͼ�ı仯���������������¼��ַ��������ļ����ͼ��
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class MonitorEventManager extends Observable implements Observer
{
	private static final Logger			logger	= LoggingPlugin.getLogger(MonitorEventManager.class.getName());

	/**
	 * ʱ���������
	 */
	public static MonitorEventManager	manager;

	/**
	 * ���ʱ���������
	 * 
	 * @return ʱ���������</br>
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
			// ɾ�����¼�Դ
			this.deleteObserver((Observer) obs);
		}
		Event event = (Event) arg;
		switch (event.getEventType())
		{

			case Event.EVENT_STOP:
			{
				logger.info("���յ�ִ��ģ�鷢�͵�Event.EVENT_STOP�¼���");
				// ������������Ϣ�㲥�������ļ����ͼ
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
				// ������������Ϣ�㲥�������ļ����ͼ
				this.setChanged();
				this.notifyObservers(event);
				this.clearChanged();
				break;
			}
			default:
			{
				logger.warning("δ֪���¼����͡�");
			}
		}
		if (obs instanceof Observer)
		{
			// ���½��¼�Դ��ӵ�������
			this.addObserver((Observer) obs);
		}
	}
}