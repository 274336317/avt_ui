package com.coretek.spte.monitor.manager;

import java.util.concurrent.TimeoutException;

import com.coretek.common.template.SPTEMsg;

/**
 * �����Ĺ������ӿ�
 * 
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public interface IContextManager
{

	public boolean shutDown() throws TimeoutException;

	/**
	 * ��ѯʱ�䷶Χ�ڵ���Ϣ
	 * 
	 * @param time ��ѯ���
	 * @param length ʱ�䳤��
	 * @param range ��Ϣ�������
	 * @return </br>
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range);

	/**
	 * ��ȡһ��ʱ���ڵ���Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param length ʱ�䳤��
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length);

	/**
	 * ��ʼ��
	 * 
	 * @param icdPath
	 * @param msgDBPath</br>
	 */
	public void initParser(String icdPath, String msgDBPath);

	/**
	 * ��ջ��� </br>
	 */
	public void clearCache();

}
