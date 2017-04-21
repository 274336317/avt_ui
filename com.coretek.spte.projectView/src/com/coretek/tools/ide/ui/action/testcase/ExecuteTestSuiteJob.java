package com.coretek.tools.ide.ui.action.testcase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;

/**
 * ִ�в�������Job
 * 
 * @author Administrator
 * @date 2010.10.21
 */
public class ExecuteTestSuiteJob extends Job
{

	/*
	 * ����̨����������
	 */
	private static final String CONSOLE_NAME = "ִ�в�������";

	/*
	 * �ܵĽ�����������
	 */
	private static final int TOTAL_WORK = 100;

	/*
	 * ���Լ��ļ���
	 */
	private IFolder folder = null;

	/*
	 * make.exe·��
	 */
	private String makePath = "";

	public ExecuteTestSuiteJob(IFolder folder)
	{
		super(CONSOLE_NAME);
		this.folder = folder;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask(CONSOLE_NAME, TOTAL_WORK);
		monitor.subTask(CONSOLE_NAME);
		monitor.worked(10);// 10%
		// ����makefile�ļ�
		generateMakefile();

		monitor.subTask("����" + makePath.toString());//$NON-NLS-1$
		monitor.worked(10);

		if (monitor.isCanceled())
		{
			monitor.done();
			return Status.CANCEL_STATUS;
		}

		String command = makePath + " all -C " + this.folder.getProject().getLocation().toOSString();
		try
		{
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			IOConsole ioConsole = getConsole();
			ioConsole.clearConsole();
			String line = reader.readLine();
			while (line != null)
			{
				if (!line.startsWith("make:"))
				{
					ioConsole.newOutputStream().write(line + "\n");
				}
				line = reader.readLine();
			}
			process.waitFor();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return Status.CANCEL_STATUS;
	}

	/**
	 * ��ȡ����̨,����ǰ
	 * 
	 * @return IOConsole
	 */
	private IOConsole getConsole()
	{
		IOConsole ioConsole = null;
		for (IConsole console : ConsolePlugin.getDefault().getConsoleManager().getConsoles())
		{
			if (console.getName().equals(CONSOLE_NAME))
			{
				ioConsole = (IOConsole) console;
			}
		}
		if (ioConsole == null)
		{
			ioConsole = new IOConsole(CONSOLE_NAME, null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]
			{ ioConsole });
		}

		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(ioConsole);

		return ioConsole;
	}

	/**
	 * ����makefile�ļ�
	 */
	private void generateMakefile()
	{
		URL installURL = Platform.getInstallLocation().getURL();
		IPath paltFormPath = new Path(installURL.getFile()).removeTrailingSeparator();
		makePath = paltFormPath.toOSString() + "\\cygwin\\make";

		// makefile�ļ�����
		StringBuffer makeFileContent = new StringBuffer();
		makeFileContent.append("ALL += executionManager");
		makeFileContent.append("\n");
		makeFileContent.append("ICDS_PATH = " + this.folder.getProject().getFile("icd").getLocation().toOSString());
		makeFileContent.append("\n");
		makeFileContent.append("CAS_PATH = " + this.folder.getLocation().toOSString());
		makeFileContent.append("\n");
		makeFileContent.append("BIN_PATH = " + paltFormPath.toOSString() + "\\cygwin");
		makeFileContent.append("\n");
		makeFileContent.append("# All Target");
		makeFileContent.append("\n");
		makeFileContent.append("all: $(ALL)");
		makeFileContent.append("\n");
		makeFileContent.append("\n");

		makeFileContent.append("executionManager:");
		makeFileContent.append("\n");
		makeFileContent.append("\t");
		makeFileContent.append("@$(BIN_PATH)\\executionManager.exe $(ICDS_PATH) $(CAS_PATH)");
		makeFileContent.append("\n");

		File makefile = new File(this.folder.getProject().getLocation().toOSString() + "/makefile");
		try
		{
			FileOutputStream stream = new FileOutputStream(makefile);
			stream.write(makeFileContent.toString().getBytes());

			this.folder.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		catch (FileNotFoundException e)
		{

			e.printStackTrace();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
		catch (CoreException e)
		{

			e.printStackTrace();
		}
	}
}
