/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.i18n;

import java.util.Locale;

/**
 * @author ���Ρ
 * @date 2012-2-20
 */
public enum Language
{

	English("English", "English", Locale.ENGLISH), Chinese("Chinese", "����", Locale.CHINESE);

	private String	displayName;

	private String	name;

	private Locale	locale;

	Language(String name, String displayName, Locale locale)
	{
		this.displayName = displayName;
		this.name = name;
		this.locale = locale;
	}

	/**
	 * @return the name <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-20
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the locale <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-20
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * @return the displayName <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-20
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString() <br/> <b>����</b> ���Ρ </br> <b>����</b>
	 * 2012-2-20
	 */
	@Override
	public String toString()
	{

		return this.name;
	}

}