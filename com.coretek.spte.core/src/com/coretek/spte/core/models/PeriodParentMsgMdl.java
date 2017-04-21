package com.coretek.spte.core.models;

import org.eclipse.swt.graphics.Color;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * �̶���Ϣģ�ͣ��̶���Ϣ�������ڷ��͵����ԡ� �̶���Ϣģ��ӵ��һ��������ģ�ͣ�����һ�����һ�� ������Ϣ
 * 
 * @author ���Ρ
 * @date 2010-8-30
 */
public class PeriodParentMsgMdl extends AbtCycleMsgMdl
{

	private static final long serialVersionUID = 1604003277110705821L;

	// ����Ϣ����
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