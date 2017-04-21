/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * ����װ���ࡣ���ڽ����������������Ź�����һ��
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class CommandDecorator extends AbstractCommand
{

	private String				result;					// ����ִ�н��

	private volatile boolean	succeed		= false;	// ��������ʾ���߼��Ƿ���ȷִ��

	private volatile boolean	executed	= false;	// �����Ƿ�ִ����ִ�С�

	private AbstractCommand		orgCommand;				// ԭʼ����

	/**
	 * ��������װ����
	 * 
	 * @param index ����������
	 * @param command ����</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public CommandDecorator(int index, AbstractCommand command)
	{
		super(command.getCommand());
		this.command = StringUtils.concat(index, ",", command.getCommand());
		this.orgCommand = command;
	}

	/**
	 * ��ȡԭʼ����
	 * 
	 * @return the orgCommand <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-4
	 */
	public AbstractCommand getOrgCommand()
	{
		return orgCommand;
	}

	/**
	 * ��ȡ������������߼��Ƿ�ɹ��ر�ִ�С�
	 * 
	 * @return the succeed <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-16
	 */
	public boolean isSucceed()
	{
		return succeed;
	}

	/**
	 * ���������Ƿ��Ѿ���ִ��
	 * 
	 * @return the executed <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public boolean isExecuted()
	{
		return executed;
	}

	/**
	 * ��ȡ�ɷ��������ִ�������յ���ִ�еĽ����
	 * 
	 * @return the result <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * ����ִ�н����Ϣ��
	 * 
	 * @param executed �Ƿ�ִ����ִ��
	 * @param result ִ�еĽ��
	 * @param successed ��������ʾ��ҵ���߼��Ƿ�ִ�����ɹ���ִ����</br> <b>����</b> ���Ρ </br>
	 *            <b>����</b> 2012-1-16
	 */
	public void setExecuted(boolean executed, String result, boolean successed)
	{
		this.executed = executed;
		this.result = result;
		this.succeed = successed;
	}

	/**
	 * ����һ������װ����ʵ��
	 * 
	 * @param index ����������
	 * @param command ����
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public static CommandDecorator getInstance(int index, AbstractCommand command)
	{

		return new CommandDecorator(index, command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() <br/> <b>����</b> ���Ρ </br> <b>����</b>
	 * 2012-1-14
	 */
	@Override
	public String toString()
	{

		return StringUtils.concat("command=", this.command, ";result=", this.result);
	}

}