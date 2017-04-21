package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtNode;

/**
 * 删除消息，同时删除相关的时间间隔连线
 * 
 * @author lifs
 * @date 2010-9-21
 */
public abstract class DelCmd extends ConnCommand
{

	/*
	 * 上一条时间间隔source
	 */
	private AbtNode		upIntervalsource		= null;

	/*
	 * 上一条时间间隔Target
	 */
	private AbtNode		upIntervalTarget		= null;

	/*
	 * 下一条时间间隔source
	 */
	private AbtNode		downIntervalsource		= null;

	/*
	 * 下一条时间间隔Target
	 */
	private AbtNode		downIntervalTarget		= null;

	/*
	 * 消息的上一条时间间隔
	 */
	private AbtConnMdl	upIntervalConnection	= null;

	/*
	 * 消息的下一条时间间隔
	 */
	private AbtConnMdl	downIntervalConnection	= null;

	public AbtConnMdl getUpIntervalConnection()
	{
		return upIntervalConnection;
	}

	public void setUpIntervalConnection(AbtConnMdl upIntervalConnection)
	{
		this.upIntervalConnection = upIntervalConnection;
		if (this.upIntervalConnection != null)
		{
			upIntervalsource = this.upIntervalConnection.getSource();
			upIntervalTarget = this.upIntervalConnection.getTarget();
		}
	}

	public AbtConnMdl getDownIntervalConnection()
	{
		return downIntervalConnection;
	}

	public void setDownIntervalConnection(AbtConnMdl downIntervalConnection)
	{
		this.downIntervalConnection = downIntervalConnection;
		if (this.downIntervalConnection != null)
		{
			downIntervalsource = this.downIntervalConnection.getSource();
			downIntervalTarget = this.downIntervalConnection.getTarget();
		}
	}

	@Override
	public void execute()
	{
		if (getUpIntervalConnection() != null)
		{
			if (this.upIntervalsource.getOutgoingConnections().indexOf(this.upIntervalConnection) >= 0 && this.upIntervalTarget.getIncomingConnections().indexOf(this.upIntervalConnection) >= 0)
			{
				upIntervalsource.removeOutput(upIntervalConnection);
				upIntervalTarget.removeInput(upIntervalConnection);
				this.upIntervalConnection.setSource(null);
				this.upIntervalConnection.setTarget(null);
			}
		}

		if (getDownIntervalConnection() != null)
		{
			if (this.downIntervalsource.getOutgoingConnections().indexOf(this.downIntervalConnection) >= 0 && this.downIntervalTarget.getIncomingConnections().indexOf(this.downIntervalConnection) >= 0)
			{
				downIntervalsource.removeOutput(downIntervalConnection);
				downIntervalTarget.removeInput(downIntervalConnection);
				this.downIntervalConnection.setSource(null);
				this.downIntervalConnection.setTarget(null);
			}
		}
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		if (getUpIntervalConnection() != null)
		{
			if (this.upIntervalsource.getOutgoingConnections().indexOf(this.upIntervalConnection) < 0 && this.upIntervalTarget.getIncomingConnections().indexOf(this.upIntervalConnection) < 0)
			{
				upIntervalConnection.setSource(upIntervalsource);
				upIntervalConnection.setTarget(upIntervalTarget);
				upIntervalsource.addOutput(upIntervalConnection);
				upIntervalTarget.addInput(upIntervalConnection);
			}
		}

		if (getDownIntervalConnection() != null)
		{
			if (this.downIntervalsource.getOutgoingConnections().indexOf(this.downIntervalConnection) < 0 && this.downIntervalTarget.getIncomingConnections().indexOf(this.downIntervalConnection) < 0)
			{
				downIntervalConnection.setSource(downIntervalsource);
				downIntervalConnection.setTarget(downIntervalTarget);
				downIntervalsource.addOutput(downIntervalConnection);
				downIntervalTarget.addInput(downIntervalConnection);
			}
		}
	}
}
