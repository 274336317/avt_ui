/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author SunDawei 2012-6-7
 */
public class TablePaintListener implements PaintListener
{
	public void paintControl(PaintEvent e)
	{
		Table table = (Table) e.getSource();
		TableItem[] items = table.getItems();

		for (TableItem item : items)
		{
			TestCase test = (TestCase) item.getData();
			if (test != null && !test.isEnabled())
			{
				item.setForeground(ColorConstants.gray);
			}
		}
	}
}
