/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �������������
 * 
 * @author ���Ρ
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
			mainPage.setErrorMessage("ѡ����Ĳ���������ʽ����");
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
		setWindowTitle("�������������"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new ImportTestCaseWizardPage("�����������", selection); // NON-NLS-1
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
