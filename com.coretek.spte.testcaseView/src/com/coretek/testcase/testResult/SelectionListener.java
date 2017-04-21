/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testResult;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.MD5Util;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;

/**
 * @author SunDawei 2012-6-6
 */
public class SelectionListener extends SelectionAdapter
{

	private CheckboxTableViewer	tableViewer;

	public SelectionListener()
	{

	}

	public void setTableViewer(CheckboxTableViewer tableViewer)
	{
		this.tableViewer = tableViewer;
	}

	@SuppressWarnings( { "unchecked" })
	public void widgetSelected(SelectionEvent event)
	{
		if (event.widget instanceof MenuItem)
		{
			MenuItem menuItem = (MenuItem) event.widget;
			// ��ʼ��ִ�ж���
			ExecutionQueue excutionQueue = new ExecutionQueue();
			String text = menuItem.getText();
			if (text.equals("��������ͨ������"))
			{
				List<CompareResult> compareResults = (List<CompareResult>) tableViewer.getInput();
				for (CompareResult compareResult : compareResults)
				{
					if (compareResult.isStatus())
					{
						IFile file = compareResult.getTestCaseFile();
						TestCase testCase = new TestCase();
						testCase.setCaseName(file.getName());
						testCase.setSuiteName(file.getParent().getName());
						testCase.setPath(file.getProjectRelativePath().toOSString());
						testCase.setProjectName(file.getProject().getName());
						excutionQueue.add(testCase);
					}
				}
				if (excutionQueue.getQueue().size() == 0)
				{
					MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), "������ͨ��������");
				}
			} else if (text.equals("��������δͨ������"))
			{
				List<CompareResult> compareResults = (List<CompareResult>) tableViewer.getInput();
				for (CompareResult compareResult : compareResults)
				{
					if (!compareResult.isStatus())
					{
						IFile file = compareResult.getTestCaseFile();
						TestCase testCase = new TestCase();
						testCase.setCaseName(file.getName());
						testCase.setSuiteName(file.getParent().getName());
						testCase.setPath(file.getProjectRelativePath().toOSString());
						testCase.setProjectName(file.getProject().getName());
						excutionQueue.add(testCase);
					}
				}
				if (excutionQueue.getQueue().size() == 0)
				{
					MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), "������δͨ��������");
				}
			} else if (text.equals("������ѡ�е�����"))
			{
				Object[] objects = tableViewer.getCheckedElements();
				if (objects.length > 0)
				{
					for (Object object : objects)
					{
						if (object instanceof CompareResult)
						{
							final CompareResult compareResult = (CompareResult) object;
							IFile file = compareResult.getTestCaseFile();
							// �ļ������ڣ�����������Ϣ��ȡ������ִ��
							if (file == null || !file.exists())
							{
								MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", compareResult.getCaseName() + "�������������ڣ�ϵͳ��ֹ�˴�ִ�в�����");
								return;
							}
							if (!validateMD5(compareResult))
							{
								boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "ȷ��", compareResult.getCaseName() + "���������Ѿ����޸ģ�����ܵ���������Ϣ�뱾�����е���Ϣ��ƥ�䡣���Ƿ������");
								if (!result)
								{
									return;
								}
							}
							TestCase testCase = new TestCase();
							testCase.setCaseName(file.getName());
							testCase.setSuiteName(file.getParent().getName());
							testCase.setPath(file.getProjectRelativePath().toOSString());
							testCase.setProjectName(file.getProject().getName());
							excutionQueue.add(testCase);
						}
					}
				} else
				{
					MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), "ѡ����������Ϊ�գ�");
				}

			} else if (text.equals("���н������������"))
			{
				List<CompareResult> compareResults = (List<CompareResult>) tableViewer.getInput();
				for (CompareResult compareResult : compareResults)
				{
					IFile file = compareResult.getTestCaseFile();

					TestCase testCase = new TestCase();
					testCase.setCaseName(file.getName());
					testCase.setSuiteName(file.getParent().getName());
					testCase.setPath(file.getProjectRelativePath().toOSString());
					testCase.setProjectName(file.getProject().getName());
					excutionQueue.add(testCase);
				}
				if (excutionQueue.getQueue().size() == 0)
				{
					MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), "���н���в���������");
				}
			}
			// ִ�в��Զ���
			if (excutionQueue.getQueue().size() > 0)
			{
				excutionQueue.excute();
			}

		}
	}

	/**
	 * To check whether the selected testCase had been removed or modified.
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-6
	 */
	private boolean validateMD5(CompareResult result)
	{
		IFile file = result.getTestCaseFile();
		String currentMD5 = MD5Util.getMD5Digest(file);
		if (currentMD5.equals(result.getMD5()))
		{
			return true;
		}
		return false;
	}
}
