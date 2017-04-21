package com.coretek.testcase.unit;

import com.coretek.common.template.ICDUnit;
import com.coretek.common.template.ICDUnitType;
import com.coretek.common.utils.StringUtils;

/**
 * �ṩһЩ����ʹ�õ�λ�ķ���,����Ҫע�������Щ�����͵�λ�ļ���һ����ϵ��
 * 
 * @author duyisen 2012-01-13
 * 
 */
public class UnitUtils
{

	public static final double	��	= 3.1415926;

	/**
	 * 
	 * @param unitType ��λ����
	 * @param fromUnit ��Ҫת���ĵ�λ
	 * @param toUnit ��Ҫת���ɵĵ�λ
	 * @param value ת��ǰ��ֵ
	 * @return ת�����ֵ <b>����</b> duyisen </br> <b>����</b> 2012-1-14
	 */
	public static String conversion(ICDUnitType unitType, ICDUnit fromUnit, ICDUnit toUnit, String value)
	{
		if (fromUnit.getID() != toUnit.getID())
		{
			value = converToDef(unitType, fromUnit, value);
			value = converToNew(unitType, toUnit, value);
		}
		return value;
	}

	/**
	 * ����λת��ΪĬ�ϵ�λ
	 * 
	 * @param unitType ��λ����
	 * @param fromUnit ��Ҫת���ĵ�λ
	 * @param valueStr ��Ҫת����ֵ
	 * 
	 *            �޸ģ�Ϊ����Ӷ�Dms��hms�Ĵ����ı亯���Ĳ����ͷ���ֵ����
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-1-14
	 */
	private static String converToDef(ICDUnitType unitType, ICDUnit fromUnit, String valueStr)
	{
		double value = 0;
		int D = 0;
		int h = 0;
		int m = 0;
		int s = 0;
		if (StringUtils.isDouble(valueStr))
		{
			value = Double.parseDouble(valueStr);
		} else if (unitType.getID() == 3)
		{// �Ƕ�
			if (valueStr.contains("D") && valueStr.contains("m") && valueStr.contains("s"))
			{
				if (StringUtils.isNumber(valueStr.substring(0, valueStr.indexOf("D"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("D") + 1, valueStr.indexOf("m"))) && StringUtils.isNumber(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s"))))
				{
					D = Integer.parseInt(valueStr.substring(0, valueStr.indexOf("D")));
					m = Integer.parseInt(valueStr.substring(valueStr.indexOf("D") + 1, valueStr.indexOf("m")));
					s = Integer.parseInt(valueStr.substring(valueStr.indexOf("m") + 1, valueStr.indexOf("s")));
				}
			}
		} else if (unitType.getID() == 6)
		{// ʱ��
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
		switch (unitType.getID())
		{
			case 1:// ����
				// Ĭ�ϵ�λ��ʱ��Ϊ��(m)
				def = 102;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 101)
				{
					return String.valueOf(value * 0.3048);
				} // Ӣ��
				else if (fromUnit.getID() == 103)
				{
					return String.valueOf(value * 1852);
				} // ����
				else if (fromUnit.getID() == 104)
				{
					return String.valueOf(value * 0.0254);
				} // Ӣ��
				else if (fromUnit.getID() == 105)
				{
					return String.valueOf(value * 1000);
				} // ǧ��
				else if (fromUnit.getID() == 107)
				{
					return String.valueOf(value / (double) 1000);
				} // ����
			case 2:// �ٶ�
				// Ĭ�ϵ�λ��ʱ��Ϊm/s
				def = 203;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 201)
				{
					return String.valueOf(value * 0.3048);
				} // Ӣ��ÿ��
				else if (fromUnit.getID() == 202)
				{
					return String.valueOf(value * 1852 / (double) 3600);
				} // ����ÿСʱ���ڣ�
				else if (fromUnit.getID() == 204)
				{
					return String.valueOf(value * 0.3048 / (double) 60);
				} // Ӣ��ÿ��
				else if (fromUnit.getID() == 205)
				{
					return String.valueOf(value / 3.6);
				} // ǧ��ÿСʱ
				else if (fromUnit.getID() == 206)
				{
					return String.valueOf(value / (double) 1000);
				} // ����ÿ��
				else if (fromUnit.getID() == 207)
				{
					return String.valueOf(value * 340);
				} // 1���=1����

			case 3:// �Ƕ�
				// �Ƕ�Ĭ�ϵ�λΪs_c
				def = 305;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 301)
				{
					return String.valueOf(value / ��);
				} // rad���ȣ� 1s_c = 180�� = �� rad
				else if (fromUnit.getID() == 302)
				{
					return String.valueOf(value / (double) 180);
				} // ��
				else if (fromUnit.getID() == 303)
				{
					return String.valueOf((D + m / 60.0 + s / 3600.0) / (double) 180);
				} // �ȷ���
				else if (fromUnit.getID() == 304)
				{
					return String.valueOf(value / (1000 * ��));
				} // ������

			case 4:// �¶�
				// Ĭ�ϵ�λΪ�� idΪ1
				def = 402;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} // Cel
				else if (fromUnit.getID() == 401)
				{
					return String.valueOf(value / 1.8 + 273.15);
				} // Ran
				else if (fromUnit.getID() == 403)
				{
					return String.valueOf((value - 32) / 1.8);
				} // Far
				else if (fromUnit.getID() == 404)
				{
					return String.valueOf(value - 273.15);
				} // Kel

			case 5:// ѹ��
				// Ĭ�ϵ�λΪkPa idΪ9
				def = 510;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 501)
				{
					return String.valueOf(value * 101.325);
				} // ��׼����ѹ
				else if (fromUnit.getID() == 502)
				{
					return String.valueOf(value * 3.3863882);
				} // һӢ��Hg
				else if (fromUnit.getID() == 503)
				{
					return String.valueOf(value * 1.3332237);
				} // һ����Hg
				else if (fromUnit.getID() == 504)
				{
					return String.valueOf(value * 6.895);
				} // Psi
				else if (fromUnit.getID() == 505)
				{
					return String.valueOf(value * 0.1);
				} // mbar ����
				else if (fromUnit.getID() == 506)
				{
					return String.valueOf(value * 0.00980665);
				} // kg/�O ������
				else if (fromUnit.getID() == 507)
				{
					return String.valueOf(value * 98.0665);
				} // kg/c�O ������
				else if (fromUnit.getID() == 508)
				{
					return String.valueOf(value * 0.1);
				} // hPa
				else if (fromUnit.getID() == 509)
				{
					return String.valueOf(value * 98.0665);
				} // kgf/c�O
				else if (fromUnit.getID() == 511)
				{
					return String.valueOf(value * 1000);
				} // Mpa

			case 6:// ʱ��
				// Ĭ�ϵ�λΪs
				def = 601;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 602)
				{
					return String.valueOf(value * 0.05);
				} // Cyc =50����
				else if (fromUnit.getID() == 603)
				{
					return String.valueOf(h * 3600 + m * 60 + s);
				} // Hms
				else if (fromUnit.getID() == 604)
				{
					return String.valueOf(value * 0.001);
				} // M_Sc����
				else if (fromUnit.getID() == 605)
				{
					return String.valueOf(value * 0.000001);
				} // Mic΢��
				else if (fromUnit.getID() == 606)
				{
					return String.valueOf(value * 60 * 60);
				} // h
				else if (fromUnit.getID() == 607)
				{
					return String.valueOf(value * 60);
				} // min
				// else if(fromUnit.getID() == 7) {return
				// String.valueOf(value*0.000000001);} //ns

