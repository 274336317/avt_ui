package com.coretek.testcase.projectView.action;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.actions.OpenInNewWindowAction;
import org.eclipse.ui.actions.OpenSystemEditorAction;
import org.eclipse.ui.actions.OpenWithMenu;

import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.views.ProjectView;

public class OpenActionGroup extends DiagramViewActionGroup
{

	private OpenCasFileAction		openFileAction;

	private OpenSystemEditorAction	openSystemEditorAction;

	/**
	 * The id for the Open With submenu.
	 */
	public static final String		OPEN_WITH_ID	= PlatformUI.PLUGIN_ID + ".OpenWithSubMenu";	//$NON-NLS-1$

	/**
	 * Creates a new action group for open actions.
	 * 
	 * @param navigator the resource navigator
	 */
	public OpenActionGroup(ProjectView view)
	{
		super(view);
	}

	protected void makeActions()
	{
		openFileAction = new OpenCasFileAction(getView().getSite().getPage());
		openSystemEditorAction = new OpenFileAction(getView().getSite().getPage());
	}

	public void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty() && ResourceSelectionUtil.allResourcesAreOfType(selection, IResource.PROJECT | IResource.FOLDER | IResource.FILE);
		boolean onlyFilesSelected = !selection.isEmpty() && ResourceSelectionUtil.allResourcesAreOfType(selection, IResource.FILE);

		if (onlyFilesSelected)
		{
			openSystemEditorAction.selectionChanged(selection);
			menu.add(openSystemEditorAction);
			fillOpenWithMenu(menu, selection);
		}

		if (anyResourceSelected)
		{
			addNewWindowAction(menu, selection);
		}
	}

	/**
	 * Adds the OpenWith submenu to the context menu.
	 * 
	 * @param menu the context menu
	 * @param selection the current selection
	 */
	private void fillOpenWithMenu(IMenuManager menu, IStructuredSelection selection)
	{
		// 将这段代码注释掉，这样open with菜单就不会出现了
		// Only supported if exactly one file is selected.
		if (selection.size() != 1)
		{
			return;
		}
		Object element = selection.getFirstElement();
		if (!(element instanceof IFile))
		{
			return;
		}

		MenuManager submenu = new MenuManager(Messages.getString("ResourceDiagramView_openWith"), OPEN_WITH_ID);
		submenu.add(new OpenWithMenu(getView().getSite().getPage(), (IFile) element));
		menu.add(submenu);
	}

	/**
	 * Adds the Open in New Window action to the context menu.
	 * 
	 * @param menu the context menu
	 * @param selection the current selection
	 */
	private void addNewWindowAction(IMenuManager menu, IStructuredSelection selection)
	{

		// Only supported if exactly one container (i.e open project or folder)
		// is selected.
		if (selection.size() != 1)
		{
			return;
		}
		Object element = selection.getFirstElement();
		if (!(element instanceof IContainer))
		{
			return;
		}
		if (element instanceof IProject && !(((IProject) element).isOpen()))
		{
			return;
		}

		menu.add(new OpenInNewWindowAction(getView().getSite().getWorkbenchWindow(), (IContainer) element));
	}

	/**
	 * Runs the default action (open file).
	 */
	public void runDefaultAction(IStructuredSelection selection)
	{
		Object element = selection.getFirstElement();
		if (element instanceof IFile)
		{
			IFile file = (IFile) element;
			if (Utils.isIcdFile(file.getLocation().toFile()))
			{
				PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor(file.getName(), "ar.com.tadp.xmleditor.basic");
				if (openSystemEditorAction == null)
				{
					openSystemEditorAction = new OpenFileAction(getView().getSite().getPage());
				}
				openSystemEditorAction.selectionChanged(selection);
				openSystemEditorAction.run();
			} else if (Utils.isCasFile(file))
			{
				PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor(file.getName(), SPTEEditor.ID);
				openFileAction.selectionChanged(selection);
				openFileAction.run();
			} else
			{
				PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor(file.getName(), "org.eclipse.ui.systemInPlaceEditor");
				if (openSystemEditorAction == null)
				{
					openSystemEditorAction = new OpenFileAction(getView().getSite().getPage());
				}
				openSystemEditorAction.selectionChanged(selection);
				openSystemEditorAction.run();
			}

		}
	}
}