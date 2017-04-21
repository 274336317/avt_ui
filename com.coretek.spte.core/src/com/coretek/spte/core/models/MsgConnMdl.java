package com.coretek.spte.core.models;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.testcase.Message;

/**
 * 消息连线模型
 * 
 * @author 孙大巍
 * @date 2010-11-24
 * 
 */
public class MsgConnMdl extends AbtConnMdl
{
	private static final long	serialVersionUID	= 6243273446122831867L;

	protected transient SPTEMsg	tcMsg;

	public MsgConnMdl()
	{

	}

	public MsgConnMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
	}

	public SPTEMsg getTcMsg()
	{
		return tcMsg;
	}

	public void setTcMsg(SPTEMsg tcMsg)
	{
		this.tcMsg = tcMsg;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		MsgConnMdl model = (MsgConnMdl) super.clone();
		if (tcMsg != null)
		{
			SPTEMsg spteMsg = new SPTEMsg((Message) tcMsg.getMsg().clone(), tcMsg.getICDMsg());
			model.setTcMsg(spteMsg);
		}

		model.source = null;
		model.target = null;

		return model;
	}
}