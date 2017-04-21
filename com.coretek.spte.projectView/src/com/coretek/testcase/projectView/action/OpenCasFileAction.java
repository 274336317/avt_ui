package com.coretek.testcase.projectView.action;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.OpenSystemEditorAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.DialogUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.coretek.tools.ide.internal.ui.IHelpContextIds;

/**
 * Open case file
 * 
 * @author SunDawei 2012-5-12
 */
public class OpenCasFileAction extends OpenSystemEditorAction
{

	/**
	 * The id of this action.
	 */
	public static final String	ID	= PlatformUI.PLUGIN_ID + ".OpenCasFileAction";	//$NON-NLS-1$

	/**
	 * The editor to open.
	 */
	private IEditorDescriptor	editorDescriptor;

	private IWorkbenchPage		page;

	/**
	 * Creates a new action that will open editors on the then-selected file
	 * resources. Equivalent to <code>OpenFileAction(page,null)</code>.
	 * 
	 * @param page the workbench page in which to open the editor
	 */
	public OpenCasFileAction(IWorkbenchPage page)
	{
		this(page, new SPTEEditorDescriptor());
	}

	/**
	 * Creates a new action that will open instances of the specified editor on
	 * the then-selected file resources.
	 * 
	 * @param page the workbench page in which to open the editor
	 * @param descriptor the editor descriptor, or <code>null</code> if
	 *            unspecified
	 */
	public OpenCasFileAction(IWorkbenchPage page, IEditorDescriptor descriptor)
	{
		super(page);
		setText(descriptor == null ? Messages.getString("OpenFileAction_text") : descriptor.getLabel());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.OPEN_FILE_ACTION);
		setToolTipText(Messages.getString("OpenFileAction_toolTip"));
		setId(ID);
		this.page = page;
		this.editorDescriptor = descriptor;
	}

	/**
	 * Opens an editor on the given file resource.
	 * 
	 * @param file the file resource
	 */
	private void openFile(final IFile file)
	{
		try
		{
			boolean activate = OpenStrategy.activateOnOpen();
			if (editorDescriptor == null)
			{
				IDE.openEditor(this.page, file, activate);
			} else
			{
				this.page.openEditor(new FileEditorInput(file), editorDescriptor.getId(), activate);
			}
		} catch (PartInitException e)
		{
			DialogUtil.openError(this.page.getWorkbenchWindow().getShell(), Messages.getString("OpenFileAction_openFileShellTitle"), e.getMessage(), e);
		}
	}

	public void run()
	{
		Iterator itr = getSelectedResources().iterator();
		while (itr.hasNext())
		{
			IResource resource = (IResource) itr.next();
			if (resource instanceof IFile)
			{
				openFile((IFile) resource);
			}
		}
	}

}
