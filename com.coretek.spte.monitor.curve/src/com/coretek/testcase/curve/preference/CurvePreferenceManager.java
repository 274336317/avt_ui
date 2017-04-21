/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.preference;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.testcase.curve.CurveViewPlugin;

/**
 * 曲线视图首选项配置管理器
 * 
 * @author 尹军 2012-3-14
 */
public final class CurvePreferenceManager
{
	// 一个曲线视图列与列之间的宽度
	private int	colWidth;

	// 一个曲线视图同时支持的最大信号数
	private int	fieldElementNUM;

	// 一个曲线视图能支持的最大信号页
	private int	pageNum;

	// 一个曲线视图能支持的信号页的最大子项数
	private int	pageSubItemNum;

	// 曲线视图重绘的间隔时间
	private int	paintIntervalTime;

	// 一个曲线视图一次能显示的最大信号长度
	private int	timestampLengthEachPage;

	// 一个曲线视图X轴支持的刻度数
	private int	xIntervalNum;

	// 一个曲线视图Y轴支持的刻度数
	private int	yIntervalNum;

	/**
	 * 一个曲线视图列与列之间的宽度
	 * 
	 * @return 一个曲线视图列与列之间的宽度
	 */
	public int getColWidth()
	{
		return colWidth;
	}

	/**
	 * 一个曲线视图同时支持的最大信号数
	 * 
	 * @return 一个曲线视图同时支持的最大信号数
	 */
	public int getFieldElementNUM()
	{
		return fieldElementNUM;
	}

	/**
	 * 一个曲线视图能支持的最大信号页
	 * 
	 * @return 一个曲线视图能支持的最大信号页
	 */
	public int getPageNum()
	{
		return pageNum;
	}

	/**
	 * 一个曲线视图能支持的信号页的最大子项数
	 * 
	 * @return 一个曲线视图能支持的信号页的最大子项数
	 */
	public int getPageSubItemNum()
	{
		return pageSubItemNum;
	}

	/**
	 * 曲线视图重绘的间隔时间
	 * 
	 * @return 曲线视图重绘的间隔时间
	 */
	public int getPaintIntervalTime()
	{
		return paintIntervalTime;
	}

	/**
	 * 一个曲线视图一次能显示的最大信号长度
	 * 
	 * @return 一个曲线视图一次能显示的最大信号长度
	 */
	public int getTimestampLengthEachPage()
	{
		return timestampLengthEachPage;
	}

	/**
	 * 一个曲线视图X轴支持的刻度数
	 * 
	 * @return 一个曲线视图X轴支持的刻度数
	 */
	public int getxIntervalNum()
	{
		return xIntervalNum;
	}

	/**
	 * 一个曲线视图Y轴支持的刻度数
	 * 
	 * @return 一个曲线视图Y轴支持的刻度数
	 */
	public int getyIntervalNum()
	{
		return yIntervalNum;
	}

	/**
	 * 初始化首选项参数
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
	 * 一个曲线视图列与列之间的宽度
	 * 
	 * @param colWidth 一个曲线视图列与列之间的宽度
	 */
	public void setColWidth(int colWidth)
	{
		this.colWidth = colWidth;
	}

	/**
	 * 一个曲线视图同时支持的最大信号数
	 * 
	 * @param fieldElementNUM 一个曲线视图同时支持的最大信号数
	 */
	public void setFieldElementNUM(int fieldElementNUM)
	{
		this.fieldElementNUM = fieldElementNUM;
	}

	/**
	 * 一个曲线视图能支持的最大信号页
	 * 
	 * @param pageNum 一个曲线视图能支持的最大信号页
	 */
	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	/**
	 * 一个曲线视图能支持的信号页的最大子项数
	 * 
	 * @param pageSubItemNum 一个曲线视图能支持的信号页的最大子项数
	 */
	public void setPageSubItemNum(int pageSubItemNum)
	{
		this.pageSubItemNum = pageSubItemNum;
	}

	/**
	 * 曲线视图重绘的间隔时间
	 * 
	 * @param paintIntervalTime 曲线视图重绘的间隔时间
	 */
	public void setPaintIntervalTime(int paintIntervalTime)
	{
		this.paintIntervalTime = paintIntervalTime;
	}

	/**
	 * 一个曲线视图一次能显示的最大信号长度
	 * 
	 * @param timestampLengthEachPage 一个曲线视图一次能显示的最大信号长度
	 */
	public void setTimestampLengthEachPage(int timestampLengthEachPage)
	{
		this.timestampLengthEachPage = timestampLengthEachPage;
	}

	/**
	 * * 一个曲线视图X轴支持的刻度数
	 * 
	 * @param xIntervalNum 一个曲线视图X轴支持的刻度数
	 * @param xIntervalNum
	 */
	public void setxIntervalNum(int xIntervalNum)
	{
		this.xIntervalNum = xIntervalNum;
	}

	/**
	 * @param yIntervalNum 一个曲线视图Y轴支持的刻度数
	 */
	public void setyIntervalNum(int yIntervalNum)
	{
		this.yIntervalNum = yIntervalNum;
	}
}