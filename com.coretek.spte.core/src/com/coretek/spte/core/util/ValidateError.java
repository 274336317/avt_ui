/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.util;

/**
 * @author SunDawei 2012-5-12
 */
public enum ValidateError
{
	ICDFILENOTEXISTS("测试用例所依赖的ICD文件不存在"), MD5NOTMATCH("ICD文件内容已经被改变，这可能导致测试用例与ICD文件描述的内容不一致！");

	String	errorMsg;

	ValidateError(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

}