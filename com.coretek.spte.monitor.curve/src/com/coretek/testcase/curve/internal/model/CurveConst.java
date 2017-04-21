/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

/**
 * 曲线常量
 * 
 * @author 尹军 2012-3-14
 */
public final class CurveConst
{
	// 画布高度
	public static int					CANVAS_HEIGHT			= 550;

	// 画布宽度
	public static int					CANVAS_WIDTH			= 1080;

	// 曲线常量实例,单例
	private volatile static CurveConst	data;

	// X轴的长度
	public static int					X_AXIS_LENGTH			= 1000;

	// Y轴的下偏移
	public final static int				Y_AXIS_V_BOTTOM_OFFSET	= 30;

	// Y轴的上偏移
	public final static int				Y_AXIS_V_TOP_OFFSET		= 10;

	public static CurveConst getInstance()
	{
		if (data == null)
		{
			data = new CurveConst();
		}
		return data;
	}

	/**
	 * 画布粒度
	 */
	private double	scale	= 1.0;

	private CurveConst()
	{

	}

	public synchronized double getScale()
	{
		return scale;
	}

	public synchronized void reset()
	{
		this.scale = 1.0;
	}

	public synchronized void setScale(double scale)
	{
		this.scale = scale * this.scale;
	}

}