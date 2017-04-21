package com.coretek.testcase.projectView.views;

import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.AddBookmarkAction;
import org.eclipse.ui.actions.AddTaskAction;
import org.eclipse.ui.actions.ExportResourcesAction;
import org.eclipse.ui.actions.ImportResourcesAction;
import org.eclipse.ui.actions.WorkingSetFilterActionGroup;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.operations.UndoRedoActionGroup;

import com.coretek.testcase.projectView.action.CollapseAllAction;
import com.coretek.testcase.projectView.action.DiagramViewActionGroup;
import com.coretek.testcase.projectView.action.GotoActionGroup;
import com.coretek.testcase.projectView.action.OpenActionGroup;
import com.coretek.testcase.projectView.action.RefactorActionGroup;
import com.coretek.testcase.projectView.action.ToggleLinkingAction;
import com.coretek.testcase.projectView.action.WorkspaceActionGroup;
import com.coretek.tools.ide.ui.action.testcase.TestCaseActionGroup;

/**
 * pop up menu group
 * 
 * @author Administrator
 * 
 */
public class MainActionGroup extends DiagramViewActionGroup
{

	protected AddBookmarkAction		addBookmarkAction;

	protected AddTaskAction			addTaskAction;

	protected PropertyDialogAction	propertyDialogAction;

	protected ImportResourcesAction	importAction;

	protected ExportResourcesAction	exportAction;

	protected CollapseAllAction		collapseAllAction;

	protected ToggleLinkingAction	toggleLinkingAction;

	protected GotoActionGroup		gotoGroup;

	protected OpenActionGroup		openGroup;

	protected RefactorActionGroup	refactorGroup;

	protected UndoRedoActionGroup	undoRedoGroup;

	protected WorkspaceActionGroup	workspaceGroup;

	private IResourceChangeListener	resourceChangeListener;

	private NewWizardMenu			newWizardMenu;

	private TestCaseActionGroup		testCaseActionGroup;

	/**
	 * Constructs the main action group.
	 * 
	 * @param navigator the DiagramView view
	 */
	public MainActionGroup(ProjectView view)
	{
		super(view);
		resourceChangeListener = new IResourceChangeListener()
		{
			public void resourceChanged(IResourceChangeEvent event)
			{
				handleResourceChanged(event);
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
		makeSubGroups();
	}

	/**
	 * Handles a resource changed event by updating the enablement if one of the
	 * selected projects is opened or closed.
	 */
	protected void handleResourceChanged(IResourceChangeEvent event)
	{
		ActionContext context = getContext();
		if (context == null)
		{
			return;
		}

		final IStructuredSelection selection = (IStructuredSelection) context.getSelection();
		if (!ResourceSelectionUtil.allResourcesAreOfType(selection, IResource.PROJECT))
		{
			return;
		}
		List sel = selection.toList();
		IResourceDelta delta = event.getDelta();
		if (delta == null)
		{
			return;
		}
		IResourceDelta[] projDeltas = delta.getAffectedChildren(IResourceDelta.CHANGED);
		for (int i = 0; i < projDeltas.length; ++i)
		{
			IResourceDelta projDelta = projDeltas[i];
			if ((projDelta.getFlags() & (IResourceDelta.OPEN | IResourceDelta.DESCRIPTION)) != 0)
			{
				if (sel.contains(projDelta.getResource()))
				{
					getView().getSite().getShell().getDisplay().syncExec(new Runnable()
					{
						public void run()
						{
							addTaskAction.selectionChanged(selection);
							gotoGroup.updateActionBars();
							refactorGroup.updateActionBars();
							workspaceGroup.updateActionBars();
						}
					});
				}
			}
		}
	}

	/**
	 * Makes the actions contained directly in this action group.
	 */
	protected void makeActions()
	{
		IShellProvider provider = getView().getSite();

		newWizardMenu = new NewWizardMenu(getView().getSite().getWorkbenchWindow());
		addBookmarkAction = new AddBookmarkAction(provider, true);
		addTaskAction = new AddTaskAction(provider);
		propertyDialogAction = new PropertyDialogAction(provider, getView().getViewer());

		importAction = new ImportResourcesAction(getView().getSite().getWorkbenchWindow());
		importAction.setDisabledImageDescriptor(getImageDescriptor("dtool16/import_wiz.gif")); //$NON-NLS-1$
		importAction.setImageDescriptor(getImageDescriptor("etool16/import_wiz.gif")); //$NON-NLS-1$		

		exportAction = new ExportResourcesAction(getView().getSite().getWorkbenchWindow());
		exportAction.setDisabledImageDescriptor(getImageDescriptor("dtool16/export_wiz.gif")); //$NON-NLS-1$
		exportAction.setImageDescriptor(getImageDescriptor("etool16/export_wiz.gif")); //$NON-NLS-1$		

		collapseAllAction = new CollapseAllAction(getView());
		toggleLinkingAction = new ToggleLinkingAction(getView());

	}

	/**
	 * Makes the sub action groups.
	 */
	protected void makeSubGroups()
	{
		gotoGroup = new GotoActionGroup(getView());
		openGroup = new OpenActionGroup(getView());
		refactorGroup = new RefactorActionGroup(getView());
		IPropertyChangeListener workingSetUpdater = new IPropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent event)
			{
				String property = event.getProperty();

				if (WorkingSetFilterActionGroup.CHANGE_WORKING_SET.equals(property))
				{

					Object newValue = event.getNewValue();
					if (newValue instanceof IWorkingSet)
					{
						getView().setWorkingSet((IWorkingSet) newValue);
					} else if (newValue == null)
					{
						getView().setWorkingSet(null);
					}
				}
			}
		};

		TreeViewer treeView = getView().getViewer();
		workspaceGroup = new WorkspaceActionGroup(getView());
		IUndoContext workspaceContext = (IUndoContext) ResourcesPlugin.getWorkspace().getAdapter(IUndoContext.class);
		undoRedoGroup = new UndoRedoActionGroup(getView().getSite(), workspaceContext, true);

		testCaseActionGroup = new TestCaseActionGroup(getView());
	}

