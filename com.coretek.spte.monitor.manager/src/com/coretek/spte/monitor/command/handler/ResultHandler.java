/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import java.util.Observable;
import java.util.regex.Pattern;

import com.coretek.common.utils.StringUtils;

/**
 * ִ�������ؽ��������
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public abstract class ResultHandler extends Observable implements IResultHandler
{
	private String		regex	= "";	// ������ʽ

	protected Pattern	pattern;

	public ResultHandler(String regex)
	{
		if (StringUtils.isNull(regex))
		{
			throw new IllegalArgumentException("������ʽ����Ϊ�ա�");
		}
		this.regex = regex;
	}

	protected void init()
	{
		this.pattern = Pattern.compile(this.regex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IResultHandler#isMyType(java.
	 * lang.String)
	 */
	@Override
	public boolean isMyType(String result)
	{
		return this.pattern.matcher(result).matches();
	}

	/**
	 * ��ȡ��������������ʽ
	 * 
	 * @return the regex <br/>
	 */
	public String getRegex()
	{
		return regex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) <b>����</b> 2012-1-14
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj instanceof ResultHandler)
		{
			ResultHandler handler = (ResultHandler) obj;
			if (handler.regex.equals(this.regex))
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() 2012-1-14
	 */
	@Override
	public int hashCode()
	{
		return this.regex.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() 2012-1-14
	 */
	@Override
	public String toString()
	{
		return this.regex;
	}

	/**
	 * ֪ͨ���еĹ۲���
	 * 
	 * @param eventName ʱ����
	 */
	protected void notifyListeners(String eventName)
	{
		this.setChanged();
		this.notifyObservers(eventName);
	}

}