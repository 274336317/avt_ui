/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * 枚举值
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class ICDEnum extends Helper
{

	private static final long	serialVersionUID	= -6577221697542957458L;

	private Integer				value;											// 值

	private String				symbol;										// 标识符

	/**
	 * 构建一个枚举对象
	 * 
	 * @param value 枚举值
	 * @param symbol 枚举标识
	 */
	public ICDEnum(Integer value, String symbol)
	{
		this.value = value;
		this.symbol = symbol;
	}

	/**
	 * 获取枚举值
	 * 
	 * @return the value <br/>
	 */
	public Integer getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set <br/>
	 */
	public void setValue(Integer value)
	{
		this.value = value;
	}

	/**
	 * 获取标识
	 * 
	 * @return the symbol <br/>
	 */
	public String getSymbol()
	{
		return symbol;
	}

	/**
	 * @param symbol the symbol to set <br/>
	 */
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

}
