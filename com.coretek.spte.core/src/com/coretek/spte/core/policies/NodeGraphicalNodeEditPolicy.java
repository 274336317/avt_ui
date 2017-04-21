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
 * 创建 创建新连接的命令
 * 
 * @author 孙大巍
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
			// 创建周期消息的父连接命令
			PeriodMsgCreationCmd command = (PeriodMsgCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_TIMER))
		{
			// 创建时间间隔
			IntervalCreationCmd command = (IntervalCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_BACKGROUND))
		{
			// 创建背景消息
			BackgroundMsgCreationCmd command = (BackgroundMsgCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_PARALLEL))
		{
			// 创建并行消息
			ParallelCreationCmd command = (ParallelCreationCmd) request.getStartCommand();
			command.setTarget((TestNodeMdl) getHost().getModel());
			cmd = command;
		}
		else
		{
			// 创建普通消息连线
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
			// 创建周期消息的父连接命令
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
			// 创建背景消息
			BackgroundMsgCreationCmd command = new BackgroundMsgCreationCmd();
			command.setSource((TestNodeMdl) getHost().getModel());
			request.setStartCommand(command);
			cmd = command;
		}
		else if (type.equals(SPTEConstants.REQ_TYPE_PARALLEL))
		{
			// 创建并行消息
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