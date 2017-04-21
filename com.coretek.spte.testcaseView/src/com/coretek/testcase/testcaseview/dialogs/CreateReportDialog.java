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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.core.util.Utils;

public class CreateReportDialog extends MessageDialog
{

	private Text	reportPathText;

	private String	reportPath;

	public CreateReportDialog()
	{
		super(Utils.getShell(), "设置报告保存路径", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
	}

	protected Control createCustomArea(final Composite parent)
	{
		Composite panel2 = new Composite(parent, SWT.BORDER);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		panel2.setLayout(grid);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 400;
		gd.heightHint = 80;
		panel2.setLayoutData(gd);

		Label label = new Label(panel2, SWT.RIGHT);
		label.setText(" 报告保存路径:");
		gd = new GridData();
		gd.widthHint = 95;
		label.setLayoutData(gd);

		this.reportPathText = new Text(panel2, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		this.reportPathText.setLayoutData(gd);
		reportPathText.setText("");

		Button button = new Button(panel2, SWT.PUSH);
		button.setText("浏览...");
		gd = new GridData();
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog dd = new DirectoryDialog(parent.getShell(), SWT.NONE);
				dd.setText("选择执行器路径:");

				reportPathText.setText(dd.open());
				setReportPath(reportPathText.getText());

			}
		});

		return panel2;
	}

	public String getReportPath()
	{
		return reportPath;
	}

	public void setReportPath(String reportPath)
	{
		this.reportPath = reportPath;
	}

}
