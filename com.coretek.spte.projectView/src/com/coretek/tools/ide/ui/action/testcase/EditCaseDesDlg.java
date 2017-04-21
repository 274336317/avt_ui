package com.coretek.tools.ide.ui.action.testcase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.testcase.TestCase;

public class EditCaseDesDlg extends TitleAreaDialog
{

	// ������
	Text				REQText;

	// ��������
	Text				inputText;

	// �Ⱦ�����
	Text				conditionText;

	// ���۽����׼��
	Text				criterionText;

	// ���Թ��
	Text				procedureText;

	// �����Լ��
	Text				restrictionText;

	// Ԥ�ڽ��
	Text				expectedText;

	private String		REQ			= "";

	private String		condition	= "";

	private String		criterion	= "";

	private String		procedure	= "";

	private String		restriction	= "";

	private String		expected	= "";

	private String		input		= "";

	private TestCase	testCase;

	private IFile		file;

	public EditCaseDesDlg(Shell parentShell, TestCase testCase, IFile file)
	{
		super(parentShell);
		this.file = file;
		this.testCase = testCase;
		String str = "";
		REQ = StringUtils.isNotNull(str = this.testCase.getCaseDes().getREQ()) ? str : "";
		condition = StringUtils.isNotNull(str = this.testCase.getCaseDes().getCondition()) ? str : "";
		criterion = StringUtils.isNotNull(str = this.testCase.getCaseDes().getCriterion()) ? str : "";
		procedure = StringUtils.isNotNull(str = this.testCase.getCaseDes().getProcedure()) ? str : "";
		restriction = StringUtils.isNotNull(str = this.testCase.getCaseDes().getRestriction()) ? str : "";
		expected = StringUtils.isNotNull(str = this.testCase.getCaseDes().getExpected()) ? str : "";
		input = StringUtils.isNotNull(str = this.testCase.getCaseDes().getInput()) ? str : "";
	}

	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("�༭����������������Ϣ");
	}

	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		setTitle("�༭����������������Ϣ");
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		return control;
	}

	protected Control createDialogArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.LEFT);
		GridLayout viewLayout = new GridLayout(4, true);
		viewLayout.marginHeight = 5;
		viewLayout.marginWidth = 5;
		panel.setLayout(viewLayout);
		GridData pgd = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(pgd);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 40;
		gridData.horizontalSpan = 4;

		// ��Ӧ������
		Label requireNumLabel = new Label(panel, SWT.NULL);
		requireNumLabel.setText(Messages.getString("I18N_REQUIRE_NUM") + ":");

		REQText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		REQText.setFont(panel.getFont());
		REQText.setLayoutData(gridData);
		REQText.setEditable(true);
		REQText.setText(REQ);
		REQText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!REQ.equals(REQText.getText()))
				{
					REQ = REQText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}

		});

		// �Ⱦ�����
		Label conditionLabel = new Label(panel, SWT.NULL);
		conditionLabel.setText(Messages.getString("I18N_CONDITION") + ":");

		conditionText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		conditionText.setFont(panel.getFont());
		conditionText.setLayoutData(gridData);
		conditionText.setEditable(true);
		conditionText.setText(condition);
		conditionText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!condition.equals(conditionText.getText()))
				{
					condition = conditionText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}

			}

		});

		// ��������
		Label inputLabel = new Label(panel, SWT.NULL);
		inputLabel.setText(Messages.getString("I18N_TEST_INPUT") + ":");

		inputText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		inputText.setFont(panel.getFont());
		inputText.setLayoutData(gridData);
		inputText.setEditable(true);
		inputText.setText(input);
		inputText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!input.equals(inputText.getText()))
				{
					input = inputText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}

			}

		});

		// ���۽����׼��
		Label regulationLabel = new Label(panel, SWT.NULL);
		regulationLabel.setText(Messages.getString("I18N_REGULATION") + ":");

		criterionText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		criterionText.setFont(panel.getFont());
		criterionText.setLayoutData(gridData);
		criterionText.setEditable(true);
		criterionText.setText(criterion);
		criterionText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!criterion.equals(criterionText.getText()))
				{
					criterion = criterionText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}

		});

		// ���Թ��
		Label procedureLabel = new Label(panel, SWT.NULL);
		procedureLabel.setText(Messages.getString("I18N_PROCEDURE") + ":");

		procedureText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		procedureText.setFont(panel.getFont());
		procedureText.setLayoutData(gridData);
		procedureText.setEditable(true);
		procedureText.setText(procedure);
		procedureText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!procedure.equals(procedureText.getText()))
				{
					procedure = procedureText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}

		});

		// �����Լ��
		Label constrainLabel = new Label(panel, SWT.NULL);
		constrainLabel.setText(Messages.getString("I18N_CONSTRAIN") + ":");

		restrictionText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		restrictionText.setFont(panel.getFont());
		restrictionText.setLayoutData(gridData);
		restrictionText.setEditable(true);
		restrictionText.setText(restriction);
		restrictionText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!restriction.equals(restrictionText.getText()))
				{
					restriction = restrictionText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}

		});

		// Ԥ�ڵĲ��Խ��
		Label caseInfoLabel = new Label(panel, SWT.NULL);
		caseInfoLabel.setText(Messages.getString("I18N_CASE_EXPECTED") + ":");

		expectedText = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		expectedText.setFont(panel.getFont());
		expectedText.setLayoutData(gridData);
		expectedText.setEditable(true);
		expectedText.setText(expected);
		expectedText.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (!expected.equals(expectedText.getText()))
				{
					expected = expectedText.getText();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}
		});
		return parent;

	}

	public String getDes()
	{
		return expected;
	}

	public void setDes(String des)
	{
		this.expected = des;
	}

	public String getRequireNum()
	{
		return REQ;
	}

	public void setRequireNum(String requireNum)
	{
		this.REQ = requireNum;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getRegulation()
	{
		return criterion;
	}

	public void setRegulation(String regulation)
	{
		this.criterion = regulation;
	}

	public String getProcedure()
	{
		return procedure;
	}

	public void setProcedure(String procedure)
	{
		this.procedure = procedure;
	}

	public String getConstrain()
	{
		return restriction;
	}

	public void setConstrain(String constrain)
	{
		this.restriction = constrain;
	}

	protected void okPressed()
	{

		testCase.getCaseDes().setREQ(REQ);
		testCase.getCaseDes().setCondition(condition);
		testCase.getCaseDes().setCriterion(criterion);
		testCase.getCaseDes().setProcedure(procedure);
		testCase.getCaseDes().setRestriction(restriction);
		testCase.getCaseDes().setExpected(expected);
		testCase.getCaseDes().setInput(input);

		try
		{
			OutputStream os = new FileOutputStream(file.getLocation().toFile());
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			PrintWriter writer = new PrintWriter(osw);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append(testCase.toString());
			writer.write(sb.toString());
			writer.close();
			// IDE.openEditor(EclipseUtils.getActivePage(), file,
			// OpenStrategy.activateOnOpen());
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null); // �Թ����ռ����ˢ�£�����ļ�ϵͳ��ͬ������
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.okPressed();
	}

}
