/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.StringUtils;

/**
 * �����
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class CommandCache
{

	private static final Logger				logger	= LoggingPlugin.getLogger(CommandCache.class.getName());

	// ��������
	private volatile int					index	= 0;

	// ��������
	private Map<Integer, AbstractCommand>	cache	= new HashMap<Integer, AbstractCommand>();

	public CommandCache()
	{

	}

	/**
	 * ������������С�
	 * 
	 * @param command ����</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public synchronized CommandDecorator addCommand(AbstractCommand command)
	{
		++index;
		CommandDecorator cd = new CommandDecorator(index, command);
		this.cache.put(Integer.valueOf(index), cd);
		logger.info(StringUtils.concat("�������:", cd.getCommand()));
		return cd;
	}

	/**
	 * ��������Ϊ�Ѿ���ִ��
	 * 
	 * @param index ����������
	 * @param result
	 * @param successed
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-4
	 */
	public synchronized CommandDecorator updateCommand(int index, String result, boolean successed)
	{
		CommandDecorator command = (CommandDecorator) this.cache.get(index);
		if (command != null)
		{
			logger.info(StringUtils.concat(command.getCommand(), " ���ִ���ˡ�ִ�еķ��ؽ��Ϊ:\n", result));
			command.setExecuted(true, result, successed);
		} else
		{
			logger.warning(StringUtils.concat("�޷��ҵ�index=", index, "�����result=", result));
		}

		return command;

	}

	/**
	 * �������� </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
	 */
	public synchronized void clear()
	{
		this.cache.clear();
		this.index = 0;
		logger.config("������Ѿ�����ա�");
	}

}