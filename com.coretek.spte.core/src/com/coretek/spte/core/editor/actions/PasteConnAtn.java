package com.coretek.spte.core.editor.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.commands.PasteMsgCmd;
import com.coretek.spte.core.commands.PastePeriodMsgCmd;
import com.coretek.spte.core.commands.PasteTimerCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.parts.TestNodePart;
import com.coretek.spte.core.util.Utils;

/**
 * 黏贴消息 黏贴规则： 1.当黏贴一个时间间隔的时候，不能黏贴到没有消息连线的item上，不能黏贴到已经作为其它时间间隔的输出端的item上，
 * 不能黏贴到从被选中的item往下没有其它item拥有消息连线的Item上
 * 2.当黏贴一个消息连线的时候，不能黏贴到已经拥有消息连线的item上，不能黏贴到时间间隔的中间item上，不能黏贴到周期消息的中间item上
 * 
 * @author 孙大巍
 * @date 2010-9-3
 * 
 */
public class PasteConnAtn extends SelectionAction
{

	@SuppressWarnings("unchecked")
	@Override
	public List getSelectedObjects()
	{
		return super.getSelectedObjects();
	}

	@Override
	protected void init()
	{
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Messages.getString("I18N_PASTE"));
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEnabled()
	{
		if (this.getSelection() == null)
		{
			return false;
		}
		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();

		// 黏贴一个模型
		if (models != null && models.size() == 1)
		{
			if (!(this.getSelection() instanceof StructuredSelection))
			{
				return false;
			}
			StructuredSelection selection = (StructuredSelection) this.getSelection();
			if (selection.getFirstElement() instanceof TestNodePart)
			{
				TestNodePart item = (TestNodePart) selection.getFirstElement();
				TestNodeMdl itemModel = (TestNodeMdl) item.getModel();
				// 黏贴时间间隔，不能黏贴到没有消息连线的item上，也不能黏贴到从被选中的item往下的其余item没有消息连线上
				if (models.get(0) instanceof IntervalConnMdl)
				{
					if (!Utils.hasConnection((TestNodeMdl) (item).getModel()))
					{
						return false;
					}
					if (!Utils.hasMessageFromSelectedItem((TestNodeMdl) (item).getModel()))
					{
						return false;
					}
				} else if (models.get(0) instanceof MsgConnMdl)
				{// 黏贴消息，不能黏贴到时间间隔之间的item上，不能黏贴到周期消息中间，不能年贴到已经拥有消息连线的item上
					if (Utils.hasConnection(itemModel))
					{
						return false;
					}
					if (Utils.isInsideCycleMessage(itemModel))
					{
						return false;
					}
					if (Utils.isBetweenTimeredConnections(itemModel))
					{
						return false;
					}
				}
			}
		}

		// 黏贴多个模型
		for (Object object : this.getSelectedObjects())
		{
			if (object instanceof TestNodePart)
			{
				Object copiedContent = Clipboard.getDefault().getContents();
				if (copiedContent != null && copiedContent instanceof List)
				{
					if (((List) copiedContent).size() > 0)
						return true;
				}
			}
		}

		return false;
	}

	@Override
	public void run()
	{
		Command command = this.createPasteCommand();
		((PasteMsgCmd) command).setSelectedItem((TestNodeMdl) ((TestNodePart) this.getSelectedObjects().get(0)).getModel());
		if (command.canExecute())
		{
			execute(command);
		}
	}

	public PasteConnAtn(IWorkbenchPart part)
	{
		super(part);

	}

	@Override
	protected boolean calculateEnabled()
	{
		if (!Utils.isEditingStatus())
		{
			return false;
		}
		Command command = createPasteCommand();
		return command != null && command.canExecute();
	}

	@SuppressWarnings("unchecked")
	private Command createPasteCommand()
	{
		if (this.getSelectedObjects() == null || this.getSelectedObjects().size() == 0)
		{
			return null;
		}
		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();
		if (this.getSelectedObjects().get(0) instanceof TestNodePart)
		{
			if (models != null && models.size() > 0)
			{
				if (models.get(0) instanceof PeriodParentMsgMdl)
				{
					return new PastePeriodMsgCmd();
				} else if (models.get(0) instanceof MsgConnMdl)
				{
					return new PasteMsgCmd();
				} else if (models.get(0) instanceof IntervalConnMdl)
				{
					return new PasteTimerCmd();
				}
			}
		}
		return null;
	}

	@Override
	protected void handleSelectionChanged()
	{
		super.handleSelectionChanged();
	}
}