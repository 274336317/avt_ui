/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.testcase.curve.internal.model.FieldElementSet;

/**
 * ������
 * 
 * @author ���� 2012-3-14
 */
public class PointFgr extends Shape
{
	// �źż��϶���
	private FieldElementSet	field;

	// ��ĸ߶�
	private int				height	= 1;

	// ʱ���
	private long			time;

	// �ź�ֵ
	private int				value;

	// ��Ŀ��
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
	 * ����źż���
	 * 
	 * @return �źż���
	 */
	public synchronized FieldElementSet getField()
	{
		return field;
	}

	/**
	 * ��õ�ĸ߶�
	 * 
	 * @return ��ĸ߶�
	 */
	public synchronized int getHeight()
	{
		return height;
	}

	/**
	 * ��õ��ʱ���
	 * 
	 * @return ���ʱ���
	 */
	public synchronized long getTime()
	{
		return time;
	}

	/**
	 * ��õ��ֵ
	 * 
	 * @return ���ֵ
	 */
	public synchronized int getValue()
	{
		return value;
	}

	/**
	 * ��õ�Ŀ��
	 * 
	 * @return ��Ŀ��
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
	 * �����źż���
	 * 
	 * @param field �źż���
	 */
	public synchronized void setField(FieldElementSet field)
	{
		this.field = field;
	}

	/**
	 * ���õ�ĸ߶�
	 * 
	 * @param height ���õ�ĸ߶�
	 */
	public synchronized void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * ���õ��ʱ���
	 * 
	 * @param time ���ʱ���
	 */
	public synchronized void setTime(long time)
	{
		this.time = time;
	}

	/**
	 * ���õ��ֵ
	 * 
	 * @param value ���ֵ
	 */
	public synchronized void setValue(int value)
	{
		this.value = value;
	}

	/**
	 * ���õ�Ŀ��
	 * 
	 * @param width ���
	 */
	public synchronized void setWidth(int width)
	{
		this.width = width;
	}

}