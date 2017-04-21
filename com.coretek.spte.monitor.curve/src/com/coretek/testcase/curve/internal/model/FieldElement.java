/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import java.io.Serializable;

/**
 * 信号元素
 * 
 * @author 尹军 2012-3-14
 */
public final class FieldElement implements Serializable, Cloneable
{
	private static final long	serialVersionUID	= 204744528523637592L;

	// 信号时间戳
	private long				time;

	// 信号值
	private int					value;

	public FieldElement(int value, long time)
	{
		this.value = value;
		this.time = time;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{

		return (FieldElement) super.clone();
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean flag = false;
		if (obj == this)
		{
			flag = true;
		} else if (obj instanceof FieldElement)
		{
			FieldElement element = (FieldElement) obj;
			if (element.value == this.value)
			{
				if (element.time == this.time)
				{
					flag = true;
				}
			}
		}

		return flag;
	}

	/**
	 * 获得信号时间戳
	 * 
	 * @return 信号时间戳 </br>
	 */
	public long getTime()
	{
		return time;
	}

	/**
	 * 获得信号值
	 * 
	 * @return 信号值 </br>
	 */
	public int getValue()
	{
		return value;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.value;
		result = result * 31 + (int) (this.time >> 32);

		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<element value=\"").append(this.value).append("\"");
		sb.append(" time=\"").append(this.time).append("\"/>");
		sb.append("\n");
		return sb.toString();
	}
}