/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.file;

/**
 * 引用关系
 * 
 * @author 孙大巍 2011-12-25
 */
public class Reference
{

	private String	type;

	private String	name;

	private String	condition;

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public Reference()
	{

	}

	/**
	 * @return the type <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type the type to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the name <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the condition <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public String getCondition()
	{
		return condition;
	}

	/**
	 * @param condition the condition to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void setCondition(String condition)
	{
		this.condition = condition;
	}

}
