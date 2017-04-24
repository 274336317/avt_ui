/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * 功能单元消息。当想获取消息的属性值时，请调用spteMsg下的ICDMsg对象获取对应属性值。
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionCellMsg extends Helper
{

	private static final long		serialVersionUID	= 6721420885356567295L;

	private SPTEMsg					spteMsg;

	private ICDFunctionCell			parentCell;

	private ICDFunctionSubDomain	parentSubDomain;

	private ICDFunctionDomain		parentDomain;

	public ICDFunctionCellMsg(SPTEMsg spteMsg, ICDFunctionSubDomain parentSubDomain)
	{
		this.spteMsg = spteMsg;
		this.parentSubDomain = parentSubDomain;
	}

	public ICDFunctionCellMsg(SPTEMsg spteMsg, ICDFunctionDomain parentDomain)
	{
		this.spteMsg = spteMsg;
		this.parentDomain = parentDomain;
	}

	/**
	 * 当消息被子域所引用时，可以获取消息所属的子域。
	 * 
	 * @return the parentSubDomain <br/>
	 * 
	 */
	public ICDFunctionSubDomain getParentSubDomain()
	{
		return parentSubDomain;
	}

	/**
	 * 当消息被域所引用时，可以获取消息所属的域。
	 * 
	 * @return the parentDomain <br/>
	 * 
	 */
	public ICDFunctionDomain getParentDomain()
	{
		return parentDomain;
	}

	/**
	 * 获取消息所属的功能单元。如果消息被功能域消息或功能子域消息所引用，则此属性为空
	 * 
	 * @return the parent <br/>
	 * 
	 */
	public ICDFunctionCell getParentCell()
	{
		return parentCell;
	}

	/**
	 * 当消息被功能单元所引用时，可以获取消息所属的功能单元。
	 * 
	 * @param parent </br>
	 */
	public ICDFunctionCellMsg(ICDFunctionCell parent)
	{
		this.parentCell = parent;
	}

	/**
	 * @return the spteMsg <br/>
	 * 
	 */
	public SPTEMsg getSpteMsg()
	{
		return spteMsg;
	}

	/**
	 * @param spteMsg the spteMsg to set <br/>
	 * 
	 */
	public void setSpteMsg(SPTEMsg spteMsg)
	{
		this.spteMsg = spteMsg;
	}

}
