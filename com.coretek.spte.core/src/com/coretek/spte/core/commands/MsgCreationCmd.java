package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;

/**
 * ������Ϣ����
 * 
 * @author ���Ρ
 * @date 2010-8-21
 * 
 */
public class MsgCreationCmd extends AbstractCreationConnCmd
{

	protected AbtConnMdl	connection;

	/**
	 * ����Դ��
	 */
	protected TestNodeMdl	source;

	/**
	 * ����Ŀ���
	 */
	protected TestNodeMdl	target;

	protected ICreation		creationMessage;

	public MsgCreationCmd()
	{
		this.creationMessage = new CreationMsg();
	}

	public void setSource(TestNodeMdl source)
	{
		this.source = source;
	}

	public void setConnection(AbtConnMdl connection)
	{
		this.connection = connection;
	}

	public AbtConnMdl getConnection()
	{
		return connection;
	}

	public void setTarget(TestNodeMdl target)
	{
		this.target = target;
		int sourceIndex = source.getParent().getChildren().indexOf(source);
		this.target = (TestNodeMdl) target.getParent().getChildren().get(sourceIndex);
	}

	public void execute()
	{
		((CreationMsg) this.creationMessage).setSource(source);
		((CreationMsg) this.creationMessage).setTarget(target);
		((CreationMsg) this.creationMessage).setConn((MsgConnMdl) this.connection);
		this.connection = (AbtConnMdl) this.creationMessage.execute();
	}

	public String getLabel()
	{
		return "������Ϣ";
	}

	public void redo()
	{
		this.source.addOutput(this.connection);
		this.target.addInput(this.connection);
	}

	public void undo()
	{
		this.source.removeOutput(this.connection);
		this.target.removeInput(this.connection);
	}

	@Override
	public boolean canExecute()
	{
		// ��ֹ��Ϣ�������ӱ���
		if (source == target)
		{
			return false;
		}
		((CreationMsg) creationMessage).setSource(source);
		if (this.target != null)
		{
			((CreationMsg) creationMessage).setTarget(target);
		}
		boolean result = creationMessage.validate();
		if (!result)
		{
			return false;
		}

		return true;
	}
}