/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionDomainMsg extends ICDFunctionObjMsg
{

	private ICDFunctionDomain	parent;

	public ICDFunctionDomainMsg(ICDFunctionDomain parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the parent <br/>
	 */
	public ICDFunctionDomain getParent()
	{
		return parent;
	}

}
