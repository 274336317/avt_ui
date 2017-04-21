package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * ��ʷ��¼���������ģ����ڴ����ݿ��м�����Ϣ���ڴ沢�����ʵ��Ļ���
 * 
 * @author SunDawei
 * 
 * @date 2012-10-16
 */
public interface IReviewContext extends IContext
{

	/**
	 * ��ȡ��Ϣ������
	 * 
	 * @return
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs();

	/**
	 * ��ȡ���������ݿ��е�ICD�ļ�·��
	 * 
	 * @return
	 */
	public String getIcdPath();

}
