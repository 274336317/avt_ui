package com.coretek.testcase.projectView.action;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.framelist.BackAction;
import org.eclipse.ui.views.framelist.ForwardAction;
import org.eclipse.ui.views.framelist.FrameList;
import org.eclipse.ui.views.framelist.GoIntoAction;
import org.eclipse.ui.views.framelist.UpAction;

import com.coretek.testcase.projectView.views.ProjectView;

/**
 * This is the action group for the goto actions.
 */
public class GotoActionGroup extends DiagramViewActionGroup
{

	private BackAction		backAction;
	private ForwardAction	forwardAction;
	private GoIntoAction	goIntoAction;
	private UpAction		upAction;

	public GotoActionGroup(ProjectView view)
	{
		super(view);
	}

	public void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection celements = (IStructuredSelection) getContext().getSelection();
		IStructuredSelection selection = SelectionConverter.convertSelectionToResources(celements);
		if (selection.size() == 1)
		{
			if (SelectionConverter.allResourcesAreOfType(selection, IResource.FOLDER))
			{
				menu.add(goIntoAction);
			} else
			{
				IStructuredSelection resourceSelection = SelectionConverter.allResources(selection, IResource.PROJECT);
				if (resourceSelection != null && !resourceSelection.isEmpty())
				{
					IProject project = (IProject) resourceSelection.getFirstElement();
					if (project.isOpen())
					{
						menu.add(goIntoAction);
					}
				}
			}
		}
	}

	public void fillActionBars(IActionBars actionBars)
	{
		actionBars.setGlobalActionHandler(IWorkbenchActionConstants.GO_INTO, goIntoAction);
		actionBars.setGlobalActionHandler(ActionFactory.BACK.getId(), backAction);
		actionBars.setGlobalActionHandler(ActionFactory.FORWARD.getId(), forwardAction);
		actionBars.setGlobalActionHandler(IWorkbenchActionConstants.UP, upAction);

		IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(backAction);
		toolBar.add(forwardAction);
		toolBar.add(upAction);
	}

	protected void makeActions()
	{
		FrameList frameList = getView().getFrameList();
		goIntoAction = new GoIntoAction(frameList);
		backAction = new BackAction(frameList);
		forwardAction = new ForwardAction(frameList);
		upAction = new UpAction(frameList);
	}

	/*
	 * (non-Javadoc)
	 */
	public void updateActionBars()
	{
		ActionContext context = getContext();
		boolean enable = false;

		if (context != null)
		{
			IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
			if (selection.size() == 1)
			{
				Object object = selection.getFirstElement();
				if (object instanceof IAdaptable)
				{
					IResource resource = (IResource) ((IAdaptable) object).getAdapter(IResource.class);
					if (resource instanceof IProject)
					{
						enable = ((IProject) resource).isOpen();
					} else if (resource instanceof IFolder)
					{
						enable = true;
					}
				}
			}
		}
		goIntoAction.setEnabled(enable);
	}
}
