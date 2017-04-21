package com.coretek.spte.core.commands;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;

/**
 * 黏贴时间间隔
 * 
 * @author 孙大巍
 * @date 2010-9-21
 * 
 */
public class PasteTimer implements ICreation
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
	private TestNodeContainerMdl	sourceItemParent;

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
		int step = sourceItemParent.getChildren().indexOf((TestNodeMdl) this.connection.getTarget()) - sourceItemParent.getChildren().indexOf((TestNodeMdl) this.connection.getSource());
		/*
		 * 黏贴到另外一个lifeline
		 */
		if (this.selectedItem.getParent() != this.connection.getSource().getParent())
		{
			this.targetItem = (TestNodeMdl) this.selectedItem.getParent().getChildren().get(index + step);
		} else
		{
			this.targetItem = (TestNodeMdl) sourceItemParent.getChildren().get(index + step);
		}
		this.clonedConnection.setSource(this.selectedItem);
		this.clonedConnection.setTarget(this.targetItem);
		this.sourceItem = this.selectedItem;
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

		/*
		 * 禁止跨工程或者editor进行拷贝
		 */
		AbtConnMdl model = models.get(0);
		TestNodeMdl source = (TestNodeMdl) model.getSource();
		TestNodeMdl target = (TestNodeMdl) model.getTarget();
		if (source.getParent().getChildren().indexOf(this.selectedItem) < 0 && target.getParent().getChildren().indexOf(this.selectedItem) < 0)
		{
			return false;
		}

		return true;
	}
}