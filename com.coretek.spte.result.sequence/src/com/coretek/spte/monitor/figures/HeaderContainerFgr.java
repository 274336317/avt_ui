/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;

/**
 * 头容器
 * 
 * @author 孙大巍
 * @date 2012-1-6
 */
public class HeaderContainerFgr extends Shape
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.fillRectangle(this.getBounds());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#repaint()
	 */
	@Override
	public void repaint()
	{
		super.repaint();
	}

}
