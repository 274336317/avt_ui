package com.coretek.spte.core.editor.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.util.Utils;

/**
 * 删除图像中的对象
 * 
 * @author 孙大巍
 * @date 2010-11-23
 * 
 */
public class DelMsgAtn extends AbstractDeletionAtn
{

	public final static String	ID	= "com.coretek.tools.sequence.actions.deleteMessageAction";

	public DelMsgAtn(IWorkbenchPart part)
	{
		super(part);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		CompoundCommand cc = new CompoundCommand();
		List<Object> selectedObjects = this.getSelectedObjects();
		for (Object object : selectedObjects)
		{
			Command cmd = this.createCommand(object);
			cc.add(cmd);
		}
		execute(cc);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean calculateEnabled()
	{
		if (!Utils.isEditingStatus())
		{
			return false;
		}
		List<Object> selectedObjects = this.getSelectedObjects();
		for (Object object : selectedObjects)
		{
			Command cmd = this.createCommand(object);
			if (cmd == null)
			{
				return false;
			}
			if (!cmd.canExecute())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	protected void init()
	{
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Messages.getString("I18N_DELETE"));
		setId(ActionFactory.DELETE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setEnabled(false);
	}
}