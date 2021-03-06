/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * 此类对应于xml上的 "object" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "object", xmlName = "object", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class TestedObject extends com.coretek.common.template.XMLBean
{
	/** */
	private static final long	serialVersionUID	= 2095269436271634792L;
	// 此属性对应于"id"
	@FieldRules(xmlName = "id", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				id;
	// 此属性对应于"name"
	@FieldRules(xmlName = "name", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				name;

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 * 2012-4-24
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{

		return super.clone();
	}

}
