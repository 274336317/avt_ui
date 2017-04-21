/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.RGB;

/**
 * 颜色工厂
 * 
 * @author 尹军 2012-3-14
 */
public class ColorFactory
{
	/**
	 * 当前颜色数值
	 */
	private static int	current	= 0;

	public static RGB getColor()
	{
		switch ((current++) % 10)
		{

			// 蓝色
			case 0:
			{
				return ColorConstants.blue.getRGB();
			}

				// 洋红色
			case 1:
			{
				return ColorConstants.cyan.getRGB();
			}

				// 灰色
			case 2:
			{
				return ColorConstants.gray.getRGB();
			}

				// 绿色
			case 3:
			{
				return ColorConstants.green.getRGB();
			}

				// 桔黄色
			case 4:
			{
				return ColorConstants.orange.getRGB();
			}

				// 红色
			case 5:
			{
				return ColorConstants.red.getRGB();
			}

				// 亮灰色
			case 6:
			{
				return ColorConstants.lightGray.getRGB();
			}

				// 黄色
			case 7:
			{
				return ColorConstants.yellow.getRGB();
			}

				// 亮蓝色
			case 8:
			{
				return ColorConstants.lightBlue.getRGB();
			}

				// 白色
			case 9:
			{
				return ColorConstants.white.getRGB();
			}
		}

		return null;
	}
}