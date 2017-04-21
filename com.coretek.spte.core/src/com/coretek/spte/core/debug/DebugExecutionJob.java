/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.MD5Util;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.TestResultManager;
import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.core.reference.ReferingCase;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.ResultHandler;
import com.coretek.spte.monitor.command.handler.DoneHandler;
import com.coretek.spte.monitor.command.handler.HandlerRegistries;
import com.coretek.spte.monitor.command.handler.LogicErrorHandler;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * ����ִ����ҵ
 * 
 * @author ���Ρ 2012-3-31
 */
public class DebugExecutionJob extends Job
{

	private final static Logger					logger				= LoggingPlugin.getLogger(DebugExecutionJob.class);

	// ���ڱ�ʶִ�������Ƿ��Ѿ����
	private volatile boolean					done				= false;
	// ���ڱ�ʶִ��ʱ�Ƿ����
	private volatile boolean					error				= false;

	private ExecutorSession						executorSession;

	private List<CompareResult>					testCaseResultList	= new ArrayList<CompareResult>();

	private Map<String, List<CompareResult>>	testRecordsMap		= new HashMap<String, List<CompareResult>>();

	private IFile								testCaseFile;

	private String								startUUID;

	private String								endUUID;

	private DebugActionsGroup					actionsGroup;

	/**
	 * @param name </br>
	 */
	public DebugExecutionJob(ExecutorSession session, IFile testCaseFile, String startUUID, String endUUID, DebugActionsGroup actionsGroup)
	{
		super(Messages.getString("DEBUG_TESTCASE") + testCaseFile.getName());
		this.executorSession = session;
		this.testCaseFile = testCaseFile;
		this.startUUID = startUUID;
		this.endUUID = endUUID;
		this.actionsGroup = actionsGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
	 * IProgressMonitor) <br/>
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask(this.getName(), IProgressMonitor.UNKNOWN);
		String timeString = StringUtils.getCurrentTime();

		if (monitor.isCanceled())
			return Status.CANCEL_STATUS;
		super.setName(Messages.getString("DEBUG_TESTCASE") + this.testCaseFile.getLocation().toFile().getAbsolutePath());
		String command = Utils.getExecutorCommand();
		ExecutorSession.setRunning(true);
		this.done = false;
		this.error = false;

		IProject project = testCaseFile.getProject();
		IFolder folder = (IFolder) this.testCaseFile.getParent();
		String casePath = testCaseFile.getLocation().toOSString();
		monitor.subTask("���ڽ�����������...");
		ClazzManager caseManger = TemplateEngine.getEngine().parseCase(testCaseFile.getLocation().toFile());
		Entity testCase1 = caseManger.getTestCase();

		// icd�ļ��ľ���·��
		String icdPath = TemplateUtils.getICDOfTestCase(testCase1);
		executorSession = ExecutorSession.getInstanceForExecutionOnly(icdPath);
		if (executorSession == null)
		{
			logger.severe("�޷���ȡExecutorSession��ʵ��");
			return Status.CANCEL_STATUS;
		}
		HandlerRegistries.addObserver(new DoneObserver(project, folder, testCaseFile, timeString, testCase1, icdPath, monitor), DoneHandler.class);
		HandlerRegistries.addObserver(new LogicErrorObserver(), LogicErrorHandler.class);
		monitor.subTask("����ִ����...");
		try
		{
			String prjPath = testCaseFile.getProject().getFullPath().toFile().getAbsolutePath();
			executorSession.launchExecutor(command, casePath, this.startUUID, this.endUUID, prjPath);

		} catch (TimeoutException e)
		{
			LoggingPlugin.logException(logger, e);
			error = true;
			done = true;
			try
			{
				ExecutorSession.shutDown();
				executorSession.shutDownOnError();
			} catch (TimeoutException e1)
			{
				executorSession.shutDownOnError();
			}
		}
		monitor.subTask("ִ��������������Ϣ...");
		while (!done)
		{
			try
			{
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				LoggingPlugin.logException(logger, e);
			}
		}

		System.gc();
		executorSession = null;
		monitor.done();

