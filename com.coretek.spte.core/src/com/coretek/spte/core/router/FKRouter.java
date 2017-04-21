package com.coretek.spte.core.router;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Ray;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * ����·�ɣ������ĸ��� ��һ���㣬��Դ�� �ڶ����㣬��Դ�㿪ʼˮƽһ�ξ��� �������㣬�ӵڶ����㿪ʼ����ֱһ�ξ��룬����=end.y-start.y
 * ���ĸ��㣬�����ߵ�Ŀ���
 * 
 * @author Administrator
 * 
 */
public class FKRouter extends AbstractRouter
{

	public FKRouter()
	{

	}

	public void route(Connection conn)
	{
		if ((conn.getSourceAnchor() == null || (conn.getTargetAnchor() == null)))
		{
			return;
		}
		Point startPoint = this.getStartPoint(conn);
		conn.translateToRelative(startPoint);
		Point endPoint = this.getEndPoint(conn);
		conn.translateToRelative(endPoint);
		Ray start = new Ray(startPoint);
		Ray end = new Ray(endPoint);

		if ((start.x == end.x) && (start.y == end.y))
		{// ͬһ��,����Ҫ����
			return;
		}

		this.processPositions(start, end, conn);
	}

	private void processPositions(Ray start, Ray end, Connection conn)
	{
		PointList points = new PointList();
		points.addPoint(new Point(start.x, start.y));

		Point p = new Point(0, 0);// ���ϵ�

		p.x = start.x + SPTEConstants.FK_LINE_OFFSET;
		p.y = start.y;
		points.addPoint(p);

		p = new Point(0, 0);// ���µ�
		p.x = start.x + SPTEConstants.FK_LINE_OFFSET;
		p.y = end.y;
		points.addPoint(p);

		points.addPoint(new Point(end.x, end.y));
		conn.setPoints(points);
	}
}
