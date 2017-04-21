/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.command.CommandCache;
import com.coretek.spte.monitor.command.CommandDecorator;

/**
 * ����ִ�������ص�done��Ϣ������ĸ�ʽΪ:<b>���,^done,{reason=""}</b>
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class DoneHandler extends ResultHandler
{
	public final static String	RUN		= "run";

	public final static String	EXIT	= "exit";

	private CommandCache		cache;

	public DoneHandler(CommandCache cache)
	{
		this();
		this.cache = cache;
	}

	private DoneHandler()
	{
		super("[0-9]{1,}\\,\\^(done).*");
		this.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.command.parser.IInfoHandler#parser(java.lang
	 * .String) <br/>
	 */
	@Override
	public Object parser(String result)
	{
		String[] strs = result.split("\\,");
		int index = Integer.valueOf(strs[0]);
		// �������״̬����Ϊ�Ѿ���ִ��
		CommandDecorator cd = this.cache.updateCommand(index, result, true);
		if (cd != null)
		{
			String str = cd.getOrgCommand().getCommand();
			if (StringUtils.isNotNull(str) && str.indexOf("run,") >= 0)
			{
				this.setChanged();
				// ִ�����������˲�������������֪ͨ
				this.notifyListeners(RUN);
				this.clearChanged();
			} else if (StringUtils.isNotNull(str) && str.indexOf("exit") >= 0)
			{
				this.setChanged();
				// ֪ͨ���м�����ִ�����Ѿ��˳�
				this.notifyListeners(EXIT);
				this.clearChanged();
			}
		}
		return null;
	}

}