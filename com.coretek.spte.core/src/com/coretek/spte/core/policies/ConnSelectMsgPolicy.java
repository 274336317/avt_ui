package com.coretek.spte.core.policies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.coretek.spte.core.commands.RenameCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.request.ChangeLineNameRequest;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 选择消息后 更新消息名命令
 * 
 * @author lifs
 * @date 2010-08-22
 * 
 */
public class ConnSelectMsgPolicy extends AbstractEditPolicy
{

	public Command getCommand(Request request)
	{
		if (request.getType().equals(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE))
		{
			if (request instanceof ChangeLineNameRequest)
			{
				RenameCmd command = new RenameCmd();
				command.setNode((AbtConnMdl) getHost().getModel());
				command.setName(((ChangeLineNameRequest) request).getName());
				return command;
			}
		}
		return null;
	}
}
