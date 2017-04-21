package com.coretek.spte.core.locators;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.core.figures.AbtConnFgr;

/**
 * 定位器，用于定位时间标签的坐标以及大小
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class FKConnectionLabelEditorLocator implements CellEditorLocator
{

	private AbtConnFgr	nodeFigure;

	public FKConnectionLabelEditorLocator(AbtConnFgr nodeFigure)
	{
		this.nodeFigure = nodeFigure;
	}

	public void relocate(CellEditor celleditor)
	{
		Text text = (Text) celleditor.getControl();
		Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle rect = this.nodeFigure.getLabel().getBounds();
		this.nodeFigure.getLabel().translateToAbsolute(rect);
		text.setBounds(rect.x, rect.y, pref.x + 1, pref.y + 1);
	}
}