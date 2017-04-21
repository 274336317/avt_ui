/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * 命令装饰类。用于将命令与命令索引号关联在一起。
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class CommandDecorator extends AbstractCommand
{

	private String				result;					// 命令执行结果

	private volatile boolean	succeed		= false;	// 命令所表示的逻辑是否被正确执行

	private volatile boolean	executed	= false;	// 命令是否被执行器执行。

	private AbstractCommand		orgCommand;				// 原始命令

	/**
	 * 构建命令装饰器
	 * 
	 * @param index 命令索引号
	 * @param command 命令</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public CommandDecorator(int index, AbstractCommand command)
	{
		super(command.getCommand());
		this.command = StringUtils.concat(index, ",", command.getCommand());
		this.orgCommand = command;
	}

	/**
	 * 获取原始命令
	 * 
	 * @return the orgCommand <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-4
	 */
	public AbstractCommand getOrgCommand()
	{
		return orgCommand;
	}

	/**
	 * 获取命令所代表的逻辑是否成功地被执行。
	 * 
	 * @return the succeed <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-16
	 */
	public boolean isSucceed()
	{
		return succeed;
	}

	/**
	 * 返回命令是否已经被执行
	 * 
	 * @return the executed <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public boolean isExecuted()
	{
		return executed;
	}

	/**
	 * 获取由发送命令后，执行器接收到后执行的结果。
	 * 
	 * @return the result <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * 设置执行结果信息。
	 * 
	 * @param executed 是否被执行器执行
	 * @param result 执行的结果
	 * @param successed 命令所表示的业务逻辑是否被执行器成功地执行了</br> <b>作者</b> 孙大巍 </br>
	 *            <b>日期</b> 2012-1-16
	 */
	public void setExecuted(boolean executed, String result, boolean successed)
	{
		this.executed = executed;
		this.result = result;
		this.succeed = successed;
	}

	/**
	 * 构建一个命令装饰器实例
	 * 
	 * @param index 命令索引号
	 * @param command 命令
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public static CommandDecorator getInstance(int index, AbstractCommand command)
	{

		return new CommandDecorator(index, command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() <br/> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 * 2012-1-14
	 */
	@Override
	public String toString()
	{

		return StringUtils.concat("command=", this.command, ";result=", this.result);
	}

}