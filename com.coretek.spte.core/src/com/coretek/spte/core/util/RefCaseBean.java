package com.coretek.spte.core.util;

import java.io.Serializable;

import com.coretek.common.utils.StringUtils;

/**
 * 引用测试用例的数据结构
 * 
 * @author 孙大巍
 * @date 2010-11-30
 * 
 */
public class RefCaseBean implements Cloneable, Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5265841146024252116L;

	/**
	 * 工程名
	 */
	private String				projectName;

	/**
	 * 测试集名
	 */
	private String				unitName;

	/**
	 * 用例名
	 */
	private String				caseName;

	public RefCaseBean()
	{

	}

	/**
	 * 
	 * @param projectName 工程名
	 * @param unitName 测试集名
	 * @param caseName 测试用例名
	 */
	public RefCaseBean(String projectName, String unitName, String caseName)
	{
		this.projectName = projectName;
		this.unitName = unitName;
		this.caseName = caseName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{

		this.unitName = unitName;
	}

	public String getCaseName()
	{
		return caseName;
	}

	public void setCaseName(String caseName)
	{

		this.caseName = caseName;
	}

	@Override
	public String toString()
	{
		if (StringUtils.isNull(this.projectName) || StringUtils.isNull(this.unitName) || StringUtils.isNull(this.caseName))
		{
			return "";
		}
		return this.projectName + "/" + this.unitName + "/" + this.caseName;
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
		if (obj instanceof RefCaseBean)
		{
			RefCaseBean rb = (RefCaseBean) obj;
			if (this.projectName.equals(rb.getProjectName()))
			{
				if (this.unitName.equals(rb.getUnitName()))
				{
					if (this.caseName.equals(rb.getCaseName()))
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
		result = 31 * result + this.projectName.hashCode();
		result = 31 * result + this.unitName.hashCode();
		result = 31 * result + this.caseName.hashCode();
		return result;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		RefCaseBean rb = (RefCaseBean) super.clone();
		return rb;
	}

}
