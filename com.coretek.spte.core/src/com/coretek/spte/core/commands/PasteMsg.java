package com.coretek.spte.core.commands;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * 黏贴消息
 * 
 * @author 孙大巍
 * @date 2010-9-21
 * 
 */
public class PasteMsg implements ICreation
{

	/**
	 * 被选中的节点
	 */
	protected TestNodeMdl			selectedItem;

	/**
	 * 连接目标
	 */
	protected TestNodeMdl			targetItem;

	/**
	 * 连接源
	 * 
	 */
	protected TestNodeMdl			sourceItem;

	/**
	 * 需要被复制的连接模型
	 */
	protected AbtConnMdl			connection;

	/**
	 * 被复制生成的连接模型
	 */
	protected AbtConnMdl			clonedConnection;

	/**
	 * 被复制消息的连接源
	 */
	protected TestNodeContainerMdl	sourceItemParent;

	/**
	 * 被复制消息的连接目标
	 */
	protected TestNodeContainerMdl	targetItemParent;

	public void setTargetItemParent(TestNodeContainerMdl targetItemParent)
	{
		this.targetItemParent = targetItemParent;
	}

	public void setSourceItemParent(TestNodeContainerMdl sourceItemParent)
	{
		this.sourceItemParent = sourceItemParent;
	}

	public void setTargetItem(TestNodeMdl targetItem)
	{
		this.targetItem = targetItem;
	}

	public TestNodeMdl getTargetItem()
	{
		return targetItem;
	}

	public TestNodeMdl getSourceItem()
	{
		return sourceItem;
	}

	public void setSourceItem(TestNodeMdl sourceItem)
	{
		this.sourceItem = sourceItem;
	}

	public void setConnection(AbtConnMdl connection)
	{
		this.connection = connection;
	}

	public void setClonedConnection(AbtConnMdl clonedConnection)
	{
		this.clonedConnection = clonedConnection;
	}

	public void setSelectedItem(TestNodeMdl selectedItem)
	{
		this.selectedItem = selectedItem;
	}

	public Object execute()
	{
		int index = this.selectedItem.getParent().getChildren().indexOf(this.selectedItem);
		if (index < 0)
		{
			return null;
		}

		// 选择节点为源节点
		if (sourceItemParent.getChildren().contains(this.selectedItem))
		{
			this.targetItem = (TestNodeMdl) targetItemParent.getChildren().get(index);
			this.clonedConnection.setSource(this.selectedItem);
			this.clonedConnection.setTarget(this.targetItem);
			this.sourceItem = this.selectedItem;

		} else
		{// 选择节点为目标节点
			this.sourceItem = (TestNodeMdl) sourceItemParent.getChildren().get(index);
			this.clonedConnection.setSource(this.sourceItem);
			this.clonedConnection.setTarget(this.selectedItem);
			this.targetItem = this.selectedItem;
		}

		this.sourceItem.addOutput(this.clonedConnection);
		this.targetItem.addInput(this.clonedConnection);

		return this.clonedConnection;
	}

	@SuppressWarnings("unchecked")
	public boolean validate()
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
		// 禁止跨工程或者editor进行拷贝
		AbtConnMdl model = models.get(0);
		TestNodeMdl source = (TestNodeMdl) model.getSource();
		TestNodeMdl target = (TestNodeMdl) model.getTarget();
		if (source.getParent().getChildren().indexOf(this.selectedItem) < 0 && target.getParent().getChildren().indexOf(this.selectedItem) < 0)
		{
			return false;
		}
		// 不能将连线黏贴到已经拥有消息连线的item上
		if (Utils.hasMessage(selectedItem))
		{
			return false;
		}

		// 不能将连线复制到间隔时间连线之间的item(s)上
		if (Utils.isBetweenTimeredConnections(selectedItem))
		{
			return false;
		}

		// 不能将连线复制到周期消息的父连线和子连线之间
		if (Utils.isInsideCycleMessage(selectedItem))
		{
			return false;
		}

		return true;
	}
}
