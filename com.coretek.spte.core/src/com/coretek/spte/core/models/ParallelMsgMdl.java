package com.coretek.spte.core.models;

import com.coretek.spte.core.InstanceUtils;

/**
 * 并行消息
 * 
 * @author 孙大巍
 * 
 */
public class ParallelMsgMdl extends MsgConnMdl
{

	private static final long	serialVersionUID	= -6279990746705306175L;

	public ParallelMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
		this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
	}

	public ParallelMsgMdl()
	{
		this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ParallelMsgMdl model = (ParallelMsgMdl) super.clone();
		return model;
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}