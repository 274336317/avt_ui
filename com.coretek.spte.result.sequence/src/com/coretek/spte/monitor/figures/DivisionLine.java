/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.SWT;

/**
 * �߽���
 * 
 * @author ���Ρ
 * @date 2012-2-3
 */
public class DivisionLine extends Shape
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 * <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-3
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
	 * <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-3
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