package com.coretek.common.template;

import java.io.File;

import com.coretek.common.utils.MD5Util;

/**
 * 用于保存被解析后的ICD文件。一旦ICD文件被解析之后，就会被保存到硬盘上， 每次用户解析ICD文件时首先会查找ICD文件是否已经被解析过，查找的标准是，
 * 通过ICD文件的名字以及MD5值判断。
 * 
 * @author 孙大巍
 * 
 */
public class Registry
{
	// ICD文件的绝对路径
	private String	targetXML;

	private String	rulesXML;

	// ICD文件的MD5值，用于判断文件是否被修改过
	private String	md5;

	public Registry(File targetXML, File rulesXML)
	{
		this.targetXML = targetXML.getAbsolutePath();
		;
		this.rulesXML = rulesXML.getAbsolutePath();
		this.md5 = MD5Util.getMD5Digest(targetXML);
	}

	public Registry(String targetXML, String rulesXML)
	{
		this.targetXML = targetXML;
		this.rulesXML = rulesXML;
		this.md5 = MD5Util.getMD5Digest(new File(targetXML));
	}

	public Registry(Registry oldRegistry)
	{
		this(oldRegistry.targetXML, oldRegistry.rulesXML);
	}

	/**
	 * 判断目标文件是否能够被解析。判断之前保存的文件MD5值与当前的文件的MD5值是否相等， 如果不相等则返回true表示可以进行解析。
	 * 
	 * @return
	 */
	public boolean canParsing()
	{
		File file = new File(this.targetXML);
		if (!new File(this.targetXML).exists())
		{
			throw new RuntimeException("解析的目标文件 " + this.targetXML + " 不存在！");
		}
		String temp = MD5Util.getMD5Digest(file);
		if (!this.md5.equals(temp))
		{
			return true;
		}
		return false;
	}

	public String getTargetXML()
	{
		return targetXML;
	}

	public String getMd5()
	{
		return md5;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Registry)
		{
			Registry m = (Registry) obj;
			if (this.rulesXML.equals(m.rulesXML) && this.targetXML.equals(m.targetXML))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.rulesXML.hashCode();
		result = result * 31 + this.targetXML.hashCode();
		result = result * 31 + this.md5.hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("targetXML=").append(this.targetXML);
		sb.append("\n rulesXML=").append(this.rulesXML);
		sb.append("\n").append("md5=").append(this.md5);
		return sb.toString();
	}
}