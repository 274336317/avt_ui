package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.coretek.spte.core.commands.DelConnCmd;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ����ɾ�����ӵ�����
 * 
 * @author ���Ρ
 * @date 2010-8-21
 * 
 */
public class ConnEditPolicy extends ComponentEditPolicy
{

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest)
	{
		AbtConnMdl conn = (AbtConnMdl) getHost().getModel();
		DelConnCmd cmd = new DelConnCmd();
		cmd.setConnection(conn);
		cmd.setSource(conn.getSource());
		cmd.setTarget(conn.getTarget());
		return cmd;
	}
}
