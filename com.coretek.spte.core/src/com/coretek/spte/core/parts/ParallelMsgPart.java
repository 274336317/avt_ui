package com.coretek.spte.core.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.figures.MsgFgr;
import com.coretek.spte.core.figures.ParallelFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.tools.ConnMouseMotionListener;

/**
 * 并行消息
 * 
 * @author 孙大巍
 * 
 */
public class ParallelMsgPart extends MsgConnPart
{

	@Override
	protected IFigure createFigure()
	{
		MsgFgr figure = new ParallelFgr((AbtConnMdl) this.getModel());
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
			((AbtConnMdl) this.getModel()).setDefaultColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor());
			((AbtConnMdl) this.getModel()).setColor(((AbtConnMdl) this.getModel()).getDefaultColor());
		}
	}
}