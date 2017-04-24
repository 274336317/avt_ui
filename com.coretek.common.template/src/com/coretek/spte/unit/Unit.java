/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.unit;

import java.util.List;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.codeTemplate.ReferenceRules;

/**
 * �����Ӧ��xml�ϵ� "��λ" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "��λ", xmlName = "��λ", displayField = "null", dragable = false)
public class Unit extends Entity
{
	// �����Զ�Ӧ��"����"
	@FieldRules(xmlName = "����", display = false, type = "String", node = true, editable = false)
	private String	name;
	// �����Զ�Ӧ��"ID"
	@FieldRules(xmlName = "ID", display = false, type = "Integer", node = true, editable = false)
	private Integer	ID;
	// �����Զ�Ӧ��"��ʾ����"
	@FieldRules(xmlName = "��ʾ����", display = false, type = "String", node = true, editable = false)
	private String	displayName;

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

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String toString()
	{
		String str = "";
		str = str + "name=" + this.name + "\n; ";
		str = str + "ID=" + this.ID + "\n; ";
		str = str + "displayName=" + this.displayName + "\n; ";
		return str;
	}
}