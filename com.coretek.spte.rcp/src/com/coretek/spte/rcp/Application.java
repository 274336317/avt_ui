package com.coretek.spte.rcp;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication
{

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception
	{
		IDE.registerAdapters();
		Display display = PlatformUI.createDisplay();

		try
		{
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode != PlatformUI.RETURN_RESTART)
			{
				return IApplication.EXIT_OK;
			}
			return IApplication.EXIT_RELAUNCH.equals(Integer.getInteger("eclipse.exitcode")) ? IApplication.EXIT_RELAUNCH : IApplication.EXIT_RESTART;
		} finally
		{
			if (display != null)
			{
				display.dispose();
			}
			Location instanceLoc = Platform.getInstanceLocation();
			if (instanceLoc != null)
				instanceLoc.release();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop()
	{
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable()
		{
			public void run()
			{
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

}
