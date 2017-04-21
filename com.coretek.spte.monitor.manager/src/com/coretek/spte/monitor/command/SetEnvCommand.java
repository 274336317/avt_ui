/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * 设置环境变量(setEnv)命令
 * 
 * @author 孙大巍
 * @date 2012-1-13
 */
public class SetEnvCommand extends AbstractCommand
{

	/**
	 * 构建设置环境变量命令
	 * 
	 * @param endian 大小端:big、little
	 * @param port 端口号</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-13
	 */
	public SetEnvCommand(String endian, String port)
	{
		super(StringUtils.concat("setEnv,{endian=\"", endian, "\",port=\"", port, "\"}"));
	}

}