/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.SequenceViewPart;
import com.coretek.spte.monitor.Sequence;
import com.coretek.spte.testcase.TestCase;

/**
 * 将监控消息保存为测试用例
 * 
 * @author 孙大巍
 * @date 2012-1-6
 */
public class SaveAsDelegate implements IViewActionDelegate
{

	private SequenceViewPart	view;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-6
	 */
	@Override
	public void init(IViewPart view)
	{
		this.view = (SequenceViewPart) view;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-6
	 */
	@Override
	public void run(IAction action)
	{
		Sequence sequence = this.view.getSequence();
		if (sequence == null)
			return;
		List<FunctionNode> nodes = sequence.getHeaderNodes();
		List<Result> resultList = sequence.getResultList();
		ClazzManager icdManager = sequence.getIcdManager();
		TestCase sourceCase = sequence.getTestCase();
		SaveAsTestCaseDialog dialog = new SaveAsTestCaseDialog(Display.getCurrent().getActiveShell(), nodes, resultList, icdManager, sourceCase);
		if (Window.OK == dialog.open())
		{
			TestCase testCase = dialog.getDestCase();
			File file = new File(dialog.getPath() + File.separator + dialog.getFileName());
			if (file.exists())
			{
				if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "确认", StringUtils.concat("已经存在同名的文件", dialog.getFileName(), ", 是否覆盖？")))
				{
					return;
				}
				if (!file.delete())
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", StringUtils.concat("删除文件", dialog.getFileName(), "失败！"));
					return;
				}

			}

			try
			{
				if (!file.createNewFile())
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", StringUtils.concat("创建文件", dialog.getFileName(), "失败！"));
					return;
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			try
			{
				PrintWriter writer = new PrintWriter(file, "UTF-8");
				writer.print(testCase.toXML());
				writer.flush();
				writer.close();
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2012-1-6
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}