/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * �¼����������������ÿ�������ͼ�ı仯���������������¼��ַ��������ļ����ͼ��
 * 
 * @author ���Ρ
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
			// ɾ�����¼�Դ
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
				// ������������Ϣ�㲥�������ļ����ͼ
				this.notifyObservers(event);
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

	/**
	 * ����ֹͣ����¼�
	 */
	public void fireShutDownEvent()
	{
		logger.info("����Event.EVENT_STOP �¼���");
		Event event = new Event(Event.EVENT_STOP);
		this.setChanged();
		this.notifyObservers(event);
		this.clearChanged();
	}

	/**
	 * ���Ϳ�ʼ�����¼�
	 */
	public void fireStartEvent()
	{
		Event event = new Event(Event.EVENT_START);
		this.setChanged();
		this.notifyObservers(event);
	}
}