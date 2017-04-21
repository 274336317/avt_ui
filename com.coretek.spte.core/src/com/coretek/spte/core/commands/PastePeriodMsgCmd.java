package com.coretek.spte.core.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 黏贴周期消息
 * 
 * @author 孙大巍
 * @date 2010-9-16
 * 
 */
public class PastePeriodMsgCmd extends PasteMsgCmd
{

	/**
	 * 子连接的连接目标
	 */
	private TestNodeMdl			childTargetItem;

	/**
	 * 子连接的连接源
	 */
	private TestNodeMdl			chidlSourceItem;

	/**
	 * 被复制生成的子连接模型
	 */
	private PeriodChildMsgMdl	clonedChildConnection;

	public PastePeriodMsgCmd()
	{

	}

	public void undo()
	{
		super.undo();

		this.chidlSourceItem.removeOutput(clonedChildConnection);
		this.childTargetItem.removeInput(clonedChildConnection);
	}

	@Override
	public boolean canExecute()
	{
		if (!super.canExecute())
		{
			return false;
		}
		// 黏贴目标item的下相邻的item不能有消息连线
		TestNodeMdl nextItem = Utils.getNextNode(this.selectedItem);
		if (Utils.hasConnection(nextItem))
		{
			return false;
		}
		// 黏贴目标item的下相邻的item的下相邻的item不能有消息连线
		if (Utils.hasConnection(Utils.getNextNode(Utils.getNextNode(this.selectedItem))))
		{
			return false;
		}
		return true;
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_PASTE_FIXED_MSG");
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
			this.connection = (PeriodParentMsgMdl) it.next();

			try
			{
				this.clonedConnection = (PeriodParentMsgMdl) ((PeriodParentMsgMdl) this.connection).clone();
				this.clonedChildConnection = (PeriodChildMsgMdl) ((PeriodParentMsgMdl) this.connection).getFixedChild().clone();
				this.clonedChildConnection.setParent(this.clonedConnection);
				((PeriodParentMsgMdl) this.clonedConnection).setFixedChild(this.clonedChildConnection);
				// 被复制消息的连接源
				TestNodeContainerMdl sourceItemParent = (TestNodeContainerMdl) this.connection.getSource().getParent();
				// 被复制消息的连接目标
				TestNodeContainerMdl targetItemParent = (TestNodeContainerMdl) this.connection.getTarget().getParent();
				int index = this.selectedItem.getParent().getChildren().indexOf(this.selectedItem);
				if (index < 0)
				{
					return;
				}
				// 选择节点为源节点
				if (sourceItemParent.getChildren().contains(this.selectedItem))
				{
					this.targetItem = (TestNodeMdl) targetItemParent.getChildren().get(index);
					this.childTargetItem = (TestNodeMdl) targetItemParent.getChildren().get(index + 2);
					this.chidlSourceItem = (TestNodeMdl) sourceItemParent.getChildren().get(index + 2);
					this.clonedConnection.setSource(this.selectedItem);
					this.clonedConnection.setTarget(this.targetItem);
					this.sourceItem = this.selectedItem;

				} else
				{// 选择节点为目标节点
					this.sourceItem = (TestNodeMdl) sourceItemParent.getChildren().get(index);
					this.chidlSourceItem = (TestNodeMdl) sourceItemParent.getChildren().get(index + 2);
					this.childTargetItem = (TestNodeMdl) targetItemParent.getChildren().get(index + 2);
					this.clonedConnection.setSource(this.sourceItem);
					this.clonedConnection.setTarget(this.selectedItem);
					this.targetItem = this.selectedItem;
				}

				this.clonedChildConnection.setSource(this.chidlSourceItem);
				this.clonedChildConnection.setTarget(this.childTargetItem);
				this.sourceItem.addOutput(this.clonedConnection);
				this.targetItem.addInput(this.clonedConnection);
				this.chidlSourceItem.addOutput(this.clonedChildConnection);
				this.childTargetItem.addInput(this.clonedChildConnection);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
	}
}