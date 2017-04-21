package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.IntervalConnMdl;

/**
 * 删除时间间隔
 * 
 * @author 孙大巍
 * @date 2010-11-25
 * 
 */
public class DelIntervalCmd extends Command
{

	private AbtNode			targetNode;

	private AbtNode			sourceNode;

	private IntervalConnMdl	timer;

	public IntervalConnMdl getTimer()
	{
		return timer;
	}

	public void setTimer(IntervalConnMdl timer)
	{
		this.timer = timer;
	}

	public AbtNode getTargetNode()
	{
		return targetNode;
	}

	public void setTargetNode(AbtNode targetNode)
	{
		this.targetNode = targetNode;
	}

	public AbtNode getSourceNode()
	{
		return sourceNode;
	}

	public void setSourceNode(AbtNode sourceNode)
	{
		this.sourceNode = sourceNode;
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_DEL_INTERVAL");
	}

	@Override
	public boolean canExecute()
	{
		return true;
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}

	@Override
	public void execute()
	{
		if (this.sourceNode.getOutgoingConnections().indexOf(timer) >= 0 && this.targetNode.getIncomingConnections().indexOf(timer) >= 0)
		{
			this.sourceNode.removeOutput(timer);
			this.targetNode.removeInput(timer);
			timer.setSource(null);
			timer.setTarget(null);
		}

	}

	@Override
	public void redo()
	{
		this.execute();
	}

	@Override
	public void undo()
	{
		if (this.sourceNode.getOutgoingConnections().indexOf(timer) < 0 && this.targetNode.getIncomingConnections().indexOf(timer) < 0)
		{
			this.timer.setSource(this.sourceNode);
			this.timer.setTarget(this.targetNode);
			this.sourceNode.addOutput(timer);
			this.targetNode.addInput(timer);
		}
	}

}
