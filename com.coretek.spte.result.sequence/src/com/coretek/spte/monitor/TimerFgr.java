/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;

/**
 * 时间轴图形
 * 
 * @author 孙大巍
 * @date 2012-1-6
 */
public class TimerFgr extends Shape implements PropertyChangeListener
{

	// 每个节点所占的高度
	private int	nodeHeight;

	// 时间刻度的个数
	private int	sum;

	// 每个刻度所代表的时间
	private int	scale		= 5;

	// 第一个刻度所代表的时间
	private int	startTime	= 0;

	public TimerFgr(int startTime)
	{
		this.startTime = startTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		// TODO Auto-generated method stub

	}

	public void repaint(int startTime, int scale)
	{
		this.scale = scale;
		this.startTime = startTime;
		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.setForegroundColor(ColorConstants.red);
		graphics.drawLine(0, 0, 0, this.getBounds().height);
		graphics.setLineWidth(1);

		// 每个节点所占的高度
		nodeHeight = Sequence.NODE_HEIGHT + Sequence.NODE_INTERVAL;
		// 节点的个数
		sum = Sequence.CANVAS_DEFAULT_HEIGHT / nodeHeight;

		for (int i = 0; i < sum; i++)
		{
			graphics.drawLine(0, i * 25 + 10, 7, i * 25 + 10);
			graphics.drawString(String.valueOf(i * this.scale + this.startTime), 12, i * 25 + 5);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-2
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (Sequence.EVENT_SCALE.equals(evt.getPropertyName()))
		{
			scale = Integer.valueOf(evt.getNewValue().toString());
			this.repaint();
		}
	}
}