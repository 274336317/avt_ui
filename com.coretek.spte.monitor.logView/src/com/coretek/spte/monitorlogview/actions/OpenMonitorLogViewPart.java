/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.coretek.common.utils.EclipseUtils;

/**
 * �򿪼����־��ͼ
 * 
 * @author ���� 2012-3-14
 */
public class OpenMonitorLogViewPart implements IWorkbenchWindowActionDelegate
{

	public void dispose()
	{

	}

	public void init(IWorkbenchWindow window)
	{

	}

	public void run(IAction action)
	{
		EclipseUtils.openView("com.coretek.spte.monitorLog.view");

	}

	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}
