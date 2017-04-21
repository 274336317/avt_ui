package com.coretek.testcase.testcaseview;

import java.io.File;

import com.coretek.common.utils.StringUtils;

/**
 * 测试用例
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class TestCase
{
	/**
	 * 工程名
	 */
	private String			projectName;
	/**
	 * 测试集名
	 */
	private String			suiteName;
	/**
	 * 用例名
	 */
	private String			caseName;

	/**
	 * 用例路径
	 */
	private String			path;

	/**
	 * 测试集路径
	 */
	private String			suitePath;

	/**
	 * 禁止/启用标识位
	 */
	private boolean			enabled	= true;

	private TestCaseGroup	group;

	public TestCaseGroup getGroup()
	{
		return group;
	}

	public void setGroup(TestCaseGroup group)
	{
		this.group = group;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getSuiteName()
	{
		return suiteName;
	}

	public void setSuiteName(String suiteName)
	{
		this.suiteName = suiteName;
	}

	public String getCaseName()
	{
		return caseName;
	}

	public void setCaseName(String caseName)
	{
		this.caseName = caseName;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getSuitePath()
	{
		return suitePath;
	}

	public void setSuitePath(String suitePath)
	{
		this.suitePath = suitePath;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
		if (StringUtils.isNotNull(path))
		{
			String pt = path.substring(0, path.lastIndexOf("\\"));
			setSuitePath(pt);
		}

	}

	@Override
	public String toString()
	{

		return new StringBuilder(this.projectName).append(File.separator).append(this.path).toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (this == obj)
		{
			return true;
		}
		if (obj instanceof TestCase)
		{
			TestCase test = (TestCase) obj;
			if (StringUtils.isNotNull(test.projectName) && test.projectName.equals(this.projectName))
			{
				if (StringUtils.isNotNull(test.suiteName) && test.suiteName.equals(this.suiteName))
				{
					if (StringUtils.isNotNull(test.caseName) && test.caseName.equals(this.caseName))
					{
						if (StringUtils.isNotNull(test.path) && test.path.equals(this.path))
						{
							return true;
						}

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
		result = result * 31 + this.projectName.hashCode();
		result = result * 31 + this.suiteName.hashCode();
		result = result * 31 + this.caseName.hashCode();

		return result;
	}
}
