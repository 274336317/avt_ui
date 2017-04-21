package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;

import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.editor.actions.toolbar.SPTETBContributor;
import com.coretek.spte.core.util.ImageManager;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.action.Messages;

/**
 * 开启调试功能
 * 
 * @author 孙大巍
 * 
 */
public class DebugTestCaseAction extends Action
{

	private IFile	file;

	public DebugTestCaseAction(IFile file)
	{
		this.file = file;
		this.setText(Messages.getString("I18N_DEBUG_TESTCASE"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/debug.gif"));
	}

	@Override
	public void run()
	{
		try
		{
			SPTEEditor editor = Utils.openEditor(this.file);
			SPTETBContributor contributor = (SPTETBContributor) editor.getEditorSite().getActionBarContributor();
			contributor.fireDebugCmd();
		} catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}
}