package com.coretek.spte.core.locators;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * ���ڱ궨ʱ������ͼ�ϵı�ǩλ���Լ���С
 * 
 * @author ���Ρ
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
		// �е�λ��
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
