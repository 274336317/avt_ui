package com.coretek.spte.core.commands;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;

/**
 * 
 * @author 孙大巍
 * 
 *         2011-4-24
 */
public class DropMsgCreationCmd extends AbstractCreationConnCmd
{

	protected AbtConnMdl	connection;

	/**
	 * 连接源点
	 */
	protected TestNodeMdl	source;

	/**
	 * 连接目标点
	 */
	protected TestNodeMdl	target;

	protected ICreation		creationMessage;

	protected String		versionId;

	public DropMsgCreationCmd(String versionId)
	{
		this.creationMessage = new DropCreationMsg();
		this.versionId = versionId;
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
		((DropCreationMsg) this.creationMessage).setSource(source);
		((DropCreationMsg) this.creationMessage).setTarget(target);
		((DropCreationMsg) this.creationMessage).setConn((MsgConnMdl) this.connection);
		this.connection = (AbtConnMdl) this.creationMessage.execute();
		// BlocksBean block =
		// MessageManager.getBlock(((MsgConnMdl)this.connection).getTCMsgBean().getBlockId(),
		// this.versionId, Utils.getInputOfActiveEditor().getProject());
		// ChildrenBean childrenBean =
		// MessageManager.getChildrenBean(((MsgConnMdl)this.connection).getTCMsgBean().getId(),
		// block);
		// ((MsgConnMdl)this.connection).setBlocks(block);
		// ((MsgConnMdl)this.connection).setChildrenBean(childrenBean);
		// this.updateIdSet((MsgConnMdl)this.connection,
		// ((MsgConnMdl)this.connection).getTCMsgBean().getModuleId(), true);
	}

	public String getLabel()
	{
		return Messages.getString("I18N_DND_MSG");
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
		// 禁止消息连线连接本身
		if (source == target)
		{
			return false;
		}
		((DropCreationMsg) creationMessage).setSource(source);
		if (this.target != null)
		{
			((DropCreationMsg) creationMessage).setTarget(target);
		}
		boolean result = creationMessage.validate();
		if (!result)
		{
			return false;
		}

		return true;
	}
}