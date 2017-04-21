/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * 监听刷新按钮被选中的事件
 * 
 * @author SunDawei 2012-6-7
 */
public class RefreshImageSelectionListener implements SelectionListener
{
	private TestCaseViewPart	viewPart;

	public RefreshImageSelectionListener(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public void widgetDefaultSelected(SelectionEvent e)
	{

	}

	public void widgetSelected(SelectionEvent e)
	{
		this.viewPart.refereshTable();
	}
}
