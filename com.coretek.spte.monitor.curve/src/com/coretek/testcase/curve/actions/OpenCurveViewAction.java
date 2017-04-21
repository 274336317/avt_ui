/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import com.coretek.common.utils.EclipseUtils;

/**
 * 新建一个曲线视图
 * 
 * @author 尹军 2012-3-14
 */
public class OpenCurveViewAction implements IWorkbenchWindowActionDelegate
{

	private static final String	VIEW_ID	= "com.coretek.tools.curve.views.CurveView";
	
	private static int index = 0;

	public void dispose()
	{

	}

	public void init(IWorkbenchWindow window)
	{

	}

	public void run(IAction action)
	{
		IWorkbenchPage page = EclipseUtils.getActivePage();
//		IViewPart part = page.findView(VIEW_ID);
//		if (part == null)
//		{
//			try
//			{
//				page.showView(VIEW_ID);
//			} catch (PartInitException e)
//			{
//				e.printStackTrace();
//			}
//			page.bringToTop(part);
//		}
		try {
			IViewPart view = page.showView(VIEW_ID, VIEW_ID+index, IWorkbenchPage.VIEW_ACTIVATE);
			index++;
			view.setFocus();
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection)
	{

	}
}