/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ���Ρ
 * @date 2012-1-3
 */
public abstract class Helper implements Serializable
{

	private static final long	serialVersionUID	= 7394589471702348542L;

	protected List<Attribute>	attributes			= new ArrayList<Attribute>();	// ���Լ���

	/**
	 * ��ȡ��������
	 * 
	 * @return the attributes <br/>
	 * 
	 */
	public List<Attribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes the attributes to set <br/>
	 * 
	 */
	public void setAttributes(List<Attribute> attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * �������
	 * 
	 * @param attribute </br>
	 */
	public void addAttribute(Attribute attribute)
	{
		this.attributes.add(attribute);
	}

	/**
	 * �������Ե����ֻ�ȡ���Զ���
	 * 
	 * @param name ������
	 * @return </br>
	 */
	public Attribute getAttribute(String name)
	{
		for (Attribute att : this.attributes)
		{
			if (name.equals(att.getName()))
			{
				return att;
			}
		}

		return null;
	}
}