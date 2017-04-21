package com.coretek.spte.core.figures;

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
			p = ((TestNodeFgr) getOwner()).getLeftAnchorLoc();
		} else
		{
			p = ((TestNodeFgr) getOwner()).getRightAnchorLoc();
		}
		getOwner().translateToAbsolute(p);
		return p;
	}
}
