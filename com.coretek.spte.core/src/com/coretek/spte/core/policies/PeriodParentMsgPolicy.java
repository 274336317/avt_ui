package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.DelPeriodMsgCmd;
import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * 创建删除周期消息的命令
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class PeriodParentMsgPolicy extends ComponentEditPolicy
{

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		PeriodParentMsgMdl parentModel = (PeriodParentMsgMdl) getHost().getModel();
		DelPeriodMsgCmd command = new DelPeriodMsgCmd();
		command.setFixedParent(parentModel);
		command.setSource(parentModel.getSource());
		command.setTarget(parentModel.getTarget());
		return command;
	}

}
