package com.coretek.spte.parser;

/**
 * ����ȡ����������ض������ж�ȡ����������
 * 
 * @author ���Ρ 2012-3-20
 */
public interface IStreamReader
{

	public final static byte[]	ENDTAG	= new byte[2 * 1024];

	/**
	 * ��ȡ���ݡ�
	 * 
	 * @param buffer ��Ŵ����ж�ȡ������
	 * @param size ������ݵĻ����ֽڴ�С
	 * @return ����ʵ�ʶ�ȡ��С����������ĩβʱ����-1��
	 */
	int read(byte[] buffer, int size);

	/**
	 * �ж����Ƿ�ɶ���
	 * 
	 * @return ����true��ʾ�ɶ���false��ʾ���ɶ�
	 */
	boolean canRead();

	/**
	 * ֹͣ��ȡ����
	 */
	void shutDown();
}