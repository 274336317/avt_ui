package com.coretek.spte.core.reference;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

/**
 * 引用目标
 * 
 * @author 孙大巍
 * @date 2010-12-1
 * 
 */
public class ReferingCase extends Reference
{

	public ReferingCase(IProject project, IFolder testUnit, IFile testCase)
	{
		this.project = project;
		this.testUnit = testUnit;
		this.testCase = testCase;
	}

	public ReferingCase()
	{

	}

}
