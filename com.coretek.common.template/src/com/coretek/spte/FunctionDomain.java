/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte;

import java.util.List;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.codeTemplate.ReferenceRules;

/**
 * �����Ӧ��xml�ϵ� "������" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "������", xmlName = "������", displayField = "name", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class FunctionDomain extends com.coretek.common.template.build.codeTemplate.Entity
{
	// �����Զ�Ӧ��"����"
	@FieldRules(xmlName = "����", display = false, type = "String", node = true, editable = false, textNode = false)
	private String	name;
	// �����Զ�Ӧ��"ID"
	@FieldRules(xmlName = "ID", display = false, type = "Integer", node = true, editable = false, textNode = false)
	private Integer	ID;
	// �����Զ�Ӧ��"������"
	@FieldRules(xmlName = "������", display = false, type = "String", node = true, editable = false, textNode = false)
	private String	abbreviation;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getID()
	{
		return this.ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public String getAbbreviation()
	{
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation)
	{
		this.abbreviation = abbreviation;
	}
}