package com.coretek.common.template;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TemplateEnginePlugin extends AbstractUIPlugin implements IStartup
{

	// The plug-in ID
	public static final String			PLUGIN_ID	= "com.coretek.common.template";

	// The shared instance
	private static TemplateEnginePlugin	plugin;

	/**
	 * The constructor
	 */
	public TemplateEnginePlugin()
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
	public static TemplateEnginePlugin getDefault()
	{
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup() <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2012-2-29
	 */
	@Override
	public void earlyStartup()
	{
		// TODO Auto-generated method stub

	}

}
