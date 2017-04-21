/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MsgWatchViewPlugin extends AbstractUIPlugin
{

	// The shared instance
	private static MsgWatchViewPlugin	plugin;

	// The plug-in ID
	public static final String			PLUGIN_ID	= "com.coretek.spte.monitor.watchView";

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MsgWatchViewPlugin getDefault()
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
	 * The constructor
	 */
	public MsgWatchViewPlugin()
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
