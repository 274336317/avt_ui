/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

/**
 * ��������
 * 
 * @author ���Ρ
 * @date 2012-2-10
 */
public enum ErrorTypesEnum
{

	/**
	 * ��Ԥ����Ϣ
	 */
	UNEXPECTED("unexpected"),
	/**
	 * ��Ϣֵ����
	 */
	WRONGVALUE("wrongValue"),
	/**
	 * ��Ϣ��ʱ
	 */
	TIMEOUT("timeout"),
	/**
	 * ��Ϣ��ʧ
	 */
	LOST("lost");

	private String	name;

	ErrorTypesEnum(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-10
	 */
	public String getName()
	{
		return name;
	}

}