/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.command.CommandCache;

/**
 * 处理执行器返回的逻辑错误信息。命令格式为:编号,^error,{reason=”” }
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class LogicErrorHandler extends ResultHandler
{
	private CommandCache	cache;

	public LogicErrorHandler(CommandCache cache)
	{
		this();
		this.cache = cache;
	}

	/**
	 * @param regex
	 */
	private LogicErrorHandler()
	{
		super("[0-9]{1,}\\,(\\^error\\,\\{reason=\").*");
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
		if (StringUtils.isNull(result))
		{
			throw new IllegalArgumentException("执行结果为空。");
		}
		String[] strs = result.split("\\,");
		Integer index = Integer.valueOf(strs[0]);
		this.cache.updateCommand(index, result, false);
		this.setChanged();
		this.notifyObservers(result);
		return null;
	}

}