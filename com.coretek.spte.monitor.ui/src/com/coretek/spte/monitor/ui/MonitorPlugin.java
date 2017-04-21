/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.common.service.IPaintService;

/**
 * The activator class controls the plug-in life cycle
 */
/**
 * @author 尹军 2012-3-14
 */
public class MonitorPlugin extends AbstractUIPlugin implements IStartup
{

	/**
	 * The shared instance
	 */
	private static MonitorPlugin	plugin;

	private BundleContext			context;

	private IPaintService			paintService;

	/**
	 * The plug-in ID
	 */
	public static final String		PLUGIN_ID	= "com.coretek.spte.monitor.ui";

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MonitorPlugin getDefault()
	{
		return plugin;
	}

	/**
	 * @return the paintService
	 */
	public IPaintService getPaintService()
	{
		if (this.paintService == null)
		{
			Display.getCurrent().syncExec(new Runnable()
			{

				@Override
				public void run()
				{
					EclipseUtils.openView("com.coretek.spte.monitor.monitorView");
				}

			});

			ServiceReference sr = context.getServiceReference(IPaintService.class.getName());
			this.paintService = (IPaintService) context.getService(sr);
		}

		return this.paintService;
	}

	/**
	 * The constructor
	 */
	/**
	 * </br>
	 */
	public MonitorPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * ) <br/>
	 */
	public void start(final BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
		this.context = context;
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

	@Override
	public void earlyStartup()
	{
		// TODO Auto-generated method stub

	}
}
