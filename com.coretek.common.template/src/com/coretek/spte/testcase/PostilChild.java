/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * 此类对应于xml上的 "postilChild" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "postilChild", xmlName = "postilChild", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class PostilChild extends com.coretek.common.template.XMLBean
{
	/** */
	private static final long	serialVersionUID	= -6285980529642141401L;
	// 此属性对应于"text"
	@FieldRules(xmlName = "text", display = false, type = "String", node = true, editable = false, textNode = false)
	private String				text;
	// 此属性对应于"width"
	@FieldRules(xmlName = "width", display = false, type = "Integer", node = false, editable = false, textNode = false)
	private Integer				width;
	// 此属性对应于"high"
	@FieldRules(xmlName = "high", display = false, type = "Integer", node = false, editable = false, textNode = false)
	private Integer				high;
	// 此属性对应于"y"
	@FieldRules(xmlName = "y", display = false, type = "Integer", node = false, editable = false, textNode = false)
	private Integer				y;
	// 此属性对应于"x"
	@FieldRules(xmlName = "x", display = false, type = "Integer", node = false, editable = false, textNode = false)
	private Integer				x;

	public String getText()
	{
		return this.text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Integer getWidth()
	{
		return this.width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public Integer getHigh()
	{
		return this.high;
	}

	public void setHigh(Integer high)
	{
		this.high = high;
	}

	public Integer getY()
	{
		return this.y;
	}

	public void setY(Integer y)
	{
		this.y = y;
	}

	public Integer getX()
	{
		return this.x;
	}

	public void setX(Integer x)
	{
		this.x = x;
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
