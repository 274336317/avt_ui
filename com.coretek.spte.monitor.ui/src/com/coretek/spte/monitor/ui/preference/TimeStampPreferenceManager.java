/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitor.ui.MonitorPlugin;

/**
 * 时间戳产生线程首选项配置管理器
 * 
 * @author 尹军 2012-3-14
 */
public final class TimeStampPreferenceManager
{

	/**
	 * 时间产生线程休息的时间间隔
	 */
	public int					sleepIntervalNum;

	/**
	 * 时间产生线程的间隔
	 */
	public int					timestampIntervalNum;

	private IPreferenceStore	prefStore;

	/**
	 * </br> <b>作者</b> 尹军 </br> <b>日期</b> 2012-3-14
	 */
	public TimeStampPreferenceManager()
	{

	}

	/**
	 * 获得时间产生线程休息的时间间隔
	 * 
	 * @return 时间产生线程休息的时间间隔 </br>
	 */
	public int getSleepIntervalNum()
	{
		return prefStore.getInt(TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM);
	}

	/**
	 * 获得时间产生线程的间隔
	 * 
	 * @return 时间产生线程的间隔 </br>
	 */
	public int getTimestampIntervalNum()
	{
		return prefStore.getInt(TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM);
	}

	/**
	 * 初始化元素的初始值 </br>
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
	 * 设置时间产生线程休息的时间间隔
	 * 
	 * @param sleepIntervalNum 时间产生线程休息的时间间隔</br>
	 */
	public void setSleepIntervalNum(int sleepIntervalNum)
	{
		this.sleepIntervalNum = sleepIntervalNum;
	}

	/**
	 * 设置时间产生线程的间隔
	 * 
	 * @param timestampIntervalNum 时间产生线程的间隔 </br>
	 */
	public void setTimestampIntervalNum(int timestampIntervalNum)
	{
		this.timestampIntervalNum = timestampIntervalNum;
	}
}