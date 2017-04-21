/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 监控消息首选项配置常量
 * 
 * @author 尹军 2012-3-14
 */

public class MonitorNodePreferenceConstants
{

	// 最大页的长度
	public final static String	MAX_PAGE_NUM				= "MAX_PAGE_NUM";

	// 页的最大子项数
	public final static String	MAX_PAGE_SUB_ITEM_NUM		= "MAX_PAGE_SUB_ITEM_NUM";

	// 一页的最大时间戳长度
	public final static String	MAX_TIMESTAMP_PAGE_LENGTH	= "MAX_TIMESTAMP_PAGE_LENGTH";

	// 刷新的间隔时间
	public final static String	REFRESH_MESSAGE_INTERVAL	= "REFRESH_MESSAGE_INTERVAL";

	/**
	 * 初始化缺省值
	 * 
	 * @param store </br>
	 */
	public static void initializeDefaultValues(IPreferenceStore store)
	{
		store.setDefault(MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH, 5000);
		store.setDefault(MonitorNodePreferenceConstants.MAX_PAGE_NUM, 300);
		store.setDefault(MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM, 1000);
		store.setDefault(MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL, 100);
	}
}