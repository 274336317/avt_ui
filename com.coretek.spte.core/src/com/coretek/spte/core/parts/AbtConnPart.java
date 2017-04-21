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
 * ������Ϣ�Ļ���
 * 
 * @author ���Ρ
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
		// ֧��ɾ����Ϣ����
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnEditPolicy());
		// ֧��������Ϣ���ߵ�ѡ�в���
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		// ֧��ֱ�ӱ༭
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnDirectEditPolicy());
		// ѡ����Ϣ���޸�����
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
