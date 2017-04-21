/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;

/**
 * To show testcase's description and provide user input interface.
 * 
 * @author SunDawei 2012-5-8
 */
public class TestCaseDescriptionWidget
{

	private final static int	TEXT_HEIGHT	= 40;

	// 对应需求编号
	private String				REQ			= "";

	private String				condition	= "";

	private String				criterion	= "";

	private String				procedure	= "";

	private String				constraint	= "";

	// 用例输入
	private String				input		= "";

	// 预期的测试结果
	private String				expected	= "";

	private Text				txtRequireNum;		// Requirement number

	private Text				txtPrecondition;	// precondition

	private Text				txtRegulation;		// the result regulation

	private Text				txtProcedure;		// the testing procedure

	private Text				txtContraint;		// the hypothesis and
	// constraint

	private Text				txtTestInput;		// the test input

	private Text				txtExpectation;	// the test expectation

	public TestCaseDescriptionWidget()
	{

	}

	public String getREQ()
	{
		return REQ;
	}

	public void setREQ(String REQ)
	{
		this.REQ = REQ;
		this.txtRequireNum.setText(this.REQ);
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
		this.txtPrecondition.setText(this.condition);
	}

	public String getCriterion()
	{
		return criterion;
	}

	public void setCriterion(String criterion)
	{
		this.criterion = criterion;
		this.txtRegulation.setText(this.criterion);

	}

	public String getProcedure()
	{
		return procedure;
	}

	public void setProcedure(String procedure)
	{
		this.procedure = procedure;
		this.txtProcedure.setText(this.procedure);
	}

	public String getConstraint()
	{
		return constraint;
	}

	public void setRestriction(String restriction)
	{
		this.constraint = restriction;
		this.txtContraint.setText(this.constraint);
	}

	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
		this.txtTestInput.setText(this.input);
	}

	public String getExpected()
	{
		return expected;
	}

	public void setExpected(String expected)
	{
		this.expected = expected;
		this.txtExpectation.setText(this.expected);
	}

	protected Text showRequireNum(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		// 对应需求编号
		Label requireNumLabel = new Label(panel, SWT.NULL);
		requireNumLabel.setText(Messages.getString("I18N_REQUIRE_NUM") + ":");
		requireNumLabel.setLayoutData(gridData);

		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(REQ);
		text.setFocus();
		text.addListener(SWT.Modify, new Listener()
		{
			public void handleEvent(Event event)
			{
				REQ = text.getText();
			}

		});

		return text;
	}

	protected Text showPrecondition(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label conditionLabel = new Label(panel, SWT.LEFT);
		conditionLabel.setText(Messages.getString("I18N_CONDITION") + ":");
		conditionLabel.setLayoutData(gridData);

		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(condition);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				condition = text.getText();
			}

		});

		return text;
	}

	protected Text showRegulation(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label regulationLabel = new Label(panel, SWT.NULL);
		regulationLabel.setText(Messages.getString("I18N_REGULATION") + ":");
		regulationLabel.setLayoutData(gridData);

		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(criterion);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				criterion = text.getText();
			}

		});

		return text;

	}

	protected Text showProcedure(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		// 测试规程
		Label procedureLabel = new Label(panel, SWT.NULL);
		procedureLabel.setText(Messages.getString("I18N_PROCEDURE") + ":");
		procedureLabel.setLayoutData(gridData);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(procedure);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				procedure = text.getText();
			}

		});
		return text;
	}

	protected Text showConstraint(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label constrainLabel = new Label(panel, SWT.NULL);
		constrainLabel.setText(Messages.getString("I18N_CONSTRAIN") + ":");
		constrainLabel.setLayoutData(gridData);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setFont(panel.getFont());
		text.setLayoutData(text);
		text.setEditable(true);
		text.setText(constraint);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				constraint = text.getText();
			}

		});
		text.setLayoutData(gridData);
		return text;
	}

	protected Text showTestInput(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label caseInputLabel = new Label(panel, SWT.NULL);
		caseInputLabel.setText(Messages.getString("I18N_TEST_INPUT") + ":");
		caseInputLabel.setLayoutData(gridData);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(input);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				input = text.getText();
			}

		});

		return text;
	}

	protected Text showExpectation(Composite panel)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label caseInfoLabel = new Label(panel, SWT.NULL);
		caseInfoLabel.setText(Messages.getString("I18N_CASE_EXPECTED") + ":");
		caseInfoLabel.setLayoutData(gridData);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = TEXT_HEIGHT;
		final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setFont(panel.getFont());
		text.setLayoutData(gridData);
		text.setEditable(true);
		text.setText(expected);
		text.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				expected = text.getText();
			}

		});

		return text;
	}

	public Composite showWidget(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.LEFT);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		panel.setLayout(layout);
		GridData pgd = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(pgd);

		// 对应需求编号
		this.txtRequireNum = this.showRequireNum(panel);

		// 先决条件
		this.txtPrecondition = this.showPrecondition(panel);

		// 评价结果的准则
		this.txtRegulation = this.showRegulation(panel);

		// 测试规程
		this.txtProcedure = this.showProcedure(panel);

		// 假设和约束
		this.txtContraint = this.showConstraint(panel);

		// 测试输入
		this.txtTestInput = this.showTestInput(panel);

		// 预期的测试结果
		this.txtExpectation = this.showExpectation(panel);

		txtRequireNum.addKeyListener(new TabKeyAdapter(txtPrecondition));
		txtPrecondition.addKeyListener(new TabKeyAdapter(txtRegulation));
		txtRegulation.addKeyListener(new TabKeyAdapter(txtProcedure));
		txtProcedure.addKeyListener(new TabKeyAdapter(txtContraint));
		txtContraint.addKeyListener(new TabKeyAdapter(txtTestInput));
		txtTestInput.addKeyListener(new TabKeyAdapter(txtExpectation));
		txtExpectation.addKeyListener(new TabKeyAdapter(txtRequireNum));

		return panel;
	}

	public Text getTxtRequireNum()
	{
		return txtRequireNum;
	}

	public Text getTxtPrecondition()
	{
		return txtPrecondition;
	}

	public Text getTxtRegulation()
	{
		return txtRegulation;
	}

	public Text getTxtProcedure()
	{
		return txtProcedure;
	}

	public Text getTxtContraint()
	{
		return txtContraint;
	}

	public Text getTxtTestInput()
	{
		return txtTestInput;
	}

	public Text getTxtExpectation()
	{
		return txtExpectation;
	}

	private class TabKeyAdapter extends KeyAdapter
	{

		private Text	text;

		public TabKeyAdapter(Text text)
		{
			this.text = text;
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.keyCode == 9 && text != null)
			{
				text.setFocus();
				e.doit = false;
			}
			return;
		}

	}

}