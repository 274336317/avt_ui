/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * 运行(run)命令
 * 
 * @author 孙大巍
 * @date 2012-1-13
 */
public class RunCommand extends AbstractCommand
{

	/**
	 * 构建一个运行命令
	 * 
	 * @param testCase
	 * @param startMsg
	 * @param prjPath
	 * @param endMsg </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public RunCommand(String testCase, String startMsg, String endMsg, String prjPath)
	{
		super(StringUtils.concat("run,", "{testCase=\"", testCase, "\", startMsg=\"", startMsg, "\", endMsg=\"", endMsg, "\", plaftformPath=\"", prjPath, "\"}"));
	}

}