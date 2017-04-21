package com.coretek.spte.core.models;

import com.coretek.spte.core.InstanceUtils;

/**
 * ������Ϣ��������ģ��
 * 
 * @author ���Ρ
 * 
 *         2011-3-30
 */
public class BackgroundChildMsgMdl extends PeriodChildMsgMdl
{

	private static final long	serialVersionUID	= 6617341562864028387L;

	public BackgroundChildMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
		this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
	}

	public BackgroundChildMsgMdl()
	{
		this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		this.defaultColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BackgroundChildMsgMdl model = (BackgroundChildMsgMdl) super.clone();
		model.parent = null;
		model.source = null;
		model.target = null;

		return model;
	}
}
