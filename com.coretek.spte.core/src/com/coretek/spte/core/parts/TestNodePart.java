package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;

import com.coretek.spte.core.figures.ItemBorderAnchor;
import com.coretek.spte.core.figures.TestNodeFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.policies.NodeGraphicalNodeEditPolicy;
import com.coretek.spte.core.tools.NodeMouseMotionListener;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 连线节点控制器，主要负责创建视图以及监听模型的属性改变
 * 
 * @author 孙大巍
 * @date 2010-8-21
 */
public class TestNodePart extends AbtPart
{

	public IFigure createFigure()
	{
		return new TestNodeFgr((TestNodeMdl) this.getModel());
	}

	public IFigure getFigure()
	{
		TestNodeMdl mdl = (TestNodeMdl) this.getModel();
		IFigure fgr = super.getFigure();
		fgr.addMouseMotionListener(new NodeMouseMotionListener(mdl));
		return fgr;
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(AbtNode.PROP_LOCATION))
			refreshVisuals();
		else if (evt.getPropertyName().equals(AbtNode.PROP_NAME))
			refreshItemName();
		else if (evt.getPropertyName().equals(AbtNode.PROP_INPUTS))
			refreshTargetConnections();
		else if (evt.getPropertyName().equals(AbtNode.PROP_OUTPUTS))
			refreshSourceConnections();
		else if (evt.getPropertyName().equals(AbtElement.PRO_CHILD))
		{
			this.refreshChildren();
		}
		else if (SPTEConstants.EVENT_PROP_SELECTED.equals(evt.getPropertyName()))
		{
			this.refreshVisuals();
		}
		else if (evt.getPropertyName().equals(SPTEConstants.EVENT_STATUS_CHANGED))
		{
			this.refreshVisuals();
		}
	}

	@Override
	protected void createEditPolicies()
	{
		TestNodeMdl model = (TestNodeMdl) this.getModel();
		if (model.getType() == TestNodeMdl.TYPE_COLUMN || model.getType() == TestNodeMdl.TYPE_COLUMN2)
		{
			// 支持创建连线
			installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NodeGraphicalNodeEditPolicy());
		}
	}

	protected List<AbtElement> getModelChildren()
	{
		return ((TestNodeMdl) getModel()).getChildren();
	}

	protected void refreshVisuals()
	{
		TestNodeFgr ti = (TestNodeFgr) this.getFigure();
		TestNodeMdl node = (TestNodeMdl) getModel();
		// 根据 Mode.Location 判断 Figure.Bounds是否需要更新
		if (ti.getBounds().y == 0 /**(ti.getBounds().y != node.getLocation().y + 60) && (ti.getBounds().y != node.getLocation().y - 8)**/)
		{
			Point loc = node.getLocation();
			
			Dimension size = node.refreshRegion();
			Rectangle rectangle = new Rectangle(loc, size);
			ti.setBounds(rectangle);
		}
		else
		{
			ti.setBackgroundColor(ColorConstants.orange);
			ti.setForegroundColor(ColorConstants.orange);
		}

	}

	private void refreshItemName()
	{
		TestNodeFgr ti = (TestNodeFgr) this.getFigure();
		TestNodeMdl node = (TestNodeMdl) getModel();
		ti.setName(node.getName());
	}

	public void refreshAll()
	{
		refreshVisuals();
		refreshSourceConnections();
		refreshTargetConnections();
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

	protected List<AbtConnMdl> getModelSourceConnections()
	{
		return getSubject().getShowOutgoingConnections();
	}

	protected List<AbtConnMdl> getModelTargetConnections()
	{
		return getSubject().getShowIncomingConnections();
	}
}
