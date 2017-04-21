package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.coretek.testcase.projectView.action.Messages;

/**
 * 创建测试用例
 * 
 * @author 孙大巍
 * @date 2011-2-18
 */
public class TestCaseCreationAction extends Action
{

	private IFolder	folder;

	public TestCaseCreationAction()
	{

	}

	public TestCaseCreationAction(IFolder folder)
	{
		setText(Messages.getString("CreatTestCaseAction.title"));
		this.folder = folder;
	}

	public void run()
	{
		Wizard wizard = new TestCaseNewWizard(this.folder);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.open();
	}
}