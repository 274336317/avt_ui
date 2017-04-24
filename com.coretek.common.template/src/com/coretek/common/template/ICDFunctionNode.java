/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能节点
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionNode extends Helper
{

	private List<ICDFunctionNodeMsg>	nodeMsgs	= new ArrayList<ICDFunctionNodeMsg>();

	private ICDFunctionSubDomain		parent;

	public ICDFunctionNode(ICDFunctionSubDomain parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the nodeMsgs <br/>
	 * 
	 */
	public List<ICDFunctionNodeMsg> getNodeMsgs()
	{
		return nodeMsgs;
	}

	/**
	 * @param nodeMsgs the nodeMsgs to set <br/>
	 * 
	 */
	public void setNodeMsgs(List<ICDFunctionNodeMsg> nodeMsgs)
	{
		this.nodeMsgs = nodeMsgs;
	}

	public void addNodeMsg(ICDFunctionNodeMsg nodeMsg)
	{
		this.nodeMsgs.add(nodeMsg);

	}

	/**
	 * @return the parent <br/>
	 * 
	 */
	public ICDFunctionSubDomain getParent()
	{
		return parent;
	}

}
