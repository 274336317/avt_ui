/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * 功能节点消息。当想获取消息的属性值时，请调用spteMsg下的ICDMsg对象获取对应属性值。
 * 
 * @author 孙大巍
 * @date 2012-1-4
 */
public class ICDFunctionNodeMsg extends Helper
{
	private static final long		serialVersionUID	= 8726625041286207713L;

	private SPTEMsg					spteMsg;

	private ICDFunctionNode			parentNode;

	private ICDFunctionSubDomain	parentSubDomain;

	private ICDFunctionDomain		parentDomain;

	public ICDFunctionNodeMsg(SPTEMsg spteMsg, ICDFunctionSubDomain parentSubDomain)
	{
		this.spteMsg = spteMsg;
		this.parentSubDomain = parentSubDomain;
	}

	public ICDFunctionNodeMsg(SPTEMsg spteMsg, ICDFunctionDomain parentDomain)
	{
		this.spteMsg = spteMsg;
		this.parentDomain = parentDomain;
	}

	public ICDFunctionNodeMsg(ICDFunctionNode parent)
	{
		this.parentNode = parent;
	}

	/**
	 * 获取消息所属的功能节点。如果消息被功能子域或功能域引用，则此属性为空
	 * 
	 * @return the parentNode <br/>
	 * 
	 */
	public ICDFunctionNode getParentNode()
	{
		return parentNode;
	}

	/**
	 * 如果消息被功能子域所引用，获取消息所属的功能子域。
	 * 
	 * @return the parentSubDomain <br/>
	 * 
	 */
	public ICDFunctionSubDomain getParentSubDomain()
	{
		return parentSubDomain;
	}

	/**
	 * 如果消息被功能域所引用，获取消息所属的功能域。
	 * 
	 * @return the parentDomain <br/>
	 * 
	 */
	public ICDFunctionDomain getParentDomain()
	{
		return parentDomain;
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