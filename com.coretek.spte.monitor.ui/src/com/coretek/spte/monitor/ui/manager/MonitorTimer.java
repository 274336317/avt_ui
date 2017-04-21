/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ����ʱ����߳�
 * 
 * @author ���� 2012-3-14
 */
public class MonitorTimer extends Thread
{

	/**
	 * ʱ���������
	 */
	private TimeStampManager	manager;

	private static final Logger	logger	= LoggingPlugin.getLogger(MonitorTimer.class.getName());

	public static boolean		flag	= false;

	private int					sleepTime;
	private int					intervalTime;

	/**
	 * @param name ʱ����߳���
	 * @param manager ʱ���������
	 */
	public MonitorTimer(String name, TimeStampManager manager)
	{
		super(name);
		this.manager = manager;
		sleepTime = getManager().getPreferenceManager().getSleepIntervalNum();
		intervalTime = getManager().getPreferenceManager().getTimestampIntervalNum();
	}

	/**
	 * @return ʱ���������
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
				logger.config("MoniterTimer����ʱ���������ѯ���ݣ�ʱ���Ϊ��" + time + "-" + interval);
				SPTEMsg[] msgs = ExecutorSession.getInstance().querySPTEMsgs(time, interval);
				logger.config("��ѯ������Ϊ��" + msgs.length);
				if (msgs.length > 0)
				{ // ����ǰʱ������Ϊ����ѯ��Ϣ�����ʱ���+1
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
					// ֪ͨ�۲��߶������˼�ض�ʱ�¼�����֪ͨ��ǰʱ����������Ŀ�ʼʱ���������ʱ�������ǰʱ�����
					Event event = new Event(Event.EVENT_TIME_GENERATED, time, getManager().getCurrentTimeStamp(), getManager().getCurrentTimeStamp());
					event.setSpteMsgs(msgs);
					getManager().notifyObservers(event);
				}
			}

			// ���߼�ض�ʱ���ʱ��
			Thread.sleep(sleepTime);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}