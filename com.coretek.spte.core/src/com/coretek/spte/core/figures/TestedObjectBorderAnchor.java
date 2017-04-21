package com.coretek.spte.core.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * 被测对象的锚点
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class TestedObjectBorderAnchor extends ChopboxAnchor
{

	public TestedObjectBorderAnchor(IFigure figure)
	{
		super(figure);
	}

	public Point getLocation(Point reference)
	{
		Point p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x)
		{
			p = ((TestedObjectFgr) getOwner()).getLeftAnchorLoc();
			p.y += 200;
		} else
		{
			p = ((TestedObjectFgr) getOwner()).getRightAnchorLoc();
		}
		getOwner().translateToAbsolute(p);
		return p;
	}
}
