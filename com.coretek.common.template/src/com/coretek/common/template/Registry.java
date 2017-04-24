package com.coretek.common.template;

import java.io.File;

import com.coretek.common.utils.MD5Util;

/**
 * ���ڱ��汻�������ICD�ļ���һ��ICD�ļ�������֮�󣬾ͻᱻ���浽Ӳ���ϣ� ÿ���û�����ICD�ļ�ʱ���Ȼ����ICD�ļ��Ƿ��Ѿ��������������ҵı�׼�ǣ�
 * ͨ��ICD�ļ��������Լ�MD5ֵ�жϡ�
 * 
 * @author ���Ρ
 * 
 */
public class Registry
{
	// ICD�ļ��ľ���·��
	private String	targetXML;

	private String	rulesXML;

	// ICD�ļ���MD5ֵ�������ж��ļ��Ƿ��޸Ĺ�
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
	 * �ж�Ŀ���ļ��Ƿ��ܹ����������ж�֮ǰ������ļ�MD5ֵ�뵱ǰ���ļ���MD5ֵ�Ƿ���ȣ� ���������򷵻�true��ʾ���Խ��н�����
	 * 
	 * @return
	 */
	public boolean canParsing()
	{
		File file = new File(this.targetXML);
		if (!new File(this.targetXML).exists())
		{
			throw new RuntimeException("������Ŀ���ļ� " + this.targetXML + " �����ڣ�");
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