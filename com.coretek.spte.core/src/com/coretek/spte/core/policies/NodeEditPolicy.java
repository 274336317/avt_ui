package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.AddingPostilCmd;
import com.coretek.spte.core.commands.DelNodeCmd;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;

/**
 * ´´½¨É¾³ýÃüÁî
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class NodeEditPolicy extends ComponentEditPolicy
{

	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		DelNodeCmd deleteCommand = new DelNodeCmd();
		deleteCommand.setTransModel((RootContainerMdl) getHost().getParent().getModel());
		deleteCommand.setNode((ContainerMdl) getHost().getModel());

		return deleteCommand;
	}
}
