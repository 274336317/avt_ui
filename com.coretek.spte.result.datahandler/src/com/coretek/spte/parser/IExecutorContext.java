package com.coretek.spte.parser;

/**
 * ִ��������
 * 
 * @author ���Ρ 2012-3-27
 */
public interface IExecutorContext extends IContext
{

	/**
	 * �鿴���������Ƿ����
	 * 
	 * @return �����������ִ����ϣ��򷵻�true�����򷵻�false
	 */
	public boolean isFinished();

	/**
	 * ��ʼ����
	 * 
	 * @return ��������ɹ��򷵻�true�����򷵻�false
	 */
	public boolean startParsing();

}
