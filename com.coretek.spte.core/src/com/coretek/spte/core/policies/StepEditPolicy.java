package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.TestedObjectDeletionCmd;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;

/**
 * ����ɾ�� ���Թ���/������� ����
 * 
 * @author ���Ρ
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
