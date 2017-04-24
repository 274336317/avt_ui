/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * ���ܽڵ���Ϣ�������ȡ��Ϣ������ֵʱ�������spteMsg�µ�ICDMsg�����ȡ��Ӧ����ֵ��
 * 
 * @author ���Ρ
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
	 * ��ȡ��Ϣ�����Ĺ��ܽڵ㡣�����Ϣ������������������ã��������Ϊ��
	 * 
	 * @return the parentNode <br/>
	 * 
	 */
	public ICDFunctionNode getParentNode()
	{
		return parentNode;
	}

	/**
	 * �����Ϣ���������������ã���ȡ��Ϣ�����Ĺ�������
	 * 
	 * @return the parentSubDomain <br/>
	 * 
	 */
	public ICDFunctionSubDomain getParentSubDomain()
	{
		return parentSubDomain;
	}

	/**
	 * �����Ϣ�������������ã���ȡ��Ϣ�����Ĺ�����
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