/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * @author ���Ρ
 * @date 2012-1-4
 */
public class ICDFunctionSubDomainMsg extends ICDFunctionObjMsg
{

	private ICDFunctionSubDomain	parent;

	public ICDFunctionSubDomainMsg(ICDFunctionSubDomain parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the parent <br/>
	 */
	public ICDFunctionSubDomain getParent()
	{
		return parent;
	}

}
