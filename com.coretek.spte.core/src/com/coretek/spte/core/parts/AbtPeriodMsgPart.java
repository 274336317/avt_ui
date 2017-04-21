package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;

import com.coretek.spte.core.figures.AbtConnFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtCycleMsgMdl;

/**
 * 周期消息控制器的基类
 * 
 * @author 孙大巍
 * @date 2010-8-31
 * 
 */
public abstract class AbtPeriodMsgPart extends AbtConnPart
{

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		String pName = evt.getPropertyName();
		if (evt.getPropertyName() == AbtCycleMsgMdl.PROP_COLOR)
		{
			AbtCycleMsgMdl model = (AbtCycleMsgMdl) this.getModel();
			if (!model.getDefaultColor().equals((Color) evt.getNewValue()))
			{
				((AbtConnFgr) this.getFigure()).setColor(((AbtConnMdl) this.getModel()).getColor());
			} else
			{
				((AbtConnFgr) this.getFigure()).setColor(model.getDefaultColor());
			}

			this.refreshVisuals();
		} else if (pName.equals(AbtConnMdl.PROP_NAME))
		{
			refreshVisuals();
		}
	}

	@Override
	public void activate()
	{
		super.activate();
		((AbtCycleMsgMdl) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate()
	{
		super.deactivate();
		((AbtCycleMsgMdl) getModel()).removePropertyChangeListener(this);
	}

	@Override
	protected void refreshVisuals()
	{
		Point loc = new Point(0, 0);
		Dimension size = new Dimension(149, 40);
		Rectangle rectangle = new Rectangle(loc, size);
		((AbtConnFgr) this.getFigure()).setName(((AbtConnMdl) this.getModel()).getName());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
	}
}