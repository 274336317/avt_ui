package com.coretek.common.utils.widget;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DialogUtils
{

	public static void openWindow()
	{
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		FloatWindow w = new FloatWindow(ww.getShell());
		w.setBlockOnOpen(false);
		w.setShellStyle(SWT.BORDER);
		w.open();
	}

}
