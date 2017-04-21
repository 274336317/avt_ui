package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 删除连接的命令
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class DelConnCmd extends DelCmd
{

	private AbtNode		source;

	private AbtNode		target;

	private AbtConnMdl	connection;

	public void setConnection(AbtConnMdl connection)
	{
		this.connection = connection;
		if (connection instanceof MsgConnMdl)
		{
			this.setUpIntervalConnection(Utils.getIncomingInterval(Utils.getTestedLineItem(connection)));
			this.setDownIntervalConnection(Utils.getOutcomingInterval(Utils.getTestedLineItem(connection)));
		}
	}

	public void setSource(AbtNode source)
	{
		this.source = source;
	}

	public void setTarget(AbtNode target)
	{
		this.target = target;
	}

	@Override
	public void execute()
	{
		super.execute();
		source.removeOutput(connection);
		target.removeInput(connection);
	}

	public String getLabel()
	{
		return "删除消息";
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		super.undo();
		connection.setSource(source);
		connection.setTarget(target);
		source.addOutput(connection);
		target.addInput(connection);
	}
}