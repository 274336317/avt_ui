package com.coretek.spte.core;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.coretek.spte.core.util.CloseCaseEditorWhenICDChangedListener;
import com.coretek.spte.core.util.CloseEditorResourceChangeListener;
import com.coretek.spte.core.views.MessageView;

/**
 * The main plugin class to be used in the desktop.
 */
public class SPTEPlugin extends AbstractUIPlugin
{

	public static final String PLUGIN_ID = "com.coretek.spte.core"; //$NON-NLS-1$

	// The shared instance.
	private static SPTEPlugin plugin;

	private IResourceChangeListener listener = new CloseEditorResourceChangeListener();

	private IResourceChangeListener listener2 = new CloseCaseEditorWhenICDChangedListener();

	/**
	 * The constructor.
	 */
	public SPTEPlugin()
	{
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(listener);
		workspace.addResourceChangeListener(listener2);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(listener);
		workspace.removeResourceChangeListener(listener2);
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static SPTEPlugin getDefault()
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

	public static Shell getActiveWorkbenchShell()
	{
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null)
		{
			return window.getShell();
		}
		return null;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow()
	{
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * ÖÃÇ°Message View
	 * 
	 * @param viewId
	 */
	public void activeViewAndBringToTop(final String viewId)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();
		if (display.isDisposed())
		{
			return;
		}
		display.syncExec(new Runnable()
		{

			public void run()
			{
				IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
				if (activeWorkbenchWindow == null)
				{
					return;
				}
				IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
				if (page == null)
				{
					return;
				}
				IViewPart part = page.findView(viewId);
//				if (part instanceof MessageView)
//				{
//					MessageView m_MessageView = (MessageView) part;
//					System.out.println("===>>>" + m_MessageView.getPartName());
//				}
				//System.out.println("===>>>" + part.getTitle() +"\t"+ part.getTitleImage());
				if (part == null)
				{
					try
					{
						page.showView(viewId);
					}
					catch (PartInitException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					page.bringToTop(part);
				}
			}
		});
	}

}
