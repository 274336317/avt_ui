package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.testcase.TimeSpan;

/**
 * 设置时间间隔
 * 
 * @author 孙大巍
 * @date 2010-12-23
 */
public class CfgIntervalCmd extends Command
{

	private IntervalConnMdl	model;

	private TimeSpan		oldInterval;

	private TimeSpan		interval;

	public void setModel(IntervalConnMdl model)
	{
		this.model = model;
	}

	public void setInterval(TimeSpan interval)
	{
		this.interval = interval;
	}

	@Override
	public void execute()
	{
		this.oldInterval = this.model.getResultInterval();
		this.model.setResultInterval(this.interval);
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_SET_INTERVAL");
	}

	@Override
	public void redo()
	{
		this.execute();
	}

	@Override
	public void undo()
	{
		this.model.setResultInterval(this.oldInterval);
	}

}
