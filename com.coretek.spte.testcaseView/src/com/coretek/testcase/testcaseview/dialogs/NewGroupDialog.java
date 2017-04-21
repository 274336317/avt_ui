package com.coretek.testcase.testcaseview.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.util.Utils;

/**
 * 
 * 创建新的用例分组对话框
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class NewGroupDialog extends MessageDialog
{

	private Text	text;

	private String	name;

	public NewGroupDialog()
	{
		super(Utils.getShell(), Messages.getString("I18N_NEW_CASE_GROUP"), null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
	}

	@Override
	protected void buttonPressed(int buttonId)
	{
		if (buttonId != 0)
			super.buttonPressed(buttonId);
		else
		{
			this.name = this.text.getText();
			super.buttonPressed(buttonId);
		}
	}

	public String getText()
	{
		return this.name;
	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		panel.setLayout(grid);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		panel.setLayoutData(gd);

		Label label = new Label(panel, SWT.NONE);
		label.setText(Messages.getString("I18N_GROUP_NAME"));
		gd = new GridData();
		label.setLayoutData(gd);

		text = new Text(panel, SWT.BORDER);
		text.setSize(50, 25);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		text.setLayoutData(gd);

		return panel;
	}
}
