/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * @author ���Ρ
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
