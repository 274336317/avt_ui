/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * ����ִ��������info�������ʽΪ:^info,{time=""}
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class InfoHandler extends ResultHandler
{

	public InfoHandler()
	{
		super("\\^(info)\\,\\{.*");
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IInfoHandler#parser(java.lang
	 * .String)s
	 */
	@Override
	public Object parser(String result)
	{
		return null;
	}

}