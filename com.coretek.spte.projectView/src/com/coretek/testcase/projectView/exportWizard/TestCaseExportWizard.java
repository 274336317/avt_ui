package com.coretek.testcase.projectView.exportWizard;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;

public class TestCaseExportWizard extends Wizard implements IExportWizard
{
	private IStructuredSelection				selection;

	private WizardFileSystemResourceExportPage1	mainPage;

	/**
	 * Creates a wizard for exporting workspace resources to the local file
	 * system.
	 */
	public TestCaseExportWizard()
	{
		IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("FileSystemExportWizard");//$NON-NLS-1$
		if (section == null)
		{
			section = workbenchSettings.addNewSection("FileSystemExportWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public void addPages()
	{
		super.addPages();
		mainPage = new WizardFileSystemResourceExportPage1(selection);
		addPage(mainPage);
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		this.selection = currentSelection;
		List selectedResources = IDE.computeSelectedResources(currentSelection);
		if (!selectedResources.isEmpty())
		{
			this.selection = new StructuredSelection(selectedResources);
		}

		// look it up if current selection (after resource adapting) is empty
		if (selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null)
		{
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if (page != null)
			{
				IEditorPart currentEditor = page.getActiveEditor();
				if (currentEditor != null)
				{
					Object selectedResource = currentEditor.getEditorInput().getAdapter(IResource.class);
					if (selectedResource != null)
					{
						selection = new StructuredSelection(selectedResource);
					}
				}
			}
		}

		setWindowTitle(DataTransferMessages.DataTransfer_export);
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportdir_wiz.png"));//$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish()
	{
		return mainPage.finish();
	}
}
