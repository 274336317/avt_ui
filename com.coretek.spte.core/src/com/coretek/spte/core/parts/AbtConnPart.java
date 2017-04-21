package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.coretek.spte.core.figures.MsgFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.policies.ConnDirectEditPolicy;
import com.coretek.spte.core.policies.ConnEditPolicy;
import com.coretek.spte.core.policies.ConnSelectMsgPolicy;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 连接消息的基类
 * 
 * @author 孙大巍
 * @date 2010-9-6
 * 
 */
public abstract class AbtConnPart extends AbstractConnectionEditPart implements PropertyChangeListener
{

	public void propertyChange(PropertyChangeEvent evt)
	{
		String pName = evt.getPropertyName();
		if (pName.equals(AbtConnMdl.PROP_NAME))
		{
			refreshVisuals();
		} else if (pName.equals(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE))
		{
			refreshVisuals();
		} else if (pName.equals(AbtConnMdl.PROP_COLOR))
		{
			this.refreshVisuals();
		}
	}

	@Override
	public void activate()
	{
		if (isActive())
		{
			return;
		}
		super.activate();
		((AbtConnMdl) getModel()).addPropertyChangeListener(this);

	}

	@Override
	public void deactivate()
	{
		if (!isActive())
		{
			return;
		}
		super.deactivate();
		((AbtConnMdl) getModel()).removePropertyChangeListener(this);

	}

	@Override
	protected void refreshVisuals()
	{
		Point loc = new Point(0, 0);
		Dimension size = new Dimension(149, 40);
		Rectangle rectangle = new Rectangle(loc, size);
		
		this.getModel();
		
		((MsgFgr) this.getFigure()).setName(((AbtConnMdl) this.getModel()).getName());
		((MsgFgr) this.getFigure()).setColor(((AbtConnMdl) this.getModel()).getColor());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
	}

	@Override
	protected void createEditPolicies()
	{
		// 支持删除消息连线
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnEditPolicy());
		// 支持鼠标对消息连线的选中操作
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		// 支持直接编辑
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnDirectEditPolicy());
		// 选择消息后，修改名字
		installEditPolicy(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE, new ConnSelectMsgPolicy());
	}

	@Override
	public IFigure getFigure()
	{
		if (figure == null)
			setFigure(createFigure());
		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);
	}
}
