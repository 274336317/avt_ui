/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.coretek.spte.core.util.Utils;

/**
 * 导入测试用例向导
 * 
 * @author 孙大巍
 * @date 2010-11-29
 * 
 */
public class ImportTestCaseWizard extends Wizard implements IImportWizard
{
	private ImportTestCaseWizardPage	mainPage;

	public ImportTestCaseWizard()
	{
		super();
	}

	public boolean performFinish()
	{
		IFile file = mainPage.createNewFile();
		if (file == null || !file.exists())
			return false;
		if (!Utils.validateImportedTestCase(file))
		{
			mainPage.setErrorMessage("选择导入的测试用例格式错误。");
			try
			{
				file.delete(true, null);
			} catch (CoreException e)
			{
				e.printStackTrace();
			}
			return false;
		}

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
		setWindowTitle("导入测试用例向导"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ImportTestCaseWizardPage("导入测试用例", selection); // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages()
	{
		addPage(mainPage);
	}

}
