package com.coretek.spte.core.locators;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * 用于标定时间间隔视图上的标签位置以及大小
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class FKMidpointOffsetLocator extends MidpointLocator
{
	private Point	offset;

	public FKMidpointOffsetLocator(Connection c, int i)
	{
		super(c, i);
		PointList points = c.getPoints();
		// 中点位置
		offset = new Point(SPTEConstants.FK_LINE_OFFSET + 5, points.getPoint(0).y + 5);
	}

	@Override
	protected Point getReferencePoint()
	{
		Point point = super.getReferencePoint();
		return point.getTranslated(offset);
	}

	public Point getOffset()
	{
		return offset;
	}

	public void setOffset(Point offset)
	{
		this.offset = offset;
	}
}
