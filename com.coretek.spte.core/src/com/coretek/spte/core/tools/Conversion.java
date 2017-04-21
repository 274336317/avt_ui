package com.coretek.spte.core.tools;

/**
 * 单位换算
 * 目前只实现了常用长度单位，时间单位，温度单位的换算，
 * 其他换算可根据单位换算关系再添加。
 * 
 * Designed And Developed By zjMa
 * 
 */

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;

public class Conversion
{

	/**
	 * 
	 * @param index 单位类型ID
	 * @param cb icd默认该类型单位的ID
	 * @param cb0 需要转化的单位ID
	 * @param cb1 将要转化成的单位ID
	 * @param value 转化前的值
	 * @return </br> 转化后的值 <b>作者</b> duyisen </br> <b>日期</b> 2011-7-11
	 */
	public String indexConversion(int index, int cb0, int cb1, String value)
	{
		if (cb0 != cb1)
		{
			value = indexNewToDef(index, cb0, value);
			value = indexDefTONew(index, cb1, value);
		}

		return value;

	}

	/**
	 * 将单位转化为默认单位
	 * 
	 * @param index 单位类型ID
	 * @param cb0 需要转化的单位id
	 * @param value 需要转换的值
	 * 
	 *            修改：为了添加对Dms和hms的处理，改变函数的参数和返回值类型
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2011-7-12
	 */
	public String indexNewToDef(int index, int cb0, String valueStr)
	{
		double value = 0;
		int D = 0;
		int h = 0;
		int m = 0;
		int s = 0;
		if (StringUtils.isDouble(valueStr))
		{
			value = Double.parseDouble(valueStr);
		} else if (index == 3)
		{// 角度
			if (valueStr.contains("D") && valueStr.contains("m") && valueStr.contains("s"))
			{
				if (StringUtils.isNumber(valueStr.substring(0, valueStr.indexOf("D"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("D") + 1, valueStr.indexOf("m"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s"))))
				{
					D = Integer.parseInt(valueStr.substring(0, valueStr.indexOf("D")));
					m = Integer.parseInt(valueStr.substring(valueStr.indexOf("D") + 1, valueStr.indexOf("m")));
					s = Integer.parseInt(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s")));
				}
			}
		} else if (index == 6)
		{// 时间
			if (valueStr.contains("h") && valueStr.contains("m") && valueStr.contains("s"))
			{
				if (StringUtils.isNumber(valueStr.substring(0, valueStr.indexOf("h"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("h") + 1, valueStr.indexOf("m"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s"))))
				{
					h = Integer.parseInt(valueStr.substring(0, valueStr.indexOf("h")));
					m = Integer.parseInt(valueStr.substring(valueStr.indexOf("h") + 1, valueStr.indexOf("m")));
					s = Integer.parseInt(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s")));
				}
			}
		}
		int def = 0;
		switch (index)
		{
			case 1:// 距离
				// 默认单位暂时定为米(m)
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.3048);
				} // 英尺
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1852);
				} // 海里
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.0254);
				} // 英寸
				else if (cb0 == 4)
				{
					return String.valueOf(value * 1000);
				} // 千米
				else if (cb0 == 5)
				{
					return String.valueOf(value);
				} // 未知
				else if (cb0 == 6)
				{
					return String.valueOf(value / (double) 1000);
				} // 毫米
			case 2:// 速度
				// 默认单位暂时定为m/s
				def = 2;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.3048);
				} // 英寸每秒
				else if (cb0 == 1)
				{
					return String.valueOf(value * 1852 / (double) 3600);
				} // 海里每小时
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.3048 / (double) 60);
				} // 英寸每分
				else if (cb0 == 4)
				{
					return String.valueOf(value / 3.6);
				} // 千米每小时
				else if (cb0 == 5)
				{
					return String.valueOf(value / (double) 1000);
				} // 毫米每秒
				else if (cb0 == 6)
				{
					return String.valueOf(value * 340);
				} // 1马赫=1音速
				else if (cb0 == 7)
				{
					return String.valueOf(value / (double) 60000);
				} // mm/min
			case 3:// 角度
				// 角度默认单位为s_c，id为4
				def = 4;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value / Utils.π);
				} // rad弧度， 1s_c = 180° = π rad
				else if (cb0 == 1)
				{
					return String.valueOf(value / (double) 180);
				} // 度
				else if (cb0 == 2)
				{
					return String.valueOf((D + m / 60.0 + s / 3600.0) / (double) 180);
				} // 度分秒
				else if (cb0 == 3)
				{
					return String.valueOf(value / (1000 * Utils.π));
				} // 毫弧度

			case 4:// 温度
				// 默认单位为℃ id为1
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} // Ran
				else if (cb0 == 0)
				{
					return String.valueOf(value / 1.8 + 273.15);
				} // Cel
				else if (cb0 == 2)
				{
					return String.valueOf((value - 32) / 1.8);
				} // Far
				else if (cb0 == 3)
				{
					return String.valueOf(value - 273.15);
				} // Kel

			case 5:// 压力
				// 默认单位为kPa id为9
				def = 9;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 101.325);
				} // 标准大气压
				else if (cb0 == 1)
				{
					return String.valueOf(value * 3.3863882);
				} // 一英寸Hg
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1.3332237);
				} // 一厘米Hg
				else if (cb0 == 3)
				{
					return String.valueOf(value * 6.895);
				} // Psi
				else if (cb0 == 4)
				{
					return String.valueOf(value * 0.1);
				} // mbar 毫巴
				else if (cb0 == 5)
				{
					return String.valueOf(value * 0.00980665);
				} // kg/㎡ 有问题
				else if (cb0 == 6)
				{
					return String.valueOf(value * 98.0665);
				} // kg/c㎡ 有问题
				else if (cb0 == 7)
				{
					return String.valueOf(value * 0.1);
				} // hPa
				else if (cb0 == 8)
				{
					return String.valueOf(value * 98.0665);
				} // kgf/c㎡
				else if (cb0 == 10)
				{
					return String.valueOf(value * 1000);
				} // Mpa
			case 6:// 时间
				// 默认单位为s id为0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value * 0.05);
				} // Cyc =50毫秒
				else if (cb0 == 2)
				{
					return String.valueOf(h * 3600 + m * 60 + s);
				} // Hms
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.001);
				} // M_Sc毫秒
				else if (cb0 == 4)
				{
					return String.valueOf(value * 0.000001);
				} // Mic微秒
				else if (cb0 == 5)
				{
					return String.valueOf(value * 60 * 60);
				} // h
				else if (cb0 == 6)
				{
					return String.valueOf(value * 60);
				} // min
				else if (cb0 == 7)
				{
					return String.valueOf(value * 0.000000001);
				} // ns
			case 7:// 重量
				// 默认单位是kg id为1
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.4536);
				} // lbr 镑
			case 8:// 角速度
				// 默认单位是D/s id为0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value * 180);
				} // S/s
				else if (cb0 == 2)
				{
					return String.valueOf(value * 180 / Utils.π);
				} // R/s
				else if (cb0 == 3)
				{
					return String.valueOf(value * 180 / (1000 * Utils.π));
				} // mrad/s
				else if (cb0 == 4)
				{
					return String.valueOf(value / (double) 3600);
				} // Sec/s
			case 9:// 加速度
				// 默认单位是m/s2 id为1
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.3048);
				} // ft/s2
				else if (cb0 == 2)
				{
					return String.valueOf(value * 9.80665);
				} // g
			case 10:// 频率
				// 默认单位Hz id为0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value * 1000);
				} // KHz
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1000000);
				} // MHz
			case 11:// 容器
				// 默认单位L id为0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value / (double) 1000);
				} // ml
			case 51:// 字节
				// 默认单位kb id为1
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value / (double) 1024);
				} // b
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1024);
				} // Mb
			default:
				break;

		}

		return valueStr;

	}

	/**
	 * 将默认单位转换为其他单位
	 * 
	 * @param index 单位类型ID
	 * @param def 默认单位
	 * @param cb1 需要转换成为的单位
	 * @param valueStr 需要转化的值，该值对应默认单位
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2011-7-11
	 */
	public String indexDefTONew(int index, int cb1, String valueStr)
	{
		double value = 0;
		// int D = 0;
		int h = 0;
		int m = 0;
		int s = 0;
		if (StringUtils.isDouble(valueStr))
		{
			value = Double.parseDouble(valueStr);
		}
		if (index == 6 && cb1 == 2)
		{
			h = (int) (value / 3600);
			m = (int) ((value % 3600) / 60);
			s = (int) (value % 60);

		}
		// else if(index == 3){//角度
		// if(valueStr.contains("D")&&valueStr.contains("m")&&valueStr.contains("s")){
		// if (StringUtils.isNumeric(valueStr.substring(0,
		// valueStr.indexOf("D")))
		// && StringUtils.isNumeric(valueStr.substring(valueStr.indexOf("h") +
		// 1, valueStr.indexOf("m")))
		// && StringUtils.isNumeric(valueStr.substring(valueStr.indexOf("m")+1,
		// valueStr.indexOf("s")))) {
		// D = Integer.parseInt(valueStr.substring(0, valueStr.indexOf("D")));
		// m = Integer.parseInt(valueStr.substring(valueStr.indexOf("h")+1,
		// valueStr.indexOf("m")));
		// s = Integer.parseInt(valueStr.substring(valueStr.indexOf("m")+1,
		// valueStr.indexOf("s")));
		// }
		// }
		// } else if(index == 6){//时间
		// if(valueStr.contains("h")&&valueStr.contains("m")&&valueStr.contains("s")){
		// if (StringUtils.isNumeric(valueStr.substring(0,
		// valueStr.indexOf("h")))
		// && StringUtils.isNumeric(valueStr.substring(valueStr.indexOf("h") +
		// 1, valueStr.indexOf("m")))
		// && StringUtils.isNumeric(valueStr.substring(valueStr.indexOf("m")+1,
		// valueStr.indexOf("s")))) {
		// h = Integer.parseInt(valueStr.substring(0, valueStr.indexOf("h")));
		// m = Integer.parseInt(valueStr.substring(valueStr.indexOf("h")+1,
		// valueStr.indexOf("m")));
		// s = Integer.parseInt(valueStr.substring(valueStr.indexOf("m")+1,
		// valueStr.indexOf("s")));
		// }
		// }
		// }
		int def = 0;
		switch (index)
		{
			case 1:// 距离
				// 默认单位暂时定为米(m)
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.3048);
				} // 英尺
				else if (cb1 == 2)
				{
					return String.valueOf(value / (double) 1852);
				} // 海里
				else if (cb1 == 3)
				{
					return String.valueOf(value / 0.0254);
				} // 英寸
				else if (cb1 == 4)
				{
					return String.valueOf(value / (double) 1000);
				} // 千米
				else if (cb1 == 5)
				{
					return String.valueOf(value);
				} // 未知
				else if (cb1 == 6)
				{
					return String.valueOf(value * 1000);
				} // 毫米
			case 2:// 速度
				// 默认单位暂时定为m/s
				def = 2;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.3048);
				} // 英寸每秒
				else if (cb1 == 1)
				{
					return String.valueOf(value / (1852 / (double) 3600));
				} // 海里每小时
				else if (cb1 == 3)
				{
					return String.valueOf(value / (0.3048 / (double) 60));
				} // 英寸每分
				else if (cb1 == 4)
				{
					return String.valueOf(value * 3.6);
				} // 千米每小时
				else if (cb1 == 5)
				{
					return String.valueOf(value * 1000);
				} // 毫米每秒
				else if (cb1 == 6)
				{
					return String.valueOf(value / (double) 340);
				} // 1马赫=1音速
				else if (cb1 == 7)
				{
					return String.valueOf(value * 60000);
				}
			case 3:// 角度
				// 角度默认单位为s_c，id为4
				def = 4;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value * Utils.π);
				} // rad弧度， 1s_c = 180° = π rad
				else if (cb1 == 1)
				{
					return String.valueOf(value * 180);
				} // 度
				else if (cb1 == 2)
				{
					return String.valueOf((int) value * 180) + "D0m0s";
				} // 度分秒
				else if (cb1 == 3)
				{
					return String.valueOf(value * (1000 * Utils.π));
				} // 毫弧度

			case 4:// 温度
				// 默认单位为℃ id为1
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} // Ran
				else if (cb1 == 0)
				{
					return String.valueOf((value - 273.15) * 1.8);
				} // Cel
				else if (cb1 == 2)
				{
					return String.valueOf(value * 1.8 + 32);
				} // Far
				else if (cb1 == 3)
				{
					return String.valueOf(value + 273.15);
				} // Kel
			case 5:// 压力
				// 默认单位为kPa id为9
				def = 9;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 101.325);
				} // 标准大气压
				else if (cb1 == 1)
				{
					return String.valueOf(value / 3.3863882);
				} // 一英寸Hg
				else if (cb1 == 2)
				{
					return String.valueOf(value / 1.3332237);
				} // 一厘米Hg
				else if (cb1 == 3)
				{
					return String.valueOf(value / 6.895);
				} // Psi
				else if (cb1 == 4)
				{
					return String.valueOf(value / 0.1);
				} // mbar 毫巴
				else if (cb1 == 5)
				{
					return String.valueOf(value / 0.00980665);
				} // kg/㎡ 有问题
				else if (cb1 == 6)
				{
					return String.valueOf(value / 98.0665);
				} // kg/c㎡ 有问题
				else if (cb1 == 7)
				{
					return String.valueOf(value / 0.1);
				} // hPa
				else if (cb1 == 8)
				{
					return String.valueOf(value / 98.0665);
				} // kgf/c㎡
				else if (cb1 == 10)
				{
					return String.valueOf(value / (double) 1000);
				} // Mpa
			case 6:// 时间
				// 默认单位为s id为0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value * 20);
				} // Cyc =50毫秒
				else if (cb1 == 2)
				{
					return String.valueOf(h) + "h" + String.valueOf(m) + "m" + String.valueOf(s) + "s";
				} // Hms
				else if (cb1 == 3)
				{
					return String.valueOf(value * 1000);
				} // M_Sc = 毫秒
				else if (cb1 == 4)
				{
					return String.valueOf(value * 1000000);
				} // Mic = 微秒
				else if (cb1 == 5)
				{
					return String.valueOf(value / (double) 3600);
				} // h
				else if (cb1 == 6)
				{
					return String.valueOf(value / (double) 60);
				} // min
				else if (cb1 == 7)
				{
					return String.valueOf(value * 1000000000);
				} // ns
			case 7:// 重量
				// 默认单位是kg id为1
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.4536);
				} // lbr 镑
			case 8:// 角速度
				// 默认单位是D/s id为0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value / (double) 180);
				} // S/s
				else if (cb1 == 2)
				{
					return String.valueOf(value * Utils.π / (double) 180);
				} // R/s
				else if (cb1 == 3)
				{
					return String.valueOf((value * (1000 * Utils.π) / (double) 180));
				}// mrad/s
				else if (cb1 == 4)
				{
					return String.valueOf(value * 3600);
				} // Sec/s
			case 9:// 加速度
				// 默认单位是m/s2 id为1
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.3048);
				} // ft/s2
				else if (cb1 == 2)
				{
					return String.valueOf(value / 9.80665);
				} // g
			case 10:// 频率
				// 默认单位Hz id为0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value * 0.001);
				} // KHz
				else if (cb1 == 2)
				{
					return String.valueOf(value / (double) 1000000);
				} // MHz
			case 11:// 容器
				// 默认单位L id为0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value * 1000);
				} // ml
			case 51:// 字节
				// 默认单位kb id为1
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value * 1024);
				} // b
				else if (cb1 == 2)
				{
					return String.valueOf(value / (double) 1024);
				} // Mb
			default:
				break;

		}
		return valueStr;
	}

}
