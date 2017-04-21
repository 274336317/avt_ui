/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.StringUtils;

/**
 * 命令缓存
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class CommandCache
{

	private static final Logger				logger	= LoggingPlugin.getLogger(CommandCache.class.getName());

	// 命令索引
	private volatile int					index	= 0;

	// 保存命令
	private Map<Integer, AbstractCommand>	cache	= new HashMap<Integer, AbstractCommand>();

	public CommandCache()
	{

	}

	/**
	 * 添加命令到命令缓存中。
	 * 
	 * @param command 命令</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public synchronized CommandDecorator addCommand(AbstractCommand command)
	{
		++index;
		CommandDecorator cd = new CommandDecorator(index, command);
		this.cache.put(Integer.valueOf(index), cd);
		logger.info(StringUtils.concat("添加命令:", cd.getCommand()));
		return cd;
	}

	/**
	 * 将命令标记为已经被执行
	 * 
	 * @param index 命令索引号
	 * @param result
	 * @param successed
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-4
	 */
	public synchronized CommandDecorator updateCommand(int index, String result, boolean successed)
	{
		CommandDecorator command = (CommandDecorator) this.cache.get(index);
		if (command != null)
		{
			logger.info(StringUtils.concat(command.getCommand(), " 命令被执行了。执行的返回结果为:\n", result));
			command.setExecuted(true, result, successed);
		} else
		{
			logger.warning(StringUtils.concat("无法找到index=", index, "的命令。result=", result));
		}

		return command;

	}

	/**
	 * 清空命令缓存 </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public synchronized void clear()
	{
		this.cache.clear();
		this.index = 0;
		logger.config("命令缓存已经被清空。");
	}

}