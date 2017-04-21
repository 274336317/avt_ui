/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.i18n.messages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 错误消息的国际化
 * 
 * @author 孙大巍 2011-12-9
 */
public final class ErrorMessages
{

	private static final String					BUNDLE_NAME		= "com.coretek.common.i18n.messages.ErrorMessages";

	private static ResourceBundle				RESOURCE_BUNDLE	= null;

	private static Map<String, ResourceBundle>	bundles			= new HashMap<String, ResourceBundle>();

	public static void init(Locale locale)
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		bundles.put(BUNDLE_NAME, RESOURCE_BUNDLE);
	}

	public static void init()
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		bundles.put(BUNDLE_NAME, RESOURCE_BUNDLE);
	}

	/**
	 * 添加新的国际化文件
	 * 
	 * @param bundleName </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-9
	 */
	public static void addResourceBundle(String bundleName)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		bundles.put(bundleName, bundle);
	}

	/**
	 * 添加国际化资源文件，并指定添加哪中语言的国际化资源文件
	 * 
	 * @param bundleName 资源的名字
	 * @param loale 地区</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-9
	 */
	public static void addResourceBundle(String bundleName, Locale locale)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
		bundles.put(bundleName, bundle);
	}

	/**
	 * 删除所有已经加载的国际化资源 </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-9
	 */
	public static void clear()
	{
		bundles.clear();
	}

	/**
	 * 获取字符串
	 * 
	 * @param key
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-9
	 */
	public static String getString(String key)
	{

		try
		{
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	/**
	 * 获取格式化的字符串
	 * 
	 * @param key
	 * @param params
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-9
	 */
	public static String getFormat(String key, Object[] params)
	{

		return String.format(getString(key), params);
	}
}
