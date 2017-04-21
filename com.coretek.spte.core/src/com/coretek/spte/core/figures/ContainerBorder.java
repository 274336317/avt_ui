package com.coretek.spte.core.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class ContainerBorder extends AbstractBorder
{

	public Insets getInsets(IFigure figure)
	{
		return new Insets(1, 0, 0, 0);
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets)
	{
		int x = getPaintRectangle(figure, insets).getTopLeft().x;
		int y = getPaintRectangle(figure, insets).getTopLeft().y + 10;
		Point left = new Point(x, y);
		x = getPaintRectangle(figure, insets).getTopRight().x;
		y = getPaintRectangle(figure, insets).getTopRight().y + 10;
		Point right = new Point(x, y);
		graphics.setLineWidth(1);
		graphics.drawLine(left, right);
	}
}