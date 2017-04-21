/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.core.internal.runtime.RuntimeLog;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 在插件启动时，会查找当前工作空间中的.metadata目录下的Log文件夹中是否存在CoretekLog.properties文件，
 * 如果不存在，则创建它。创建它之后用户就可以根据需要修改日志输出以及日志级别等相关参数。
 * 
 * @author 孙大巍 2011-12-5
 */
public class LoggingPlugin extends AbstractUIPlugin implements IStartup
{

	// The plug-in ID
	public static final String		PLUGIN_ID	= "com.coretek.common.logging"; //$NON-NLS-1$

	// The shared instance
	private static LoggingPlugin	plugin;

	private LogListener				logListener;

	/**
	 * The constructor
	 */
	public LoggingPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
		InputStream in = LoggingPlugin.class.getClassLoader().getResourceAsStream("CoretekLog.properties");
		String workspacePath = new StringBuilder(ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString()).append("/.metadata").toString();
		String propertiesPath = new StringBuilder(workspacePath).append("/Log/CoretekLog.properties").toString();
		File file = new File(propertiesPath);
		if (!file.exists())
		{
			/*
			 * 日志配置文件会默认从当前插件的CoretekLog.properties拷贝到用户的workspace下面，
			 * 用户在切换workspace之后，系统应当从CoretekLog.properties拷贝一份到用户的当前workspace下面。
			 * 每次启动此插件时，都要进行这些检查。
			 */
			file = new File(new StringBuilder(workspacePath).append("/Log").toString());
			if (!file.exists())
			{
				if (!file.mkdir())
				{
					throw new RuntimeException("Can not create Log folder.");
				}
			}
			file = new File(propertiesPath);
			if (!file.exists())
			{
				// 创建日志配置文件
				file.createNewFile();
				if (in != null)
				{
					// 如果日志配置文件不存在，则创建它。从当前插件下的CoretekLog.properties文件复制
					PrintWriter writer = new PrintWriter(file);
					InputStreamReader isr = new InputStreamReader(in);
					BufferedReader reader = new BufferedReader(isr);
					String contents = null;
					while ((contents = reader.readLine()) != null)
					{
						if (contents.indexOf("java.util.logging.FileHandler.pattern") >= 0)
						{
							contents = new StringBuilder("java.util.logging.FileHandler.pattern=").append(workspacePath).append("/Log/CoretekLog%u.log").toString();
						}
						writer.println(contents);
					}
					writer.flush();
					reader.close();
					isr.close();
					writer.close();
				} else
				{
					List<String> cfg = new ArrayList<String>();
					// FILE
					cfg.add("handlers=java.util.logging.FileHandler,java.util.logging.ConsoleHandler");
					cfg.add(new StringBuilder("java.util.logging.FileHandler.pattern=").append(workspacePath).append("/Log/CoretekLog%u.log").toString());
					cfg.add("java.util.logging.FileHandler.limit = 500000");
					cfg.add("java.util.logging.FileHandler.count = 100");
					cfg.add("java.util.logging.FileHandler.level = INFO");
					cfg.add("java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter");
					// CONSOLE
					cfg.add("java.util.logging.ConsoleHandler.level = OFF");
					cfg.add("java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter");
					PrintWriter writer = new PrintWriter(file);
					// 将配置信息写到文件里
					for (String str : cfg)
					{
						writer.println(str);
					}
					writer.flush();
					writer.close();
				}
			}
		}

		in = new FileInputStream(propertiesPath);
		// 初始化日志
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration(in);
		if (in != null)
		{
			in.close();
		}
		this.logListener = new LogListener();
		// 监听eclipse平台监听到的日志
		RuntimeLog.addLogListener(this.logListener);
	}

	/**
	 * 将异常记录到日志中
	 * 
	 * @param logger
	 * @param throwable </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-14
	 */
	public static void logException(Logger logger, Throwable throwable)
	{
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		throwable.printStackTrace(writer);
		logger.severe(stringWriter.toString());
		writer.flush();
		writer.close();
		try
		{
			stringWriter.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		RuntimeLog.removeLogListener(this.logListener);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LoggingPlugin getDefault()
	{
		return plugin;
	}

	/**
	 * 如果需要日志服务请从此接口获取一个日志实例
	 * 
	 * @param name 类名
	 * @param level 日志级别
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-5
	 */
	public static Logger getLogger(String name, Level level)
	{
		if (LoggingPlugin.getDefault() == null)
			return null;
		Logger logger = Logger.getLogger(name);
		logger.setLevel(level);

		return logger;
	}

	/**
	 * 如果需要日志服务请从此接口获取一个日志实例
	 * 
	 * @param clazz
	 * @param level 日志级别
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public static Logger getLogger(Class<?> clazz, Level level)
	{

		return getLogger(clazz.getName(), level);
	}

	/**
	 * 如果需要日志服务请从此接口获取一个日志实例
	 * 
	 * @param name 类名
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-5
	 */
	public static Logger getLogger(String name)
	{
		if (LoggingPlugin.getDefault() == null)
			return null;

		return Logger.getLogger(name);
	}

	/**
	 * 如果需要日志服务请从此接口获取一个日志实例
	 * 
	 * @param clazz
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public static Logger getLogger(Class<?> clazz)
	{

		return getLogger(clazz.getName());
	}

	/**
	 * 如果需要日志服务请从此接口获取一个日志实例
	 * 
	 * @param clazz
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public static Logger getConfigLevelLogger(Class<?> clazz)
	{
		if (LoggingPlugin.getDefault() == null)
			return null;
		Logger logger = Logger.getLogger(clazz.getName());
		logger.setLevel(Level.CONFIG);

		return logger;
	}

	@Override
	public void earlyStartup()
	{

	}

}