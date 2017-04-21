/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

/**
 * 错误类型
 * 
 * @author 孙大巍
 * @date 2012-2-10
 */
public enum ErrorTypesEnum
{

	/**
	 * 非预期消息
	 */
	UNEXPECTED("unexpected"),
	/**
	 * 消息值错误
	 */
	WRONGVALUE("wrongValue"),
	/**
	 * 消息超时
	 */
	TIMEOUT("timeout"),
	/**
	 * 消息丢失
	 */
	LOST("lost");

	private String	name;

	ErrorTypesEnum(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-10
	 */
	public String getName()
	{
		return name;
	}

}