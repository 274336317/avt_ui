/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;

/**
 * 节点
 * 
 * @author 孙大巍
 * @date 2012-1-5
 */
public class NodeFgr extends Shape
{

	private int	xIndex;

	private int	yIndex;

	public NodeFgr(int xIndex, int yIndex)
	{
		this.xIndex = xIndex;
		this.yIndex = yIndex;
	}

	/**
	 * @return the xIndex <br/>
	 * 
	 */
	public int getxIndex()
	{
		return xIndex;
	}

	/**
	 * @return the yIndex <br/>
	 * 
	 */
	public int getyIndex()
	{
		return yIndex;
	}

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
		graphics.setBackgroundColor(ColorConstants.darkGreen);
		graphics.fillRectangle(this.getBounds());

	}

	/**
	 * 获取左边的锚点位置
	 * 
	 * @return
	 */
	public Point getLeftAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		rst.x = this.getBounds().x - 1;
		rst.y = this.getBounds().y + 10;
		return rst;
	}

	/**
	 * 获取右边的锚点位置
	 * 
	 * @return
	 */
	public Point getRightAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		rst.x = this.getBounds().x + 5;
		rst.y = this.getBounds().y + 10;
		return rst;
	}

}
