package com.coretek.spte.core.models;

import org.eclipse.swt.graphics.Color;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 固定消息模型，固定消息具有周期发送的特性。 固定消息模型拥有一个子连接模型，他们一起代表一个 周期消息
 * 
 * @author 孙大巍
 * @date 2010-8-30
 */
public class PeriodParentMsgMdl extends AbtCycleMsgMdl
{

	private static final long serialVersionUID = 1604003277110705821L;

	// 子消息对象
	private PeriodChildMsgMdl fixedChild;

	public PeriodChildMsgMdl getFixedChild()
	{
		return fixedChild;
	}

	public void setFixedChild(PeriodChildMsgMdl fixedChild)
	{
		this.fixedChild = fixedChild;
	}

	public PeriodParentMsgMdl()
	{
	}

	public PeriodParentMsgMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
	}

	public void setFailed(boolean failed)
	{
		if (this.failed != failed)
		{
			this.failed = failed;
			this.setColor(this.getDefaultColor());
			this.fixedChild.setFailed(failed);
		}
	}

	@Override
	public void setStatus(TestCaseStatus status)
	{
		super.setStatus(status);
		this.fixedChild.setStatus(status);
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
		this.fixedChild.setColor(color);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		PeriodParentMsgMdl model = (PeriodParentMsgMdl) super.clone();
		model.source = null;
		model.target = null;
		model.setFixedChild(null);

		return model;
	}

	@Override
	public void setName(String name)
	{
		super.setName(name);
		if (this.fixedChild != null)
			this.fixedChild.setName(name);
	}

	@Override
	public void setTcMsg(SPTEMsg tcMsg)
	{
		super.setTcMsg(tcMsg);
		if (this.fixedChild != null)
			this.fixedChild.setTcMsg(tcMsg);
	}

}