package com.coretek.spte.console;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * �Զ������̨
 * 
 * @author ���Ρ
 * @date 2010-12-17
 */
public class ConsoleFactory implements IConsoleFactory
{

	private static MessageConsole console = new MessageConsole("����̨", null);

	public void openConsole()
	{
		showConsole();
		System.out.println("����̨������");
	}

	public static void showConsole()
	{
		try
		{
			if (console != null)
			{
				// ��������ʾconsole
				IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
				IConsole[] existings = manager.getConsoles();
				boolean exists = false;
				for (IConsole existing : existings)
				{
					if (console == existing)
					{
						exists = true;
					}
				}
				if (!exists)
				{
					manager.addConsoles(new IConsole[]
					{ console });
				}
				manager.showConsoleView(console);
				//����console��
				MessageConsoleStream stream = console.newMessageStream();
				stream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				System.setOut(new PrintStream(stream));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void closeConsole()
	{
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		if (console != null)
		{
			manager.removeConsoles(new IConsole[]
			{ console });
		}
	}

	public static MessageConsole getConsole()
	{
		return console;
	}

}
