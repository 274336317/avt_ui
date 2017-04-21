/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.spte.monitor.command.handler.HandlerRegistries;
import com.coretek.spte.monitor.command.handler.ResultHandler;

/**
 * 处理器线程。当读取线程接收到执行器的输出后，会创建此类的一个对象进行处理。
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class ResultHandlerThread extends Thread
{

	private String	result;

	public ResultHandlerThread(String result)
	{
		this.result = result;
		this.setName("命令返回处理线程");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	@Override
	public void run()
	{
		ResultHandler handler = HandlerRegistries.getHandler(result);
		if (handler != null)
		{
			handler.parser(result);
		}
	}

}