package com.coretek.tools.ide.ui.action.testcase;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.coretek.testcase.projectView.action.Messages;
import com.coretek.testcase.projectView.utils.ProjectUtils;
import com.coretek.testcase.projectView.views.ProjectView;

/**
 * 
 * �������Լ�
 * 
 * @author ���Ρ
 * @date 2010-12-16
 */
public class TestSuiteNewWizard extends BasicNewResourceWizard
{

	private TestSuiteWizardPage	page;

	/*
	 * �����Ŀ¼��Դ
	 */
	private IProject			project;

	private IFolder				folder;

	public TestSuiteNewWizard()
	{
		super();
	}

	public TestSuiteNewWizard(IProject project)
	{
		this.project = project;
	}

	@Override
	public void addPages()
	{
		page = new TestSuiteWizardPage(project);
		addPage(page);
	}

	@Override
	public boolean performFinish()
	{
		IRunnableWithProgress op = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException
			{
				try
				{
					doFinish(monitor);
				} catch (CoreException e)
				{
					throw new InvocationTargetException(e);
				} finally
				{
					monitor.done();
				}
			}
		};
		try
		{
			getContainer().run(true, false, op);
		} catch (InterruptedException e)
		{
			return false;
		} catch (InvocationTargetException e)
		{
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		IStructuredSelection s = (IStructuredSelection) getSelection();
		ProjectView viewPart = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.coretek.tools.ide.ui.DiagramView");
		viewPart.getTreeViewer().expandToLevel(s.getFirstElement(), 1);
		return true;
	}

	private void doFinish(IProgressMonitor monitor) throws CoreException {
		// ��ò��Լ���ţ�����Ӳ��Լ�Ŀ¼��
		String suiteNum = page.getSuiteName();
		String suiteFolderName = suiteNum;
		String suiteFolderPath = "";
		// �������Լ�Ŀ¼
		suiteFolderPath = project.getFullPath().removeFirstSegments(1)
				.toOSString()
				+ File.separator + suiteFolderName;
		ProjectUtils.createFolder(project, suiteFolderPath);

		// ����xml�ļ�����Ӹ��ļ�������Ϊtestcase�����������ļ���
		ProjectUtils
				.setFolderProperty(project.getFolder(suiteFolderPath),
						ProjectUtils.FODLDER_TYPE,
						ProjectUtils.FODLDER_TYPE_TEST_SUITE);

		project.getFolder(suiteFolderName).refreshLocal(
				IResource.DEPTH_INFINITE, null);
	}

	/*
	 * Method declared on IWorkbenchWizard.
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		super.init(workbench, currentSelection);
		setWindowTitle(Messages.getString("test.suite.wizard.page.create.test.suite"));

		Object object = selection.getFirstElement();
		IProject p = null;
		if (object instanceof IProject)
		{
			p = (IProject) object;
		} else if (object instanceof IFolder)
		{
			folder = (IFolder) object;
			p = folder.getProject();
		}
		try {
			if(p!=null && p.hasNature("com.coretek.spte.projectView.testProjectNature")){
				this.project = p;
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @return project
	 */
	public IProject getResource()
	{
		return project;
	}
	
	public void setProject(IProject project) {
		boolean result = false;
		try {
			result = project.hasNature("com.coretek.spte.projectView.testProjectNature");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.isLegal(result, "�����õĹ��̲���һ�����Թ���");
		this.project = project;
	}
}
