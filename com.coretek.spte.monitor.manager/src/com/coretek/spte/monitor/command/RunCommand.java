/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * ����(run)����
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class RunCommand extends AbstractCommand
{

	/**
	 * ����һ����������
	 * 
	 * @param testCase
	 * @param startMsg
	 * @param prjPath
	 * @param endMsg </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public RunCommand(String testCase, String startMsg, String endMsg, String prjPath)
	{
		super(StringUtils.concat("run,", "{testCase=\"", testCase, "\", startMsg=\"", startMsg, "\", endMsg=\"", endMsg, "\", plaftformPath=\"", prjPath, "\"}"));
	}

}