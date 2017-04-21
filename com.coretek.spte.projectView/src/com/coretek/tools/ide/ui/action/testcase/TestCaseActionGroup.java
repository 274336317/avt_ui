package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.action.DiagramViewActionGroup;
import com.coretek.testcase.projectView.views.ProjectView;

/**
 * 
 * @author Administrator
 * 
 */
public class TestCaseActionGroup extends DiagramViewActionGroup
{

	private RunTestCaseAction	runAction;

	private DebugTestCaseAction	debugAction;

	private EditCaseDesAction	editCaseDesAction;

	/**
	 * @param cview
	 */
	public TestCaseActionGroup(ProjectView view)
	{
		super(view);
	}

	@Override
	public void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		if (!selection.isEmpty())
		{
			Object object = selection.getFirstElement();
			if ((object instanceof IFile))
			{
				IFile file = (IFile) object;
				if (Utils.isCasFile(file))
				{
					this.runAction = new RunTestCaseAction(file);
					this.debugAction = new DebugTestCaseAction(file);
					this.editCaseDesAction = new EditCaseDesAction(file);
					menu.add(editCaseDesAction);
					// menu.add(this.runAction);
					// menu.add(this.debugAction);
				}
			}
		}
	}

	@Override
	protected void makeActions()
	{

	}
}
