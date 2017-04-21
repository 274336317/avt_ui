/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * �����Ϣ��ѡ�����ó���
 * 
 * @author ���� 2012-3-14
 */

public class MonitorNodePreferenceConstants
{

	// ���ҳ�ĳ���
	public final static String	MAX_PAGE_NUM				= "MAX_PAGE_NUM";

	// ҳ�����������
	public final static String	MAX_PAGE_SUB_ITEM_NUM		= "MAX_PAGE_SUB_ITEM_NUM";

	// һҳ�����ʱ�������
	public final static String	MAX_TIMESTAMP_PAGE_LENGTH	= "MAX_TIMESTAMP_PAGE_LENGTH";

	// ˢ�µļ��ʱ��
	public final static String	REFRESH_MESSAGE_INTERVAL	= "REFRESH_MESSAGE_INTERVAL";

	/**
	 * ��ʼ��ȱʡֵ
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