package com.coretek.spte.console;

import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

public class ConsolePageParticipant implements IConsolePageParticipant
{

	public void activated()
	{
		System.out.println("===>>>true");
	}

	public void deactivated()
	{
		System.out.println("===>>>true");
	}

	public void dispose()
	{
		System.out.println("===>>>true");
	}

	public void init(IPageBookViewPage page, IConsole console)
	{
		System.out.println("===>>>true");
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter)
	{
		System.out.println("===>>>true");
		return null;
	}

}
