/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

/**
 * �����
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public abstract class AbstractCommand
{

	protected String	command;	// ����

	public AbstractCommand(String command)
	{
		this.command = command;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return the command <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public String getCommand()
	{
		return command;
	}

}