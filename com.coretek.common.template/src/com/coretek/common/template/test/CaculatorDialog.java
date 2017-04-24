/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �����������ڵ�λ����
 * 
 * @author ���Ρ 2011-12-28
 */
public class CaculatorDialog extends MessageDialog
{

	private String	result;		// ������

	private String	unit;		// ��λ����

	private Text	txtReuslt;

	private Combo	cmbUnit;

	/**
	 * ����һ������������
	 * 
	 * @param parentShell </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-28
	 */
	public CaculatorDialog(Shell parentShell)
	{

		super(parentShell, "��λ������", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		setShellStyle(SWT.RESIZE | getShellStyle());
		this.setBlockOnOpen(true);
		this.setShellStyle(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE);

	}

	/**
	 * ��ȡ������
	 * 
	 * @return the result <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-28
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * ��ȡ��λ����
	 * 
	 * @return the unit <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-28
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
	 * .Composite) <br/> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-28
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{

		// ������
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);

		GridData grid = new GridData();
		grid.widthHint = 300;
		grid.heightHint = 150;
		panel.setLayoutData(grid);

		// ԭʼ����
		Label label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);

		label.setText("��λ����:");

		label = new Label(panel, SWT.NONE);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("����");

		// ԭʼ��λ
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);

		label.setText("ԭʼ��λ:");

		Text text = new Text(panel, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		text.setLayoutData(grid);
		text.setText("Ӣ��");

		// ��ʾ��λ������
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;

		label.setLayoutData(grid);
		label.setText("��λ:");

		this.cmbUnit = new Combo(panel, SWT.BORDER);
		this.cmbUnit.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				// TODO ������λ������

			}

		});
		this.cmbUnit.add("����");
		this.cmbUnit.add("ǧ��");
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		this.cmbUnit.setLayoutData(grid);

		// ����
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("����:");

		text = new Text(panel, SWT.BORDER);
		grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.heightHint = 20;
		text.setLayoutData(grid);

		// ת����Ľ��
		label = new Label(panel, SWT.RIGHT);
		grid = new GridData();
		grid.widthHint = 60;
		grid.heightHint = 20;
		label.setLayoutData(grid);
		label.setText("���:");

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
	 * <b>����</b> ���Ρ </br> <b>����</b> 2011-12-28
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
