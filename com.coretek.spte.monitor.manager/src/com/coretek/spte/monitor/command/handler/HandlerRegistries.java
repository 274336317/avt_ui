/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 处理器注册表。处理器将自己注册到注册表中，当接收执行器返回的结果后， 系统会到注册表中进行查找对应的接收处理器。
 * 
 * @author 孙大巍
 * @date 2012-1-14
 */
public class HandlerRegistries
{
	private static final Logger			logger		= LoggingPlugin.getLogger(HandlerRegistries.class.getName());

	private static Set<ResultHandler>	handlers	= new HashSet<ResultHandler>();

	/**
	 * 注册处理器
	 * 
	 * @param handler 针对执行器返回信息的特定处理器
	 */
	public static void registerHandler(ResultHandler handler)
	{
		if (!handlers.add(handler))
		{
			logger.warning(StringUtils.concat("已经存在处理器。clazz=", handler.getClass().getName(), " regex=", handler.getRegex()));
		}

		logger.info(StringUtils.concat("注册了处理器。clazz=", handler.getClass().getName(), " regex=", handler.getRegex()));
	}

	/**
	 * 添加监听器
	 * 
	 * @param observer 监听者对象
	 * @param targetClazz 需要监听的目标
	 * @return </br>
	 */
	public static boolean addObserver(Observer observer, Class<? extends ResultHandler> targetClazz)
	{
		for (ResultHandler handler : handlers)
		{
			if (handler.getClass().equals(targetClazz))
			{
				handler.addObserver(observer);
				logger.info(StringUtils.concat("添加监听器成功。targetClazz=", targetClazz.getName()));
				return true;
			}
		}
		
		logger.warning(StringUtils.concat("无法找到匹配的类型。targetClazz=", targetClazz.getName()));

		return false;
	}

	/**
	 * 删除监听器
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
	 * 将所有的监听器删除，然后将已经注册的处理器清除。
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
	 * 获取返回信息对应的处理器
	 * 
	 * @param result 处理器返回信息
	 * @return
	 */
	public static ResultHandler getHandler(String result)
	{
		if (StringUtils.isNull(result))
		{
			logger.warning("接收到的执行器返回为空。");
			return null;
		}

		for (ResultHandler handler : handlers)
		{
			if (handler.isMyType(result))
			{
				logger.config(StringUtils.concat("找到了处理器。clazz=", handler.getClass().getName(), ";regex=", handler.getRegex()));
				return handler;
			}
		}
		
		logger.warning(StringUtils.concat("查找不到能够处理 [", result, "]的处理器！"));
		MessagePrinter.printInfo(result);
		return null;
	}

}