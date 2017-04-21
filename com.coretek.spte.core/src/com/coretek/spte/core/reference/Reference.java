package com.coretek.spte.core.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

/**
 * 引用
 * 
 * @author 孙大巍
 * @date 2010-12-1
 * 
 */
public abstract class Reference
{

	/**
	 * 工程
	 */
	protected IProject	project;
	/**
	 * 测试集
	 */
	protected IFolder	testUnit;
	/**
	 * 测试用例
	 */
	protected IFile		testCase;

	public IProject getProject()
	{
		return project;
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}

	public IFolder getTestUnit()
	{
		return testUnit;
	}

	public void setTestUnit(IFolder testUnit)
	{
		this.testUnit = testUnit;
	}

	public IFile getTestCase()
	{
		return testCase;
	}

	public void setTestCase(IFile testCase)
	{
		this.testCase = testCase;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		if (obj instanceof Reference)
		{
			Reference re = (Reference) obj;
			if (re.getProject().equals(this.project))
			{
				if (re.getTestUnit().equals(this.testUnit))
				{
					if (re.getTestCase().equals(this.testCase))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.project.hashCode();
		result = result * 31 + this.testUnit.hashCode();
		result = result * 31 + this.testCase.hashCode();
		return result;
	}

	@Override
	public String toString()
	{

		return this.project.getName() + "/" + this.testUnit.getProjectRelativePath() + "/" + this.testCase.getName();
	}

	public static void main(String[] args)
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 1000000000; i++)
		{
			UUID id = java.util.UUID.randomUUID();
			if (list.indexOf(id.toString()) >= 0)
			{
				break;
			} else
			{
				list.add(id.toString());
			}
		}
	}
}
