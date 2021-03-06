/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.testcase;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;

/**
 * 此类对应于xml上的 "postils" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "postils", xmlName = "postils", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class Postils extends com.coretek.common.template.XMLBean
{

	/** */
	private static final long	serialVersionUID	= -6010924583038467132L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 * 2012-4-24
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		List<Entity> dest = new ArrayList<Entity>(this.getChildren().size());
		for (Entity entity : this.getChildren())
		{
			Postil postil = (Postil) entity;
			Postil clonedP = (Postil) postil.clone();
			dest.add(clonedP);
		}
		Postils p = (Postils) super.clone();
		p.setChildren(dest);
		return p;
	}
}