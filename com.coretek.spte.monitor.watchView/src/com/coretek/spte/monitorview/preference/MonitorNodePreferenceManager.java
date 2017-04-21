/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitorview.MsgWatchViewPlugin;

/**
 * �����Ϣ��ѡ�����ù�����
 * 
 * @author ���� 2012-3-14
 */

public final class MonitorNodePreferenceManager
{

	// ���ҳ�ĳ���
	private int	pageNum;

	// ҳ�����������
	private int	pageSubItemNum;

	// ˢ�µļ��ʱ��
	private int	refreshIntervalTime;

	// һҳ�����ʱ�������
	private int	timestampLengthEachPage;

	/**
	 ** ������ҳ�ĳ���
	 * 
	 * @return ���ҳ�ĳ��� </br>
	 */
	public int getPageNum()
	{
		return pageNum;
	}

	/**
	 * ���ҳ�����������
	 * 
	 * @return ҳ����������� </br>
	 */
	public int getPageSubItemNum()
	{
		return pageSubItemNum;
	}

	/**
	 * ���ˢ�µļ��ʱ��
	 * 
	 * @return ˢ�µļ��ʱ�� </br>
	 */
	public int getRefreshIntervalTime()
	{
		return refreshIntervalTime;
	}

	/**
	 * ���һҳ�����ʱ�������
	 * 
	 * @return һҳ�����ʱ������� </br>
	 */
	public int getTimestampLengthEachPage()
	{
		return timestampLengthEachPage;
	}

	/**
	 * ��ʼ��Ԫ�صĳ�ֵ</br>
	 */
	public void init()
	{
		IPreferenceStore prefStore = MsgWatchViewPlugin.getDefault().getPreferenceStore();

		// һҳ�����ʱ�������
		int timestampLengthEachPage = prefStore.getInt(MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH);
		if (timestampLengthEachPage > 0)
		{
			setTimestampLengthEachPage(timestampLengthEachPage);
		}

		// ���ҳ�ĳ���
		int pageNum = prefStore.getInt(MonitorNodePreferenceConstants.MAX_PAGE_NUM);
		if (pageNum > 0)
		{
			setPageNum(pageNum);
		}

		// ҳ�����������
		int pageSubItemNum = prefStore.getInt(MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM);
		if (pageSubItemNum > 0)
		{
			setPageSubItemNum(pageSubItemNum);
		}

		// ˢ�µļ��ʱ��
		int paintTime = prefStore.getInt(MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL);
		if (paintTime > 0)
		{
			setRefreshIntervalTime(paintTime);
		}
	}

	/**
	 * �������ҳ�ĳ���
	 * 
	 * @param pageNum ���ҳ�ĳ��� </br>
	 */
	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	/**
	 * ����ҳ�����������
	 * 
	 * @param pageSubItemNum ҳ����������� </br>
	 */
	public void setPageSubItemNum(int pageSubItemNum)
	{
		this.pageSubItemNum = pageSubItemNum;
	}

	/**
	 * ����ˢ�µļ��ʱ��
	 * 
	 * @param refreshIntervalTime ˢ�µļ��ʱ�� </br>
	 */
	public void setRefreshIntervalTime(int refreshIntervalTime)
	{
		this.refreshIntervalTime = refreshIntervalTime;
	}

	/**
	 * ����һҳ�����ʱ�������
	 * 
	 * @param timestampLengthEachPage һҳ�����ʱ������� </br>
	 */
	public void setTimestampLengthEachPage(int timestampLengthEachPage)
	{
		this.timestampLengthEachPage = timestampLengthEachPage;
	}
}