/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

/**
 * ִ�������ؽ��������
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public interface IResultHandler
{

	/**
	 * ����ִ����������Ϣ
	 * 
	 * @param result ִ����������Ϣ
	 * @return
	 */
	public Object parser(String result);

	/**
	 * �����жϴ˽���������Ƿ��ܹ������ض��ķ��ؽ��
	 * 
	 * @param result ִ�������ؽ��
	 * @return
	 */
	public boolean isMyType(String result);

}
