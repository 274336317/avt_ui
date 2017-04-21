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
	ICDFILENOTEXISTS("����������������ICD�ļ�������"), MD5NOTMATCH("ICD�ļ������Ѿ����ı䣬����ܵ��²���������ICD�ļ����������ݲ�һ�£�");

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