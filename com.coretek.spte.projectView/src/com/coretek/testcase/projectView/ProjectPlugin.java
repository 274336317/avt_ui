package com.coretek.testcase.projectView;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.coretek.testcase.projectView.utils.ResourceChangeListener;
import com.coretek.tools.ide.internal.ui.UIPluginImages;

/**
 * The UIPlugin class controls the plug-in life cycle
 * 
 * @date 10.09.06
 */
public class ProjectPlugin extends AbstractUIPlugin
{

	// The plug-in ID
	public static final String		PLUGIN_ID	= "com.coretek.spte.projectView";	// "com.coretek.tools.ide.ui";

	// The View ID
	public static final String		VIEW_ID		= PLUGIN_ID + ".DiagramView";

	// The shared instance
	private static ProjectPlugin	plugin;

	private ResourceChangeListener	listener;

	/**
	 * The constructor
	 */
	public ProjectPlugin()
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
		listener = new ResourceChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
		// 将ICD文件编辑器注册，以便在项目打开的时候就能使用该编辑器
		PlatformUI.getWorkbench().getEditorRegistry().setDefaultEditor("*.xml", "ar.com.tadp.xmleditor.basic");
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
		UIPluginImages.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ProjectPlugin getDefault()
	{
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Returns the standard display to be used. The method first checks, if the
	 * thread calling this method has an associated display. If so, this display
	 * is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay()
	{
		Display display = Display.getCurrent();
		if (display == null)
		{
			display = Display.getDefault();
		}
		return display;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow()
	{
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * ?Message View
	 * 
	 * @param viewId
	 */
	public void activeViewAndBringToTop(final String viewId)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();
		if (display.isDisposed())
			return;
		display.syncExec(new Runnable()
		{
			public void run()
			{
				IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
				if (activeWorkbenchWindow == null)
					return;
				IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
				if (page == null)
					return;
				IViewPart part = page.findView(viewId);
				if (part == null)
				{
					try
					{
						page.showView(viewId);
					} catch (PartInitException e)
					{
						e.printStackTrace();
					}
				} else
				{
					page.bringToTop(part);
				}
			}
		});
	}
}
