/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * 处理执行器返回的系统错误信息。命令格式为:^error,{reason="" }
 * 
 * @author 孙大巍
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