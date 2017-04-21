/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * @author SunDawei 2012-5-30
 */
public class ICDProjectNature implements IProjectNature
{
	private IProject	project;

	public ICDProjectNature()
	{

	}

	public void configure() throws CoreException
	{

	}

	public void deconfigure() throws CoreException
	{

	}

	public IProject getProject()
	{
		return this.project;
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}
}