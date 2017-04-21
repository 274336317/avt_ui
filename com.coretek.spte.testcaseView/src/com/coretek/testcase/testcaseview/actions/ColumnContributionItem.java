/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview.actions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.coretek.common.utils.StringUtils;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * @author SunDawei 2012-6-7
 */
public class ColumnContributionItem extends ContributionItem
{

	private TestCaseViewPart	viewPart;

	public ColumnContributionItem(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	@Override
	public void fill(Menu parent, int index)
	{
		super.fill(parent, index);
		for (TableColumn column : viewPart.getTableViewer().getTable().getColumns())
		{
			if (StringUtils.isNull(column.getText()))
			{
				continue;
			}
			MenuItem item = new MenuItem(parent, SWT.CHECK, index++);
			item.setText(column.getText());
			if (column.getWidth() == 0)
			{
				item.setSelection(true);
			}
			item.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{

				}

				public void widgetSelected(SelectionEvent e)
				{
					MenuItem item = (MenuItem) e.getSource();
					TableViewer tableViewer = viewPart.getTableViewer();
					Table table = tableViewer.getTable();
					if (item.getSelection())
					{
						for (TableColumn column : table.getColumns())
						{
							if (StringUtils.isNotNull(column.getText()) && column.getText().equals(item.getText()))
							{
								column.setWidth(0);
								column.setMoveable(false);
								column.setResizable(false);
							}
						}
					} else
					{
						for (TableColumn column : table.getColumns())
						{
							if (StringUtils.isNotNull(column.getText()) && column.getText().equals(item.getText()))
							{
								column.setWidth(100);
								column.setMoveable(true);
								column.setResizable(true);
							}
						}
					}
				}
			});
		}
	}
}
