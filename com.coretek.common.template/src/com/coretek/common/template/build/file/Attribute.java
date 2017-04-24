/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.file;

/**
 * 属性
 * 
 * @author 孙大巍 2011-12-23
 */
public class Attribute
{

	private String		name;

	private String		xmlName;				// xml上的名字

	private boolean		display		= false;	// 是否需要显示

	private boolean		editable	= false;	// 是否可编辑

	private FieldTypes	type;					// 属性的数据类型

	private boolean		node;					// 是否为节点

	private boolean		textNode;				// 是否为文本节点

	/**
	 * @return the textNode <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-30
	 */
	public boolean isTextNode()
	{
		return textNode;
	}

	/**
	 * @param textNode the textNode to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-30
	 */
	public void setTextNode(boolean textNode)
	{
		this.textNode = textNode;
	}

	/**
	 * @return the editable <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
	 */
	public boolean isEditable()
	{
		return editable;
	}

	/**
	 * @param editable the editable to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-26
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}

	/**
	 * @return the node <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public boolean isNode()
	{
		return node;
	}

	/**
	 * @param node the node to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void setNode(boolean node)
	{
		this.node = node;
	}

	/**
	 * @return the display <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-24
	 */
	public boolean isDisplay()
	{
		return display;
	}

	/**
	 * @param display the display to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-24
	 */
	public void setDisplay(boolean display)
	{
		this.display = display;
	}

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public Attribute()
	{

	}

	/**
	 * @return the name <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the xmlName <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getXmlName()
	{
		return xmlName;
	}

	/**
	 * @param xmlName the xmlName to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	/**
	 * @return the type <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public FieldTypes getType()
	{
		return type;
	}

	/**
	 * @param type the type to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setType(FieldTypes type)
	{
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2011-12-23
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj instanceof Attribute)
		{
			Attribute field = (Attribute) obj;
			if (this.name != null && this.name.equals(field.name))
			{
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 * 2011-12-23
	 */
	@Override
	public int hashCode()
	{
		if (this.name == null || this.name.trim().length() == 0)
			return 0;

		return this.name.hashCode();
	}

}
