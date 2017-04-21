/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

/**
 * ������ִ����ͨ�ŵķ�����
 * 
 * @author ���Ρ 2012-3-20
 */
public interface IServer extends IStreamReader
{

	/**
	 * ��ʼ���з�����
	 * 
	 * @param port �˿ں�
	 * @param cacheSize �����С
	 * @return ����ʧ�ܷ���false���ɹ��򷵻�true
	 */
	public boolean start(int port, int cacheSize);

	/**
	 * �رշ�����
	 */
	public void shutDown();

}