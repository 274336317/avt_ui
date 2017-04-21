package com.coretek.spte.core.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * 测试工具锚点
 * 
 * @author 孙大巍
 * @date 2011-2-21
 */
public class TestToolBorderAnchor extends ChopboxAnchor
{
	public TestToolBorderAnchor(IFigure figure)
	{
		super(figure);
	}

	public Point getLocation(Point reference)
	{
		Point p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x)
		{
			p = ((TestToolFgr) getOwner()).getLeftAnchorLoc();
		} else
		{
			p = ((TestToolFgr) getOwner()).getRightAnchorLoc();
			p.y += 200;
		}
		getOwner().translateToAbsolute(p);
		return p;

	}
}
