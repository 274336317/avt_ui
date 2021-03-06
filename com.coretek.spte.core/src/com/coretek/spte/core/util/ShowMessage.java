package com.coretek.spte.core.util;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class ShowMessage
{
	static public final boolean	DEBUG	= false;

	/**
	 * Prints a line of text and quit.
	 */
	static public void printError(String s)
	{
		System.out.println(s);
	}

	/**
	 * Prints a message in an info dialog box.
	 */
	static public void printMessage(String msg)
	{
		MessageBox m = new MessageBox(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.ICON_WARNING);
		m.setText("警告");
		m.setMessage(msg);
		m.open();
	}

	/**
	 * Prints a message in an info dialog box.
	 */
	static public void printAbout(String msg)
	{
		MessageBox m = new MessageBox(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.ICON_INFORMATION);
		m.setText("消息");
		m.setMessage(msg);
		m.open();
	}

	/**
	 * Prints a message in an info dialog box.
	 */
	static public boolean printQuestion(String msg)
	{
		MessageBox m = new MessageBox(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.OK | SWT.CANCEL);
		m.setText("选择");
		m.setMessage(msg);

		if (m.open() == SWT.OK)
			return true;

		return false;
	}
}
