/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能域
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionDomain extends Helper
{

	// 功能域下的所有子域对象
	private List<ICDFunctionSubDomain>	subDomains	= new ArrayList<ICDFunctionSubDomain>();
	// 功能域下的所有功能域消息
	private List<ICDFunctionDomainMsg>	domainMsgs	= new ArrayList<ICDFunctionDomainMsg>();

	/**
	 * 获取所有子域
	 * 
	 * @return the subDomains <br/>
	 * 
	 */
	public List<ICDFunctionSubDomain> getSubDomains()
	{
		return subDomains;
	}

	/**
	 * 添加子域
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