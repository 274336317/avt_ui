/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.test;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 计算器，用于单位换算
 * 
 * @author 孙大巍 2011-12-28
 */
public class CaculatorDialog extends MessageDialog
{

	private String	result;		// 计算结果

	private String	unit;		// 单位代码

	private Text	txtReuslt;

	private Combo	cmbUnit;

	/**
	 * 构建一个计算器对象
	 * 
	 * @param parentShell </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-28
	 */
	public CaculatorDialog(Shell parentShell)
	{

		super(parentShell, "单位计算器", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		setShellStyle(SWT.RESIZE | getShellStyle());
		this.setBlockOnOpen(true);
		this.setShellStyle(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE);

	}

	/**
	 * 获取计算结果
	 * 
	 * @return the result <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-28
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * 获取单位代码
	 * 
	 * @return the unit <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-28
	 */
	public String getUnit()
	{
		return unit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-28
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{

		// 组件面板
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);

		GridData grid = new GridData();
		grid.widthHint = 300;
		grid.heightHint = 150;
		panel.setLayoutData(grid);

		// 原始类型
		Label label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);

		label.setText("单位类型:");

		label = new Label(panel, SWT.NONE);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("距离");

		// 原始单位
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);

		label.setText("原始单位:");

		Text text = new Text(panel, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		text.setLayoutData(grid);
		text.setText("英里");

		// 显示单位下拉框
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;

		label.setLayoutData(grid);
		label.setText("单位:");

		this.cmbUnit = new Combo(panel, SWT.BORDER);
		this.cmbUnit.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				// TODO 监听单位下拉框

			}

		});
		this.cmbUnit.add("海里");
		this.cmbUnit.add("千米");
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		this.cmbUnit.setLayoutData(grid);

		// 输入
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("输入:");

		text = new Text(panel, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		text.setLayoutData(grid);

		// 转换后的结果
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("结果:");

		this.txtReuslt = new Text(panel, SWT.BORDER);
		this.txtReuslt.setText(String.valueOf("dd"));
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		this.txtReuslt.setLayoutData(grid);

		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int) <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-28
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (buttonId != 0)
		{
			super.buttonPressed(buttonId);
			return;
		}
		this.unit = this.cmbUnit.getText();
		this.result = this.txtReuslt.getText();
		super.buttonPressed(buttonId);
	}

}
