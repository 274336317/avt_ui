package com.coretek.testcase.projectView.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.TextActionHandler;

import com.coretek.testcase.projectView.views.ProjectView;

/**
 * This is the action group for refactor actions, including global action
 * handlers for copy, paste and delete.
 * 
 */
public class RefactorActionGroup extends DiagramViewActionGroup
{

	private Clipboard						clipboard;

	private CopyAction						copyAction;
	private DeleteResourceAction			deleteAction;
	private PasteAction						pasteAction;
	// ��"�ƶ�"�˵�ע�͵�
	private ResourceDiagramViewRenameAction	renameAction;
	private ResourceDiagramViewMoveAction	moveAction;
	private TextActionHandler				textActionHandler;

	public RefactorActionGroup(ProjectView view)
	{
		super(view);
	}

	public void dispose()
	{
		if (clipboard != null)
		{
			clipboard.dispose();
			clipboard = null;
		}
		super.dispose();
	}

	public void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection celements = (IStructuredSelection) getContext().getSelection();
		IStructuredSelection selection = SelectionConverter.convertSelectionToResources(celements);

		boolean anyResourceSelected = !selection.isEmpty() && SelectionConverter.allResourcesAreOfType(selection, IResource.PROJECT | IResource.FOLDER | IResource.FILE);

		copyAction.selectionChanged(selection);
		menu.add(copyAction);
		pasteAction.selectionChanged(selection);
		menu.add(pasteAction);

		if (anyResourceSelected)
		{
			deleteAction.selectionChanged(selection);
			menu.add(deleteAction);
			moveAction.selectionChanged(selection);
			menu.add(moveAction);
			menu.add(new Separator());
			renameAction.selectionChanged(selection);
			menu.add(renameAction);
		}
	}

	public void fillActionBars(IActionBars actionBars)
	{
		textActionHandler = new TextActionHandler(actionBars); // hooks handlers
		textActionHandler.setCopyAction(copyAction);
		textActionHandler.setPasteAction(pasteAction);
		textActionHandler.setDeleteAction(deleteAction);
		renameAction.setTextActionHandler(textActionHandler);

		actionBars.setGlobalActionHandler(ActionFactory.MOVE.getId(), moveAction);
		actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), renameAction);
	}

	/**
	 * Handles a key pressed event by invoking the appropriate action.
	 */
	public void handleKeyPressed(KeyEvent event)
	{
		if (event.character == SWT.DEL && event.stateMask == 0)
		{
			if (deleteAction.isEnabled())
			{
				deleteAction.run();
			}
			// Swallow the event.
			event.doit = false;
		} else if (event.keyCode == SWT.F2 && event.stateMask == 0)
		{
			if (renameAction.isEnabled())
			{
				renameAction.run();
			}
			// Swallow the event.
			event.doit = false;
		}
	}

	protected void makeActions()
	{
		TreeViewer treeViewer = getView().getViewer();
		Shell shell = getView().getSite().getShell();
		clipboard = new Clipboard(shell.getDisplay());

		pasteAction = new PasteAction(shell, clipboard);
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		pasteAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		pasteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pasteAction.setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));

		copyAction = new CopyAction(shell, clipboard, pasteAction);
		copyAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		copyAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		copyAction.setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));

		moveAction = new ResourceDiagramViewMoveAction(shell, treeViewer);
		renameAction = new ResourceDiagramViewRenameAction(shell, treeViewer);

		deleteAction = new DeleteResourceAction(shell);
		deleteAction.setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		deleteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteAction.setHoverImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	}

	public void updateActionBars()
	{
		IStructuredSelection celements = (IStructuredSelection) getContext().getSelection();
		IStructuredSelection selection = SelectionConverter.convertSelectionToResources(celements);

		copyAction.selectionChanged(selection);
		pasteAction.selectionChanged(selection);
		deleteAction.selectionChanged(selection);
		moveAction.selectionChanged(selection);
		renameAction.selectionChanged(selection);
	}
}
