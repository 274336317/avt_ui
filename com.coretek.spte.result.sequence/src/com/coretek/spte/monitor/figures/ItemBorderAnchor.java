package com.coretek.spte.monitor.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class ItemBorderAnchor extends ChopboxAnchor
{
	public ItemBorderAnchor(IFigure figure)
	{
		super(figure);
	}

	public Point getLocation(Point reference)
	{
		Point p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x)
		{
			p = ((NodeFgr) getOwner()).getLeftAnchorLoc();
		} else
		{
			p = ((NodeFgr) getOwner()).getRightAnchorLoc();
		}
		getOwner().translateToAbsolute(p);
		return p;
	}
}