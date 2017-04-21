/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.spte.monitor.command.handler.HandlerRegistries;
import com.coretek.spte.monitor.command.handler.ResultHandler;

/**
 * �������̡߳�����ȡ�߳̽��յ�ִ����������󣬻ᴴ�������һ��������д���
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class ResultHandlerThread extends Thread
{

	private String	result;

	public ResultHandlerThread(String result)
	{
		this.result = result;
		this.setName("����ش����߳�");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run() <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	@Override
	public void run()
	{
		ResultHandler handler = HandlerRegistries.getHandler(result);
		if (handler != null)
		{
			handler.parser(result);
		}
	}

}