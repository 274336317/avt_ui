/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �ڲ������ʱ������ҵ�ǰ�����ռ��е�.metadataĿ¼�µ�Log�ļ������Ƿ����CoretekLog.properties�ļ���
 * ��������ڣ��򴴽�����������֮���û��Ϳ��Ը�����Ҫ�޸���־����Լ���־�������ز�����
 * 
 * @author ���Ρ 2011-12-5
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
			 * ��־�����ļ���Ĭ�ϴӵ�ǰ�����CoretekLog.properties�������û���workspace���棬
			 * �û����л�workspace֮��ϵͳӦ����CoretekLog.properties����һ�ݵ��û��ĵ�ǰworkspace���档
			 * ÿ�������˲��ʱ����Ҫ������Щ��顣
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
				// ������־�����ļ�
				file.createNewFile();
				if (in != null)
				{
					// �����־�����ļ������ڣ��򴴽������ӵ�ǰ����µ�CoretekLog.properties�ļ�����
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
					// ��������Ϣд���ļ���
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
		// ��ʼ����־
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration(in);
		if (in != null)
		{
			in.close();
		}
		this.logListener = new LogListener();
		// ����eclipseƽ̨����������־
		RuntimeLog.addLogListener(this.logListener);
	}

	/**
	 * ���쳣��¼����־��
	 * 
	 * @param logger
	 * @param throwable </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-14
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
	 * �����Ҫ��־������Ӵ˽ӿڻ�ȡһ����־ʵ��
	 * 
	 * @param name ����
	 * @param level ��־����
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-5
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
	 * �����Ҫ��־������Ӵ˽ӿڻ�ȡһ����־ʵ��
	 * 
	 * @param clazz
	 * @param level ��־����
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-9
	 */
	public static Logger getLogger(Class<?> clazz, Level level)
	{

		return getLogger(clazz.getName(), level);
	}

	/**
	 * �����Ҫ��־������Ӵ˽ӿڻ�ȡһ����־ʵ��
	 * 
	 * @param name ����
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-5
	 */
	public static Logger getLogger(String name)
	{
		if (LoggingPlugin.getDefault() == null)
			return null;

		return Logger.getLogger(name);
	}

	/**
	 * �����Ҫ��־������Ӵ˽ӿڻ�ȡһ����־ʵ��
	 * 
	 * @param clazz
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-9
	 */
	public static Logger getLogger(Class<?> clazz)
	{

		return getLogger(clazz.getName());
	}

	/**
	 * �����Ҫ��־������Ӵ˽ӿڻ�ȡһ����־ʵ��
	 * 
	 * @param clazz
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-9
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