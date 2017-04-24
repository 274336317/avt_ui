/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

/**
 * 调用方法后的返回对象。在此方法中可以设置需要返回的值或者错误信息。
 * 
 * @author 孙大巍 2011-12-31
 */
public class MethodResult
{

	private Object	returnedValue;	// 返回值

	private boolean	status;		// 标识方法是否执行成功

	private Object	errorMsg;		// 错误信息

	/**
	 * 构建一个方法结果对象
	 * 
	 * @param returnedValue 返回值
	 * @param status 是否执行成功
	 * @param errorMsg 错误消息
	 */
	private MethodResult(Object returnedValue, boolean status, Object errorMsg)
	{
		this.returnedValue = returnedValue;
		this.errorMsg = errorMsg;
		this.status = status;
	}

	/**
	 * 
	 * @param returnedValue 返回值
	 * @param status 逻辑处理是否成功
	 * @return
	 */
	public static MethodResult getSuccInstance(Object returnedValue, boolean status)
	{
		return new MethodResult(returnedValue, status, null);
	}

	/**
	 * 返回一个方法执行成功的结果对象
	 * 
	 * @param returnedValue 返回给调用者的结果
	 * @return
	 */
	public static MethodResult getSuccInstance(Object returnedValue)
	{
		return new MethodResult(returnedValue, true, null);
	}

	/**
	 * 返回一个方法执行失败或出错的结果对象
	 * 
	 * @param error
	 * @return
	 */
	public static MethodResult getFailedInstance(Object error)
	{
		return new MethodResult(null, false, error);
	}

	/**
	 * 获取方法返回值
	 * 
	 * @return the returnedValue
	 */
	public Object getReturnedValue()
	{
		return returnedValue;
	}

	/**
	 * 方法是否执行成功
	 * 
	 * @return the status
	 */
	public boolean isStatus()
	{
		return status;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return the errorMsg
	 */
	public Object getErrorMsg()
	{
		return errorMsg;
	}
}