		return Status.OK_STATUS;
	}

	/**
	 * ����ִ�������߼�����
	 * 
	 * @author ���Ρ 2012-4-11
	 */
	private class LogicErrorObserver implements Observer
	{
		public void update(Observable o, Object arg)
		{
			if (o instanceof LogicErrorHandler)
			{
				try
				{
					logger.warning("������ִ���������Ĵ�����Ϣ�����ν�����ִ�������������ݡ�");
					ExecutorSession.shutDown();
				} catch (TimeoutException e)
				{
					LoggingPlugin.logException(logger, e);
					executorSession.shutDownOnError();
				} finally
				{
					error = true;
				}
			}
		}
	}

	/**
	 * ���������ִ�н������
	 * 
	 * @author ���Ρ 2012-3-31
	 */
	private class DoneObserver implements Observer
	{

		private IProject			project;

		private IFolder				folder;

		private IFile				testCaseFile;

		private String				timeString;

		private Entity				testCase1;

		private String				icdPath;

		private IProgressMonitor	monitor;

		public DoneObserver(IProject project, IFolder folder, IFile testCaseFile, String timeString, Entity testCase1, String icdPath, IProgressMonitor monitor)
		{
			this.project = project;
			this.folder = folder;
			this.testCaseFile = testCaseFile;
			this.timeString = timeString;
			this.testCase1 = testCase1;
			this.icdPath = icdPath;
			this.monitor = monitor;
		}

		public void update(Observable arg0, Object arg1)
		{
			if ("exit".equals(arg1) && error)
			{
				done = true;
			} else if ("exit".equals(arg1) && !error && !done)
			{
				this.handleExitDone();
			} else if ("run".equals(arg1))
			{// �ڽ��յ���run���ص�done֮��Ӧ�÷���exit
				handleRunDone();
			}
		}

		private void handleExitDone()
		{
			while (true)
			{
				if (!executorSession.canShutDown())
				{
					logger.info("���ڻ����ܹر�ִ�����Ự��");
					try
					{
						Thread.sleep(2000);
					} catch (InterruptedException e)
					{
						LoggingPlugin.logException(logger, e);
					}
				} else
				{
					logger.info("��Ϣ�����Ѿ��������������Ϣ�����ڿ���ִ������������");
					break;
				}
			}

			try
			{
				this.monitor.subTask("����ִ�н��...");
				ReferingCase refering = new ReferingCase(project, folder, testCaseFile);
				TestResultManager.getInstance().saveTestCases(refering, timeString, TestResultManager.DEBUG_FILE_NAME);
				TestResultManager.getInstance().saveDB(executorSession.getMsgDBPath(), timeString, TestResultManager.DEBUG_FILE_NAME, testCaseFile);
				// ִ�в���������Ľ��
				CompareResult runTestCaseResult = null;

				if (testCase1 instanceof com.coretek.spte.testcase.TestCase)
				{
					runTestCaseResult = ResultHandler.compareTestcaseWithDB4Debug((com.coretek.spte.testcase.TestCase) testCase1, startUUID, endUUID, executorSession.getMsgDBPath(), icdPath);
				}
				if (null != runTestCaseResult)
				{
					runTestCaseResult.setTestCaseFile(testCaseFile);
					String md5 = MD5Util.getMD5Digest(testCaseFile);
					TestResultManager.getInstance().saveResultFile(timeString, TestResultManager.DEBUG_FILE_NAME, runTestCaseResult, md5);
					testCaseResultList.add(runTestCaseResult);
				}
				testRecordsMap.put(timeString, testCaseResultList);
				this.monitor.subTask("��ʾִ�н��...");
				this.showResult();

			} catch (Exception e)
			{
				LoggingPlugin.logException(logger, e);
			} finally
			{
				done = true;
				ExecutorSession.dispose();
				ExecutorSession.reset();
				actionsGroup.setEnable(true);
			}
		}

		private void showResult()
		{
			Display.getDefault().syncExec(new Runnable()
			{

				public void run()
				{
					// �򿪲������������ͼ����չʾ��ǰ���
					IWorkbenchPage page = EclipseUtils.getActivePage();
					if (page.findView("com.coretek.tools.testResult.view") == null)
					{
						try
						{
							page.showView("com.coretek.tools.testResult.view");
						} catch (PartInitException e)
						{
							e.printStackTrace();
						}
					}
				}
			});
		}

		private void handleRunDone()
		{
			try
			{
				ExecutorSession.shutDown();
			} catch (TimeoutException e)
			{
				LoggingPlugin.logException(logger, e);

				ExecutorSession.dispose();
				ExecutorSession.reset();
				executorSession.shutDownOnError();
				error = true;
				done = true;
			}
		}

	}
}