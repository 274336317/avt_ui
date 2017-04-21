/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author 尹军 2012-3-14
 */
public class CurveViewPlugin extends AbstractUIPlugin
{

	private static CurveViewPlugin	plugin;

	public static final String		PLUGIN_ID	= "com.coretek.spte.monitor.curve";

	public static CurveViewPlugin getDefault()
	{
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public CurveViewPlugin()
	{
	}

	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}
}
