/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.eclipse.core.internal.runtime.IRuntimeConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.MD5Util;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ExecutionPreferencePage;
import com.coretek.spte.core.TestResultManager;
import com.coretek.spte.core.reference.ReferedCase;
import com.coretek.spte.core.reference.ReferingCase;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.ResultHandler;
import com.coretek.spte.monitor.command.handler.DoneHandler;
import com.coretek.spte.monitor.command.handler.HandlerRegistries;
import com.coretek.spte.monitor.command.handler.LogicErrorHandler;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.testcase.testResult.TestResultViewPart;

/**
 * ִ����ҵ
 * 
 * @author ���Ρ 2012-3-31
 */
public class ExecutionJob extends Job implements SelectionListener {
	private final static Logger logger = LoggingPlugin.getLogger(ExecutionJob.class);

	public final static String EXIT = "exit";

	public final static String RUN = "run";

	// ���ڱ�ʶִ�������Ƿ��Ѿ����
	private volatile boolean done = false;
	// ���ڱ�ʶִ��ʱ�Ƿ����
	private volatile boolean error = false;

	private ExecutorSession executorSession;

	private List<TestCase> queue;

	private List<CompareResult> testCaseResultList = new ArrayList<CompareResult>();

	private Map<String, List<CompareResult>> testRecordsMap = new HashMap<String, List<CompareResult>>();

	private IProgressMonitor monitor;

	/**
	 * @param name
	 */
	public ExecutionJob(String name, ExecutorSession session, List<TestCase> queue) {
		super("����ִ��ִ�в�������" + name);
		this.queue = Collections.unmodifiableList(queue);
		this.executorSession = session;
	}

