/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;

/**
 * ʱ����ͼ��
 * 
 * @author ���Ρ
 * @date 2012-1-6
 */
public class TimerFgr extends Shape implements PropertyChangeListener
{

	// ÿ���ڵ���ռ�ĸ߶�
	private int	nodeHeight;

	// ʱ��̶ȵĸ���
	private int	sum;

	// ÿ���̶��������ʱ��
	private int	scale		= 5;

	// ��һ���̶��������ʱ��
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

		// ÿ���ڵ���ռ�ĸ߶�
		nodeHeight = Sequence.NODE_HEIGHT + Sequence.NODE_INTERVAL;
		// �ڵ�ĸ���
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
	 * PropertyChangeEvent) <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-2
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