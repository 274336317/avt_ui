package com.coretek.testcase.projectView.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ImportICDFileWizard extends Wizard implements IImportWizard
{
	ImportICDFileWizardPage	mainPage;

	public ImportICDFileWizard()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish()
	{
		IFile file = mainPage.createNewFile();
		if (file == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		setWindowTitle("ICD文件导入"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ImportICDFileWizardPage("ICD文件", selection); // NON-NLS-1

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages()
	{
		super.addPages();
		addPage(mainPage);
	}

}