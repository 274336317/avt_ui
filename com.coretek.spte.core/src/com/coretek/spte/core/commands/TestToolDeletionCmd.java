package com.coretek.spte.core.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;

public class TestToolDeletionCmd extends Command
{
	private ContainerMdl		subtransmodel;

	private TestMdl				step;

	private List<AbtConnMdl>	inputCons;

	private List<AbtConnMdl>	outputCons;

	public void setSubTransModel(ContainerMdl transmodel)
	{
		this.subtransmodel = transmodel;
	}

	public void setNode(TestMdl step)
	{
		this.step = step;
	}

	public void execute()
	{
		inputCons = step.getRoot().getAllIncomings();
		outputCons = step.getRoot().getAllOutgoings();
		step.removeAllConnections();
		subtransmodel.removeChild(step);
	}

	public String getLabel()
	{
		return "";
	}

	public void redo()
	{
		execute();
	}

	public void undo()
	{
		subtransmodel.addChild(step);
		reconnectIncomings();
		reconnectOutgoings();
	}

	private void reconnectIncomings()
	{
		for (Iterator<AbtConnMdl> iter = inputCons.iterator(); iter.hasNext();)
		{
			AbtConnMdl con = (AbtConnMdl) iter.next();
			con.reConnect();
		}
	}

	private void reconnectOutgoings()
	{
		for (Iterator<AbtConnMdl> iter = outputCons.iterator(); iter.hasNext();)
		{
			AbtConnMdl con = (AbtConnMdl) iter.next();
			con.reConnect();
		}
	}
}
