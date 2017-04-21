/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testResult;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;

/**
 * @author SunDawei 2012-6-6
 */
public class DropdownSelectionListener extends SelectionAdapter
{
	private Menu					menu;

	private CheckboxTableViewer		tableViewer;

	private List<SelectionListener>	listeners	= new ArrayList<SelectionListener>();

	/**
	 * Constructs a DropdownSelectionListener
	 * 
	 * @param dropdown the dropdown this listener belongs to
	 */
	public DropdownSelectionListener(ToolItem dropdown)
	{
		menu = new Menu(dropdown.getParent().getShell());
	}

	public void setTableViewer(CheckboxTableViewer tableViewer)
	{
		this.tableViewer = tableViewer;
		for (SelectionListener listener : listeners)
		{
			listener.setTableViewer(tableViewer);
		}
	}

	/**
	 * Adds an item to the dropdown list
	 * 
	 * @param item the item to add
	 */
	public void add(String item)
	{
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(item);
		SelectionListener listener = new SelectionListener();
		menuItem.addSelectionListener(listener);
		this.listeners.add(listener);
	}

	public void widgetSelected(SelectionEvent event)
	{
		if (event.detail == SWT.ARROW)
		{
			ToolItem item = (ToolItem) event.widget;
			Rectangle rect = item.getBounds();
			Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
			menu.setLocation(pt.x, pt.y + rect.height);
			menu.setVisible(true);
		} else
		{
			ExecutionQueue excutionQueue = new ExecutionQueue();

			Object[] objects = tableViewer.getCheckedElements();
			if (objects.length > 0)
			{
				for (Object object : objects)
				{
					if (object instanceof CompareResult)
					{
						CompareResult compareResult = (CompareResult) object;
						IFile file = compareResult.getTestCaseFile();
						TestCase testCase = new TestCase();
						testCase.setCaseName(file.getName());
						testCase.setSuiteName(file.getParent().getName());
						testCase.setPath(file.getProjectRelativePath().toOSString());
						testCase.setProjectName(file.getProject().getName());
						excutionQueue.add(testCase);
					}
				}
			} else
			{
				MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), "选择用例不能为空！");
			}
			// 执行测试队列
			if (excutionQueue.getQueue().size() > 0)
			{
				excutionQueue.excute();
			}

		}
	}

}
