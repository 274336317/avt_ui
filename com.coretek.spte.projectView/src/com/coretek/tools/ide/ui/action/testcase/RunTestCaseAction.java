package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;

import com.coretek.spte.core.util.ImageManager;
import com.coretek.testcase.projectView.action.Messages;
import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;

/**
 * 创建测试用例
 * 
 * @author 孙大巍
 * 
 */
public class RunTestCaseAction extends Action
{
	private IFile	file;

	public RunTestCaseAction()
	{
		super();
		this.setText(Messages.getString("I18N_RUN_TESTCASE"));
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/running.gif"));
	}

	public RunTestCaseAction(IFile file)
	{
		this();
		this.file = file;
	}

	public IFile getFile()
	{
		return file;
	}

	public void setFile(IFile file)
	{
		this.file = file;
	}

	@Override
	public void run()
	{
		ExecutionQueue excutionQueue = new ExecutionQueue();
		TestCase testCase = new TestCase();
		testCase.setCaseName(file.getName());
		testCase.setProjectName(file.getProject().getName());
		testCase.setSuiteName(file.getParent().getName());
		testCase.setPath(file.getProjectRelativePath().toOSString());
		excutionQueue.add(testCase);
		excutionQueue.excute();
	}
}
