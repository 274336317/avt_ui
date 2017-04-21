package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;

import com.coretek.spte.core.figures.ItemBorderAnchor;
import com.coretek.spte.core.figures.TestNodeContainerFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;

/**
 * ²âÊÔ½ÚµãÈÝÆ÷
 * 
 * @author Ëï´óÎ¡
 * 
 */
public class TestNodeContainerPart extends AbtPart
{

	@Override
	public IFigure createFigure()
	{

		return new TestNodeContainerFgr();
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(AbtElement.PRO_CHILD))
		{
			this.refreshChildren();
		}
	}

	@Override
	protected List<AbtElement> getModelChildren()
	{
		return ((TestNodeContainerMdl) getModel()).getChildren();
	}

	@Override
	protected void refreshVisuals()
	{
		TestNodeContainerFgr ti = (TestNodeContainerFgr) this.getFigure();
		if (ti.getBounds().x == 0 && ti.getBounds().y == 0)
		{
			TestNodeContainerMdl node = (TestNodeContainerMdl) getModel();
			Point loc = node.getLocation();
			Dimension size = node.refreshRegion();
			Rectangle rectangle = new Rectangle(loc, size);
			ti.setBounds(rectangle);
		} else
		{
			TestNodeContainerMdl node = (TestNodeContainerMdl) getModel();
			this.getParent().refresh();
			Point loc = node.getLocation().getCopy();
			Dimension size = node.refreshRegion();
			Rectangle rectangle = new Rectangle(loc, size);
			ti.setBounds(rectangle);
		}
	}

	public void refreshAll()
	{
		refreshVisuals();
		TestNodePart part = null;
		for (int i = 0; i < this.getChildren().size(); i++)
		{
			part = (TestNodePart) this.getChildren().get(i);
			part.refreshAll();
		}
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return new ItemBorderAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{
		return new ItemBorderAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{
		return new ItemBorderAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{
		return new ItemBorderAnchor(getFigure());
	}

	protected TestNodeMdl getSubject()
	{
		return (TestNodeMdl) getModel();
	}

	@Override
	protected List<AbtConnMdl> getModelSourceConnections()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<AbtConnMdl> getModelTargetConnections()
	{
		return Collections.EMPTY_LIST;
	}
}