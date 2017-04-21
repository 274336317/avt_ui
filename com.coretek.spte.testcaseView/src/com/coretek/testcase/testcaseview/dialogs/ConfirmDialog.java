package com.coretek.testcase.testcaseview.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.coretek.common.i18n.messages.Messages;

public class ConfirmDialog extends MessageDialog
{

	private String	message;

	private Button	checkBox;

	private boolean	isAlwaysDoSo;

	private boolean	isOverride;

	public ConfirmDialog(Shell shell, String msg)
	{
		super(shell, "∏≤∏«»∑»œ", null, null, MessageDialog.NONE, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
		this.message = msg;
	}

	public boolean isAlwaysDoSo()
	{
		return isAlwaysDoSo;
	}

	public boolean isOverride()
	{
		return isOverride;
	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite shell = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 50;
		data.widthHint = 400;
		shell.setLayoutData(data);
		shell.setLayout(new GridLayout(2, false));

		Label label = new Label(shell, SWT.NONE);
		label.setText(message);
		data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);

		checkBox = new Button(shell, SWT.CHECK);
		data = new GridData();
		data.widthHint = 20;
		checkBox.setLayoutData(data);
		checkBox.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				isAlwaysDoSo = true;
			}
		});

		label = new Label(shell, SWT.NONE);
		label.setText(Messages.getString("I18N_ALWAYS_DO_SO"));
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.BEGINNING);
		data.widthHint = 370;
		label.setLayoutData(data);

		return shell;
	}

	@Override
	protected void buttonPressed(int buttonId)
	{
		if (buttonId != 0)
		{
			this.isOverride = false;
		} else
		{
			this.isOverride = true;
		}
		super.buttonPressed(buttonId);
	}
}
