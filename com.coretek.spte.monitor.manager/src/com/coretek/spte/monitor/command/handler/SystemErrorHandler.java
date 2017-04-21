/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * ����ִ�������ص�ϵͳ������Ϣ�������ʽΪ:^error,{reason="" }
 * 
 * @author ���Ρ
 * @date 2012-1-15
 */
public class SystemErrorHandler extends ResultHandler
{

	public SystemErrorHandler()
	{
		super("^(\\^error\\,\\{reason=\").*");
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IResultHandler#parser(java.lang
	 * .String)
	 */
	@Override
	public Object parser(String result)
	{

		return null;
	}

}