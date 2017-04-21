/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.RGB;

/**
 * ��ɫ����
 * 
 * @author ���� 2012-3-14
 */
public class ColorFactory
{
	/**
	 * ��ǰ��ɫ��ֵ
	 */
	private static int	current	= 0;

	public static RGB getColor()
	{
		switch ((current++) % 10)
		{

			// ��ɫ
			case 0:
			{
				return ColorConstants.blue.getRGB();
			}

				// ���ɫ
			case 1:
			{
				return ColorConstants.cyan.getRGB();
			}

				// ��ɫ
			case 2:
			{
				return ColorConstants.gray.getRGB();
			}

				// ��ɫ
			case 3:
			{
				return ColorConstants.green.getRGB();
			}

				// �ۻ�ɫ
			case 4:
			{
				return ColorConstants.orange.getRGB();
			}

				// ��ɫ
			case 5:
			{
				return ColorConstants.red.getRGB();
			}

				// ����ɫ
			case 6:
			{
				return ColorConstants.lightGray.getRGB();
			}

				// ��ɫ
			case 7:
			{
				return ColorConstants.yellow.getRGB();
			}

				// ����ɫ
			case 8:
			{
				return ColorConstants.lightBlue.getRGB();
			}

				// ��ɫ
			case 9:
			{
				return ColorConstants.white.getRGB();
			}
		}

		return null;
	}
}