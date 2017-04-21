package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.coretek.spte.core.commands.RenameCmd;
import com.coretek.spte.core.figures.AbtConnFgr;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 编辑直线连接的名字
 * 
 * @author 孙大巍
 * @date 2010-8-18
 * 
 */
public class ConnDirectEditPolicy extends DirectEditPolicy
{

	protected Command getDirectEditCommand(DirectEditRequest request)
	{
		RenameCmd cmd = new RenameCmd();
		cmd.setNode((AbtConnMdl) getHost().getModel());
		cmd.setName((String) request.getCellEditor().getValue());
		return cmd;
	}

	protected void showCurrentEditValue(DirectEditRequest request)
	{
		String value = (String) request.getCellEditor().getValue();
		((AbtConnFgr) this.getHostFigure()).setName(value);
	}
}
