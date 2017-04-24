/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.testcase;

import java.io.Serializable;

import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * @author SunDawei 2012-8-13
 */
@EntityRules(xpath = "simuObject", xmlName = "simuObject", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class SimuObject extends com.coretek.common.template.XMLBean implements Serializable, Cloneable
{

	/** */
	private static final long	serialVersionUID	= -4825748001209001767L;

	@FieldRules(xmlName = "id", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				id;

	@FieldRules(xmlName = "name", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				name;

	/**
	 * @return the id <br/>
	 *         <b>Author</b> SunDawei </br> <b>Date</b> 2012-8-13
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set <br/>
	 *            <b>Author</b> SunDawei </br> <b>Date</b> 2012-8-13
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the name <br/>
	 *         <b>Author</b> SunDawei </br> <b>Date</b> 2012-8-13
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set <br/>
	 *            <b>Author</b> SunDawei </br> <b>Date</b> 2012-8-13
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.common.template.XMLBean#clone() <br/> <b>Author</b>
	 * SunDawei </br> <b>Date</b> 2012-8-13
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{

		return super.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) <br/> <b>Author</b>
	 * SunDawei </br> <b>Date</b> 2012-8-13
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
			return false;
		if (null == this.id)
			return false;
		if (null == this.name)
			return false;
		if (obj instanceof SimuObject)
		{
			SimuObject so = (SimuObject) obj;
			if (this.id.equals(so.id))
			{
				if (this.name.equals(so.name))
				{
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() <br/> <b>Author</b> SunDawei </br>
	 * <b>Date</b> 2012-8-13
	 */
	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.id.hashCode();
		result = result * 31 + this.name.hashCode();
		return result;
	}

}