			case 7:// ����
				// Ĭ�ϵ�λ��kg
				def = 701;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 702)
				{
					return String.valueOf(value * 0.4536);
				} // lbr ��

			case 8:// ���ٶ�
				// Ĭ�ϵ�λ��D/s idΪ0
				def = 801;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 802)
				{
					return String.valueOf(value * 180);
				} // S/s
				else if (fromUnit.getID() == 803)
				{
					return String.valueOf(value * 180 / ��);
				} // R/s
				else if (fromUnit.getID() == 804)
				{
					return String.valueOf(value * 180 / (1000 * ��));
				} // mrad/s
				else if (fromUnit.getID() == 805)
				{
					return String.valueOf(value / (double) 3600);
				} // Sec/s

			case 9:// ���ٶ�
				// Ĭ�ϵ�λ��m/s2 idΪ1
				def = 902;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 901)
				{
					return String.valueOf(value * 0.3048);
				} // ft/s2
				else if (fromUnit.getID() == 903)
				{
					return String.valueOf(value * 9.80665);
				} // g

			case 10:// Ƶ��
				// Ĭ�ϵ�λHz
				def = 1001;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 1002)
				{
					return String.valueOf(value * 1000);
				} // KHz
				else if (fromUnit.getID() == 1003)
				{
					return String.valueOf(value * 1000000);
				} // MHz

			case 11:// ����
				// Ĭ�ϵ�λL
				def = 1101;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 1102)
				{
					return String.valueOf(value / (double) 1000);
				} // ml

			case 12:// �Ǽ��ٶ�
				// Ĭ�ϵ�λD/s2
				def = 1201;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 1202)
				{
					return String.valueOf(value * 180);
				} // S/s
				else if (fromUnit.getID() == 1203)
				{
					return String.valueOf(value * 180 / ��);
				} // R/s2
				else if (fromUnit.getID() == 1204)
				{
					return String.valueOf(value * 180 / (1000 * ��));
				} // mrad/s2

			case 13:// ����
				// Ĭ�ϵ�λPpm
				def = 1301;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				}

			case 51:// �洢��
				// Ĭ�ϵ�λkb idΪ1
				def = 5102;
				if (fromUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (fromUnit.getID() == 5101)
				{
					return String.valueOf(value / (double) 1024);
				} // b
				else if (fromUnit.getID() == 5103)
				{
					return String.valueOf(value * 1024);
				} // Mb
			default:
				break;
		}

		return valueStr;
	}

	/**
	 * ��Ĭ�ϵ�λת��Ϊ������λ
	 * 
	 * @param unitType ��λ����
	 * @param toUnit ��Ҫת����Ϊ�ĵ�λ
	 * @param valueStr ��Ҫת����ֵ
	 * 
	 *            �޸ģ�Ϊ����Ӷ�Dms��hms�Ĵ����ı亯���Ĳ����ͷ���ֵ����
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-1-14
	 */
	private static String converToNew(ICDUnitType unitType, ICDUnit toUnit, String valueStr)
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
		if (unitType.getID() == 6 && toUnit.getID() == 2)
		{// FIXME:�޸ı��
			h = (int) (value / 3600);
			m = (int) ((value % 3600) / 60);
			s = (int) (value % 60);

		}

		int def = 0;
		switch (unitType.getID())
		{
			case 1:// ����
				// Ĭ�ϵ�λ��ʱ��Ϊ��(m)
				def = 102;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 101)
				{
					return String.valueOf(value / 0.3048);
				} // Ӣ��
				else if (toUnit.getID() == 103)
				{
					return String.valueOf(value / (double) 1852);
				} // ����
				else if (toUnit.getID() == 104)
				{
					return String.valueOf(value / 0.0254);
				} // Ӣ��
				else if (toUnit.getID() == 105)
				{
					return String.valueOf(value / (double) 1000);
				} // ǧ��
				else if (toUnit.getID() == 107)
				{
					return String.valueOf(value * 1000);
				} // ����

			case 2:// �ٶ�
				// Ĭ�ϵ�λ��ʱ��Ϊm/s
				def = 203;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 201)
				{
					return String.valueOf(value / 0.3048);
				} // Ӣ��ÿ��
				else if (toUnit.getID() == 202)
				{
					return String.valueOf(value / (1852 / (double) 3600));
				} // ����ÿСʱ
				else if (toUnit.getID() == 204)
				{
					return String.valueOf(value / (0.3048 / (double) 60));
				} // Ӣ��ÿ��
				else if (toUnit.getID() == 205)
				{
					return String.valueOf(value * 3.6);
				} // ǧ��ÿСʱ
				else if (toUnit.getID() == 206)
				{
					return String.valueOf(value * 1000);
				} // ����ÿ��
				else if (toUnit.getID() == 207)
				{
					return String.valueOf(value / (double) 340);
				} // 1���=1����

			case 3:// �Ƕ�
				// �Ƕ�Ĭ�ϵ�λΪs_c��idΪ4
				def = 305;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 301)
				{
					return String.valueOf(value * ��);
				} // rad���ȣ� 1s_c = 180�� = �� rad
				else if (toUnit.getID() == 302)
				{
					return String.valueOf(value * 180);
				} // ��
				else if (toUnit.getID() == 303)
				{
					return String.valueOf((int) value * 180) + "D0m0s";
				} // �ȷ���
				else if (toUnit.getID() == 304)
				{
					return String.valueOf(value * (1000 * ��));
				} // ������

			case 4:// �¶�
				// Ĭ�ϵ�λΪ�� idΪ1
				def = 402;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 401)
				{
					return String.valueOf((value - 273.15) * 1.8);
				} // Ran
				else if (toUnit.getID() == 403)
				{
					return String.valueOf(value * 1.8 + 32);
				} // Far
				else if (toUnit.getID() == 404)
				{
					return String.valueOf(value + 273.15);
				} // Kel

			case 5:// ѹ��
				// Ĭ�ϵ�λΪkPa idΪ9
				def = 510;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 501)
				{
					return String.valueOf(value / 101.325);
				} // ��׼����ѹ
				else if (toUnit.getID() == 502)
				{
					return String.valueOf(value / 3.3863882);
				} // һӢ��Hg
				else if (toUnit.getID() == 503)
				{
					return String.valueOf(value / 1.3332237);
				} // һ����Hg
				else if (toUnit.getID() == 504)
				{
					return String.valueOf(value / 6.895);
				} // Psi
				else if (toUnit.getID() == 505)
				{
					return String.valueOf(value / 0.1);
				} // mbar ����
				else if (toUnit.getID() == 506)
				{
					return String.valueOf(value / 0.00980665);
				} // kg/�O ������
				else if (toUnit.getID() == 507)
				{
					return String.valueOf(value / 98.0665);
				} // kg/c�O ������
				else if (toUnit.getID() == 508)
				{
					return String.valueOf(value / 0.1);
				} // hPa
				else if (toUnit.getID() == 509)
				{
					return String.valueOf(value / 98.0665);
				} // kgf/c�O
				else if (toUnit.getID() == 511)
				{
					return String.valueOf(value / (double) 1000);
				} // Mpa

			case 6:// ʱ��
				// Ĭ�ϵ�λΪs idΪ0
				def = 601;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 602)
				{
					return String.valueOf(value * 20);
				} // Cyc =50����
				else if (toUnit.getID() == 603)
				{
					return String.valueOf(h) + "h" + String.valueOf(m) + "m" + String.valueOf(s) + "s";
				} // Hms
				else if (toUnit.getID() == 604)
				{
					return String.valueOf(value * 1000);
				} // M_Sc = ����
				else if (toUnit.getID() == 605)
				{
					return String.valueOf(value * 1000000);
				} // Mic = ΢��
				else if (toUnit.getID() == 606)
				{
					return String.valueOf(value / (double) 3600);
				} // h
				else if (toUnit.getID() == 607)
				{
					return String.valueOf(value / (double) 60);
				} // min
				// else if(toUnit.getID() == 7) {return
				// String.valueOf(value*1000000000);} //ns

			case 7:// ����
				// Ĭ�ϵ�λ��kg idΪ1
				def = 702;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 701)
				{
					return String.valueOf(value / 0.4536);
				} // lbr ��

			case 8:// ���ٶ�
				// Ĭ�ϵ�λ��D/s idΪ0
				def = 801;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 802)
				{
					return String.valueOf(value / (double) 180);
				} // S/s
				else if (toUnit.getID() == 803)
				{
					return String.valueOf(value * �� / (double) 180);
				} // R/s
				else if (toUnit.getID() == 804)
				{
					return String.valueOf((value * (1000 * ��) / (double) 180));
				} // mrad/s
				else if (toUnit.getID() == 805)
				{
					return String.valueOf(value * 3600);
				} // Sec/s

			case 9:// ���ٶ�
				// Ĭ�ϵ�λ��m/s2 idΪ1
				def = 902;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 901)
				{
					return String.valueOf(value / 0.3048);
				} // ft/s2
				else if (toUnit.getID() == 903)
				{
					return String.valueOf(value / 9.80665);
				} // g

			case 10:// Ƶ��
				// Ĭ�ϵ�λHz idΪ0
				def = 1001;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 1002)
				{
					return String.valueOf(value * 0.001);
				} // KHz
				else if (toUnit.getID() == 1003)
				{
					return String.valueOf(value / (double) 1000000);
				} // MHz

			case 11:// ����
				// Ĭ�ϵ�λL idΪ0
				def = 1101;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 1102)
				{
					return String.valueOf(value * 1000);
				} // ml

			case 12:// �Ǽ��ٶ�
				// Ĭ�ϵ�λ D/s2
				def = 1201;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 1202)
				{
					return String.valueOf(value / (double) 180);
				} // S/s2
				else if (toUnit.getID() == 1203)
				{
					return String.valueOf(value * �� / (double) 180);
				} // R/s2
				else if (toUnit.getID() == 1204)
				{
					return String.valueOf((value * (1000 * ��) / (double) 180));
				} // mrad/s2
				// else if(toUnit.getID() == 805) {return
				// String.valueOf(value*3600);} //Sec/s

			case 13:// ����
				// Ĭ�ϵ�λPpm
				def = 1301;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				}

			case 51:// �洢��
				// Ĭ�ϵ�λkb idΪ1
				def = 5102;
				if (toUnit.getID() == def)
				{
					return String.valueOf(value);
				} else if (toUnit.getID() == 5101)
				{
					return String.valueOf(value * 1024);
				} // b
				else if (toUnit.getID() == 5103)
				{
					return String.valueOf(value / (double) 1024);
				} // Mb
		}

		return valueStr;
	}
}
