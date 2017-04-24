/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ܵ�Ԫ
 * 
 * @author ���Ρ
 * @date 2012-1-4
 */
public class ICDFunctionCell extends Helper
{
	private static final long			serialVersionUID	= -6816925529117887221L;

	// ���ܵ�Ԫ�µ����й��ܵ�Ԫ��Ϣ����
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
	 * ��ӹ��ܵ�Ԫ��Ϣ
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
