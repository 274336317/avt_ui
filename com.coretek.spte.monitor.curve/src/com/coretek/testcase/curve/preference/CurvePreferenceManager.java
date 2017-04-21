/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.testcase.curve.CurveViewPlugin;

/**
 * ������ͼ��ѡ�����ù�����
 * 
 * @author ���� 2012-3-14
 */
public final class CurvePreferenceManager
{
	// һ��������ͼ������֮��Ŀ��
	private int	colWidth;

	// һ��������ͼͬʱ֧�ֵ�����ź���
	private int	fieldElementNUM;

	// һ��������ͼ��֧�ֵ�����ź�ҳ
	private int	pageNum;

	// һ��������ͼ��֧�ֵ��ź�ҳ�����������
	private int	pageSubItemNum;

	// ������ͼ�ػ�ļ��ʱ��
	private int	paintIntervalTime;

	// һ��������ͼһ������ʾ������źų���
	private int	timestampLengthEachPage;

	// һ��������ͼX��֧�ֵĿ̶���
	private int	xIntervalNum;

	// һ��������ͼY��֧�ֵĿ̶���
	private int	yIntervalNum;

	/**
	 * һ��������ͼ������֮��Ŀ��
	 * 
	 * @return һ��������ͼ������֮��Ŀ��
	 */
	public int getColWidth()
	{
		return colWidth;
	}

	/**
	 * һ��������ͼͬʱ֧�ֵ�����ź���
	 * 
	 * @return һ��������ͼͬʱ֧�ֵ�����ź���
	 */
	public int getFieldElementNUM()
	{
		return fieldElementNUM;
	}

	/**
	 * һ��������ͼ��֧�ֵ�����ź�ҳ
	 * 
	 * @return һ��������ͼ��֧�ֵ�����ź�ҳ
	 */
	public int getPageNum()
	{
		return pageNum;
	}

	/**
	 * һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 * 
	 * @return һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 */
	public int getPageSubItemNum()
	{
		return pageSubItemNum;
	}

	/**
	 * ������ͼ�ػ�ļ��ʱ��
	 * 
	 * @return ������ͼ�ػ�ļ��ʱ��
	 */
	public int getPaintIntervalTime()
	{
		return paintIntervalTime;
	}

	/**
	 * һ��������ͼһ������ʾ������źų���
	 * 
	 * @return һ��������ͼһ������ʾ������źų���
	 */
	public int getTimestampLengthEachPage()
	{
		return timestampLengthEachPage;
	}

	/**
	 * һ��������ͼX��֧�ֵĿ̶���
	 * 
	 * @return һ��������ͼX��֧�ֵĿ̶���
	 */
	public int getxIntervalNum()
	{
		return xIntervalNum;
	}

	/**
	 * һ��������ͼY��֧�ֵĿ̶���
	 * 
	 * @return һ��������ͼY��֧�ֵĿ̶���
	 */
	public int getyIntervalNum()
	{
		return yIntervalNum;
	}

	/**
	 * ��ʼ����ѡ�����
	 */
	public void init()
	{
		IPreferenceStore prefStore = CurveViewPlugin.getDefault().getPreferenceStore();

		int fieldElementNUM = prefStore.getInt(CurvePreferenceConstants.MAX_FieldElement_NUM);
		if (fieldElementNUM > 0)
		{
			setFieldElementNUM(fieldElementNUM);
		}

		int timestampLengthEachPage = prefStore.getInt(CurvePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH);
		if (timestampLengthEachPage > 0)
		{
			setTimestampLengthEachPage(timestampLengthEachPage);
		}

		int pageNum = prefStore.getInt(CurvePreferenceConstants.MAX_PAGE_NUM);
		if (pageNum > 0)
		{
			setPageNum(pageNum);
		}

		int pageSubItemNum = prefStore.getInt(CurvePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM);
		if (pageSubItemNum > 0)
		{
			setPageSubItemNum(pageSubItemNum);
		}

		int colWidth = prefStore.getInt(CurvePreferenceConstants.COL_WIDTH);
		if (colWidth > 0)
		{
			setColWidth(colWidth);
		}

		int xIntervalNum = prefStore.getInt(CurvePreferenceConstants.X_INTERVAL_NUM);
		if (xIntervalNum > 0)
		{
			setxIntervalNum(xIntervalNum);
		}

		int yIntervalNum = prefStore.getInt(CurvePreferenceConstants.Y_INTERVAL_NUM);
		if (yIntervalNum > 0)
		{
			setyIntervalNum(yIntervalNum);
		}

		int paintTime = prefStore.getInt(CurvePreferenceConstants.PAINT_CURVE_INTERVAL);
		if (paintTime > 0)
		{
			setPaintIntervalTime(paintTime);
		}
	}

	/**
	 * һ��������ͼ������֮��Ŀ��
	 * 
	 * @param colWidth һ��������ͼ������֮��Ŀ��
	 */
	public void setColWidth(int colWidth)
	{
		this.colWidth = colWidth;
	}

	/**
	 * һ��������ͼͬʱ֧�ֵ�����ź���
	 * 
	 * @param fieldElementNUM һ��������ͼͬʱ֧�ֵ�����ź���
	 */
	public void setFieldElementNUM(int fieldElementNUM)
	{
		this.fieldElementNUM = fieldElementNUM;
	}

	/**
	 * һ��������ͼ��֧�ֵ�����ź�ҳ
	 * 
	 * @param pageNum һ��������ͼ��֧�ֵ�����ź�ҳ
	 */
	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	/**
	 * һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 * 
	 * @param pageSubItemNum һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 */
	public void setPageSubItemNum(int pageSubItemNum)
	{
		this.pageSubItemNum = pageSubItemNum;
	}

	/**
	 * ������ͼ�ػ�ļ��ʱ��
	 * 
	 * @param paintIntervalTime ������ͼ�ػ�ļ��ʱ��
	 */
	public void setPaintIntervalTime(int paintIntervalTime)
	{
		this.paintIntervalTime = paintIntervalTime;
	}

	/**
	 * һ��������ͼһ������ʾ������źų���
	 * 
	 * @param timestampLengthEachPage һ��������ͼһ������ʾ������źų���
	 */
	public void setTimestampLengthEachPage(int timestampLengthEachPage)
	{
		this.timestampLengthEachPage = timestampLengthEachPage;
	}

	/**
	 * * һ��������ͼX��֧�ֵĿ̶���
	 * 
	 * @param xIntervalNum һ��������ͼX��֧�ֵĿ̶���
	 * @param xIntervalNum
	 */
	public void setxIntervalNum(int xIntervalNum)
	{
		this.xIntervalNum = xIntervalNum;
	}

	/**
	 * @param yIntervalNum һ��������ͼY��֧�ֵĿ̶���
	 */
	public void setyIntervalNum(int yIntervalNum)
	{
		this.yIntervalNum = yIntervalNum;
	}
}