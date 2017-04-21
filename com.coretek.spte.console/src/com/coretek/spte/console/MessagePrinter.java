package com.coretek.spte.console;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * ������̨�������Ϣ
 * 
 * @author ���Ρ
 * @date 2010-12-17
 */
public class MessagePrinter
{

	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * ���ִ�л����������Ϣ������̨��
	 * 
	 * @param str
	 */
	public static void printEEInfo(final String str)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.orange);
					stream.println(getTime() + " ִ�л��������" + str.toString());
					// stream.println(str);
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param str
	 */
	public static void printError(final String str)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.red);
					stream.println();
					stream.println(getTime() + " ������Ϣ��" + str.toString());
					// stream.println(str);
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public static void printError(final String contents, final String header)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.red);
					if (header != null)
						stream.println();
					stream.println(getTime() + header + contents.toString());
					// stream.println(contents);
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * �����Ϣ
	 * 
	 * @param str
	 */
	public static void printInfo(final String str)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.blue);
					stream.println();
					stream.println(getTime() + " ��Ϣ��" + str.toString());
					// stream.println(str);
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		});
	}

	public static void showConsole()
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				ConsoleFactory.showConsole();
			}
		});
	}

	public static void printInfo(final String contents, final String header)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				// ConsoleFactory.showConsole();
				MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
				stream.setColor(ColorConstants.blue);
				if (header != null)
					stream.println();
				stream.println(getTime() + header + contents);
				// stream.println(contents);
				try
				{
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param str
	 */
	public static void printDebug(final String str)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.darkGreen);
					stream.println();
					stream.println(getTime() + " ������Ϣ��" + str.toString());
					// stream.println(str.toString());
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public static void printDebug(final String str, final String header)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					// ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.darkGreen);
					if (header != null)
						stream.println(getTime() + header + str.toString());
					// stream.println(str.toString());
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param str
	 */
	public static void printWaring(final String str)
	{
		Display.getDefault().asyncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					ConsoleFactory.showConsole();
					MessageConsoleStream stream = ConsoleFactory.getConsole().newMessageStream();
					stream.setColor(ColorConstants.orange);
					stream.println();
					stream.println(getTime() + " ������Ϣ��" + str.toString());
					// stream.println(str.toString());
					stream.flush();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public static String getTime()
	{
		return format.format(new Date());
	}
}
