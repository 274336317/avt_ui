/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.report;

import java.util.HashMap;
import java.util.Map;

/**
 * �����������ݹ�����
 * 
 * @author ZHANG Yi 2012-5-23
 */
public class DataUtils
{

	public static final String	KEY_COMPARERESULTS	= "COMPARERESULTS";
	public static final String	KEY_COMPARERESUTL	= "COMPARESUTL";
	public static final String	KEY_NOW				= "NOW";
	public static final String	KEY_TITLE			= "TITLE";

	/**
	 * �ṩ��������
	 * 
	 * @return
	 */
	public static Map<String, String> getSharedData()
	{
		Map<String, String> data = new HashMap<String, String>();
		data.put("OS", System.getProperty("os.name", " "));
		data.put("OS_ARCH", System.getProperty("os.arch", " "));
		data.put("JDK", System.getProperty("java.runtime.version", " "));
		return data;
	}

}
