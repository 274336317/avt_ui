/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;

/**
 * �����Ӧ��xml�ϵ� "criterion" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "criterion", xmlName = "criterion", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class Criterion extends com.coretek.common.template.XMLBean
{
	/** */
	private static final long	serialVersionUID	= -1338876079883356899L;
	// �����Զ�Ӧ��"unexpected"
	@FieldRules(xmlName = "unexpected", display = false, type = "String", node = true, editable = false, textNode = false)
	private String				unexpected;

	public String getUnexpected()
	{
		return this.unexpected;
	}

	public void setUnexpected(String unexpected)
	{
		this.unexpected = unexpected;
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