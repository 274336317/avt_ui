package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * ��Ϣ�����
 * 
 * @author ���Ρ 2012-3-20
 */
public interface IMessageWriter
{

	/**
	 * �����Ϣ
	 * 
	 * @param msg
	 * @return ����ɹ��򷵻�true�����򷵻�false</br>
	 */
	boolean write(SPTEMsg msg);
}
