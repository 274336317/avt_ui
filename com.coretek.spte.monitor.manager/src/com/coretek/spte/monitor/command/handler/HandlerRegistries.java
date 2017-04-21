/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.command.handler;

import java.util.HashSet;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.console.MessagePrinter;

/**
 * ������ע������������Լ�ע�ᵽע����У�������ִ�������صĽ���� ϵͳ�ᵽע����н��в��Ҷ�Ӧ�Ľ��մ�������
 * 
 * @author ���Ρ
 * @date 2012-1-14
 */
public class HandlerRegistries
{
	private static final Logger			logger		= LoggingPlugin.getLogger(HandlerRegistries.class.getName());

	private static Set<ResultHandler>	handlers	= new HashSet<ResultHandler>();

	/**
	 * ע�ᴦ����
	 * 
	 * @param handler ���ִ����������Ϣ���ض�������
	 */
	public static void registerHandler(ResultHandler handler)
	{
		if (!handlers.add(handler))
		{
			logger.warning(StringUtils.concat("�Ѿ����ڴ�������clazz=", handler.getClass().getName(), " regex=", handler.getRegex()));
		}

		logger.info(StringUtils.concat("ע���˴�������clazz=", handler.getClass().getName(), " regex=", handler.getRegex()));
	}

	/**
	 * ��Ӽ�����
	 * 
	 * @param observer �����߶���
	 * @param targetClazz ��Ҫ������Ŀ��
	 * @return </br>
	 */
	public static boolean addObserver(Observer observer, Class<? extends ResultHandler> targetClazz)
	{
		for (ResultHandler handler : handlers)
		{
			if (handler.getClass().equals(targetClazz))
			{
				handler.addObserver(observer);
				logger.info(StringUtils.concat("��Ӽ������ɹ���targetClazz=", targetClazz.getName()));
				return true;
			}
		}
		
		logger.warning(StringUtils.concat("�޷��ҵ�ƥ������͡�targetClazz=", targetClazz.getName()));

		return false;
	}

	/**
	 * ɾ��������
	 * 
	 * @param observer
	 * @param targetClazz
	 */
	public static void deleteObserver(Observer observer, Class<? extends ResultHandler> targetClazz)
	{
		for (ResultHandler handler : handlers)
		{
			if (handler.getClass().equals(targetClazz))
			{
				handler.deleteObserver(observer);
			}
		}
	}

	/**
	 * �����еļ�����ɾ����Ȼ���Ѿ�ע��Ĵ����������
	 */
	public static void reset()
	{
		for (ResultHandler handler : handlers)
		{
			handler.deleteObservers();
		}

		handlers.clear();
	}

	/**
	 * ��ȡ������Ϣ��Ӧ�Ĵ�����
	 * 
	 * @param result ������������Ϣ
	 * @return
	 */
	public static ResultHandler getHandler(String result)
	{
		if (StringUtils.isNull(result))
		{
			logger.warning("���յ���ִ��������Ϊ�ա�");
			return null;
		}

		for (ResultHandler handler : handlers)
		{
			if (handler.isMyType(result))
			{
				logger.config(StringUtils.concat("�ҵ��˴�������clazz=", handler.getClass().getName(), ";regex=", handler.getRegex()));
				return handler;
			}
		}
		
		logger.warning(StringUtils.concat("���Ҳ����ܹ����� [", result, "]�Ĵ�������"));
		MessagePrinter.printInfo(result);
		return null;
	}

}