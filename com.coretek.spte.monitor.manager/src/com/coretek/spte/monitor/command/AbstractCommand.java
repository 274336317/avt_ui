/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

/**
 * 命令父类
 * 
 * @author 孙大巍
 * @date 2012-1-13
 */
public abstract class AbstractCommand
{

	protected String	command;	// 命令

	public AbstractCommand(String command)
	{
		this.command = command;
	}

	/**
	 * 获取命令
	 * 
	 * @return the command <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public String getCommand()
	{
		return command;
	}

}