package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * ������Ϣ����ģ��
 * 
 * @author ���Ρ
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
				connection.setName(this.conn.getName() + this.conn.getMesType()); // ��cas������ʾ��Ϣ����
				connection.setMesType(this.conn.getMesType());
			}
			this.conn = connection;
		}
		return conn;
	}

	public boolean validate()
	{
		// ��ֹ��Ϣ���߻���������ʱ�����������ӵ���Ϣ�����м�
		if (Utils.isBetweenTimeredConnections(source))
		{
			return false;
		}
		// ��ֹ����Ϣ����������Ϣ�ĸ����ߺ�������֮��
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