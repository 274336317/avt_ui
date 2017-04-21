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

import com.coretek.spte.core.commands.CopyMsgCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 拷贝消息
 * 
 * @author 孙大巍
 * @date 2010-9-3
 * 
 */
public class CopyMsgAtn extends SelectionAction
{

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		Command cmd = this.createCopyCommand(this.getSelectedObjects());
		if (cmd != null && cmd.canExecute())
		{
			cmd.execute();
		}
	}

	@Override
	protected void init()
	{
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("复制");
		setId(ActionFactory.COPY.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}

	public CopyMsgAtn(IWorkbenchPart part)
	{
		super(part);
		setLazyEnablementCalculation(true);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean calculateEnabled()
	{
		if (!Utils.isEditingStatus())
		{
			return false;
		}
		Command cmd = this.createCopyCommand(this.getSelectedObjects());
		if (cmd == null)
		{
			return false;
		}

		return cmd.canExecute();
	}

	private Command createCopyCommand(List<Object> selectedObjects)
	{
		if (selectedObjects.isEmpty())
		{
			return null;
		}
		CopyMsgCmd command = new CopyMsgCmd();
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
					// 如果是周期消息中的子连接，则需要获得它的父连接
					if (ep.getModel() instanceof PeriodChildMsgMdl)
					{
						model = (AbtConnMdl) ((AbtConnMdl) ep.getModel()).getParent();
					} else
					{
						model = (AbtConnMdl) ep.getModel();
					}
					command.addElement(model);
				}
			}
		}
		return command;
	}
}