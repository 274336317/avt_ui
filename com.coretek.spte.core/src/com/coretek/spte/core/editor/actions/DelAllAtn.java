package com.coretek.spte.core.editor.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.ui.IWorkbenchPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 删除所有的连线
 * 
 * @author 孙大巍
 * 
 */
public class DelAllAtn extends AbstractDeletionAtn
{

	public final static String	ID	= "com.coretek.tools.sequence.actions.deleteAllMessagesAction";

	public DelAllAtn(IWorkbenchPart part)
	{
		super(part);
	}

	@Override
	protected void init()
	{
		super.init();
		this.setText(Messages.getString("I18N_DELETE_ALL_MSGS"));
		this.setId(ID);
		this.setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled()
	{

		return Utils.isEditingStatus();
	}

	@Override
	public void run()
	{
		SPTEEditor editor = (SPTEEditor) this.getWorkbenchPart();
		CompoundCommand cc = new CompoundCommand();
		List<MsgConnMdl> list = Utils.getAllMsgs(editor.getRootContainerMdl());
		for (MsgConnMdl msg : list)
		{
			Object target = editor.getGraphicalViewer().getEditPartRegistry().get(msg);
			if (target == null)
			{
				continue;
			}
			Command cmd = this.createCommand(target);
			cc.add(cmd);
		}
		execute(cc);
	}
}
