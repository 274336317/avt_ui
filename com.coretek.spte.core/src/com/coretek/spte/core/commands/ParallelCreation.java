package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;

/**
 * 创建并行消息
 * 
 * @author 孙大巍
 * 
 */
public class ParallelCreation extends CreationMsg
{
	@Override
	public Object execute()
	{
		if (this.source.getParent() != this.target.getParent())
		{
			MsgConnMdl connection = new ParallelMsgMdl(this.source, this.target);
			if (this.conn != null)
			{
				// connection.setTCMsgBean(this.conn.getTCMsgBean());
				// connection.setBlocks(this.conn.getBlocks());
				// connection.setChildrenBean(this.conn.getChildrenBean());
				connection.setTcMsg(this.conn.getTcMsg());
				connection.setName(this.conn.getName());
				connection.setMesType(this.conn.getMesType());
			}
			this.conn = connection;
		}
		return conn;
	}
}
