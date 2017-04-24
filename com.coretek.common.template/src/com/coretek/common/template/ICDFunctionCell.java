/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能单元
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionCell extends Helper
{
	private static final long			serialVersionUID	= -6816925529117887221L;

	// 功能单元下的所有功能单元消息对象
	private List<ICDFunctionCellMsg>	cellMsgs			= new ArrayList<ICDFunctionCellMsg>();

	private ICDFunctionSubDomain		parent;

	public ICDFunctionCell(ICDFunctionSubDomain parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the cellMsgs <br/>
	 * 
	 */
	public List<ICDFunctionCellMsg> getCellMsgs()
	{
		return cellMsgs;
	}

	/**
	 * @param cellMsgs the cellMsgs to set <br/>
	 * 
	 */
	public void setCellMsgs(List<ICDFunctionCellMsg> cellMsgs)
	{
		this.cellMsgs = cellMsgs;
	}

	/**
	 * 添加功能单元消息
	 * 
	 * @param cellMsg </br>
	 */
	public void addCellMsg(ICDFunctionCellMsg cellMsg)
	{
		this.cellMsgs.add(cellMsg);
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
