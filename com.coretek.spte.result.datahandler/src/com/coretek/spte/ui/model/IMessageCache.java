package com.coretek.spte.ui.model;

import com.coretek.common.template.SPTEMsg;

/**
 * ��Ϣ���棬���ڻ������֮�����Ϣ���ߴ����ݿ��м��ص���Ϣ
 * 
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public interface IMessageCache
{
	/**
	 * ��ѯ���ݿ��б������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @return
	 */
	public SPTEMsg[] getMessage(long startTime, long length);

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
