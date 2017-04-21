/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.command.CommandCache;

/**
 * ����ִ�������ص��߼�������Ϣ�������ʽΪ:���,^error,{reason=���� }
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class LogicErrorHandler extends ResultHandler
{
	private CommandCache	cache;

	public LogicErrorHandler(CommandCache cache)
	{
		this();
		this.cache = cache;
	}

	/**
	 * @param regex
	 */
	private LogicErrorHandler()
	{
		super("[0-9]{1,}\\,(\\^error\\,\\{reason=\").*");
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IResultHandler#parser(java.lang
	 * .String)
	 */
	@Override
	public Object parser(String result)
	{
		if (StringUtils.isNull(result))
		{
			throw new IllegalArgumentException("ִ�н��Ϊ�ա�");
		}
		String[] strs = result.split("\\,");
		Integer index = Integer.valueOf(strs[0]);
		this.cache.updateCommand(index, result, false);
		this.setChanged();
		this.notifyObservers(result);
		return null;
	}

}