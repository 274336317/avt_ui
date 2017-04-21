/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.coretek.common.utils.EclipseUtils;

/**
 * 打开监控消息视图
 * 
 * @author 尹军 2012-3-14
 */
public class OpenMonitorDomainView implements IWorkbenchWindowActionDelegate
{

	@Override
	public void dispose()
	{

	}

	@Override
	public void init(IWorkbenchWindow window)
	{

	}

	@Override
	public void run(IAction action)
	{
		EclipseUtils.openView("com.coretek.testcase.monitorView.monitorDomainView");

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}
