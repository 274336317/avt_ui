/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

/**
 * ���÷�����ķ��ض����ڴ˷����п���������Ҫ���ص�ֵ���ߴ�����Ϣ��
 * 
 * @author ���Ρ 2011-12-31
 */
public class MethodResult
{

	private Object	returnedValue;	// ����ֵ

	private boolean	status;		// ��ʶ�����Ƿ�ִ�гɹ�

	private Object	errorMsg;		// ������Ϣ

	/**
	 * ����һ�������������
	 * 
	 * @param returnedValue ����ֵ
	 * @param status �Ƿ�ִ�гɹ�
	 * @param errorMsg ������Ϣ
	 */
	private MethodResult(Object returnedValue, boolean status, Object errorMsg)
	{
		this.returnedValue = returnedValue;
		this.errorMsg = errorMsg;
		this.status = status;
	}

	/**
	 * 
	 * @param returnedValue ����ֵ
	 * @param status �߼������Ƿ�ɹ�
	 * @return
	 */
	public static MethodResult getSuccInstance(Object returnedValue, boolean status)
	{
		return new MethodResult(returnedValue, status, null);
	}

	/**
	 * ����һ������ִ�гɹ��Ľ������
	 * 
	 * @param returnedValue ���ظ������ߵĽ��
	 * @return
	 */
	public static MethodResult getSuccInstance(Object returnedValue)
	{
		return new MethodResult(returnedValue, true, null);
	}

	/**
	 * ����һ������ִ��ʧ�ܻ����Ľ������
	 * 
	 * @param error
	 * @return
	 */
	public static MethodResult getFailedInstance(Object error)
	{
		return new MethodResult(null, false, error);
	}

	/**
	 * ��ȡ��������ֵ
	 * 
	 * @return the returnedValue
	 */
	public Object getReturnedValue()
	{
		return returnedValue;
	}

	/**
	 * �����Ƿ�ִ�гɹ�
	 * 
	 * @return the status
	 */
	public boolean isStatus()
	{
		return status;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return the errorMsg
	 */
	public Object getErrorMsg()
	{
		return errorMsg;
	}
}