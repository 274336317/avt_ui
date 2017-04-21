/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 停止执行确认对话框
 * 
 * @author 孙大巍 2012-4-1
 */
public class StopConfirmDialog extends MessageDialog
{

	private boolean	noMoreConfirm;

	/**
	 * @param parentShell
	 */
	public StopConfirmDialog(Shell parentShell)
	{
		super(parentShell, "确认", null, "确定要停止正在执行的测试用例吗？", MessageDialog.CONFIRM, new String[] { "确认", "取消" }, 0);
	}

	/**
	 * @return the noMoreConfirm <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-1
	 */
	public boolean isNoMoreConfirm()
	{
		return noMoreConfirm;
	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);
		panel.setLayoutData(gridData);

		Button button = new Button(panel, SWT.CHECK);
		button.setText("下次不要再提醒");
		gridData = new GridData();
		gridData.widthHint = 150;
		gridData.heightHint = 20;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				Button button = (Button) e.getSource();
				noMoreConfirm = button.getSelection();
			}

		});
		return panel;
	}

	@Override
	protected void okPressed()
	{
		super.okPressed();
	}

}