package com.coretek.spte.monitor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.common.service.IPaintService;

/**
 * The activator class controls the plug-in life cycle
 */
public class SequencePlugin extends AbstractUIPlugin implements IStartup
{

	// The plug-in ID
	public static final String		PLUGIN_ID	= "com.coretek.spte.result.sequence";

	// The shared instance
	private static SequencePlugin	plugin;

	private BundleContext			context;

	private ServiceRegistration		registration;

	/**
	 * The constructor
	 */
	public SequencePlugin()
	{
	}

	public Image getImage(String path)
	{
		Image image = this.getImageRegistry().get(path);
		if (image == null)
		{
			ImageDescriptor id = ImageDescriptor.createFromFile(SequencePlugin.class, path);
			image = id.createImage();

			this.getImageRegistry().put(path, image);
		}

		return image;
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
		registration.unregister();
		super.stop(context);

	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static SequencePlugin getDefault()
	{
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup()
	{
		Display.getDefault().syncExec(new Runnable()
		{

			@Override
			public void run()
			{
				SequenceViewPart viewPart = (SequenceViewPart) EclipseUtils.openView("com.coretek.spte.monitor.monitorView");
				SequencePlugin.getDefault().registration = SequencePlugin.getDefault().context.registerService(IPaintService.class.getName(), viewPart, null);

			}

		});
	}

}