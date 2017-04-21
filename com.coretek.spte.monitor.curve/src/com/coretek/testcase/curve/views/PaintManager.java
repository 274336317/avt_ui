/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

/**
 * 绘图管理器，根据当前曲线视图的起始时间戳、结束时间戳、当前时间戳绘制当前帧的曲线图。
 * 
 * @author 尹军 2012-3-14
 */
public class PaintManager
{

	private CurveView	viewPart;

	public PaintManager(CurveView viewPart)
	{
		this.viewPart = viewPart;
	}

	/**
	 * 根据当前曲线视图的起始时间戳、结束时间戳、当前时间戳绘制当前帧的曲线图
	 */
	public void paint()
	{
		if (viewPart.getCanvas() == null || viewPart.getCanvas().isDisposed())
		{
			return;
		}
		viewPart.getCanvas().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				viewPart.getContainer().repaintScale();
			}
		});
	}
}
