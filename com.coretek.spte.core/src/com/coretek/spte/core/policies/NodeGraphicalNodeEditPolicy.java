package com.coretek.spte.core.policies;

import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.coretek.spte.core.commands.BackgroundMsgCreationCmd;
import com.coretek.spte.core.commands.IntervalCreationCmd;
import com.coretek.spte.core.commands.MsgCreationCmd;
import com.coretek.spte.core.commands.ParallelCreationCmd;
import com.coretek.spte.core.commands.PeriodMsgCreationCmd;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * ���� ���������ӵ�����
 * 
 * @author ���Ρ
 * @date 2010-8-21
 */
public class NodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy
{

	@SuppressWarnings("unchecked")
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
	{
		Map<String, String> map = request.getExtendedData();
		String type = map.get(SPTEConstants.REQ_TYPE_KEY);
		Command cmd = null;
		if (type.equals(SPTEConstants.REQ_TYPE_FIXED_MESSAGE))
		{
			// ����������Ϣ�ĸ���������
			PeriodMsgCreationCmd command = (PeriodMsgCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_TIMER))
		{
			// ����ʱ����
			IntervalCreationCmd command = (IntervalCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_BACKGROUND))
		{
			// ����������Ϣ
			BackgroundMsgCreationCmd command = (BackgroundMsgCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_PARALLEL))
		{
			// ����������Ϣ
			ParallelCreationCmd command = (ParallelCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else
		{
			// ������ͨ��Ϣ����
			MsgCreationCmd command = (MsgCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		return cmd;
	}

	@SuppressWarnings("unchecked")
	protected Command getConnectionCreateCommand(CreateConnectionRequest request)
	{
		Map<String, String> map = request.getExtendedData();
		String type = map.get(SPTEConstants.REQ_TYPE_KEY);
		Command cmd = null;

		if (type.equals(SPTEConstants.REQ_TYPE_FIXED_MESSAGE))
		{
			// ����������Ϣ�ĸ���������
			PeriodMsgCreationCmd command = new PeriodMsgCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_TIMER))
		{
			IntervalCreationCmd command = new IntervalCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_BACKGROUND))
		{
			// ����������Ϣ
			BackgroundMsgCreationCmd command = new BackgroundMsgCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_PARALLEL))
		{
			// ����������Ϣ
			ParallelCreationCmd command = new ParallelCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		else
		{
			MsgCreationCmd command = new MsgCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		return cmd;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request)
	{
		return null;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request)
	{
		return null;
	}
}