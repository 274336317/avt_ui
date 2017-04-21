package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 生命线控制器
 * 
 * @author 孙大巍
 * @date 2010-8-18
 * 
 */
public abstract class TestPart extends AbtPart
{

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		String pName = evt.getPropertyName();
		if (evt.getPropertyName().equals(AbtNode.PROP_LOCATION))
		{
			if (((TestMdl) this.getModel()).getLocation().x < ((ContainerMdl) ((ContainerPart) this.getParent()).getModel()).getLocation().x)
			{
				Point loc = new Point(50, ((TestMdl) this.getModel()).getLocation().y);
				((TestMdl) this.getModel()).setLocation(loc);
			}
			((ContainerPart) this.getParent()).refresh();
		}
		if (pName.equals(TestMdl.PRO_FIGURE))
		{
			this.refreshVisuals();
		}
		// 孩子节点被改变了
		if (pName.equals(TestMdl.PRO_CHILD))
		{
			this.refreshAll();
			this.refreshChildren();
		}
		// 大小被改变了
		if (pName.equals(TestMdl.PROP_SIZE))
		{
			this.refreshChildren();
			this.refreshAll();
		}
		if (pName.equals(SPTEConstants.CHANGE_TESTED_OBJECT_REQUEST_TYPE))
		{
			this.refreshVisuals();
		}
	}

	@Override
	public void activate()
	{
		if (!isActive())
		{
			super.activate();
		}
	}

	@Override
	public void deactivate()
	{
		if (isActive())
		{
			super.deactivate();
		}
	}

	public void refreshParent()
	{
		this.getParent().refresh();
	}

	public void refreshAll()
	{
		this.refresh();
		TestNodeContainerPart part = null;
		for (int i = 0; i < this.getChildren().size(); i++)
		{
			part = (TestNodeContainerPart) this.getChildren().get(i);
			part.refreshAll();
		}
	}

	protected List<AbtElement> getModelChildren()
	{

		return ((TestMdl) getModel()).getChildren();
	}

	protected TestNodeContainerMdl getNodeContainer()
	{
		return (TestNodeContainerMdl) ((TestMdl) getModel()).getRoot();
	}

	protected List<AbtConnMdl> getModelSourceConnections()
	{

		return Collections.EMPTY_LIST;
	}

	protected List<AbtConnMdl> getModelTargetConnections()
	{

		return Collections.EMPTY_LIST;
	}

}