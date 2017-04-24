/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * ���ܵ�Ԫ��Ϣ�������ȡ��Ϣ������ֵʱ�������spteMsg�µ�ICDMsg�����ȡ��Ӧ����ֵ��
 * 
 * @author ���Ρ
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
	 * ����Ϣ������������ʱ�����Ի�ȡ��Ϣ����������
	 * 
	 * @return the parentSubDomain <br/>
	 * 
	 */
	public ICDFunctionSubDomain getParentSubDomain()
	{
		return parentSubDomain;
	}

	/**
	 * ����Ϣ����������ʱ�����Ի�ȡ��Ϣ��������
	 * 
	 * @return the parentDomain <br/>
	 * 
	 */
	public ICDFunctionDomain getParentDomain()
	{
		return parentDomain;
	}

	/**
	 * ��ȡ��Ϣ�����Ĺ��ܵ�Ԫ�������Ϣ����������Ϣ����������Ϣ�����ã��������Ϊ��
	 * 
	 * @return the parent <br/>
	 * 
	 */
	public ICDFunctionCell getParentCell()
	{
		return parentCell;
	}

	/**
	 * ����Ϣ�����ܵ�Ԫ������ʱ�����Ի�ȡ��Ϣ�����Ĺ��ܵ�Ԫ��
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
