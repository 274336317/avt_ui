package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;

/**
 * ����ʱ����ģ��
 * 
 * @author ���Ρ
 * @date 2010-11-24
 * 
 */
public class IntervalCreationCmd extends Command
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

	private ICreation		creationTimer;

	public IntervalCreationCmd()
	{
		this.creationTimer = new CreationInterval();
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
	}

	public void execute()
	{
		this.connection = (AbtConnMdl) this.creationTimer.execute();
		this.source = ((CreationInterval) this.creationTimer).getSource();
		this.target = ((CreationInterval) this.creationTimer).getTarget();
	}

	public String getLabel()
	{
		return Messages.getString("I18N_CREATION_INTERVAL");
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
		((CreationInterval) creationTimer).setSource(source);
		if (this.target != null)
		{
			((CreationInterval) creationTimer).setTarget(target);
		}
		boolean result = creationTimer.validate();
		if (!result)
		{
			return false;
		}

		return true;
	}
}
