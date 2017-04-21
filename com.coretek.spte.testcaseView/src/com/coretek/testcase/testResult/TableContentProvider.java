/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testResult;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.coretek.spte.dataCompare.CompareResult;

/**
 * @author SunDawei 2012-6-6
 */
public class TableContentProvider implements IStructuredContentProvider
{

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement)
	{
		if (inputElement instanceof List<?>)
		{
			List<CompareResult> list = (List<CompareResult>) inputElement;
			return list.toArray(new CompareResult[list.size()]);
		}
		return (CompareResult[]) inputElement;
	}

	public void dispose()
	{

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{

	}

}
