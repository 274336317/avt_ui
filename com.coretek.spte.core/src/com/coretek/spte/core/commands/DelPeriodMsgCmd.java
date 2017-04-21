package com.coretek.spte.core.commands;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 删除周期消息的父连线，当执行删除操作的时候， 应该将子连线一并删除。
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class DelPeriodMsgCmd extends DelCmd
{

	/**
	 * 父连接的输出节点
	 */
	protected AbtNode				source;

	/**
	 * 父连接的输入节点
	 */
	protected AbtNode				target;
	/**
	 * 父模型
	 */
	protected PeriodParentMsgMdl	fixedParent;
	/**
	 * 子连接的输出节点
	 */
	protected AbtNode				childSource;
	/**
	 * 子连接的输入节点
	 */
	protected AbtNode				childTarget;
	/**
	 * 子模型
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
		// 删除父连线
		this.source.removeOutput(this.fixedParent);
		this.target.removeInput(this.fixedParent);
		// 删除子连线
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