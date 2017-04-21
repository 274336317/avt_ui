/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * 执行器返回结果处理器
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public interface IResultHandler
{

	/**
	 * 解析执行器返回信息
	 * 
	 * @param result 执行器返回信息
	 * @return
	 */
	public Object parser(String result);

	/**
	 * 用于判断此结果处理器是否能够处理特定的返回结果
	 * 
	 * @param result 执行器返回结果
	 * @return
	 */
	public boolean isMyType(String result);

}
