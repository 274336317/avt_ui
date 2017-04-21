package com.coretek.spte.core.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * ���Խڵ�����ͼ��
 * 
 * @author ���Ρ
 * 
 */
public class TestNodeContainerFgr extends Shape
{

	@Override
	protected void fillShape(Graphics graphics)
	{
		
	}

	@Override
	protected void outlineShape(Graphics graphics)
	{
		
	}

	@Override
	public void paintFigure(Graphics graphics)
	{
		graphics.pushState();
		graphics.setBackgroundColor(ColorConstants.red);
		graphics.popState();
	}

	@Override
	public void setBounds(Rectangle rect)
	{
		rect.y -= 8;
		rect.width = 50;
		super.setBounds(rect);
	}
}