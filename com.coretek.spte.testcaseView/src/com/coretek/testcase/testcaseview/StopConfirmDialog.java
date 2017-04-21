/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ִֹͣ��ȷ�϶Ի���
 * 
 * @author ���Ρ 2012-4-1
 */
public class StopConfirmDialog extends MessageDialog
{

	private boolean	noMoreConfirm;

	/**
	 * @param parentShell
	 */
	public StopConfirmDialog(Shell parentShell)
	{
		super(parentShell, "ȷ��", null, "ȷ��Ҫֹͣ����ִ�еĲ���������", MessageDialog.CONFIRM, new String[] { "ȷ��", "ȡ��" }, 0);
	}

	/**
	 * @return the noMoreConfirm <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-4-1
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
		button.setText("�´β�Ҫ������");
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