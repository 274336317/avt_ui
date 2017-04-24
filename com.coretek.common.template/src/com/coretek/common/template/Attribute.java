/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;

import com.coretek.common.template.build.file.FieldTypes;

/**
 * ���Զ���
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class Attribute implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8161403323412571097L;

	private Object				value;										// ����ֵ

	private FieldTypes			type;										// ��������

	private String				name;										// ������

	private String				xmlName;									// ��Ӧ��ICD�ļ��е�����

	/**
	 * ��ȡ������xml�е�����
	 * 
	 * @return the xmlName <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-9
	 */
	public String getXmlName()
	{
		return xmlName;
	}

	/**
	 * ����������xml�е�����
	 * 
	 * @param xmlName the xmlName to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2012-1-9
	 */
	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	/**
	 * �������Զ���
	 * 
	 * @param value ����ֵ
	 * @param type ��������
	 * @param name ���Ե�����</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-3
	 */
	public Attribute(Object value, FieldTypes type, String name, String xmlName)
	{
		this.value = value;
		this.type = type;
		this.name = name;
		this.xmlName = xmlName;
	}

	/**
	 * ��ȡ���Ե�ֵ
	 * 
	 * @return the value <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-3
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * ��ȡ���Ե���������
	 * 
	 * @return the type <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-3
	 */
	public FieldTypes getType()
	{
		return type;
	}

	/**
	 * ��ȡ���Ե�����
	 * 
	 * @return the name <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-3
	 */
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() <br/> <b>����</b> ���Ρ </br> <b>����</b>
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
	 * @see java.lang.Object#equals(java.lang.Object) <br/> <b>����</b> ���Ρ </br>
	 * <b>����</b> 2012-2-6
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