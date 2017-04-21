/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import com.coretek.common.utils.StringUtils;

/**
 * ���û�������(setEnv)����
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class SetEnvCommand extends AbstractCommand
{

	/**
	 * �������û�����������
	 * 
	 * @param endian ��С��:big��little
	 * @param port �˿ں�</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	public SetEnvCommand(String endian, String port)
	{
		super(StringUtils.concat("setEnv,{endian=\"", endian, "\",port=\"", port, "\"}"));
	}

}