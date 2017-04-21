package com.coretek.spte.core.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;

/**
 * 删除被测对象
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class TestedObjectDeletionCmd extends Command
{

	private ContainerMdl		subtransmodel;

	private TestMdl				step;

	private List<AbtConnMdl>	inputCons	= null;

	private List<AbtConnMdl>	outputCons	= null;

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