/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitor.ui.MonitorPlugin;

/**
 * ʱ��������߳���ѡ�����ù�����
 * 
 * @author ���� 2012-3-14
 */
public final class TimeStampPreferenceManager
{

	/**
	 * ʱ������߳���Ϣ��ʱ����
	 */
	public int					sleepIntervalNum;

	/**
	 * ʱ������̵߳ļ��
	 */
	public int					timestampIntervalNum;

	private IPreferenceStore	prefStore;

	/**
	 * </br> <b>����</b> ���� </br> <b>����</b> 2012-3-14
	 */
	public TimeStampPreferenceManager()
	{

	}

	/**
	 * ���ʱ������߳���Ϣ��ʱ����
	 * 
	 * @return ʱ������߳���Ϣ��ʱ���� </br>
	 */
	public int getSleepIntervalNum()
	{
		return prefStore.getInt(TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM);
	}

	/**
	 * ���ʱ������̵߳ļ��
	 * 
	 * @return ʱ������̵߳ļ�� </br>
	 */
	public int getTimestampIntervalNum()
	{
		return prefStore.getInt(TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM);
	}

	/**
	 * ��ʼ��Ԫ�صĳ�ʼֵ </br>
	 */
	public void init()
	{
		prefStore = MonitorPlugin.getDefault().getPreferenceStore();

		int timestampIntervalNum = prefStore.getInt(TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM);
		if (timestampIntervalNum > 0)
		{
			setTimestampIntervalNum(timestampIntervalNum);
		}

		int sleepIntervalNum = prefStore.getInt(TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM);
		if (sleepIntervalNum > 0)
		{
			setSleepIntervalNum(sleepIntervalNum);
		}
	}

	/**
	 * ����ʱ������߳���Ϣ��ʱ����
	 * 
	 * @param sleepIntervalNum ʱ������߳���Ϣ��ʱ����</br>
	 */
	public void setSleepIntervalNum(int sleepIntervalNum)
	{
		this.sleepIntervalNum = sleepIntervalNum;
	}

	/**
	 * ����ʱ������̵߳ļ��
	 * 
	 * @param timestampIntervalNum ʱ������̵߳ļ�� </br>
	 */
	public void setTimestampIntervalNum(int timestampIntervalNum)
	{
		this.timestampIntervalNum = timestampIntervalNum;
	}
}