/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableItem;

/**
 * 监听下移按钮的选中事件
 * 
 * @author SunDawei 2012-6-7
 */
public class DownBtnSelectionListener implements SelectionListener
{
	private TestCaseViewPart	viewPart;

	public DownBtnSelectionListener(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public void widgetDefaultSelected(SelectionEvent e)
	{

	}

	@SuppressWarnings("unchecked")
	public void widgetSelected(SelectionEvent e)
	{
		List<TestCase> tests = (List<TestCase>) this.viewPart.getTableViewer().getInput();
		int index = this.viewPart.getTableViewer().getTable().getSelectionIndex();
		if (index >= 0 && index != this.viewPart.getTableViewer().getTable().getItemCount() - 1)
		{
			TableItem item = this.viewPart.getTableViewer().getTable().getItem(index);
			TestCase selectCase = (TestCase) item.getData();
			TestCase nextCase = (TestCase) this.viewPart.getTableViewer().getTable().getItem(index + 1).getData();
			TestCase[] cases = tests.toArray(new TestCase[0]);
			tests.clear();
			for (int i = 0; i < cases.length; i++)
			{
				if (cases[i].equals(nextCase))
				{
					cases[i] = selectCase;
				} else if (cases[i].equals(selectCase))
				{
					cases[i] = nextCase;
				}
				tests.add(cases[i]);
			}
		}

		this.viewPart.getTableViewer().setInput(tests);
		this.viewPart.getTableViewer().refresh();

	}
}
