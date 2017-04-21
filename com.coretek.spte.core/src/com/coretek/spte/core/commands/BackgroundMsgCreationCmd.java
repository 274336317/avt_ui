package com.coretek.spte.core.commands;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.BackgroundChildMsgMdl;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;

/**
 * 创建背景消息
 * 
 * @author 孙大巍
 * @date 2011-7-13
 */
public class BackgroundMsgCreationCmd extends PeriodMsgCreationCmd
{

	/**
	 * @param isCycle
	 * @param root </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-22
	 */
	public BackgroundMsgCreationCmd(boolean isCycle, RootContainerMdl root)
	{
		super(isCycle, root);
	}

	public BackgroundMsgCreationCmd()
	{

	}

	@Override
	public void execute()
	{
		int sourceIndex = source.getParent().getChildren().indexOf(source);
		this.target = (TestNodeMdl) target.getParent().getChildren().get(sourceIndex);
		this.childTarget = (TestNodeMdl) target.getParent().getChildren().get(target.getParent().getChildren().indexOf(this.target) + 2);
		BackgroundMsgMdl conn = new BackgroundMsgMdl(this.source, this.target);
		this.childConnection = new BackgroundChildMsgMdl(this.childSource, this.childTarget);
		if (this.connection != null)
		{
			conn.setTcMsg(this.connection.getTcMsg());
			conn.setName(this.connection.getTcMsg().getMsg().getName() + this.connection.getMesType());
			conn.setMesType(this.connection.getMesType());
		}
		this.connection = conn;
		this.childConnection.setParent(this.connection);
		this.connection.setFixedChild(this.childConnection);
		this.childConnection.setTcMsg(this.connection.getTcMsg());

	}

	public String getLabel()
	{
		return Messages.getString("I18N_CREATE_CYCLE");
	}
}