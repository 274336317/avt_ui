/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testResult;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.TestResultManager;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.ResultHandler;
import com.coretek.spte.monitor.SequenceViewPart;
import com.coretek.spte.monitor.SequenceBuilderImpl;
import com.coretek.spte.monitor.WaterFallSequence;
import com.coretek.spte.testcase.TestCase;

/**
 * ������ͼ�д�ִ�н��
 * 
 * @author ���Ρ 2012-3-31
 */
public class OpenResultJob extends Job
{

	private String			timeString;

	private CompareResult	compareResult;

	private boolean			isDebug;		// ���ڱ�ʶ�Ƿ��Ǵ򿪵��Խ��

	public OpenResultJob(String name, String timeString, CompareResult compareResult, boolean debug)
	{
		super(name);
		this.timeString = timeString;
		this.compareResult = compareResult;
		this.isDebug = debug;
	}

	/**
	 * ��ȡ����������·��
	 * 
	 * @return </br>
	 */
	private IFile getOldCaseFile()
	{
		IFile testcaseFile = compareResult.getTestCaseFile();
		IPath path = testcaseFile.getFullPath();

		String[] segments = path.segments();
		StringBuilder sb = new StringBuilder();
		String projectName = null;
		String caseName = null;
		for (int i = 0; i < segments.length; i++)
		{
			if (i == 0)
			{
				projectName = segments[i];
			} else if (i == segments.length - 1)
			{
				caseName = segments[i];
				sb.append(File.separator);
				sb.append(segments[i].substring(0, segments[i].lastIndexOf(".")));
				sb.append(File.separator);
			} else
			{
				sb.append(File.separator);
				sb.append(segments[i]);
			}
		}
		if (StringUtils.isNull(projectName))
		{
			return null;
		}
		StringBuilder builder = new StringBuilder(projectName);
		builder.append("/");
		if (this.isDebug)
		{
			builder.append(TestResultManager.DEBUG_FILE_NAME);
		} else
		{
			builder.append(TestResultManager.RESULT_FILE_NAME);
		}

		builder.append("/");
		builder.append(timeString);
		builder.append(sb.toString());
		builder.append(caseName);

		IPath oldCasePath = new Path(builder.toString());

		IFile oldCaseFile = ResourcesPlugin.getWorkspace().getRoot().getFile(oldCasePath);

		return oldCaseFile;
	}

	/**
	 * ������������
	 * 
	 * @param caseFile
	 * @return </br>
	 */
	private ClazzManager getCaseManager(IFile caseFile)
	{
		File file = caseFile.getLocation().toFile();
		return TemplateEngine.getEngine().parseCase(file);
	}

	/**
	 * ��ȡICD�ļ��ľ���·��
	 * 
	 * @param tc
	 * @return </br>
	 */
	private String getICDPath(com.coretek.spte.testcase.TestCase tc)
	{
		String icdPath = TemplateUtils.getICDOfTestCase(tc);

		return icdPath;
	}

	private com.coretek.spte.testcase.TestCase getOldTestCase(ClazzManager caseManager)
	{
		Entity entity = caseManager.getTestCase();
		com.coretek.spte.testcase.TestCase tc = null;
		if (entity instanceof com.coretek.spte.testcase.TestCase)
		{
			tc = (com.coretek.spte.testcase.TestCase) entity;
		}

		return tc;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask("��ִ�н��", 100);

		IFile oldCaseFile = this.getOldCaseFile();
		ClazzManager oldCaseManager = this.getCaseManager(oldCaseFile);
		if (oldCaseManager == null)
		{
			return Status.CANCEL_STATUS;
		}
		// FIXME ΪʲôҪ�Ƚ����ν����
		com.coretek.spte.testcase.TestCase tc = this.getOldTestCase(oldCaseManager);
		String icdPath = this.getICDPath(tc);
		compareResult.setIcdPath(icdPath);
		monitor.worked(10);
		monitor.subTask("�����ݿ��л�ȡִ�м�¼...");
		ClazzManager caseManager = TemplateEngine.getEngine().parseCase(compareResult.getTestCaseFile().getLocation().toFile());
		monitor.worked(10);
		CompareResult cr = ResultHandler.compareTestcaseWithDB((com.coretek.spte.testcase.TestCase) caseManager.getTestCase(), compareResult.getDBPath(), compareResult.getIcdPath());
		monitor.worked(10);
		ClazzManager icdManager = TemplateEngine.getEngine().parseICD(new File(icdPath));
		monitor.worked(10);
		List<Entity> testedObjects = TemplateUtils.getTestedObjectsOfICD(icdManager.getFighter(), ((com.coretek.spte.testcase.TestCase) caseManager.getTestCase()).getTestedObjects());
		monitor.worked(5);
		monitor.subTask("�Ƚ�ִ�н��...");
		compareResult = ResultHandler.compareTestcaseWithDB(tc, compareResult.getDBPath(), icdPath);
		compareResult.setTestCaseFile(oldCaseFile);
		monitor.worked(5);
		monitor.subTask("������Ⱦִ�н��...");
		this.showSequenceView(cr, icdManager, caseManager, testedObjects, tc);
		monitor.done();

		return Status.OK_STATUS;
	}

	/**
	 * �������ʾ���������ͼ��
	 * 
	 * @param cr
	 * @param icdManager
	 * @param caseManager
	 * @param testedObjects
	 * 
	 */
	private void showSequenceView(final CompareResult cr, final ClazzManager icdManager, final ClazzManager caseManager, final List<Entity> testedObjects, final TestCase testCase)
	{
		Display.getDefault().syncExec(new Runnable()
		{

			public void run()
			{
				try
				{
					SequenceViewPart viewPart = (SequenceViewPart) EclipseUtils.getActivePage().showView(SequenceViewPart.ID);

					WaterFallSequence sequence = SequenceBuilderImpl.getInstance().buildWaterFallSequence(cr.getReslutList(), compareResult, 5);
					sequence.setResultList(cr.getReslutList(), icdManager, caseManager, testedObjects, testCase);
					viewPart.showSequence(sequence);
					viewPart.updateErrorNavigatorStatus(true, true);
					viewPart.updateCfgStatus(true);
					viewPart.updateSaveStatus(true);
				} catch (PartInitException e)
				{
					e.printStackTrace();
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", "�򿪽��ʧ�ܣ�");
				}

			}

		});
	}
}