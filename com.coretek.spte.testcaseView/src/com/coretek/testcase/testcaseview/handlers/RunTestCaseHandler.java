package com.coretek.testcase.testcaseview.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;

/**
 * 测试用例运行的Handler,主要用于工作台工具栏中
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RunTestCaseHandler extends AbstractHandler
{
	public RunTestCaseHandler()
	{

	}

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if (editorPart == null)
		{
			return null;
		}
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput == null || !(editorInput instanceof IFileEditorInput))
		{
			return null;
		}
		IFile file = ((IFileEditorInput) editorInput).getFile();
		ExecutionQueue excutionQueue = new ExecutionQueue();
		TestCase testCase = new TestCase();
		testCase.setCaseName(file.getName());
		testCase.setProjectName(file.getProject().getName());
		testCase.setSuiteName(file.getParent().getName());
		testCase.setPath(file.getProjectRelativePath().toOSString());
		excutionQueue.add(testCase);
		excutionQueue.excute();
		return null;
	}
}
