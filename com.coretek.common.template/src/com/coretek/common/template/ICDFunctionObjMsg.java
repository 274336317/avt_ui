/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能对象的消息
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public abstract class ICDFunctionObjMsg extends Helper
{

	// 节点消息
	private List<ICDFunctionNodeMsg>	nodeMsgs	= new ArrayList<ICDFunctionNodeMsg>();

	// 单元消息
	private List<ICDFunctionCellMsg>	cellMsgs	= new ArrayList<ICDFunctionCellMsg>();

	public ICDFunctionObjMsg()
	{

	}

	/**
	 * 获取功能对象所属的所有功能节点消息
	 * 
	 * @return the nodeMsgs <br/>
	 * 
	 */
	public List<ICDFunctionNodeMsg> getNodeMsgs()
	{
		return nodeMsgs;
	}

	/**
	 * 
	 * @param nodeMsgs the nodeMsgs to set <br/>
	 * 
	 */
	public void setNodeMsgs(List<ICDFunctionNodeMsg> nodeMsgs)
	{
		this.nodeMsgs = nodeMsgs;
	}

	/**
	 * 添加节点消息
	 * 
	 * @param nodeMsg </br>
	 */
	public void addNodeMsg(ICDFunctionNodeMsg nodeMsg)
	{
		this.nodeMsgs.add(nodeMsg);
	}

	/**
	 * 获取功能对象所属的所有功能单元消息。
	 * 
	 * @return the cellMsgs <br/>
	 * 
	 */
	public List<ICDFunctionCellMsg> getCellMsgs()
	{
		return cellMsgs;
	}

	/**
	 * 添加功能单元消息。
	 * 
	 * @param cellMsg </br>
	 */
	public void addCellMsg(ICDFunctionCellMsg cellMsg)
	{
		this.cellMsgs.add(cellMsg);
	}

	/**
	 * @param cellMsgs the cellMsgs to set <br/>
	 * 
	 */
	public void setCellMsgs(List<ICDFunctionCellMsg> cellMsgs)
	{
		this.cellMsgs = cellMsgs;
	}
}
