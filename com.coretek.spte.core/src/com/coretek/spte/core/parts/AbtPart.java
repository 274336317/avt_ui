package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.coretek.spte.core.models.AbtNode;

/**
 * 控制器基类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public abstract class AbtPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart
{

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(AbtNode.PROP_LOCATION))
			refreshVisuals();
		else if (evt.getPropertyName().equals(AbtNode.PROP_NAME))
			refreshVisuals();
		else if (evt.getPropertyName().equals(AbtNode.PROP_INPUTS))
			refreshTargetConnections();
		else if (evt.getPropertyName().equals(AbtNode.PROP_OUTPUTS))
			refreshSourceConnections();
	}

	@Override
	public void activate()
	{
		if (isActive())
		{
			return;
		}
		((AbtNode) getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	@Override
	public void deactivate()
	{
		if (!isActive())
		{
			return;
		}
		((AbtNode) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	protected void createEditPolicies()
	{

	}
}