/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author SunDawei 2012-6-7
 */
public class TableContentsProvider implements IStructuredContentProvider
{

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement)
	{

		List<TestCase> cases = (List<TestCase>) inputElement;

		return cases.toArray();
	}

	public void dispose()
	{

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
	}
}
