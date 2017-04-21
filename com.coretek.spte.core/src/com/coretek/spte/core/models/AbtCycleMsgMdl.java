package com.coretek.spte.core.models;

import com.coretek.spte.core.InstanceUtils;

/**
 * 周期消息的基类
 * 
 * @author 孙大巍
 * @date 2010-8-31
 * 
 */
public abstract class AbtCycleMsgMdl extends MsgConnMdl
{
	private static final long	serialVersionUID	= 1951139814432785386L;

	public AbtCycleMsgMdl()
	{
		super();
		this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
	}

	public AbtCycleMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
		this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
	}
}