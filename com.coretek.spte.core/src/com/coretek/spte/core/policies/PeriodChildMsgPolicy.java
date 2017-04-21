package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.DelPeriodMsgCmd;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * 创建删除周期消息的命令
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class PeriodChildMsgPolicy extends ComponentEditPolicy
{

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		PeriodChildMsgMdl childModel = (PeriodChildMsgMdl) getHost().getModel();
		DelPeriodMsgCmd command = new DelPeriodMsgCmd();
		command.setFixedParent((PeriodParentMsgMdl) childModel.getParent());
		command.setSource(((PeriodParentMsgMdl) childModel.getParent()).getSource());
		command.setTarget(((PeriodParentMsgMdl) childModel.getParent()).getTarget());
		return command;
	}

}
