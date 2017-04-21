package com.coretek.spte.core.tools;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import com.coretek.spte.core.figures.TestNodeFgr;
import com.coretek.spte.core.models.TestNodeMdl;

/**
 * 监听鼠标在node上的动作
 * 
 * @author 孙大巍
 * 
 *         2011-5-28
 */
public class NodeMouseMotionListener implements MouseMotionListener
{

	private TestNodeMdl	mdl;

	public NodeMouseMotionListener(TestNodeMdl mdl)
	{
		this.mdl = mdl;
	}

	public void mouseDragged(MouseEvent me)
	{

	}

	public void mouseEntered(MouseEvent me)
	{
		TestNodeFgr figure = (TestNodeFgr) me.getSource();
		figure.setCursor(new Cursor(null, SWT.CURSOR_HAND));
		if (!this.mdl.isSelected())
			this.mdl.setSelected(true);
	}

	public void mouseExited(MouseEvent me)
	{
		TestNodeFgr figure = (TestNodeFgr) me.getSource();
		figure.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
		if (this.mdl.isSelected())
			this.mdl.setSelected(false);
	}

	public void mouseHover(MouseEvent me)
	{
		TestNodeFgr figure = (TestNodeFgr) me.getSource();
		figure.setCursor(new Cursor(null, SWT.CURSOR_HAND));
		if (!this.mdl.isSelected())
			this.mdl.setSelected(true);
	}

	public void mouseMoved(MouseEvent me)
	{

	}
}