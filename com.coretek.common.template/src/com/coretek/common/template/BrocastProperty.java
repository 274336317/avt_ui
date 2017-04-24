/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * 广播属性
 * 
 * @author 孙大巍 2011-12-31
 */
public enum BrocastProperty
{

	/**
	 * 广播
	 */
	BROADCAST("BROADCAST"),
	/**
	 * 组播
	 */
	GROUP("GROUP"),
	/**
	 * 单播
	 */
	POINT("POINT");

	BrocastProperty(String name)
	{
		this.name = name;
	}

	/**
	 * 获取广播属性
	 * 
	 * @param name 广播属性的值
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-31
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
