package com.coretek.spte.core.commands;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;

/**
 * 黏贴消息
 * 
 * @author 孙大巍
 * @date 2010-9-6
 * 
 */
public class PasteMsgCmd extends Command
{

	private final static Logger	logger	= LoggingPlugin.getLogger(PasteMsgCmd.class.getName());

	/**
	 * 被选中的节点
	 */
	protected TestNodeMdl		selectedItem;

	/**
	 * 连接目标
	 */
	protected TestNodeMdl		targetItem;

	/**
	 * 连接源
	 * 
	 */
	protected TestNodeMdl		sourceItem;

	/**
	 * 需要被复制的连接模型
	 */
	protected AbtConnMdl		connection;

	/**
	 * 被复制生成的连接模型
	 */
	protected AbtConnMdl		clonedConnection;

	private ICreation			pasteMessage;

	public void setSelectedItem(TestNodeMdl selectedItem)
	{
		this.selectedItem = selectedItem;

	}

	public PasteMsgCmd()
	{
		this.pasteMessage = new PasteMsg();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canExecute()
	{
		if (this.selectedItem == null)
		{
			return false;
		}

		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();
		if (models == null || models.isEmpty())
		{
			return false;
		}
		((PasteMsg) pasteMessage).setSelectedItem(selectedItem);
		boolean result = ((PasteMsg) pasteMessage).validate();
		if (!result)
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean canUndo()
	{

		return true;
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_PASTE_MSG");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute()
	{
		if (!this.canExecute())
		{
			return;
		}
		Iterator<AbtConnMdl> it = ((List<AbtConnMdl>) Clipboard.getDefault().getContents()).iterator();
		while (it.hasNext())
		{

			// 被复制的消息
			this.connection = (AbtConnMdl) it.next();

			try
			{
				this.clonedConnection = (AbtConnMdl) this.connection.clone();
				// 被复制消息的连接源
				TestNodeContainerMdl sourceItemParent = (TestNodeContainerMdl) this.connection.getSource().getParent();
				// 被复制消息的连接目标
				TestNodeContainerMdl targetItemParent = (TestNodeContainerMdl) this.connection.getTarget().getParent();

				int index = this.selectedItem.getParent().getChildren().indexOf(this.selectedItem);
				if (index < 0)
				{
					return;
				}

				((PasteMsg) this.pasteMessage).setClonedConnection(this.clonedConnection);
				((PasteMsg) this.pasteMessage).setSelectedItem(this.selectedItem);
				((PasteMsg) this.pasteMessage).setSourceItemParent(sourceItemParent);
				((PasteMsg) this.pasteMessage).setTargetItemParent(targetItemParent);
				((PasteMsg) this.pasteMessage).setConnection(this.connection);
				this.pasteMessage.execute();
				this.sourceItem = ((PasteMsg) this.pasteMessage).getSourceItem();
				this.targetItem = ((PasteMsg) this.pasteMessage).getTargetItem();

			} catch (CloneNotSupportedException e)
			{
				LoggingPlugin.logException(logger, e);
			}
			break;// 只能复制一个消息
		}
	}

	@Override
	public void redo()
	{
		this.execute();
	}

	@Override
	public void undo()
	{
		this.sourceItem.removeOutput(this.clonedConnection);
		this.targetItem.removeInput(this.clonedConnection);
		this.clonedConnection.setSource(null);
		this.clonedConnection.setTarget(null);
	}
}
