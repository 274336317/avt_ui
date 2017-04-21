/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * ��������Ĵ���ʽ
 * 
 * @author ���Ρ
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
	 * ��ȡ��ʾ����ɫ��ע�⣬��Ҫ����{@link Color#dispose()}������
	 * 
	 * @return the color <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-10
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @return the name <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-10
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the num <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-10
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
