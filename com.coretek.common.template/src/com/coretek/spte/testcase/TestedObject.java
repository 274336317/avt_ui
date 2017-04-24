/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * �����Ӧ��xml�ϵ� "object" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "object", xmlName = "object", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class TestedObject extends com.coretek.common.template.XMLBean
{
	/** */
	private static final long	serialVersionUID	= 2095269436271634792L;
	// �����Զ�Ӧ��"id"
	@FieldRules(xmlName = "id", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				id;
	// �����Զ�Ӧ��"name"
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
	 * @see java.lang.Object#clone() <br/> <b>����</b> ���Ρ </br> <b>����</b>
	 * 2012-4-24
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{

		return super.clone();
	}

}