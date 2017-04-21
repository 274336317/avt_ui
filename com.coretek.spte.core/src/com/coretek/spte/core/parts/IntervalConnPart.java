package com.coretek.spte.core.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.window.Window;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.dialogs.IntervalDlg;
import com.coretek.spte.core.figures.TimeSpanFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.tools.ConnMouseMotionListener;

/**
 * 消息时间间隔控制器
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class IntervalConnPart extends AbtConnPart
{

	@Override
	public void performRequest(Request req)
	{
		super.performRequest(req);
		if (req.getType().equals(RequestConstants.REQ_OPEN))
		{
			// 显示消息时间间隔的请求
			IntervalDlg dialog = new IntervalDlg(SPTEPlugin.getActiveWorkbenchShell(), (AbtConnMdl) this.getModel(), this);
			if (dialog.open() == Window.OK)
			{
				dialog.setName();
			}
		}
	}

	@Override
	protected IFigure createFigure()
	{
		TimeSpanFgr figure = new TimeSpanFgr((AbtConnMdl) this.getModel());
		figure.addMouseMotionListener(new ConnMouseMotionListener((AbtConnMdl) this.getModel()));
		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
		{
			((AbtConnMdl) this.getModel()).setColor(ColorConstants.orange);
			((AbtConnMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
		} else
		{
			((AbtConnMdl) this.getModel()).setDefaultColor(InstanceUtils.getInstance().getIntervalColor());
			((AbtConnMdl) this.getModel()).setColor(((AbtConnMdl) this.getModel()).getDefaultColor());
		}
	}

	@Override
	protected void refreshVisuals()
	{
		Point loc = new Point(0, 0);
		Dimension size = new Dimension(149, 40);
		Rectangle rectangle = new Rectangle(loc, size);
		((TimeSpanFgr) this.getFigure()).setName(((AbtConnMdl) this.getModel()).getName());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
	}
}
