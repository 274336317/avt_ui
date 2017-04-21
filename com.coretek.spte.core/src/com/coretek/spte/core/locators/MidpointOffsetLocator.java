package com.coretek.spte.core.locators;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * 定位器，用于定位消息连线的标签的位置
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class MidpointOffsetLocator extends MidpointLocator
{
	private Point	offset;

	public MidpointOffsetLocator(Connection c, int i)
	{
		super(c, i);
		PointList points = c.getPoints();
		offset = new Point(points.getPoint(0).x, points.getPoint(0).y - 10);
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
