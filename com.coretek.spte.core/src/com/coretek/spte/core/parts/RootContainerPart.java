package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.RootContainerMdl;

/**
 * ÈÝÆ÷
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-18
 * 
 */
public class RootContainerPart extends AbstractGraphicalEditPart implements PropertyChangeListener
{

	protected List<AbtElement> getModelChildren()
	{
		return ((RootContainerMdl) this.getModel()).getChildren();
	}

	public void activate()
	{
		super.activate();
		((RootContainerMdl) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate()
	{
		super.deactivate();
		((RootContainerMdl) getModel()).removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		String prop = evt.getPropertyName();
		if (RootContainerMdl.PRO_CHILD.equals(prop))
			refreshChildren();
		if (RootContainerMdl.PROP_CHANGE.equals(prop))
			refreshChildren();
	}

	protected IFigure createFigure()
	{
		Figure figure = new FreeformLayer();
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	protected void createEditPolicies()
	{
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new
		// RootContainerLayoutEditPolicy());
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
	}
}