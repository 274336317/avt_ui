/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * �㲥����
 * 
 * @author ���Ρ 2011-12-31
 */
public enum BrocastProperty
{

	/**
	 * �㲥
	 */
	BROADCAST("BROADCAST"),
	/**
	 * �鲥
	 */
	GROUP("GROUP"),
	/**
	 * ����
	 */
	POINT("POINT");

	BrocastProperty(String name)
	{
		this.name = name;
	}

	/**
	 * ��ȡ�㲥����
	 * 
	 * @param name �㲥���Ե�ֵ
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-31
	 */
	public static BrocastProperty getIntance(String name)
	{
		if (BROADCAST.name.equals(name))
			return BROADCAST;
		if (GROUP.name.equals(name))
			return GROUP;
		if (POINT.name.equals(name))
			return POINT;

		return null;
	}

	private String	name;

}
