/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.testcase.curve.internal.model.FieldElementSet;

/**
 * 画点线
 * 
 * @author 尹军 2012-3-14
 */
public class PointFgr extends Shape
{
	// 信号集合对象
	private FieldElementSet	field;

	// 点的高度
	private int				height	= 1;

	// 时间戳
	private long			time;

	// 信号值
	private int				value;

	// 点的宽度
	private int				width	= 1;

	public PointFgr()
	{
	}

	@Override
	protected void fillShape(Graphics graphics)
	{
		Rectangle r = getBounds();
		int x = r.x;
		int y = r.y;
		graphics.setBackgroundColor(new Color(null, this.getField().getColor()));
		graphics.fillOval(x, y, this.width, this.width);
	}

	/**
	 * 获得信号集合
	 * 
	 * @return 信号集合
	 */
	public synchronized FieldElementSet getField()
	{
		return field;
	}

	/**
	 * 获得点的高度
	 * 
	 * @return 点的高度
	 */
	public synchronized int getHeight()
	{
		return height;
	}

	/**
	 * 获得点的时间戳
	 * 
	 * @return 点的时间戳
	 */
	public synchronized long getTime()
	{
		return time;
	}

	/**
	 * 获得点的值
	 * 
	 * @return 点的值
	 */
	public synchronized int getValue()
	{
		return value;
	}

	/**
	 * 获得点的宽度
	 * 
	 * @return 点的宽度
	 */
	public synchronized int getWidth()
	{
		return width;
	}

	@Override
	protected void outlineShape(Graphics graphics)
	{
		Rectangle r = getBounds();
		int x = r.x;
		int y = r.y;
		graphics.setForegroundColor(new Color(null, this.getField().getColor()));
		graphics.setLineWidth(1);
		graphics.drawOval(x, y, this.width, this.width);
	}

	/**
	 * 设置信号集合
	 * 
	 * @param field 信号集合
	 */
	public synchronized void setField(FieldElementSet field)
	{
		this.field = field;
	}

	/**
	 * 设置点的高度
	 * 
	 * @param height 设置点的高度
	 */
	public synchronized void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * 设置点的时间戳
	 * 
	 * @param time 点的时间戳
	 */
	public synchronized void setTime(long time)
	{
		this.time = time;
	}

	/**
	 * 设置点的值
	 * 
	 * @param value 点的值
	 */
	public synchronized void setValue(int value)
	{
		this.value = value;
	}

	/**
	 * 设置点的宽度
	 * 
	 * @param width 宽度
	 */
	public synchronized void setWidth(int width)
	{
		this.width = width;
	}

}