/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitorview.MsgWatchViewPlugin;

/**
 * 监控消息首选项配置管理器
 * 
 * @author 尹军 2012-3-14
 */

public final class MonitorNodePreferenceManager
{

	// 最大页的长度
	private int	pageNum;

	// 页的最大子项数
	private int	pageSubItemNum;

	// 刷新的间隔时间
	private int	refreshIntervalTime;

	// 一页的最大时间戳长度
	private int	timestampLengthEachPage;

	/**
	 ** 获得最大页的长度
	 * 
	 * @return 最大页的长度 </br>
	 */
	public int getPageNum()
	{
		return pageNum;
	}

	/**
	 * 获得页的最大子项数
	 * 
	 * @return 页的最大子项数 </br>
	 */
	public int getPageSubItemNum()
	{
		return pageSubItemNum;
	}

	/**
	 * 获得刷新的间隔时间
	 * 
	 * @return 刷新的间隔时间 </br>
	 */
	public int getRefreshIntervalTime()
	{
		return refreshIntervalTime;
	}

	/**
	 * 获得一页的最大时间戳长度
	 * 
	 * @return 一页的最大时间戳长度 </br>
	 */
	public int getTimestampLengthEachPage()
	{
		return timestampLengthEachPage;
	}

	/**
	 * 初始化元素的初值</br>
	 */
	public void init()
	{
		IPreferenceStore prefStore = MsgWatchViewPlugin.getDefault().getPreferenceStore();

		// 一页的最大时间戳长度
		int timestampLengthEachPage = prefStore.getInt(MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH);
		if (timestampLengthEachPage > 0)
		{
			setTimestampLengthEachPage(timestampLengthEachPage);
		}

		// 最大页的长度
		int pageNum = prefStore.getInt(MonitorNodePreferenceConstants.MAX_PAGE_NUM);
		if (pageNum > 0)
		{
			setPageNum(pageNum);
		}

		// 页的最大子项数
		int pageSubItemNum = prefStore.getInt(MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM);
		if (pageSubItemNum > 0)
		{
			setPageSubItemNum(pageSubItemNum);
		}

		// 刷新的间隔时间
		int paintTime = prefStore.getInt(MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL);
		if (paintTime > 0)
		{
			setRefreshIntervalTime(paintTime);
		}
	}

	/**
	 * 设置最大页的长度
	 * 
	 * @param pageNum 最大页的长度 </br>
	 */
	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	/**
	 * 设置页的最大子项数
	 * 
	 * @param pageSubItemNum 页的最大子项数 </br>
	 */
	public void setPageSubItemNum(int pageSubItemNum)
	{
		this.pageSubItemNum = pageSubItemNum;
	}

	/**
	 * 设置刷新的间隔时间
	 * 
	 * @param refreshIntervalTime 刷新的间隔时间 </br>
	 */
	public void setRefreshIntervalTime(int refreshIntervalTime)
	{
		this.refreshIntervalTime = refreshIntervalTime;
	}

	/**
	 * 设置一页的最大时间戳长度
	 * 
	 * @param timestampLengthEachPage 一页的最大时间戳长度 </br>
	 */
	public void setTimestampLengthEachPage(int timestampLengthEachPage)
	{
		this.timestampLengthEachPage = timestampLengthEachPage;
	}
}