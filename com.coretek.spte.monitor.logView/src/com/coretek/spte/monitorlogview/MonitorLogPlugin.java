/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author ���� 2012-3-14
 */
public class MonitorLogPlugin extends AbstractUIPlugin
{

	// The shared instance
	/**
	 * 
	 */
	private static MonitorLogPlugin	plugin;

	// The plug-in ID
	/**
	 * 
	 */
	public static final String		PLUGIN_ID	= "com.coretek.spte.monitor.logView";

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	/**
	 * @return </br>
	 */
	public static MonitorLogPlugin getDefault()
	{
		return plugin;
	}

	/**
	 * @param path
	 * @return </br>
	 */
	public static ImageDescriptor getImageDescriptor(String path)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * </br>
	 */
	public MonitorLogPlugin()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * ) <br/>
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * ) <br/>
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}
}
