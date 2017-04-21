/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.SWT;

/**
 * 边界线
 * 
 * @author 孙大巍
 * @date 2012-2-3
 */
public class DivisionLine extends Shape
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-3
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-3
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.setLineStyle(SWT.LINE_DASHDOT);
		graphics.setLineWidth(2);
		graphics.setForegroundColor(ColorConstants.lightBlue);
		graphics.fillRectangle(this.getBounds());

	}

}