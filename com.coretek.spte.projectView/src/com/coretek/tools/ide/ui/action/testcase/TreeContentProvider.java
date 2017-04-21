/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.tools.ide.ui.action.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author SunDawei 2012-5-7
 */
public class TreeContentProvider implements ITreeContentProvider
{

	public Object[] getChildren(Object parentElement)
	{
		if (parentElement instanceof IContainer)
		{
			IContainer prj = (IContainer) parentElement;
			try
			{
				IResource resources[] = prj.members();
				return this.filter(resources);
			} catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	private IResource[] filter(IResource resources[])
	{
		List<IResource> list = new ArrayList<IResource>();
		for (IResource resource : resources)
		{
			// 过滤掉测试用例文件
			if (resource.getName().indexOf(".") != 0 && resource.getName().indexOf(".cas") < 0)
			{
				list.add(resource);
			}
		}

		return list.toArray(new IResource[list.size()]);
	}

	public Object getParent(Object element)
	{
		if (element instanceof IResource)
		{
			IResource resource = (IResource) element;
			return resource.getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element)
	{

		return (this.getChildren(element) != null);
	}

	public Object[] getElements(Object inputElement)
	{
		if (inputElement instanceof IProject[])
		{
			IProject[] prjs = (IProject[]) inputElement;
			return Arrays.asList(prjs).toArray();
		}
		return null;
	}

	public void dispose()
	{

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{

	}

}