package com.coretek.testcase.projectView.projectwizard.page;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * 软件测试工程创建的向导页
 * 
 * @author 孙大巍
 * @date 2010-11-26
 * 
 */
public class TestingProjectCreationPage extends AbstractNewProjectCreationPage
{

	private CheckboxTreeViewer	ctv;

	public TestingProjectCreationPage(String pageName)
	{
		super(pageName, null);
	}

	/**
	 * Get the first selected icd file.
	 * 
	 * @return
	 */
	public IResource getSelected()
	{
		Object[] objs = ctv.getCheckedElements();
		for (Object obj : objs)
		{
			if (obj instanceof IFile)
			{
				return (IFile) obj;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org
	 * .eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);
		Dialog.applyDialogFont(getControl());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.coretek.testcase.projectView.projectwizard.page.
	 * AbstractNewProjectCreationPage
	 * #createUserEntryArea(org.eclipse.swt.widgets.Composite, boolean)
	 */
	@Override
	protected void createUserEntryArea(Composite composite, boolean defaultEnabled)
	{
		// DO NOTHING
	}

}