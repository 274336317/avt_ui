package com.coretek.spte.core.locators;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * ����������Ϣ�е�ʡ�Ժű�ǩ��λ��
 * 
 * @author ���Ρ
 * @date 2010-9-1
 * 
 */
public class MidpointOffsetLocatorForDot extends MidpointLocator
{

	private Point	offset;

	public MidpointOffsetLocatorForDot(Connection c, int i, int increment)
	{
		super(c, i);
		PointList points = c.getPoints();
		offset = new Point(points.getPoint(0).x, points.getPoint(0).y + increment);
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
