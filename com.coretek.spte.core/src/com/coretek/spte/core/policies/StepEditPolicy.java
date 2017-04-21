package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.TestedObjectDeletionCmd;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;

/**
 * 创建删除 测试工具/被测对象 命令
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class StepEditPolicy extends ComponentEditPolicy
{
	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		TestedObjectDeletionCmd deleteCommand = new TestedObjectDeletionCmd();
		deleteCommand.setSubTransModel((ContainerMdl) getHost().getParent().getModel());
		deleteCommand.setNode((TestMdl) getHost().getModel());

		return deleteCommand;
	}
}
