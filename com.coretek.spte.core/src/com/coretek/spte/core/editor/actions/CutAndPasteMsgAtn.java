package com.coretek.spte.core.editor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.commands.CutMsgCmd;
import com.coretek.spte.core.commands.CutPeriodMsgCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.parts.IntervalConnPart;
import com.coretek.spte.core.parts.MsgConnPart;
import com.coretek.spte.core.parts.PeriodChildMsgPart;
import com.coretek.spte.core.parts.PeriodParentMsgPart;
import com.coretek.spte.core.util.Utils;

/**
 * 剪贴消息 应该区分剪贴普通消息和周期消息，如果是周期消息则需要运行CutFixedMessageCommand，
 * 如果是普通消息则需要运行CutMessageCommand。
 * 
 * @author 孙大巍
 * @date 2010-11-22
 * 
 */
public class CutAndPasteMsgAtn extends SelectionAction
{

	public final static String	ID	= "com.coretek.tools.sequence.actions.cutMessageAction";

	public CutAndPasteMsgAtn(IWorkbenchPart part)
	{
		super(part);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		Command cmd = this.createCutCommand(this.getSelectedObjects());
		if (cmd != null && cmd.canExecute())
		{
			execute(cmd);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean calculateEnabled()
	{
		if (!Utils.isEditingStatus())
		{
			return false;
		}
		Command cmd = this.createCutCommand(this.getSelectedObjects());
		if (cmd == null)
		{
			return false;
		}

		return cmd.canExecute();
	}

	@Override
	protected void init()
	{
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Messages.getString("I18N_CUT_AND_PASTE"));
		setId(ActionFactory.CUT.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(true);
	}

	private Command createCutCommand(List<Object> selectedObjects)
	{
		if (selectedObjects.isEmpty())
		{
			return null;
		}
		if (!(selectedObjects.get(0) instanceof EditPart))
		{
			return null;
		}
		EditPart selectedPart = (EditPart) selectedObjects.get(0);

		/*
		 * 周期消息父连接
		 */
		if (selectedPart instanceof PeriodParentMsgPart)
		{
			CutPeriodMsgCmd command = new CutPeriodMsgCmd();
			PeriodParentMsgMdl parentModel = (PeriodParentMsgMdl) selectedPart.getModel();
			command.setFixedParent(parentModel);
			command.setSource(parentModel.getSource());
			command.setTarget(parentModel.getTarget());
			return command;
		} else if (selectedPart instanceof PeriodChildMsgPart)
		{// 周期消息子连接
			CutPeriodMsgCmd command = new CutPeriodMsgCmd();
			PeriodChildMsgMdl childModel = (PeriodChildMsgMdl) selectedPart.getModel();
			PeriodParentMsgMdl parentModel = (PeriodParentMsgMdl) childModel.getParent();
			command.setFixedParent(parentModel);
			command.setSource(parentModel.getSource());
			command.setTarget(parentModel.getTarget());
			return command;
		} else if (selectedPart instanceof MsgConnPart || selectedPart instanceof IntervalConnPart)
		{
			// 普通消息
			CutMsgCmd command = new CutMsgCmd();
			Iterator<Object> it = selectedObjects.iterator();
			while (it.hasNext())
			{
				Object object = it.next();
				if (object instanceof EditPart)
				{
					EditPart ep = (EditPart) object;
					if (ep.getModel() instanceof AbtConnMdl)
					{
						AbtConnMdl model = null;
						model = (AbtConnMdl) ep.getModel();
						command.addElement(model);
						command.setConnection(model);
						command.setTarget(model.getTarget());
						command.setSource(model.getSource());
					}
				}
				break;// 仅支持剪贴一个消息
			}
			return command;
		}
		return null;
	}
}
