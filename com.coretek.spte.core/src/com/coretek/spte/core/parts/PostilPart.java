package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DirectEditManager;

import com.coretek.spte.core.figures.PostilFgr;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.policies.PostilEditPolicy;

/**
 * 标签模型控制器
 * 
 * @author duyisen 2012-3-15
 */
public class PostilPart extends AbtPart
{

	protected DirectEditManager manager;

	public IFigure createFigure()
	{
		return new PostilFgr((PostilMdl) this.getModel());
	}

	public IFigure getFigure()
	{
		IFigure fgr = super.getFigure();
		fgr.setCursor(Cursors.HAND);
		return fgr;
	}

	protected void refreshVisuals()
	{
		PostilFgr ti = (PostilFgr) this.getFigure();
		PostilMdl node = (PostilMdl) getModel();

		if ((ti.getBounds().y != node.getLocation().y))
		{
			Point loc = node.getLocation();
			Dimension size = node.refreshRegion();
			Rectangle rectangle = new Rectangle(loc, size);
			ti.setBounds(rectangle);
			if (null != node.getRightChildrenMdl())
			{
				Rectangle re = node.getRightChildrenMdl().getConstraints();
				// 设置标签右边边X起始位置
				Rectangle childRectangle = new Rectangle(loc.x, loc.y + 2, re.width, re.height);
				node.getRightChildrenMdl().setConstraints(childRectangle);
			}
			if (null != node.getLeftChildrenMdl())
			{
				Rectangle le = node.getLeftChildrenMdl().getConstraints();
				// 设置标签左边X起始位置
				Rectangle childRectangle = new Rectangle(loc.x + 520, loc.y + 2, le.width, le.height);
				node.getLeftChildrenMdl().setConstraints(childRectangle);
			}
		}
		else
		{
			ti.setBackgroundColor(ColorConstants.orange);
			ti.setForegroundColor(ColorConstants.orange);
		}
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		String pName = evt.getPropertyName();

		if (pName.equals(TestMdl.PRO_CHILD))
		{
			this.refreshChildren();
			this.refreshVisuals();
		}
		else if (pName.equals(PostilMdl.P_CONSTRAINT))
		{
			this.refreshVisuals();
		}
	}

	@Override
	protected void createEditPolicies()
	{
		// 删除标签
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PostilEditPolicy());
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

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return null;
	}

}
