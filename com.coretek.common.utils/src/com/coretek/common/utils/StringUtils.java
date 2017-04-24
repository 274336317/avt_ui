/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 此类提供处理字符串的一些常用方法。如果你在使用过程中有任何疑问或则有更好的建议请 联系代码的作者。欢迎大家提出修改意见。
 * 
 * @author 孙大巍 2011-12-5
 */
public class StringUtils
{

	/** 空字符串对象 */
	public final static String	EMPTY_STRING	= "";

	/** 只包含一个空格的字符串 */
	public final static String	SPACE_STRING	= " ";

	/**
	 * 获取系统时间字符串（"yyyy-MM-dd HH-mm-ss"）
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
	 * 判断传入的字符串中的字符是否全为数字，负数也包括在数字的范围类。
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
	 * 判断一个字符串是否为整数或者包含“+”或“-或“+-”或“-+”在加上数字
	 * 
	 * @param str
	 * @return 如果str是则返回true，否则返回false <br/>
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
	 * 判断一个字符串是否为正整数或者字符串的模型为：$value>c或$value<c或a<$value<c a,c为任意的正整数
	 * 
	 * @param str
	 * @return 如果str是则返回true，否则返回false <br/>
	 * 
	 *         <b>作者</b> xuy </br> <b>日期</b> 2011-12-5
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
	 * 判断一个字符串是否为正整数或者字符串的模型为：$Previous+c或$Previous-c或$Previous*c或$Previous/c
	 * c为任意的正整数
	 * 
	 * @param str ,n
	 * @return 如果str是则返回true，否则返回false <br/>
	 * 
	 *         <b>作者</b> xuy </br> <b>日期</b> 2011-12-5
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
	 * 判断一个字符串是否为正整数或者字符串的模型为：$Previous+c或$Previous-c或$Previous*c或$Previous/c
	 * c为任意的正整数
	 * 
	 * @param str ,n
	 * @return 如果str是则返回true，否则返回false <br/>
	 * 
	 *         <b>作者</b> xuy </br> <b>日期</b> 2011-12-5
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
	 * 判断一个字符串是否为正整数
	 * 
	 * @param str
	 * @return 如果str是则返回true，否则返回false <br/>
	 * 
	 *         <b>作者</b> XUY </br> <b>日期</b> 2011-12-5
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
	 * 判断一个字符串是否可以转化为double型
	 * 
	 * @param str
	 * @return 如果str可以转换为double型则返回true，否则返回false </br> <b>作者</b> 杜一森 </br>
	 *         <b>日期</b> 2011-7-15
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
	 * 判断传入的字符串是否符合IPV4地址的格式
	 * 
	 * @param str
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-5
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
	 * 判断是否满足Dms
	 * 
	 * @param str
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-03-16
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
	 * 判断是否满足Dms
	 * 
	 * @param str
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-03-16
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
	 * 判断传入的字符串不为空，如果字符串仅包含""则也将作为空字符串处理
	 * 
	 * @param str
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-2
	 */
	public static boolean isNotNull(String str)
	{

		return (str != null && str.trim().length() != 0);
	}

	/**
	 * 判断传入的字符串不为空，如果字符串仅包含""则也将作为空字符串处理
	 * 
	 * @param str
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-2
	 */
	public static boolean isNull(String str)
	{

		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 判断两个字符串对象是否相等。如果通过{@link StringUtils#isNull(String)}判断出
	 * 两个字符串中的任意一个为空，则这两个字符串不相等。
	 * 
	 * @param str1
	 * @param str2
	 * @return 两个字符串相等则返回true，不相等则返回false</br> <b>作者</b> 孙大巍 </br> <b>日期</b>
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
	 * 将<、>、>=、<=、&&符号用字符串代替
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
	 * 检查字符串中是否包含了半角汉字。
	 * 
	 * @param str
	 * @return 如果包含了汉字则返回true，否则返回false。</br> <b>作者</b> 廖晓波</br> <b>日期</b>
	 *         2011-12-9
	 */
	public static boolean hasChinese(String str)
	{
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher matcher = p.matcher(str);
		return matcher.find();
	}

	/**
	 * 检查字符串中是否包含了全角字符。
	 * 
	 * @param str
	 * @return 如果包含了汉字则返回true，否则返回false。</br> <b>作者</b> 廖晓波</br> <b>日期</b>
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
	 * 连接字符串
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