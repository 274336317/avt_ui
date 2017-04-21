/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.coretek.spte.core.util.Utils;

/**
 * �������а�ť��ѡ���¼�
 * 
 * @author SunDawei 2012-6-7
 */
public class RunImageSelectionListener implements SelectionListener
{

	private TestCaseViewPart	viewPart;

	public RunImageSelectionListener(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public void widgetDefaultSelected(SelectionEvent e)
	{

	}

	public void widgetSelected(SelectionEvent e)
	{
		Object[] checked = this.viewPart.getTableViewer().getCheckedElements();
		List<Object> list = Arrays.asList(checked);
		if (list.size() > 0)
		{
			ExecutionQueue queue = new ExecutionQueue();
			for (Object test : list)
			{
				queue.add((TestCase) test);
			}
			queue.excute();
		} else
		{
			MessageDialog.openInformation(Utils.getShell(), "��ʾ", "��ѡ����Ҫִ�еĲ���������");
		}

	}
}
