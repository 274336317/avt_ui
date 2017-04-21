package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.DelPostilCmd;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PostilMdl;

public class PostilEditPolicy extends ComponentEditPolicy
{
	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		ContainerMdl rootContainerMdl = (ContainerMdl) getHost().getParent().getModel();
		PostilMdl postilMdl = (PostilMdl) getHost().getModel();

		DelPostilCmd deleteCommand = new DelPostilCmd(rootContainerMdl, postilMdl);

		return deleteCommand;
	}
}
