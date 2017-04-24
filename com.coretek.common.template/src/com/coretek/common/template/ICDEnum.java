/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * ö��ֵ
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class ICDEnum extends Helper
{

	private static final long	serialVersionUID	= -6577221697542957458L;

	private Integer				value;											// ֵ

	private String				symbol;										// ��ʶ��

	/**
	 * ����һ��ö�ٶ���
	 * 
	 * @param value ö��ֵ
	 * @param symbol ö�ٱ�ʶ
	 */
	public ICDEnum(Integer value, String symbol)
	{
		this.value = value;
		this.symbol = symbol;
	}

	/**
	 * ��ȡö��ֵ
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
	 * ��ȡ��ʶ
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
