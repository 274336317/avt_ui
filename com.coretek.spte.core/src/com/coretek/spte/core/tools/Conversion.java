package com.coretek.spte.core.tools;

/**
 * ��λ����
 * Ŀǰֻʵ���˳��ó��ȵ�λ��ʱ�䵥λ���¶ȵ�λ�Ļ��㣬
 * ��������ɸ��ݵ�λ�����ϵ����ӡ�
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
	 * @param index ��λ����ID
	 * @param cb icdĬ�ϸ����͵�λ��ID
	 * @param cb0 ��Ҫת���ĵ�λID
	 * @param cb1 ��Ҫת���ɵĵ�λID
	 * @param value ת��ǰ��ֵ
	 * @return </br> ת�����ֵ <b>����</b> duyisen </br> <b>����</b> 2011-7-11
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
	 * ����λת��ΪĬ�ϵ�λ
	 * 
	 * @param index ��λ����ID
	 * @param cb0 ��Ҫת���ĵ�λid
	 * @param value ��Ҫת����ֵ
	 * 
	 *            �޸ģ�Ϊ����Ӷ�Dms��hms�Ĵ����ı亯���Ĳ����ͷ���ֵ����
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2011-7-12
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
		} else if (index == 6)
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
		switch (index)
		{
			case 1:// ����
				// Ĭ�ϵ�λ��ʱ��Ϊ��(m)
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.3048);
				} // Ӣ��
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1852);
				} // ����
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.0254);
				} // Ӣ��
				else if (cb0 == 4)
				{
					return String.valueOf(value * 1000);
				} // ǧ��
				else if (cb0 == 5)
				{
					return String.valueOf(value);
				} // δ֪
				else if (cb0 == 6)
				{
					return String.valueOf(value / (double) 1000);
				} // ����
			case 2:// �ٶ�
				// Ĭ�ϵ�λ��ʱ��Ϊm/s
				def = 2;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.3048);
				} // Ӣ��ÿ��
				else if (cb0 == 1)
				{
					return String.valueOf(value * 1852 / (double) 3600);
				} // ����ÿСʱ
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.3048 / (double) 60);
				} // Ӣ��ÿ��
				else if (cb0 == 4)
				{
					return String.valueOf(value / 3.6);
				} // ǧ��ÿСʱ
				else if (cb0 == 5)
				{
					return String.valueOf(value / (double) 1000);
				} // ����ÿ��
				else if (cb0 == 6)
				{
					return String.valueOf(value * 340);
				} // 1���=1����
				else if (cb0 == 7)
				{
					return String.valueOf(value / (double) 60000);
				} // mm/min
			case 3:// �Ƕ�
				// �Ƕ�Ĭ�ϵ�λΪs_c��idΪ4
				def = 4;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value / Utils.��);
				} // rad���ȣ� 1s_c = 180�� = �� rad
				else if (cb0 == 1)
				{
					return String.valueOf(value / (double) 180);
				} // ��
				else if (cb0 == 2)
				{
					return String.valueOf((D + m / 60.0 + s / 3600.0) / (double) 180);
				} // �ȷ���
				else if (cb0 == 3)
				{
					return String.valueOf(value / (1000 * Utils.��));
				} // ������

			case 4:// �¶�
				// Ĭ�ϵ�λΪ�� idΪ1
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

			case 5:// ѹ��
				// Ĭ�ϵ�λΪkPa idΪ9
				def = 9;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 101.325);
				} // ��׼����ѹ
				else if (cb0 == 1)
				{
					return String.valueOf(value * 3.3863882);
				} // һӢ��Hg
				else if (cb0 == 2)
				{
					return String.valueOf(value * 1.3332237);
				} // һ����Hg
				else if (cb0 == 3)
				{
					return String.valueOf(value * 6.895);
				} // Psi
				else if (cb0 == 4)
				{
					return String.valueOf(value * 0.1);
				} // mbar ����
				else if (cb0 == 5)
				{
					return String.valueOf(value * 0.00980665);
				} // kg/�O ������
				else if (cb0 == 6)
				{
					return String.valueOf(value * 98.0665);
				} // kg/c�O ������
				else if (cb0 == 7)
				{
					return String.valueOf(value * 0.1);
				} // hPa
				else if (cb0 == 8)
				{
					return String.valueOf(value * 98.0665);
				} // kgf/c�O
				else if (cb0 == 10)
				{
					return String.valueOf(value * 1000);
				} // Mpa
			case 6:// ʱ��
				// Ĭ�ϵ�λΪs idΪ0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value * 0.05);
				} // Cyc =50����
				else if (cb0 == 2)
				{
					return String.valueOf(h * 3600 + m * 60 + s);
				} // Hms
				else if (cb0 == 3)
				{
					return String.valueOf(value * 0.001);
				} // M_Sc����
				else if (cb0 == 4)
				{
					return String.valueOf(value * 0.000001);
				} // Mic΢��
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
			case 7:// ����
				// Ĭ�ϵ�λ��kg idΪ1
				def = 1;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 0)
				{
					return String.valueOf(value * 0.4536);
				} // lbr ��
			case 8:// ���ٶ�
				// Ĭ�ϵ�λ��D/s idΪ0
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
					return String.valueOf(value * 180 / Utils.��);
				} // R/s
				else if (cb0 == 3)
				{
					return String.valueOf(value * 180 / (1000 * Utils.��));
				} // mrad/s
				else if (cb0 == 4)
				{
					return String.valueOf(value / (double) 3600);
				} // Sec/s
			case 9:// ���ٶ�
				// Ĭ�ϵ�λ��m/s2 idΪ1
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
			case 10:// Ƶ��
				// Ĭ�ϵ�λHz idΪ0
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
			case 11:// ����
				// Ĭ�ϵ�λL idΪ0
				def = 0;
				if (cb0 == def)
				{
					return String.valueOf(value);
				} else if (cb0 == 1)
				{
					return String.valueOf(value / (double) 1000);
				} // ml
			case 51:// �ֽ�
				// Ĭ�ϵ�λkb idΪ1
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
	 * ��Ĭ�ϵ�λת��Ϊ������λ
	 * 
	 * @param index ��λ����ID
	 * @param def Ĭ�ϵ�λ
	 * @param cb1 ��Ҫת����Ϊ�ĵ�λ
	 * @param valueStr ��Ҫת����ֵ����ֵ��ӦĬ�ϵ�λ
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2011-7-11
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
		// else if(index == 3){//�Ƕ�
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
		// } else if(index == 6){//ʱ��
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
			case 1:// ����
				// Ĭ�ϵ�λ��ʱ��Ϊ��(m)
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.3048);
				} // Ӣ��
				else if (cb1 == 2)
				{
					return String.valueOf(value / (double) 1852);
				} // ����
				else if (cb1 == 3)
				{
					return String.valueOf(value / 0.0254);
				} // Ӣ��
				else if (cb1 == 4)
				{
					return String.valueOf(value / (double) 1000);
				} // ǧ��
				else if (cb1 == 5)
				{
					return String.valueOf(value);
				} // δ֪
				else if (cb1 == 6)
				{
					return String.valueOf(value * 1000);
				} // ����
			case 2:// �ٶ�
				// Ĭ�ϵ�λ��ʱ��Ϊm/s
				def = 2;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.3048);
				} // Ӣ��ÿ��
				else if (cb1 == 1)
				{
					return String.valueOf(value / (1852 / (double) 3600));
				} // ����ÿСʱ
				else if (cb1 == 3)
				{
					return String.valueOf(value / (0.3048 / (double) 60));
				} // Ӣ��ÿ��
				else if (cb1 == 4)
				{
					return String.valueOf(value * 3.6);
				} // ǧ��ÿСʱ
				else if (cb1 == 5)
				{
					return String.valueOf(value * 1000);
				} // ����ÿ��
				else if (cb1 == 6)
				{
					return String.valueOf(value / (double) 340);
				} // 1���=1����
				else if (cb1 == 7)
				{
					return String.valueOf(value * 60000);
				}
			case 3:// �Ƕ�
				// �Ƕ�Ĭ�ϵ�λΪs_c��idΪ4
				def = 4;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value * Utils.��);
				} // rad���ȣ� 1s_c = 180�� = �� rad
				else if (cb1 == 1)
				{
					return String.valueOf(value * 180);
				} // ��
				else if (cb1 == 2)
				{
					return String.valueOf((int) value * 180) + "D0m0s";
				} // �ȷ���
				else if (cb1 == 3)
				{
					return String.valueOf(value * (1000 * Utils.��));
				} // ������

			case 4:// �¶�
				// Ĭ�ϵ�λΪ�� idΪ1
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
			case 5:// ѹ��
				// Ĭ�ϵ�λΪkPa idΪ9
				def = 9;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 101.325);
				} // ��׼����ѹ
				else if (cb1 == 1)
				{
					return String.valueOf(value / 3.3863882);
				} // һӢ��Hg
				else if (cb1 == 2)
				{
					return String.valueOf(value / 1.3332237);
				} // һ����Hg
				else if (cb1 == 3)
				{
					return String.valueOf(value / 6.895);
				} // Psi
				else if (cb1 == 4)
				{
					return String.valueOf(value / 0.1);
				} // mbar ����
				else if (cb1 == 5)
				{
					return String.valueOf(value / 0.00980665);
				} // kg/�O ������
				else if (cb1 == 6)
				{
					return String.valueOf(value / 98.0665);
				} // kg/c�O ������
				else if (cb1 == 7)
				{
					return String.valueOf(value / 0.1);
				} // hPa
				else if (cb1 == 8)
				{
					return String.valueOf(value / 98.0665);
				} // kgf/c�O
				else if (cb1 == 10)
				{
					return String.valueOf(value / (double) 1000);
				} // Mpa
			case 6:// ʱ��
				// Ĭ�ϵ�λΪs idΪ0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value * 20);
				} // Cyc =50����
				else if (cb1 == 2)
				{
					return String.valueOf(h) + "h" + String.valueOf(m) + "m" + String.valueOf(s) + "s";
				} // Hms
				else if (cb1 == 3)
				{
					return String.valueOf(value * 1000);
				} // M_Sc = ����
				else if (cb1 == 4)
				{
					return String.valueOf(value * 1000000);
				} // Mic = ΢��
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
			case 7:// ����
				// Ĭ�ϵ�λ��kg idΪ1
				def = 1;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 0)
				{
					return String.valueOf(value / 0.4536);
				} // lbr ��
			case 8:// ���ٶ�
				// Ĭ�ϵ�λ��D/s idΪ0
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
					return String.valueOf(value * Utils.�� / (double) 180);
				} // R/s
				else if (cb1 == 3)
				{
					return String.valueOf((value * (1000 * Utils.��) / (double) 180));
				}// mrad/s
				else if (cb1 == 4)
				{
					return String.valueOf(value * 3600);
				} // Sec/s
			case 9:// ���ٶ�
				// Ĭ�ϵ�λ��m/s2 idΪ1
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
			case 10:// Ƶ��
				// Ĭ�ϵ�λHz idΪ0
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
			case 11:// ����
				// Ĭ�ϵ�λL idΪ0
				def = 0;
				if (cb1 == def)
				{
					return String.valueOf(value);
				} else if (cb1 == 1)
				{
					return String.valueOf(value * 1000);
				} // ml
			case 51:// �ֽ�
				// Ĭ�ϵ�λkb idΪ1
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
