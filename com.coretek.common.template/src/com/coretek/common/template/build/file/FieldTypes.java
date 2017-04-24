/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.file;

import java.util.List;

/**
 * 类属性，有 byte char int long float double String
 * 
 * @author 孙大巍 2011-12-23
 */
public enum FieldTypes
{

	BYTE("Byte", "java.lang.Byte"), INT("Integer", "java.lang.Integer"), LONG("Long", "java.lang.Long"), FLOAT("Float", "java.lang.Float"), DOUBLE("Double", "java.lang.Double"), STRING("String", "java.lang.String"), LIST("List", "java.util.List"), BOOLEAN("Boolean", "java.lang.Boolean");

	FieldTypes(String code, String clazz)
	{
		this.code = code;
		this.clazz = clazz;
	}

	/**
	 * 获取类型
	 * 
	 * @return the code <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * 获取type
	 * 
	 * @return
	 * @throws ClassNotFoundException </br> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 *             2011-12-25
	 */
	public Class<?> getType() throws ClassNotFoundException
	{

		return Class.forName(this.clazz);
	}

	/**
	 * @return the clazz <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public String getClazz()
	{
		return clazz;
	}

	private String	code;

	private String	clazz;

	public static FieldTypes getFieldType(String str)
	{
		if (BYTE.code.equals(str))
			return BYTE;
		if (INT.code.equals(str))
			return INT;
		if (LONG.code.equals(str))
			return LONG;
		if (FLOAT.code.equals(str))
			return FLOAT;
		if (DOUBLE.code.equals(str))
			return DOUBLE;
		if (STRING.code.equals(str))
			return STRING;
		if (LIST.code.equals(str))
			return LIST;
		if (BOOLEAN.code.equals(str))
			return BOOLEAN;

		return null;
	}

	public static Object toValue(FieldTypes type, Object value)
	{
		if (type == null)
			return value.toString();
		if (type == BYTE)
			return Byte.valueOf(value.toString());
		switch (type)
		{
			case BYTE:
			{
				return Byte.valueOf(value.toString());
			}
			case INT:
			{
				if (value.toString().trim().length() == 0)
					return 0;
				return Integer.valueOf(value.toString().trim());
			}
			case FLOAT:
			{
				return Float.valueOf(value.toString().trim());
			}
			case DOUBLE:
			{
				if (value.toString().trim().length() == 0)
					return new Double(0.0);
				return Double.valueOf(value.toString());
			}
			case LIST:
			{
				return (List) value;
			}
			case BOOLEAN:
			{
				if (value.toString().trim().length() == 0)
					return false;
				return Boolean.valueOf(value.toString().trim());
			}
			default:
			{
				return value.toString().trim();
			}
		}
	}

}
