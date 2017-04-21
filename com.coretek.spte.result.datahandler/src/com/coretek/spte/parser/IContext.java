/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * ��������ִ��������
 * 
 * @author ���Ρ 2012-3-23
 */
public interface IContext
{
	/**
	 * ��ʼ������
	 * 
	 * @param icdPath icd�ļ��ľ���·��
	 * @param msgDBPath ��Ϣ���ݿ�
	 * @param endian
	 */
	public void init(String icdPath, String msgDBPath);

	/**
	 * �رս�������
	 */
	public void shutDown();

	/**
	 * ��ѯ���ݿ��б������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @return
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length);

	/**
	 * ��ѯ��Ϣ
	 * 
	 * @param time ���ʱ��
	 * @param length ����
	 * @param range ���������
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range);

	/**
	 * ��ջ���
	 */
	public void clearCache();

}
