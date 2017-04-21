package com.coretek.spte.core.tools;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

/**
 * 监听测试模型上的鼠标动作
 * 
 * @author 孙大巍
 * 
 *         2011-5-28
 */
public class TestMdlMouseMotionListener implements MouseMotionListener
{

	public void mouseDragged(MouseEvent me)
	{
		this.handleEvent(me);
	}

	public void mouseEntered(MouseEvent me)
	{
		this.handleEvent(me);
	}

	public void mouseExited(MouseEvent me)
	{
		IFigure fgr = (IFigure) me.getSource();
		fgr.setCursor(new Cursor(null, SWT.CURSOR_ARROW));

	}

	public void mouseHover(MouseEvent me)
	{
		this.handleEvent(me);
	}

	public void mouseMoved(MouseEvent me)
	{
		this.handleEvent(me);
	}

	private void handleEvent(MouseEvent me)
	{
		IFigure fgr = (IFigure) me.getSource();
		if (me.y < 110)
			fgr.setCursor(new Cursor(null, SWT.CURSOR_SIZEALL));
		else
		{
			fgr.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
		}
	}
}