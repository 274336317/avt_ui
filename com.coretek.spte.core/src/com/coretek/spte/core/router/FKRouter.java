package com.coretek.spte.core.router;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Ray;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * 连线路由，共有四个点 第一个点，即源点 第二个点，从源点开始水平一段距离 第三个点，从第二个点开始，垂直一段距离，距离=end.y-start.y
 * 第四个点，即连线的目标点
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
		{// 同一点,不需要画出
			return;
		}

		this.processPositions(start, end, conn);
	}

	private void processPositions(Ray start, Ray end, Connection conn)
	{
		PointList points = new PointList();
		points.addPoint(new Point(start.x, start.y));

		Point p = new Point(0, 0);// 右上点

		p.x = start.x + SPTEConstants.FK_LINE_OFFSET;
		p.y = start.y;
		points.addPoint(p);

		p = new Point(0, 0);// 右下点
		p.x = start.x + SPTEConstants.FK_LINE_OFFSET;
		p.y = end.y;
		points.addPoint(p);

		points.addPoint(new Point(end.x, end.y));
		conn.setPoints(points);
	}
}
