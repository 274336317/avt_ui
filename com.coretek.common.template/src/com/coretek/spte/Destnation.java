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
 * 此类对应于xml上的 "目的功能ID" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "目的功能ID", xmlName = "目的功能ID", displayField = "", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class Destnation extends com.coretek.common.template.build.codeTemplate.Entity
{
	// 此属性对应于"destID"
	@FieldRules(xmlName = "destID", display = false, type = "Integer", node = true, editable = false, textNode = true)
	private Integer	destID;

	public Integer getDestID()
	{
		return this.destID;
	}

	public void setDestID(Integer destID)
	{
		this.destID = destID;
	}
}
