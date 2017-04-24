/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte;

import java.util.List;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.codeTemplate.ReferenceRules;

/**
 * 此类对应于xml上的 "功能单元" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "功能单元", xmlName = "功能单元", displayField = "", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class FunctionCells extends com.coretek.common.template.build.codeTemplate.Entity
{
	// 此属性对应于"NUM"
	@FieldRules(xmlName = "NUM", display = false, type = "Integer", node = false, editable = false, textNode = false)
	private Integer	number;

	public Integer getNumber()
	{
		return this.number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}
}
