/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ������
 * 
 * @author ���Ρ
 * @date 2012-1-4
 */
public class ICDFunctionDomain extends Helper
{

	// �������µ������������
	private List<ICDFunctionSubDomain>	subDomains	= new ArrayList<ICDFunctionSubDomain>();
	// �������µ����й�������Ϣ
	private List<ICDFunctionDomainMsg>	domainMsgs	= new ArrayList<ICDFunctionDomainMsg>();

	/**
	 * ��ȡ��������
	 * 
	 * @return the subDomains <br/>
	 * 
	 */
	public List<ICDFunctionSubDomain> getSubDomains()
	{
		return subDomains;
	}

	/**
	 * �������
	 * 
	 * @param subDomain </br>
	 */
	public void addSubDomain(ICDFunctionSubDomain subDomain)
	{
		this.subDomains.add(subDomain);
	}

	/**
	 * @param subDomains the subDomains to set <br/>
	 * 
	 */
	public void setSubDomains(List<ICDFunctionSubDomain> subDomains)
	{
		this.subDomains = subDomains;
	}

	/**
	 * @return the domainMsg <br/>
	 * 
	 */
	public List<ICDFunctionDomainMsg> getDomainMsg()
	{
		return domainMsgs;
	}

	/**
	 * @param domainMsg the domainMsg to set <br/>
	 * 
	 */
	public void setDomainMsg(List<ICDFunctionDomainMsg> domainMsg)
	{
		this.domainMsgs = domainMsg;
	}

	public void addDomainMsg(ICDFunctionDomainMsg domainMsg)
	{
		this.domainMsgs.add(domainMsg);
	}

}