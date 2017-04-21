package com.coretek.testcase.projectView.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

import com.coretek.testcase.projectView.views.ProjectView;

public abstract class DiagramViewActionGroup extends ActionGroup
{

	protected ProjectView	view;

	public DiagramViewActionGroup(ProjectView view)
	{
		this.view = view;
		makeActions();
	}

	/**
	 * Returns the image descriptor with the given relative path.
	 */
	protected ImageDescriptor getImageDescriptor(String relativePath)
	{
		return IDEWorkbenchPlugin.getIDEImageDescriptor(relativePath);

	}

	/**
	 * 
	 * @return
	 */
	public ProjectView getView()
	{
		return view;
	}

	/**
	 * Handles a key pressed event by invoking the appropriate action. Does
	 * nothing by default.
	 */
	public void handleKeyPressed(KeyEvent event)
	{
	}

	/**
	 * Makes the actions contained in this action group.
	 */
	protected abstract void makeActions();

	/**
	 * Runs the default action in the group. Does nothing by default.
	 * 
	 * @param selection the current selection
	 */
	public void runDefaultAction(IStructuredSelection selection)
	{
	}

}
