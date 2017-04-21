package com.coretek.spte.core.commands;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * �����Ϣ
 * 
 * @author ���Ρ
 * @date 2010-9-21
 * 
 */
public class PasteMsg implements ICreation
{

	/**
	 * ��ѡ�еĽڵ�
	 */
	protected TestNodeMdl			selectedItem;

	/**
	 * ����Ŀ��
	 */
	protected TestNodeMdl			targetItem;

	/**
	 * ����Դ
	 * 
	 */
	protected TestNodeMdl			sourceItem;

	/**
	 * ��Ҫ�����Ƶ�����ģ��
	 */
	protected AbtConnMdl			connection;

	/**
	 * ���������ɵ�����ģ��
	 */
	protected AbtConnMdl			clonedConnection;

	/**
	 * ��������Ϣ������Դ
	 */
	protected TestNodeContainerMdl	sourceItemParent;

	/**
	 * ��������Ϣ������Ŀ��
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

		// ѡ��ڵ�ΪԴ�ڵ�
		if (sourceItemParent.getChildren().contains(this.selectedItem))
		{
			this.targetItem = (TestNodeMdl) targetItemParent.getChildren().get(index);
			this.clonedConnection.setSource(this.selectedItem);
			this.clonedConnection.setTarget(this.targetItem);
			this.sourceItem = this.selectedItem;

		} else
		{// ѡ��ڵ�ΪĿ��ڵ�
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
		// ��ֹ�繤�̻���editor���п���
		AbtConnMdl model = models.get(0);
		TestNodeMdl source = (TestNodeMdl) model.getSource();
		TestNodeMdl target = (TestNodeMdl) model.getTarget();
		if (source.getParent().getChildren().indexOf(this.selectedItem) < 0 && target.getParent().getChildren().indexOf(this.selectedItem) < 0)
		{
			return false;
		}
		// ���ܽ�����������Ѿ�ӵ����Ϣ���ߵ�item��
		if (Utils.hasMessage(selectedItem))
		{
			return false;
		}

		// ���ܽ����߸��Ƶ����ʱ������֮���item(s)��
		if (Utils.isBetweenTimeredConnections(selectedItem))
		{
			return false;
		}

		// ���ܽ����߸��Ƶ�������Ϣ�ĸ����ߺ�������֮��
		if (Utils.isInsideCycleMessage(selectedItem))
		{
			return false;
		}

		return true;
	}
}
