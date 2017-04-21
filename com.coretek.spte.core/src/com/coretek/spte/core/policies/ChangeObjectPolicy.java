package com.coretek.spte.core.policies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.coretek.spte.core.commands.SetEmulatorCmd;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 修改被测对象名
 * 
 * @author lifs
 * @date 2010-08-23
 */
public class ChangeObjectPolicy extends AbstractEditPolicy
{

	public Command getCommand(Request request)
	{
		if (request.getType().equals(SPTEConstants.CHANGE_TESTED_OBJECT_REQUEST_TYPE))
		{
			SetEmulatorCmd command = new SetEmulatorCmd((TestMdl) getHost().getModel());
			return command;
		}
		return null;
	}

}
