package com.coretek.spte.core.commands;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.util.Utils;

/**
 * ɾ��������Ϣ�ĸ����ߣ���ִ��ɾ��������ʱ�� Ӧ�ý�������һ��ɾ����
 * 
 * @author ���Ρ
 * @date 2010-9-1
 * 
 */
public class DelPeriodMsgCmd extends DelCmd
{

	/**
	 * �����ӵ�����ڵ�
	 */
	protected AbtNode				source;

	/**
	 * �����ӵ�����ڵ�
	 */
	protected AbtNode				target;
	/**
	 * ��ģ��
	 */
	protected PeriodParentMsgMdl	fixedParent;
	/**
	 * �����ӵ�����ڵ�
	 */
	protected AbtNode				childSource;
	/**
	 * �����ӵ�����ڵ�
	 */
	protected AbtNode				childTarget;
	/**
	 * ��ģ��
	 */
	protected PeriodChildMsgMdl		fixedChild;

	public void setSource(AbtNode source)
	{
		this.source = source;
	}

	public void setTarget(AbtNode target)
	{
		this.target = target;
	}

	public void setFixedParent(PeriodParentMsgMdl fixedParent)
	{
		this.fixedParent = fixedParent;
		this.fixedChild = fixedParent.getFixedChild();

		this.setUpIntervalConnection(Utils.getIncomingInterval(Utils.getTestedLineItem(fixedParent)));
		this.setDownIntervalConnection(Utils.getOutcomingInterval(Utils.getTestedLineItem(fixedChild)));

		this.childSource = fixedParent.getFixedChild().getSource();
		this.childTarget = fixedParent.getFixedChild().getTarget();
		// if(fixedParent.getTCMsgBean() != null)
		// this.updateIdSet((MsgConnMdl)fixedParent,
		// fixedParent.getTCMsgBean().getModuleId(), false);
	}

	@Override
	public void execute()
	{
		super.execute();
		// ɾ��������
		this.source.removeOutput(this.fixedParent);
		this.target.removeInput(this.fixedParent);
		// ɾ��������
		this.childSource.removeOutput(this.fixedChild);
		this.childTarget.removeInput(this.fixedChild);
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		super.undo();

		this.fixedParent.setSource(this.source);
		this.fixedParent.setTarget(this.target);
		this.source.addOutput(this.fixedParent);
		this.target.addInput(this.fixedParent);

		this.fixedChild.setSource(this.childSource);
		this.fixedChild.setTarget(this.childTarget);
		this.childSource.addOutput(this.fixedChild);
		this.childTarget.addInput(this.fixedChild);
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_DEL_CYCLE");
	}
}