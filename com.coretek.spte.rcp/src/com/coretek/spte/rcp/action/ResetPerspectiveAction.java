package com.coretek.spte.rcp.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * ª÷∏¥Õ∏ ”Õº
 * 
 * @author ÀÔ¥ÛŒ°
 * @date 2010-11-23
 * 
 */
public class ResetPerspectiveAction implements IWorkbenchWindowActionDelegate
{

	private IWorkbenchWindow	window;

	public void dispose()
	{

	}

	public void init(IWorkbenchWindow window)
	{
		this.window = window;
	}

	public void run(IAction action)
	{
		this.window.getActivePage().resetPerspective();

	}

	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}
