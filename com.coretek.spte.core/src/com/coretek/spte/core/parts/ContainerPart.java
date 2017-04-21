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
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;

import com.coretek.spte.core.figures.ContainerFgr;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.policies.ContainerLayoutEditPolicy;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class ContainerPart extends AbtPart
{

	private ContainerFgr	expandedFigure	= new ContainerFgr();

	public ContainerPart()
	{
	}

	public IFigure getContentPane()
	{
		return expandedFigure.getContainerFigure();
	}

	protected IFigure createFigure()
	{
		if (expandedFigure == null)
		{
			expandedFigure = new ContainerFgr();
		}
		return expandedFigure;
	}

	public IFigure getFigure()
	{

		return expandedFigure;
	}

	protected void refreshVisuals()
	{
		Dimension tsize = ((ContainerMdl) getModel()).getSize().getCopy();
		expandedFigure.setName(((AbtNode) this.getModel()).getName());
		Dimension msize = this.calculateSize();
		if (this.getChildren().size() > 1)
		{
			tsize.height = msize.height + 17;
			tsize.width = msize.width;
			ContainerMdl model = (ContainerMdl) getModel();
			model.setSize(tsize);
		} else
		{
			if (msize.width > tsize.width || msize.height > tsize.height)
			{
				tsize.height = msize.height + 17;
				ContainerMdl model = (ContainerMdl) getModel();
				model.setSize(tsize);
			}
		}
		Rectangle rectangle = new Rectangle(getSubject().getLocation(), ((ContainerMdl) getModel()).getSize());
		getFigure().setBounds(rectangle);
	}

	@SuppressWarnings("unchecked")
	private Dimension calculateSize()
	{
		int maxW = 0;
		int maxH = 0;
		List steps = this.getChildren();
		if (steps != null && steps.size() > 0)
		{
			Dimension s = null;
			Point l = null;
			for (int i = 0; i < steps.size(); i++)
			{
				if (steps.get(i) instanceof TestedObjectPart)
				{
					TestedObjectPart temp = (TestedObjectPart) steps.get(i);
					s = ((TestMdl) temp.getModel()).getSize();
					l = ((TestMdl) temp.getModel()).getLocation();
				} else if (steps.get(i) instanceof TestToolPart)
				{
					TestToolPart temp = (TestToolPart) steps.get(i);
					s = ((TestMdl) temp.getModel()).getSize();
					l = ((TestMdl) temp.getModel()).getLocation();
				}

				if (l.x + s.width > maxW)
				{
					maxW = l.x + s.width + 1000;
				}
				if (l.y + s.height > maxH)
				{
					maxH = l.y + s.height + 50;
				}
			}
		}
		return new Dimension(maxW, maxH);
	}

	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ContainerLayoutEditPolicy());
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeEditPolicy());
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new DiagramLayoutPolicy());
	}

	protected List<AbtElement> getModelChildren()
	{
		return getSubject().getChildren();
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		String pName = evt.getPropertyName();
		if (pName.equals(TestMdl.PRO_FIGURE))
		{
			this.refreshVisuals();
		}

		if (pName.equals(TestMdl.PRO_CHILD))
		{
			this.refreshChildren();
			this.refreshVisuals();
		}
	}

	protected ContainerMdl getSubject()
	{
		return (ContainerMdl) getModel();
	}

	@SuppressWarnings("unchecked")
	protected List getModelSourceConnections()
	{
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	protected List getModelTargetConnections()
	{
		return Collections.EMPTY_LIST;
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{

		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{

		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{

		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{

		return null;
	}
}