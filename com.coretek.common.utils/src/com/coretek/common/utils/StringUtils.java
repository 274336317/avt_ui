/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����ṩ�����ַ�����һЩ���÷������������ʹ�ù��������κ����ʻ����и��õĽ����� ��ϵ��������ߡ���ӭ�������޸������
 * 
 * @author ���Ρ 2011-12-5
 */
public class StringUtils
{

	/** ���ַ������� */
	public final static String	EMPTY_STRING	= "";

	/** ֻ����һ���ո���ַ��� */
	public final static String	SPACE_STRING	= " ";

	/**
	 * ��ȡϵͳʱ���ַ�����"yyyy-MM-dd HH-mm-ss"��
	 * 
	 * @return
	 */
	public static String getCurrentTime()
	{
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTimeInMillis(new Date().getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * �жϴ�����ַ����е��ַ��Ƿ�ȫΪ���֣�����Ҳ���������ֵķ�Χ�ࡣ
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str)
	{
		if (isNull(str))
		{
			return false;
		}
		Pattern p = Pattern.compile(new StringBuilder("[-]?+[0-9]{").append((str.length() == 0 ? 1 : str.length() - 1)).append(",").append(str.length()).append("}").toString());
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ�������߰�����+����-��+-����-+���ڼ�������
	 * 
	 * @param str
	 * @return ���str���򷵻�true�����򷵻�false <br/>
	 */
	public static boolean isTxtReviseRight(String str)
	{
		if (null != str && !str.equals(""))
		{
			Pattern p = Pattern.compile("([+][-]|[-][+]|[-]|[+])?[0-9]{1,31}+$");
			Matcher m = p.matcher(str);
			return m.matches();
		} else
		{
			return false;
		}
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ�����������ַ�����ģ��Ϊ��$value>c��$value<c��a<$value<c a,cΪ�����������
	 * 
	 * @param str
	 * @return ���str���򷵻�true�����򷵻�false <br/>
	 * 
	 *         <b>����</b> xuy </br> <b>����</b> 2011-12-5
	 */
	public static boolean isPeriodValueRight(String str)
	{
		Pattern p = null;
		Matcher m = null;
		if (null != str && !str.equals(""))
		{
			p = Pattern.compile("[0-9]{1,31}+(([>][=]|[<][=]|[<]|[>]|[!][=]){1}\\p{Sc}(?i)(value)(?-i))?(([>][=]|[<][=]|[<]|[>]|[!][=]){1}[0-9]{1,31}+)?$|\\p{Sc}(value)([>][=]|[<][=]|[<]|[>]|[!][=]){1}[0-9]{1,31}+$");
			m = p.matcher(str);
			return m.matches();
		} else
		{
			return false;
		}
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ�����������ַ�����ģ��Ϊ��$Previous+c��$Previous-c��$Previous*c��$Previous/c
	 * cΪ�����������
	 * 
	 * @param str ,n
	 * @return ���str���򷵻�true�����򷵻�false <br/>
	 * 
	 *         <b>����</b> xuy </br> <b>����</b> 2011-12-5
	 */
	public static boolean isSendValueRight(String str, int n)
	{
		Pattern p = null;
		Matcher m = null;
		if (null != str && !str.equals(""))
		{
			if (n != 1)
			{
				p = Pattern.compile("(\\p{Sc}(?i)(previous)(?-i)([+]|[-]|[*]|[/]){1}[1-9]{1,31}+)$|[0-9]{1,31}+$");
				m = p.matcher(str);
			} else
			{
				p = Pattern.compile("[0-9]{" + (str.length() == 0 ? 1 : str.length() - 1) + "," + str.length() + "}");
				m = p.matcher(str);
			}
			return m.matches();
		} else
		{
			return false;
		}
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ�����������ַ�����ģ��Ϊ��$Previous+c��$Previous-c��$Previous*c��$Previous/c
	 * cΪ�����������
	 * 
	 * @param str ,n
	 * @return ���str���򷵻�true�����򷵻�false <br/>
	 * 
	 *         <b>����</b> xuy </br> <b>����</b> 2011-12-5
	 */
	public static boolean isSendValueRight(String str)
	{
		Pattern p = null;
		Matcher m = null;
		if (null != str && !str.equals(""))
		{
			p = Pattern.compile("(\\p{Sc}(?i)(previous)(?-i)([+]|[-]|[*]|[/]){1}[1-9]{1,31}+)$|[0-9]{1,31}+$");
			m = p.matcher(str);

			return m.matches();
		} else
		{
			return false;
		}
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ������
	 * 
	 * @param str
	 * @return ���str���򷵻�true�����򷵻�false <br/>
	 * 
	 *         <b>����</b> XUY </br> <b>����</b> 2011-12-5
	 */
	public static boolean isPositiveInteger(String str)
	{
		if (isNotNull(str) && !str.equals("null"))
		{
			Pattern p = Pattern.compile("[0-9]{" + (str.length() == 0 ? 1 : str.length() - 1) + "," + str.length() + "}");
			Matcher m = p.matcher(str);
			return m.matches();
		} else
			return false;
	}

	/**
	 * �ж�һ���ַ����Ƿ����ת��Ϊdouble��
	 * 
	 * @param str
	 * @return ���str����ת��Ϊdouble���򷵻�true�����򷵻�false </br> <b>����</b> ��һɭ </br>
	 *         <b>����</b> 2011-7-15
	 */
	public static boolean isDouble(String str)
	{
		if (null != str && str.contains("."))
		{
			String strs[] = str.split("\\.");
			if (strs.length == 2 && !str.endsWith("."))
			{
				for (String str1 : strs)
				{
					if (!isNumber(str1))
					{
						return false;
					}
				}
			} else
			{
				return false;
			}
		} else
		{
			return isNumber(str);
		}
		return true;
	}

	/**
	 * �жϴ�����ַ����Ƿ����IPV4��ַ�ĸ�ʽ
	 * 
	 * @param str
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-5
	 */
	public static boolean isIPv4Format(String str)
	{
		Pattern p = Pattern.compile("[1-2]{0,1}[0-9]{1,2}\\.[1-2]{0,1}[0-9]{1,2}\\.[1-2]{0,1}[0-9]{1,2}\\.[1-2]{0,1}[0-9]{1,2}");
		Matcher m = p.matcher(str);
		if (m.matches())
		{
			String[] strs = str.split("\\.");
			for (String str1 : strs)
			{
				if (Integer.valueOf(str1).intValue() > 255)
				{
					return false;
				}
			}
			return true;
		}

		return false;
	}

	/**
	 * �ж��Ƿ�����Dms
	 * 
	 * @param str
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-03-16
	 */
	public static boolean isDmsFormat(String str)
	{
		Pattern p = Pattern.compile("[0-9]{0,31}+[D]+[0-9]{0,2}+[m]+[0-9]{0,2}+[s]");
		Matcher m = p.matcher(str);
		if (m.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * �ж��Ƿ�����Dms
	 * 
	 * @param str
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-03-16
	 */
	public static boolean isHmsFormat(String str)
	{
		Pattern p = Pattern.compile("[0-9]{0,31}+[h]+[0-9]{0,2}+[m]+[0-9]{0,2}+[s]");
		Matcher m = p.matcher(str);
		if (m.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * �жϴ�����ַ�����Ϊ�գ�����ַ���������""��Ҳ����Ϊ���ַ�������
	 * 
	 * @param str
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-2
	 */
	public static boolean isNotNull(String str)
	{

		return (str != null && str.trim().length() != 0);
	}

	/**
	 * �жϴ�����ַ�����Ϊ�գ�����ַ���������""��Ҳ����Ϊ���ַ�������
	 * 
	 * @param str
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-2
	 */
	public static boolean isNull(String str)
	{

		return (str == null || str.trim().length() == 0);
	}

	/**
	 * �ж������ַ��������Ƿ���ȡ����ͨ��{@link StringUtils#isNull(String)}�жϳ�
	 * �����ַ����е�����һ��Ϊ�գ����������ַ�������ȡ�
	 * 
	 * @param str1
	 * @param str2
	 * @return �����ַ�������򷵻�true��������򷵻�false</br> <b>����</b> ���Ρ </br> <b>����</b>
	 *         2011-12-8
	 */
	public static boolean equals(String str1, String str2)
	{
		if (isNull(str1) || isNull(str2))
		{
			return false;
		}

		return str1.equals(str2);
	}

	/**
	 * ��<��>��>=��<=��&&�������ַ�������
	 * 
	 * @param content
	 * @return
	 */
	public static String convert(String content)
	{
		if (isNull(content))
		{
			return null;
		}
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("<=", "&le");
		content = content.replaceAll(">=", "&ge");
		content = content.replaceAll("&&", "&amp;&amp;");
		return content;
	}

	/**
	 * ����ַ������Ƿ�����˰�Ǻ��֡�
	 * 
	 * @param str
	 * @return ��������˺����򷵻�true�����򷵻�false��</br> <b>����</b> ������</br> <b>����</b>
	 *         2011-12-9
	 */
	public static boolean hasChinese(String str)
	{
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher matcher = p.matcher(str);
		return matcher.find();
	}

	/**
	 * ����ַ������Ƿ������ȫ���ַ���
	 * 
	 * @param str
	 * @return ��������˺����򷵻�true�����򷵻�false��</br> <b>����</b> ������</br> <b>����</b>
	 *         2011-12-9
	 */
	public static boolean hasFullChar(String str)
	{
		Pattern p = Pattern.compile("[\ufe30-\uffa0]{1,}");
		Matcher matcher = p.matcher(str);
		return matcher.find();
	}

	/**
	 * Get a uuid
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-31
	 */
	public static String getUUID()
	{
		return UUID.randomUUID().toString();
	}

	/**
	 * �����ַ���
	 * 
	 * @param objs
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-8-8
	 */
	public static String concat(Object... objs)
	{
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs)
		{
			sb.append(obj.toString());
		}
		return sb.toString();
	}
}