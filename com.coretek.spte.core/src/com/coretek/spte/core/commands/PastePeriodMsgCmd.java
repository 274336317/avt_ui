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
 * ���������Ϣ
 * 
 * @author ���Ρ
 * @date 2010-9-16
 * 
 */
public class PastePeriodMsgCmd extends PasteMsgCmd
{

	/**
	 * �����ӵ�����Ŀ��
	 */
	private TestNodeMdl			childTargetItem;

	/**
	 * �����ӵ�����Դ
	 */
	private TestNodeMdl			chidlSourceItem;

	/**
	 * ���������ɵ�������ģ��
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
		// ���Ŀ��item�������ڵ�item��������Ϣ����
		TestNodeMdl nextItem = Utils.getNextNode(this.selectedItem);
		if (Utils.hasConnection(nextItem))
		{
			return false;
		}
		// ���Ŀ��item�������ڵ�item�������ڵ�item��������Ϣ����
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
			// �����Ƶ���Ϣ
			this.connection = (PeriodParentMsgMdl) it.next();

			try
			{
				this.clonedConnection = (PeriodParentMsgMdl) ((PeriodParentMsgMdl) this.connection).clone();
				this.clonedChildConnection = (PeriodChildMsgMdl) ((PeriodParentMsgMdl) this.connection).getFixedChild().clone();
				this.clonedChildConnection.setParent(this.clonedConnection);
				((PeriodParentMsgMdl) this.clonedConnection).setFixedChild(this.clonedChildConnection);
				// ��������Ϣ������Դ
				TestNodeContainerMdl sourceItemParent = (TestNodeContainerMdl) this.connection.getSource().getParent();
				// ��������Ϣ������Ŀ��
				TestNodeContainerMdl targetItemParent = (TestNodeContainerMdl) this.connection.getTarget().getParent();
				int index = this.selectedItem.getParent().getChildren().indexOf(this.selectedItem);
				if (index < 0)
				{
					return;
				}
				// ѡ��ڵ�ΪԴ�ڵ�
				if (sourceItemParent.getChildren().contains(this.selectedItem))
				{
					this.targetItem = (TestNodeMdl) targetItemParent.getChildren().get(index);
					this.childTargetItem = (TestNodeMdl) targetItemParent.getChildren().get(index + 2);
					this.chidlSourceItem = (TestNodeMdl) sourceItemParent.getChildren().get(index + 2);
					this.clonedConnection.setSource(this.selectedItem);
					this.clonedConnection.setTarget(this.targetItem);
					this.sourceItem = this.selectedItem;

				} else
				{// ѡ��ڵ�ΪĿ��ڵ�
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