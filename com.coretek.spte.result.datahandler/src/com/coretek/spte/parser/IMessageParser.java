/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

/**
 * ��Ϣ�������������������н�������Ϣ��
 * 
 * @author ���Ρ 2012-3-20
 */
public interface IMessageParser
{

	/**
	 * ������������ȡ��
	 * 
	 * @param istream
	 */
	public void setInDataStream(IStreamReader istream);

	/**
	 * �����������ȡ��
	 * 
	 * @param ostream
	 */
	public void setOutDataStream(IMessageWriter ostream);

	/**
	 * �鿴���������Ƿ��Ѿ����
	 * 
	 * @return ���������ɣ��򷵻�true�����򷵻�false
	 */
	public boolean isFinished();

	/**
	 * ��������������
	 * 
	 * @return
	 */
	public boolean start();
}