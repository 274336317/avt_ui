/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能子域
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionSubDomain extends Helper
{
	// 功能子域下的所有功能单元对象
	private List<ICDFunctionCell>			cells			= new ArrayList<ICDFunctionCell>();
	// 功能子域下的所有功能节点对象
	private List<ICDFunctionNode>			nodes			= new ArrayList<ICDFunctionNode>();
	// 功能子域下的所有功能子域消息对象
	private List<ICDFunctionSubDomainMsg>	subDomainMsgs	= new ArrayList<ICDFunctionSubDomainMsg>();

	// 子域所属的父功能域
	private ICDFunctionDomain				parent;

	public ICDFunctionSubDomain(ICDFunctionDomain parent)
	{
		this.parent = parent;
	}

	public void addSubDomainMsg(ICDFunctionSubDomainMsg subDomainMsg)
	{
		subDomainMsgs.add(subDomainMsg);
	}

	/**
	 * @return the subDomains <br/>
	 */
	public List<ICDFunctionSubDomainMsg> getSubDomainMsgs()
	{
		return subDomainMsgs;
	}

	/**
	 * @param subDomains the subDomains to set <br/>
	 */
	public void setSubDomains(List<ICDFunctionSubDomainMsg> subDomainMsgs)
	{
		this.subDomainMsgs = subDomainMsgs;
	}

	/**
	 * @return the cells <br/>
	 */
	public List<ICDFunctionCell> getCells()
	{
		return cells;
	}

	public void addCell(ICDFunctionCell cell)
	{
		this.cells.add(cell);
	}

	/**
	 * @param cells the cells to set <br/>
	 */
	public void setCells(List<ICDFunctionCell> cells)
	{
		this.cells = cells;
	}

	/**
	 * @return the nodes <br/>
	 */
	public List<ICDFunctionNode> getNodes()
	{
		return nodes;
	}

	public void addNode(ICDFunctionNode node)
	{
		this.nodes.add(node);
	}

	/**
	 * @param nodes the nodes to set <br/>
	 */
	public void setNodes(List<ICDFunctionNode> nodes)
	{
		this.nodes = nodes;
	}

	/**
	 * 获取子域的父节点功能域
	 * 
	 * @return </br>
	 */
	public ICDFunctionDomain getParent()
	{
		return this.parent;
	}

}
