/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.testcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;

/**
 * @author SunDawei 2012-8-13
 */
@EntityRules(xpath = "simuObjects", xmlName = "simuObjects", displayField = "", dragable = false, superClazz = "com.coretek.common.template.XMLBean")
public class SimuObjects extends com.coretek.common.template.XMLBean implements Serializable, Cloneable
{

	/** */
	private static final long	serialVersionUID	= 8305946853023714118L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.common.template.XMLBean#clone() <br/> <b>Author</b>
	 * SunDawei </br> <b>Date</b> 2012-8-13
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		List<Entity> dest = new ArrayList<Entity>(this.getChildren().size());
		for (Entity entity : this.getChildren())
		{
			dest.add((Entity) ((SimuObject) entity).clone());
		}
		SimuObjects sos = (SimuObjects) super.clone();
		sos.setChildren(dest);
		return sos;
	}

}