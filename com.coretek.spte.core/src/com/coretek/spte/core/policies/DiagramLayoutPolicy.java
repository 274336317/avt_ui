package com.coretek.spte.core.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.spte.core.commands.ChangePostilConstraintCmd;
import com.coretek.spte.core.models.PostilMdl;

public class DiagramLayoutPolicy extends AbstractXYLayoutPolicy
{
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint)
	{
		// 添加移动标签位置的命令
		if (child.getModel() instanceof PostilMdl)
		{
			ChangePostilConstraintCmd command = new ChangePostilConstraintCmd((PostilMdl) child.getModel(), (Rectangle) constraint);
			return command;
		}
		return null;

	}

	protected Command getCreateCommand(CreateRequest request)
	{
		Command command = super.getCreateCommand(request);
		return command;
	}

}