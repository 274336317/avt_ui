/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author SunDawei 2012-6-7
 */
public class TreeViewContentProvider implements ITreeContentProvider
{
	public void dispose()
	{

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{

	}

	@SuppressWarnings("unchecked")
	public Object[] getChildren(Object parentElement)
	{
		if (parentElement instanceof TestCaseGroup)
		{
			TestCaseGroup group = (TestCaseGroup) parentElement;
			if (group.getChildGroups().size() > 0)
			{
				return group.getChildGroups().toArray();
			}
		} else if (parentElement instanceof Set<?>)
		{
			Set<TestCaseGroup> groups = (Set<TestCaseGroup>) parentElement;
			return groups.toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element)
	{
		if (element instanceof TestCaseGroup)
		{
			return ((TestCaseGroup) element).getParentGroup();
		}
		return null;
	}

	public boolean hasChildren(Object element)
	{
		if (element instanceof TestCaseGroup)
		{
			TestCaseGroup group = (TestCaseGroup) element;
			return (group.getChildGroups().size() > 0);
		} else if (element instanceof Set<?>)
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement)
	{
		if (inputElement instanceof Set<?>)
		{
			Set<TestCaseGroup> groups = (Set<TestCaseGroup>) inputElement;

			return groups.toArray();
		}
		return new Object[] { inputElement };
	}
}
