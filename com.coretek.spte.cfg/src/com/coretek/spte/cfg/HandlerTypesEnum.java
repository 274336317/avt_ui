/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * 各种情况的处理方式
 * 
 * @author 孙大巍
 * @date 2012-2-10
 */
public enum HandlerTypesEnum
{

	ERROR(ColorConstants.red, "error", 1), WARNING(ColorConstants.yellow, "warning", 2), PROMPT(ColorConstants.lightGreen, "PROMPT", 3), IGNORE(null, "ignore", 4);

	private Color	color;

	private String	name;

	private int		num;

	HandlerTypesEnum(Color color, String name, int num)
	{
		this.color = color;
		this.name = name;
		this.num = num;

	}

	/**
	 * 获取显示的颜色。注意，不要调用{@link Color#dispose()}方法。
	 * 
	 * @return the color <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-10
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @return the name <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-10
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the num <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-10
	 */
	public int getNum()
	{
		return num;
	}

	public static HandlerTypesEnum valueOf(int num)
	{
		switch (num)
		{
			case 1:
				return ERROR;
			case 2:
				return WARNING;
			case 3:
				return PROMPT;
			case 4:
				return IGNORE;
			default:
				return ERROR;
		}
	}

}
