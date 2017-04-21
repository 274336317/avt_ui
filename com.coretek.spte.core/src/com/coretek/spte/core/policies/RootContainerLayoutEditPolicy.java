package com.coretek.spte.core.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.spte.core.commands.ChangePostilConstraintCmd;
import com.coretek.spte.core.commands.CreateContainerCmd;
import com.coretek.spte.core.commands.MoveNodeCmd;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.parts.ContainerPart;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class RootContainerLayoutEditPolicy extends AbstractXYLayoutPolicy
{

	protected Command createAddCommand(EditPart child, Object constraint)
	{
		return null;
	}

	protected Command createChangeConstraintCommand(EditPart child, Object constraint)
	{
		if (child.getModel() instanceof PostilMdl)
		{
			ChangePostilConstraintCmd command = new ChangePostilConstraintCmd((PostilMdl) child.getModel(), (Rectangle) constraint);
			return command;
		}
		if (!(child instanceof ContainerPart))
			return null;
		if (!(constraint instanceof Rectangle))
			return null;
		MoveNodeCmd cmd = new MoveNodeCmd();
		cmd.setNode((AbtNode) child.getModel());
		cmd.setLocation(((Rectangle) constraint).getLocation());
		return cmd;

	}

	protected Command getCreateCommand(CreateRequest request)
	{
		Command command = super.getCreateCommand(request);
		if (command != null)
		{
			return command;
		}
		CreateContainerCmd cmd = new CreateContainerCmd();
		cmd.setDiagram((RootContainerMdl) getHost().getModel());
		cmd.setNode((AbtNode) request.getNewObject());

		Rectangle constraint = (Rectangle) getConstraintFor(request);
		cmd.setLocation(constraint.getLocation());
		return cmd;
	}

	protected Command getDeleteDependantCommand(Request request)
	{
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child)
	{

		return new NoHandlePolicy();
	}
}