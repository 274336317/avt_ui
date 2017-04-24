/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * �����Ӧ��xml�ϵ� "testObjects" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "testObjects", xmlName = "testObjects", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class TestedObjects extends com.coretek.common.template.XMLBean
{
	/** */
	private static final long	serialVersionUID	= 6205337069536860200L;
	// �����Զ�Ӧ��"level"
	@FieldRules(xmlName = "level", display = false, type = "String", node = false, editable = false, textNode = false)
	private String				level;

	public String getLevel()
	{
		return this.level;
	}

	public void setLevel(String level)
	{
		this.level = level;
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
		List<Entity> dest = new ArrayList<Entity>(this.getChildren().size());
		for (Entity entity : this.getChildren())
		{
			TestedObject to = (TestedObject) entity;
			TestedObject clonedTO = (TestedObject) to.clone();
			dest.add(clonedTO);
		}
		TestedObjects objects = (TestedObjects) super.clone();
		objects.setChildren(dest);
		return objects;
	}

}