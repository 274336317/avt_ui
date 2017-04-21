/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * �¼�
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class Event
{

	/** ��ס�����¼� */
	public final static int	EVENT_LOCK				= 0x1;

	/** ���������¼� */
	public final static int	EVENT_UNLOCK			= 0x2;

	/** ֹͣ����¼� */
	public final static int	EVENT_STOP				= 0x3;

	/** ��ʼ����¼� */
	public final static int	EVENT_START				= 0x4;

	/** �û�ѡ��ĳ��ʱ����¼� */
	public final static int	EVENT_TIME_SELECTED		= 0x5;

	/** �϶��������¼� */
	public final static int	EVENT_TIME				= 0x6;

	/** ʱ���¼� */
	public final static int	EVENT_TIME_GENERATED	= 0x7;

	/** ���ؼ����ʷ��¼�¼� */
	public final static int	EVENT_LOAD				= 0x8;

	/** ���յ�ִ�������͵ĵ�һ����Ϣ */
	public final static int	EVENT_RECV_FIRST_MSG	= 0x9;

	// ��ص�ǰʱ���
	private long			currentTime;

	// ���ݿ�·��
	private String			dbPath;

	// ��ؿ�ʼʱ���
	private long			startTime;

	// ��ؽ���ʱ���
	private long			endTime;

	// �¼�����
	private int				eventType;

	private SPTEMsg[]		spteMsgs;

	/**
	 * �¼�����
	 * 
	 * @param eventType �¼�����
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
	 * ��õ�ǰʱ���
	 * 
	 * @return ��ǰʱ���
	 */
	public long getCurrentTime()
	{
		return currentTime;
	}

	/**
	 * ���ݿ�·��
	 * 
	 * @return ���ݿ�·��
	 */
	public String getDbPath()
	{
		return dbPath;
	}

	/**
	 * ���õ�ǰʱ���
	 * 
	 * @param currentTime ��ǰʱ���
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
	 * ��ȡ�¼�����
	 * 
	 * @return the eventType <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-10
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
				type = "��ס�����¼�";
				break;
			case 0x2:
				type = "���������¼�";
				break;
			case 0x3:
				type = "ֹͣ����¼�";
				break;
			case 0x4:
				type = "��ʼ����¼�";
				break;
			case 0x5:
				type = "�û�ѡ��ĳ��ʱ����¼�";
				break;
			case 0x6:
				type = "�϶��������¼�";
				break;
			case 0x7:
				type = "ʱ���¼�";
				break;
			case 0x8:
				type = "���ؼ����ʷ��¼�¼�";
				break;
			case 0x9:
				type = "���յ���һ����Ϣ�¼�";
				break;

			default:
				type = "δ֪�¼�";
				break;
		}
		return StringUtils.concat("Event [", type, " at ", getCurrentTime(), "]");
	}

}