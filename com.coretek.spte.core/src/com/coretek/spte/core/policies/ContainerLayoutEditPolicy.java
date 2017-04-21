package com.coretek.spte.core.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.spte.core.commands.ChangeNodeConstraintCmd;
import com.coretek.spte.core.commands.ChangePostilConstraintCmd;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.parts.TestPart;

/**
 * 布局策略，支持对图形的重新定位，目前只支持按x轴移动
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class ContainerLayoutEditPolicy extends AbstractXYLayoutPolicy
{

	public ContainerLayoutEditPolicy()
	{
		super();
	}

	protected boolean isHorizontal()
	{
		return false;
	}

	protected Command createChangeConstraintCommand(EditPart child, Object constraint)
	{
		if (child.getModel() instanceof PostilMdl)
		{
			ChangePostilConstraintCmd command = new ChangePostilConstraintCmd((PostilMdl) child.getModel(), (Rectangle) constraint);
			return command;
		}
		if (!(child instanceof TestPart))
			return null;
		if (!(constraint instanceof Rectangle))
			return null;
		ChangeNodeConstraintCmd cmd = new ChangeNodeConstraintCmd((TestPart) this.getHost().getChildren().get(0));
		cmd.setNode((TestMdl) child.getModel());
		cmd.setLocation(((Rectangle) constraint).getLocation());
		cmd.setDimension(((Rectangle) constraint).getSize());

		return cmd;
	}

	protected Command getCreateCommand(CreateRequest request)
	{
		Command command = super.getCreateCommand(request);
		return command;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child)
	{

		return new NoHandlePolicy();
	}

}