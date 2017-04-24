/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;

import com.coretek.common.template.build.file.FieldTypes;

/**
 * 属性对象
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class Attribute implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8161403323412571097L;

	private Object				value;										// 属性值

	private FieldTypes			type;										// 属性类型

	private String				name;										// 属性名

	private String				xmlName;									// 对应于ICD文件中的名字

	/**
	 * 获取属性在xml中的名字
	 * 
	 * @return the xmlName <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-9
	 */
	public String getXmlName()
	{
		return xmlName;
	}

	/**
	 * 设置属性在xml中的名字
	 * 
	 * @param xmlName the xmlName to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-9
	 */
	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	/**
	 * 构建属性对象
	 * 
	 * @param value 属性值
	 * @param type 数据类型
	 * @param name 属性的名字</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-3
	 */
	public Attribute(Object value, FieldTypes type, String name, String xmlName)
	{
		this.value = value;
		this.type = type;
		this.name = name;
		this.xmlName = xmlName;
	}

	/**
	 * 获取属性的值
	 * 
	 * @return the value <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-3
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * 获取属性的数据类型
	 * 
	 * @return the type <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-3
	 */
	public FieldTypes getType()
	{
		return type;
	}

	/**
	 * 获取属性的名字
	 * 
	 * @return the name <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-3
	 */
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 * 2012-1-9
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("name=" + this.name).append("\n");
		if (value != null)
		{
			sb.append("value=" + this.value.toString()).append("\n");
		}
		if (type != null)
		{
			sb.append("type=" + this.type.toString()).append("\n");
		}
		if (xmlName != null)
		{
			sb.append("xmlName=" + this.xmlName).append("\n");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2012-2-6
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (obj instanceof Attribute)
		{
			Attribute att = (Attribute) obj;
			if (att.name.equals(this.name))
			{
				if (att.type != null && att.type == this.type)
				{
					if (att.value != null && att.value.equals(this.value))
					{
						if (att.xmlName != null && att.xmlName.equals(this.xmlName))
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
		return this.toString().hashCode();
	}

}