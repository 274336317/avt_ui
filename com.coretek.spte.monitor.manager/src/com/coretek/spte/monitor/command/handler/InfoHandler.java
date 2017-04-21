/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * 处理执行器返回info。命令格式为:^info,{time=""}
 * 
 * @author 孙大巍
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