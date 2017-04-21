package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;

/**
 * 从.cas文件解析出interval，然后创建它
 * 
 * @author 孙大巍
 * @date 2010-12-8
 * 
 */
public class IntervalAutoCreationCmd extends Command
{

	private IntervalConnMdl	intervalModel;

	private TestNodeMdl		target;

	private TestNodeMdl		source;

	public IntervalConnMdl getIntervalModel()
	{
		return intervalModel;
	}

	public void setIntervalModel(IntervalConnMdl intervalModel)
	{
		this.intervalModel = intervalModel;
	}

	public TestNodeMdl getTarget()
	{
		return target;
	}

	public void setTarget(TestNodeMdl target)
	{
		this.target = target;
	}

	public TestNodeMdl getSource()
	{
		return source;
	}

	public void setSource(TestNodeMdl source)
	{
		this.source = source;
	}

	@Override
	public void execute()
	{
		this.intervalModel = new IntervalConnMdl();
		this.intervalModel.setTarget(target);
		this.intervalModel.setSource(source);
		this.target.addInput(this.intervalModel);
		this.source.addOutput(this.intervalModel);
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_CREATION_INTERVAL");
	}

	@Override
	public void redo()
	{
		this.execute();
	}

	@Override
	public void undo()
	{
		this.target.removeOutput(this.intervalModel);
		this.source.removeInput(this.intervalModel);
		this.intervalModel.setSource(null);
		this.intervalModel.setTarget(null);
		this.intervalModel = null;
	}

}
