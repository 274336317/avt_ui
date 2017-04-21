package com.coretek.spte.core.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * 黏贴时间间隔
 * 
 * @author 孙大巍
 * @date 2010-11-24
 * 
 */
public class PasteTimerCmd extends PasteMsgCmd
{

	private ICreation	pasteTimer;

	public void setSelectedItem(TestNodeMdl selectedItem)
	{
		this.selectedItem = selectedItem;
	}

	public PasteTimerCmd()
	{
		this.pasteTimer = new PasteTimer();
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_PASTE_INTERVAL");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canExecute()
	{
		if (this.selectedItem == null)
		{
			return false;
		}

		if (!Utils.hasConnection(this.selectedItem))
		{
			return false;
		}

		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();
		if (models == null || models.isEmpty())
		{
			return false;
		}
		AbtConnMdl model = models.get(0);
		if (model instanceof IntervalConnMdl)
		{
			((PasteTimer) pasteTimer).setSelectedItem(selectedItem);
			boolean result = ((PasteTimer) pasteTimer).validate();
			if (!result)
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}

	@Override
	public void execute()
	{
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

				int index = this.selectedItem.getParent().getChildren().indexOf(this.selectedItem);
				if (index < 0)
				{
					return;
				}

				((PasteTimer) this.pasteTimer).setClonedConnection(this.clonedConnection);
				((PasteTimer) this.pasteTimer).setSelectedItem(this.selectedItem);
				((PasteTimer) this.pasteTimer).setSourceItemParent(sourceItemParent);
				((PasteTimer) this.pasteTimer).setConnection(this.connection);
				this.pasteTimer.execute();
				this.sourceItem = ((PasteTimer) this.pasteTimer).getSourceItem();
				this.targetItem = ((PasteTimer) this.pasteTimer).getTargetItem();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
			break;// 只能复制一个时间间隔
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
