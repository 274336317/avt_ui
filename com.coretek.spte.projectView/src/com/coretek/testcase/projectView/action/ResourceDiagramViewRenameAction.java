package com.coretek.testcase.projectView.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RenameResourceAction;

import com.coretek.tools.ide.internal.ui.IHelpContextIds;

/**
 * 
 * ������
 * 
 * @author ���Ρ
 * @date 2010-12-16
 */
public class ResourceDiagramViewRenameAction extends RenameResourceAction
{
	private TreeViewer	viewer;

	/**
	 * Create a ResourceNavigatorRenameAction and use the tree of the supplied
	 * viewer for editing.
	 * 
	 * @param shell Shell
	 * @param treeViewer TreeViewer
	 */
	public ResourceDiagramViewRenameAction(Shell shell, TreeViewer treeViewer)
	{
		super(shell, treeViewer.getTree());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.RENAME_ACTION);
		this.viewer = treeViewer;
	}

	/*
	 * (non-Javadoc) Run the action to completion using the supplied path.
	 */
	protected void runWithNewPath(IPath path, IResource resource)
	{
		IWorkspaceRoot root = resource.getProject().getWorkspace().getRoot();
		super.runWithNewPath(path, resource);
		if (this.viewer != null)
		{
			IResource newResource = root.findMember(path);
			if (newResource != null)
			{
				this.viewer.setSelection(new StructuredSelection(newResource), true);
			}
		}
	}

	/**
	 * Handle the key release
	 */
	public void handleKeyReleased(KeyEvent event)
	{
		if (event.keyCode == SWT.F2 && event.stateMask == 0 && isEnabled())
		{
			run();
		}
	}
}
