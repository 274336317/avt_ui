package com.coretek.testcase.projectView.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.MoveProjectAction;
import org.eclipse.ui.actions.MoveResourceAction;

import com.coretek.tools.ide.internal.ui.IHelpContextIds;

public class ResourceDiagramViewMoveAction extends MoveResourceAction
{
	private StructuredViewer	viewer;

	private MoveProjectAction	moveProjectAction;

	/**
	 * Create a ResourceNavigatorMoveAction and use the supplied viewer to
	 * update the UI.
	 * 
	 * @param shell Shell
	 * @param structureViewer StructuredViewer
	 */
	public ResourceDiagramViewMoveAction(Shell shell, StructuredViewer structureViewer)
	{
		super(shell);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.MOVE_ACTION);
		this.viewer = structureViewer;
		this.moveProjectAction = new MoveProjectAction(shell);
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public void run()
	{
		if (moveProjectAction.isEnabled())
		{
			moveProjectAction.run();
			return;
		}

		super.run();
		List destinations = getDestinations();
		if (destinations != null && destinations.isEmpty() == false)
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			List resources = new ArrayList();
			Iterator iterator = destinations.iterator();

			while (iterator.hasNext())
			{
				IResource newResource = root.findMember((IPath) iterator.next());
				if (newResource != null)
				{
					resources.add(newResource);
				}
			}

			this.viewer.setSelection(new StructuredSelection(resources), true);
		}

	}

	protected boolean updateSelection(IStructuredSelection selection)
	{
		moveProjectAction.selectionChanged(selection);
		return super.updateSelection(selection) || moveProjectAction.isEnabled();
	}

}