	/**
	 * ���²��������б��е�ֹͣ��ť
	 * 
	 * @param eanbled
	 */
	private void updateStopBtn(final boolean enabled) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// �򿪲������������ͼ����չʾ��ǰ���
				IWorkbenchPage page = EclipseUtils.getActivePage();
				IViewPart viewPart = page.findView("com.coretek.tools.sequence.testcaseList");
				if (viewPart == null) {
					try {
						viewPart = page.showView("com.coretek.tools.sequence.testcaseList");
					} catch (PartInitException e) {
						LoggingPlugin.logException(logger, e);
					}
				}
				if (viewPart != null) {
					TestCaseViewPart tcvp = (TestCaseViewPart) viewPart;
					tcvp.enableStop(enabled);
					if (enabled) {
						tcvp.registerStopListener(ExecutionJob.this);
					} else {
						tcvp.cancelStopListener(ExecutionJob.this);
					}

				}
			}

		});
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		this.monitor.setCanceled(false);
		try {
			this.updateStopBtn(true);
			monitor.beginTask(this.getName(), IProgressMonitor.UNKNOWN);
			String timeString = StringUtils.getCurrentTime();
			for (TestCase testCase : queue) {
				try {
					Thread.sleep(2000);
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
					super.setName("����ִ��ִ�в�������" + testCase.getPath());
					String command = Utils.getExecutorCommand();
					ExecutorSession.setRunning(true);
					this.done = false;
					this.error = false;

					IFile testCaseFile = Utils.getCaseByName(testCase.getProjectName(), testCase.getSuitePath(), testCase.getCaseName());
					IProject project = testCaseFile.getProject();
					IFolder folder = Utils.getSuiteByName(project, testCase.getSuitePath());
					String casePath = testCaseFile.getLocation().toOSString();
					ClazzManager caseManger = TemplateEngine.getEngine().parseCase(testCaseFile.getLocation().toFile());
					Entity testCase1 = caseManger.getTestCase();

					// icd�ļ��ľ���·��
					String icdPath = TemplateUtils.getICDOfTestCase(testCase1);
					if (executorSession == null) {
						executorSession = ExecutorSession.getInstanceForExecutionOnly(icdPath);
					}
					if (executorSession == null) {
						logger.severe("Can not get the instance of MointorManager.");
						return Status.CANCEL_STATUS;
					}
					HandlerRegistries.addObserver(new DoneObserver(project, folder, testCaseFile, timeString, testCase1, icdPath, monitor), DoneHandler.class);
					HandlerRegistries.addObserver(new LogicErrorObserver(), LogicErrorHandler.class);

					try {
						String prjPath = testCaseFile.getProject().getFullPath().toFile().getAbsolutePath();
						if (executorSession.isNeedLaunchMonitor()) {
							logger.config("The use has set this execution process will launch Monitor.");
							MonitorEventManager.getMonitorEventManager().registerObserver();
							executorSession.launchBoth(command, casePath, "", "", prjPath);

						} else {
							logger.config("The user has set this execution process does not luanch Monitor.");
							executorSession.launchExecutor(command, casePath, "", "", prjPath);
						}

					} catch (TimeoutException e) {
						LoggingPlugin.logException(logger, e);
						error = true;
						done = true;
						try {
							ExecutorSession.shutDown();
							executorSession.shutDownOnError();
						} catch (TimeoutException e1) {
							executorSession.shutDownOnError();
						}
					}

					while (!done) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							LoggingPlugin.logException(logger, e);
						}
						if (monitor.isCanceled())// �û�ȡ��ִ��
						{
							return this.handleCancel();
						}
					}
				} catch (Exception e) {
					LoggingPlugin.logException(logger, e);
				} finally {
					// ÿִ��һ�β�����������Ҫ����JVMִ����������
					System.gc();
					executorSession = null;
				}
			}
		} catch (Exception e) {
			LoggingPlugin.logException(logger, e);
		} finally {
			// ��ֹֹͣ��ť
			this.updateStopBtn(false);
			executorSession = null;
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	/**
	 * ����ȡ������
	 * 
	 * @return
	 */
	private IStatus handleCancel() {
		ExecutorSession mm = ExecutorSession.getInstance();
		if (mm != null && ExecutorSession.isRunning()) {
			try {
				if (!ExecutorSession.shutDown()) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", "�ر�ִ����ʧ�ܣ�");
					return new Status(Status.ERROR, IRuntimeConstants.PI_RUNTIME, 1, "", null);
				} else {
					return Status.CANCEL_STATUS;
				}
			} catch (TimeoutException e1) {
				LoggingPlugin.logException(logger, e1);

			} finally {
				// ��ֹֹͣ��ť
				this.updateStopBtn(false);
				executorSession = null;
				ExecutorSession.dispose();
				ExecutorSession.reset();
				System.gc();
			}
		}
		return Status.CANCEL_STATUS;
	}

	/**
	 * ����ִ�������߼�����
	 * 
	 * @author ���Ρ 2012-4-11
	 */
	private class LogicErrorObserver implements Observer {
		public void update(Observable o, Object arg) {
			if (o instanceof LogicErrorHandler) {
				try {
					logger.warning("Get the error info from Executor, the received data will be discarded.");
					ExecutorSession.shutDown();

				} catch (TimeoutException e) {
					LoggingPlugin.logException(logger, e);
					executorSession.shutDownOnError();
				} finally {
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
	private class DoneObserver implements Observer {

		private IProject project;

		private IFolder folder;

		private IFile testCaseFile;

		private String timeString;

		private Entity testCase1;

		private String icdPath;

		private IProgressMonitor monitor;

		public DoneObserver(IProject project, IFolder folder, IFile testCaseFile, String timeString, Entity testCase1, String icdPath, IProgressMonitor monitor) {
			this.project = project;
			this.folder = folder;
			this.testCaseFile = testCaseFile;
			this.timeString = timeString;
			this.testCase1 = testCase1;
			this.icdPath = icdPath;
			this.monitor = monitor;
		}

		public void update(Observable arg0, Object arg1) {
			if (EXIT.equals(arg1) && error) {
				done = true;
			} else if (EXIT.equals(arg1) && !error && !done && !monitor.isCanceled()) {
				this.handleExitDone();
			} else if (RUN.equals(arg1)) {// �ڽ��յ���run���ص�done֮��Ӧ�÷���exit
				handleRunDone();
			}
		}

		private void handleExitDone() {
			while (true) {
				if (!executorSession.canShutDown()) {
					logger.info("The execution session can not be closed now.");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						LoggingPlugin.logException(logger, e);
					}
				} else {
					logger.info("The message cache already handled all of messages, now other operations can be executed.");
					break;
				}
			}

			this.hanleResult();

		}

		private void hanleResult() {
			try {
				this.monitor.subTask("�����������...");
				final ReferingCase refering = new ReferingCase(project, folder, testCaseFile);
				List<ReferedCase> list = new ArrayList<ReferedCase>(0);
				StringBuffer sb = new StringBuffer();
				for (ReferedCase unit : list) {
					sb.append(unit.getTestCase().getLocation().toFile().getAbsolutePath());
					sb.append(";");
				}
				TestResultManager.getInstance().saveTestCases(refering, timeString, TestResultManager.RESULT_FILE_NAME);

				TestResultManager.getInstance().saveDB(executorSession.getMsgDBPath(), timeString, TestResultManager.RESULT_FILE_NAME, testCaseFile);
				// ִ�в���������Ľ��
				CompareResult runTestCaseResult = null;
				this.monitor.subTask("�Ƚ�ִ�н��...");
				if (testCase1 instanceof com.coretek.spte.testcase.TestCase) {
					runTestCaseResult = ResultHandler.compareTestcaseWithDB((com.coretek.spte.testcase.TestCase) testCase1, executorSession.getMsgDBPath(), icdPath);
				}
				if (null != runTestCaseResult) {
					runTestCaseResult.setTestCaseFile(testCaseFile);
					String md5 = MD5Util.getMD5Digest(testCaseFile);
					TestResultManager.getInstance().saveResultFile(timeString, TestResultManager.RESULT_FILE_NAME, runTestCaseResult, md5);
					testCaseResultList.add(runTestCaseResult);
				}
				testRecordsMap.put(timeString, testCaseResultList);
				this.monitor.subTask("��ʾִ�н��...");
				this.showResult();

			} catch (Exception e) {
				LoggingPlugin.logException(logger, e);
			} finally {
				done = true;
				ExecutorSession.dispose();
				ExecutorSession.reset();
			}
		}

		private void showResult() {
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					// �򿪲������������ͼ����չʾ��ǰ���
					IWorkbenchPage page = EclipseUtils.getActivePage();
					IViewPart part = page.findView("com.coretek.tools.testResult.view");
					if (part != null) {
						try {
							page.showView("com.coretek.tools.testResult.view");
							((TestResultViewPart) part).refresh("ִ�н��");
						} catch (PartInitException e) {
							LoggingPlugin.logException(logger, e);
						}
					}
				}
			});
		}

		private void handleRunDone() {
			try {
				ExecutorSession.shutDown();
			} catch (TimeoutException e) {
				LoggingPlugin.logException(logger, e);

				ExecutorSession.dispose();
				ExecutorSession.reset();
				executorSession.shutDownOnError();
				error = true;
				done = true;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent) <br/> <b>Author</b> SunDawei </br>
	 * <b>Date</b> 2012-9-3
	 */
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b>
	 * 2012-9-3
	 */
	public void widgetSelected(SelectionEvent e) {
		ToolItem item = (ToolItem) e.getSource();
		if (!item.isEnabled()) {
			return;
		}

		boolean noMoreConfirm = CfgPlugin.getDefault().getPreferenceStore().getBoolean(ExecutionPreferencePage.STOP_CONFIRM);
		if (!noMoreConfirm) {
			StopConfirmDialog dialog = new StopConfirmDialog(Display.getCurrent().getActiveShell());
			if (Window.OK == dialog.open()) {
				CfgPlugin.getDefault().getPreferenceStore().setValue(ExecutionPreferencePage.STOP_CONFIRM, dialog.isNoMoreConfirm());
			} else {
				return;
			}
		}
		this.monitor.setCanceled(true);
		item.setEnabled(false);
	}
}