package com.coretek.spte.monitor.manager;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MonitorManagerPlugin extends AbstractUIPlugin implements IStartup
{

	// The plug-in ID
	public static final String			PLUGIN_ID	= "com.coretek.spte.monitor.manager";

	// The shared instance
	private static MonitorManagerPlugin	plugin;

	/**
	 * The constructor
	 */
	public MonitorManagerPlugin()
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
	 * )
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MonitorManagerPlugin getDefault()
	{
		return plugin;
	}

	@Override
	public void earlyStartup()
	{
		// TODO Auto-generated method stub

	}

}
