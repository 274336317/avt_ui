package com.coretek.spte.core.models;

import org.eclipse.swt.graphics.Color;

/**
 * 周期消息的子连接线模型
 * 
 * @author 孙大巍
 * @date 2010-8-30
 * 
 */
public class PeriodChildMsgMdl extends AbtCycleMsgMdl
{

	private static final long	serialVersionUID	= 4898857214313536503L;

	public PeriodChildMsgMdl()
	{

	}

	public PeriodChildMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
	}

	@Override
	public void setColor(Color color)
	{
		if (this.color == color)
		{
			return;
		}
		this.color = color;
		this.firePropertyChange(PROP_COLOR, this.color);
		((AbtConnMdl) this.parent).setColor(color);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		PeriodChildMsgMdl model = (PeriodChildMsgMdl) super.clone();
		model.parent = null;
		model.source = null;
		model.target = null;

		return model;
	}
}
