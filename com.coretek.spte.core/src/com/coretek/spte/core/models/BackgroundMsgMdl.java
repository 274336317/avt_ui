package com.coretek.spte.core.models;

import com.coretek.spte.core.InstanceUtils;

/**
 * 背景消息，所有的背景消息都是发送消息
 * 
 * @author 孙大巍
 * 
 *         2011-3-29
 */
public class BackgroundMsgMdl extends PeriodParentMsgMdl
{

	private static final long	serialVersionUID	= -5261627813113334905L;

	public BackgroundMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
		this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
	}

	public BackgroundMsgMdl()
	{
		this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BackgroundMsgMdl model = (BackgroundMsgMdl) super.clone();
		model.source = null;
		model.target = null;
		return model;
	}
}