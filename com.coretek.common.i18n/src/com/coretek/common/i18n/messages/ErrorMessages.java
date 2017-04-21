/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.i18n.messages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ������Ϣ�Ĺ��ʻ�
 * 
 * @author ���Ρ 2011-12-9
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
	 * ����µĹ��ʻ��ļ�
	 * 
	 * @param bundleName </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static void addResourceBundle(String bundleName)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		bundles.put(bundleName, bundle);
	}

	/**
	 * ��ӹ��ʻ���Դ�ļ�����ָ������������ԵĹ��ʻ���Դ�ļ�
	 * 
	 * @param bundleName ��Դ������
	 * @param loale ����</br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static void addResourceBundle(String bundleName, Locale locale)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
		bundles.put(bundleName, bundle);
	}

	/**
	 * ɾ�������Ѿ����صĹ��ʻ���Դ </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static void clear()
	{
		bundles.clear();
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param key
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
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
	 * ��ȡ��ʽ�����ַ���
	 * 
	 * @param key
	 * @param params
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static String getFormat(String key, Object[] params)
	{

		return String.format(getString(key), params);
	}
}
