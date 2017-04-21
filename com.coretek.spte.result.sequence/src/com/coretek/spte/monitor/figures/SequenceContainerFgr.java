/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;

/**
 * @author 孙大巍
 * @date 2012-1-6
 */
public class SequenceContainerFgr extends Shape
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 * <br/>
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		graphics.fillRectangle(this.getBounds());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 * <br/>
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.setForegroundColor(ColorConstants.red);

	}

	/**
	 * 删除所有的消息图形 </br>
	 */
	public void removeAllMsgs()
	{
		List list = this.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext())
		{
			Object obj = it.next();
			if (obj instanceof PolylineConnection)
			{
				it.remove();
			}
		}

		this.repaint();
	}

}