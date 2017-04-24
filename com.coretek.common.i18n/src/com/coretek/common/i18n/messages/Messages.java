/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.i18n.messages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * ��Ϣ�Ĺ��ʻ�
 * 
 * @author ���Ρ 2011-12-9
 */
public final class Messages
{
	private static final String					BUNDLE_NAME		= "com.coretek.common.i18n.messages.Messages";

	private static ResourceBundle				RESOURCE_BUNDLE	= null;

	private static Map<String, ResourceBundle>	BUNDLES			= new HashMap<String, ResourceBundle>();

	public static void init(Locale locale)
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		BUNDLES.put(BUNDLE_NAME, RESOURCE_BUNDLE);
	}

	public static void init()
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		BUNDLES.put(BUNDLE_NAME, RESOURCE_BUNDLE);
	}

	/**
	 * �ı���ʾ������
	 * 
	 * @param locale </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-18
	 */
	public static void changeLocale(Locale locale)
	{
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		Map<String, ResourceBundle> newBundles = new HashMap<String, ResourceBundle>(BUNDLES.size());
		Set<Entry<String, ResourceBundle>> entrySet = BUNDLES.entrySet();
		for (Entry<String, ResourceBundle> entry : entrySet)
		{
			String name = entry.getKey();
			ResourceBundle bundle = ResourceBundle.getBundle(name, locale);
			newBundles.put(name, bundle);
		}
		BUNDLES.clear();
		BUNDLES = newBundles;

	}

	/**
	 * ����µĹ��ʻ��ļ�
	 * 
	 * @param bundleName </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static void addResourceBundle(String bundleName)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		BUNDLES.put(bundleName, bundle);
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
		BUNDLES.put(bundleName, bundle);
	}

	/**
	 * ɾ�������Ѿ����صĹ��ʻ���Դ </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static void clear()
	{
		BUNDLES.clear();
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param key
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-9
	 */
	public static String getString(String key)
	{
		if (RESOURCE_BUNDLE == null)
			init();
		try
		{
			Set<Entry<String, ResourceBundle>> entrySet = BUNDLES.entrySet();
			for (Entry<String, ResourceBundle> entry : entrySet)
			{
				String value = entry.getValue().getString(key);
				if (value != null)
					return value;
			}

		} catch (MissingResourceException e)
		{
			e.printStackTrace();

		}

		return '!' + key + '!';
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