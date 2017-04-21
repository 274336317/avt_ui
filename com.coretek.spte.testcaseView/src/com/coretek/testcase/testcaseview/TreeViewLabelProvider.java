/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.coretek.common.utils.StringUtils;

/**
 * @author SunDawei 2012-6-7
 */
public class TreeViewLabelProvider implements ILabelProvider
{

	public Image getImage(Object element)
	{

		return null;
	}

	public String getText(Object element)
	{
		if (element instanceof TestCaseGroup)
		{
			return ((TestCaseGroup) element).getName();
		}
		return StringUtils.EMPTY_STRING;
	}

	public void addListener(ILabelProviderListener listener)
	{

	}

	public void dispose()
	{

	}

	public boolean isLabelProperty(Object element, String property)
	{

		return false;
	}

	public void removeListener(ILabelProviderListener listener)
	{

	}
}
