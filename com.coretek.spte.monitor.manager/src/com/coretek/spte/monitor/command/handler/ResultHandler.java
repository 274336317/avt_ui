/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import java.util.Observable;
import java.util.regex.Pattern;

import com.coretek.common.utils.StringUtils;

/**
 * 执行器返回结果处理器
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public abstract class ResultHandler extends Observable implements IResultHandler
{
	private String		regex	= "";	// 正则表达式

	protected Pattern	pattern;

	public ResultHandler(String regex)
	{
		if (StringUtils.isNull(regex))
		{
			throw new IllegalArgumentException("正则表达式不能为空。");
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
	 * 获取处理器的正则表达式
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
	 * @see java.lang.Object#equals(java.lang.Object) <b>日期</b> 2012-1-14
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
	 * 通知所有的观察者
	 * 
	 * @param eventName 时间名
	 */
	protected void notifyListeners(String eventName)
	{
		this.setChanged();
		this.notifyObservers(eventName);
	}

}