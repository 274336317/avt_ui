package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * 创建消息连接模型
 * 
 * @author 孙大巍
 * @date 2010-9-21
 * 
 */
public class CreationMsg implements ICreation
{

	protected TestNodeMdl	source;

	protected TestNodeMdl	target;

	protected MsgConnMdl	conn;

	public TestNodeMdl getSource()
	{
		return source;
	}

	public void setSource(TestNodeMdl source)
	{
		this.source = source;
	}

	public TestNodeMdl getTarget()
	{
		return target;
	}

	public void setTarget(TestNodeMdl target)
	{
		this.target = target;
	}

	public MsgConnMdl getConn()
	{
		return conn;
	}

	public void setConn(MsgConnMdl conn)
	{
		this.conn = conn;
	}

	public CreationMsg()
	{

	}

	public Object execute()
	{
		if (this.source.getParent() != this.target.getParent())
		{
			MsgConnMdl connection = new MsgConnMdl(this.source, this.target);
			if (this.conn != null)
			{
				connection.setTcMsg(this.conn.getTcMsg());
				connection.setName(this.conn.getName() + this.conn.getMesType()); // 打开cas用例显示消息名称
				connection.setMesType(this.conn.getMesType());
			}
			this.conn = connection;
		}
		return conn;
	}

	public boolean validate()
	{
		// 禁止消息连线画在两个被时间间隔连线连接的消息连线中间
		if (Utils.isBetweenTimeredConnections(source))
		{
			return false;
		}
		// 禁止将消息画在周期消息的父连线和子连线之间
		if (source != null)
		{
			if (Utils.isInsideCycleMessage(source))
			{
				return false;
			}
		}
		return true;
	}
}