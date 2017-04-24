/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������
 * 
 * @author ���Ρ
 * @date 2012-1-4
 */
public class ICDFunctionSubDomain extends Helper
{
	// ���������µ����й��ܵ�Ԫ����
	private List<ICDFunctionCell>			cells			= new ArrayList<ICDFunctionCell>();
	// ���������µ����й��ܽڵ����
	private List<ICDFunctionNode>			nodes			= new ArrayList<ICDFunctionNode>();
	// ���������µ����й���������Ϣ����
	private List<ICDFunctionSubDomainMsg>	subDomainMsgs	= new ArrayList<ICDFunctionSubDomainMsg>();

	// ���������ĸ�������
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
	 * ��ȡ����ĸ��ڵ㹦����
	 * 
	 * @return </br>
	 */
	public ICDFunctionDomain getParent()
	{
		return this.parent;
	}

}
