/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ܶ������Ϣ
 * 
 * @author ���Ρ
 * @date 2012-1-4
 */
public abstract class ICDFunctionObjMsg extends Helper
{

	// �ڵ���Ϣ
	private List<ICDFunctionNodeMsg>	nodeMsgs	= new ArrayList<ICDFunctionNodeMsg>();

	// ��Ԫ��Ϣ
	private List<ICDFunctionCellMsg>	cellMsgs	= new ArrayList<ICDFunctionCellMsg>();

	public ICDFunctionObjMsg()
	{

	}

	/**
	 * ��ȡ���ܶ������������й��ܽڵ���Ϣ
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
	 * ��ӽڵ���Ϣ
	 * 
	 * @param nodeMsg </br>
	 */
	public void addNodeMsg(ICDFunctionNodeMsg nodeMsg)
	{
		this.nodeMsgs.add(nodeMsg);
	}

	/**
	 * ��ȡ���ܶ������������й��ܵ�Ԫ��Ϣ��
	 * 
	 * @return the cellMsgs <br/>
	 * 
	 */
	public List<ICDFunctionCellMsg> getCellMsgs()
	{
		return cellMsgs;
	}

	/**
	 * ��ӹ��ܵ�Ԫ��Ϣ��
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
