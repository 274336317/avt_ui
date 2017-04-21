package com.coretek.spte.parser;

import com.coretek.common.template.ClazzManager;

/**
 * ���ݽ�������������������ݽ������ض��Ķ���
 * 
 * @author ���Ρ 2012-3-20
 */
public interface IDecoder
{

	/**
	 * ����icd�ļ����������Ķ��������
	 * 
	 * @param icdClazzManager
	 */
	public void setIcdManager(ClazzManager icdClazzManager);

	/**
	 * ����case�ļ����������Ķ��������
	 * 
	 * @param caseClazzManager
	 */
	public void setCaseManager(ClazzManager caseClazzManager);

	/**
	 * ��������
	 * 
	 * @param data
	 * @return </br>
	 */
	public Object decode(byte[] data);

}