/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.command.CommandCache;
import com.coretek.spte.monitor.command.CommandDecorator;

/**
 * 处理执行器返回的done信息。命令的格式为:<b>编号,^done,{reason=""}</b>
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class DoneHandler extends ResultHandler
{
	public final static String	RUN		= "run";

	public final static String	EXIT	= "exit";

	private CommandCache		cache;

	public DoneHandler(CommandCache cache)
	{
		this();
		this.cache = cache;
	}

	private DoneHandler()
	{
		super("[0-9]{1,}\\,\\^(done).*");
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IInfoHandler#parser(java.lang
	 * .String) <br/>
	 */
	@Override
	public Object parser(String result)
	{
		String[] strs = result.split("\\,");
		int index = Integer.valueOf(strs[0]);
		// 将命令的状态更改为已经被执行
		CommandDecorator cd = this.cache.updateCommand(index, result, true);
		if (cd != null)
		{
			String str = cd.getOrgCommand().getCommand();
			if (StringUtils.isNotNull(str) && str.indexOf("run,") >= 0)
			{
				this.setChanged();
				// 执行器运行完了测试用例，发出通知
				this.notifyListeners(RUN);
				this.clearChanged();
			} else if (StringUtils.isNotNull(str) && str.indexOf("exit") >= 0)
			{
				this.setChanged();
				// 通知所有监听器执行器已经退出
				this.notifyListeners(EXIT);
				this.clearChanged();
			}
		}
		return null;
	}

}