	/**
	 * Extends the superclass implementation to set the context in the
	 * subgroups.
	 */
	public void setContext(ActionContext context)
	{
		super.setContext(context);
		gotoGroup.setContext(context);
		openGroup.setContext(context);
		refactorGroup.setContext(context);
		// sortAndFilterGroup.setContext(context);
		workspaceGroup.setContext(context);
		undoRedoGroup.setContext(context);
		testCaseActionGroup.setContext(context);
	}

	/**
	 * Fills the context menu with the actions contained in this group and its
	 * subgroups.
	 * 
	 * @param menu the context menu
	 */
	public void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		newWizardMenu.setSelection(selection);
		MenuManager newMenu = new MenuManager(WorkbenchMessages.NewWizard_title);
		menu.add(newMenu);
		newMenu.add(newWizardMenu);

		menu.add(new Separator());
		testCaseActionGroup.fillContextMenu(menu);
		menu.add(new Separator());

		refactorGroup.fillContextMenu(menu);
		this.openGroup.fillContextMenu(menu);
		menu.add(new Separator());

		menu.add(importAction);
		menu.add(exportAction);
		importAction.selectionChanged(selection);
		exportAction.selectionChanged(selection);
		menu.add(new Separator());

		workspaceGroup.fillContextMenu(menu);

		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end"));
		menu.add(new Separator());

		if (selection.size() == 1)
		{
			propertyDialogAction.selectionChanged(selection);
			menu.add(propertyDialogAction);
		}
	}

	/**
	 * Adds the actions in this group and its subgroups to the action bars.
	 */
	public void fillActionBars(IActionBars actionBars)
	{
		actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), propertyDialogAction);
		actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), addBookmarkAction);
		actionBars.setGlobalActionHandler(IDEActionFactory.ADD_TASK.getId(), addTaskAction);

		gotoGroup.fillActionBars(actionBars);
		testCaseActionGroup.fillActionBars(actionBars);
		refactorGroup.fillActionBars(actionBars);
		workspaceGroup.fillActionBars(actionBars);
		undoRedoGroup.fillActionBars(actionBars);

		IMenuManager menu = actionBars.getMenuManager();
		menu.add(toggleLinkingAction);

		IToolBarManager toolBar = actionBars.getToolBarManager();
		toolBar.add(new Separator());
		toolBar.add(collapseAllAction);
		toolBar.add(toggleLinkingAction);
	}

	/**
	 * Updates the actions which were added to the action bars, delegating to
	 * the subgroups as necessary.
	 */
	public void updateActionBars()
	{
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		propertyDialogAction.setEnabled(selection.size() == 1);
		addBookmarkAction.selectionChanged(selection);
		addTaskAction.selectionChanged(selection);

		gotoGroup.updateActionBars();
		openGroup.updateActionBars();
		testCaseActionGroup.updateActionBars();
		refactorGroup.updateActionBars();
		workspaceGroup.updateActionBars();
		undoRedoGroup.updateActionBars();
	}

	/**
	 * Runs the default action (open file) by delegating the open group.
	 */
	public void runDefaultAction(IStructuredSelection selection)
	{
		openGroup.runDefaultAction(selection);
	}

	/**
	 * Handles a key pressed event by invoking the appropriate action,
	 * delegating to the subgroups as necessary.
	 */
	public void handleKeyPressed(KeyEvent event)
	{
		refactorGroup.handleKeyPressed(event);
		workspaceGroup.handleKeyPressed(event);
	}

	/**
	 * Extends the superclass implementation to dispose the actions in this
	 * group and its subgroups.
	 */
	public void dispose()
	{
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);

		newWizardMenu.dispose();
		importAction.dispose();
		exportAction.dispose();
		propertyDialogAction.dispose();

		gotoGroup.dispose();
		openGroup.dispose();
		testCaseActionGroup.dispose();
		refactorGroup.dispose();
		workspaceGroup.dispose();
		undoRedoGroup.dispose();
		super.dispose();
	}
}
