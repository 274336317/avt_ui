package com.coretek.spte.core.tools;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.figures.AbtConnFgr;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 监听鼠标在连线上的运动，鼠标移到连线上时，改变连线的颜色
 * 
 * @author 孙大巍
 * @date 2010-11-27
 * 
 */
/**
 * __________________________________________________________________________________
 * 
 * @Class ConnMouseMotionListener.java
 * @Description 监听鼠标在连线上的动作，鼠标连线时，改变连线颜色
 * @Auther MENDY
 * @Date 2016-5-17 下午03:45:08
 */
public class ConnMouseMotionListener implements MouseMotionListener
{

	private AbtConnMdl model;

	public ConnMouseMotionListener(AbtConnMdl model)
	{
		this.model = model;
	}

	public void mouseDragged(MouseEvent me)
	{
		this.setColor2Yellow(me);
	}

	public void mouseEntered(MouseEvent me)
	{
		this.setColor2Yellow(me);
	}

	public void mouseExited(MouseEvent me)
	{
		AbtConnFgr figure = (AbtConnFgr) me.getSource();
		if (this.model.getName().contains("普通") || this.model.getName().contains("背景"))
		{
			model.setColor(InstanceUtils.getInstance().getMesOrBackgroudDefaultColor());
		}
		if (this.model.getName().contains("周期") || this.model.getName().contains("并行"))
		{
			model.setColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor());
		}
		figure.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
	}

	public void mouseHover(MouseEvent me)
	{
		this.setColor2Yellow(me);
	}

	private void setColor2Yellow(MouseEvent me)
	{
		model.setColor(ColorConstants.orange);
		AbtConnFgr figure = (AbtConnFgr) me.getSource();
		figure.setCursor(new Cursor(null, SWT.CURSOR_HAND));
	}

	public void mouseMoved(MouseEvent me)
	{

	}
}