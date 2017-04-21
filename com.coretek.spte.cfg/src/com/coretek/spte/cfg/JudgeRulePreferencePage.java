/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coretek.common.i18n.messages.Messages;

/**
 * 判定准则配置页面
 * 
 * @author 孙大巍
 * @date 2012-2-9
 */
public class JudgeRulePreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

	private HandlerTypesEnum	unexpected	= HandlerTypesEnum.ERROR;

	private HandlerTypesEnum	lost		= HandlerTypesEnum.ERROR;

	private HandlerTypesEnum	timeout		= HandlerTypesEnum.ERROR;

	private List<Button>		btns		= new ArrayList<Button>(4);

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public JudgeRulePreferencePage()
	{

	}

	/**
	 * @param title </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public JudgeRulePreferencePage(String title)
	{
		super(title);

	}

	/**
	 * @param title
	 * @param image </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public JudgeRulePreferencePage(String title, ImageDescriptor image)
	{
		super(title, image);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);

		this.makeUnexpected(panel);
		this.makeLost(panel);
		this.makeTimeout(panel);

		Group group = new Group(panel, SWT.SHADOW_NONE);
		group.setText(Messages.getString("ALL_COLORS"));
		layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);

		Label label = new Label(group, SWT.LEFT);
		label.setText(Messages.getString("I18N_WRONG"));
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		label.setLayoutData(gridData);

		label = new Label(group, SWT.RIGHT);
		label.setBackground(ColorConstants.red);
		gridData = new GridData();
		gridData.widthHint = 80;
		label.setLayoutData(gridData);

		label = new Label(group, SWT.LEFT);
		label.setText(Messages.getString("I18N_WARNING"));
		gridData = new GridData();
		gridData.widthHint = 50;
		label.setLayoutData(gridData);

		label = new Label(group, SWT.RIGHT);
		label.setBackground(ColorConstants.yellow);
		gridData = new GridData();
		gridData.widthHint = 80;
		label.setLayoutData(gridData);

		label = new Label(group, SWT.LEFT);
		label.setText(Messages.getString("PROMPT"));
		gridData = new GridData();
		gridData.widthHint = 50;
		label.setLayoutData(gridData);

		label = new Label(group, SWT.RIGHT);
		label.setBackground(ColorConstants.lightGreen);
		gridData = new GridData();
		gridData.widthHint = 80;
		label.setLayoutData(gridData);

		return panel;
	}

	private void makeUnexpected(Composite panel)
	{
		Group group = new Group(panel, SWT.SHADOW_NONE);
		group.setText(Messages.getString("WHEN_RECEIVE_UNEXPECTED_MSG"));
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		group.setLayout(layout);

		Button btnWrong = new Button(group, SWT.RADIO);
		btnWrong.setText(Messages.getString("I18N_WRONG"));
		this.btns.add(btnWrong);
		btnWrong.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				unexpected = HandlerTypesEnum.ERROR;
			}

		});
		btnWrong.setToolTipText(Messages.getString("ERROR_WHEN_RECEIVE_UNEXPECTED_MSG"));
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		btnWrong.setLayoutData(gridData);

		Button btnWarning = new Button(group, SWT.RADIO);
		btnWarning.setText(Messages.getString("I18N_WARNING"));
		btnWarning.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				unexpected = HandlerTypesEnum.WARNING;
			}

		});
		btnWarning.setToolTipText(Messages.getString("WARNING_WHEN_RECEIVE_UNEXPECTED_MSG"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnWarning.setLayoutData(gridData);

		Button btnRemind = new Button(group, SWT.RADIO);
		btnRemind.setText(Messages.getString("PROMPT"));
		btnRemind.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				unexpected = HandlerTypesEnum.PROMPT;
			}

		});
		btnRemind.setToolTipText(Messages.getString("PROMPT_WHEN_RECEIVE_UNEXPECTED_MSG"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnRemind.setLayoutData(gridData);

		Button btnIgnore = new Button(group, SWT.RADIO);
		btnIgnore.setToolTipText(Messages.getString("NOTHING_WHEN_RECEIVE_UNEXPECTED_MSG"));
		btnIgnore.setText(Messages.getString("IGNORE"));
		btnIgnore.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				unexpected = HandlerTypesEnum.IGNORE;
			}

		});
		gridData = new GridData();
		gridData.widthHint = 80;
		btnIgnore.setLayoutData(gridData);

		switch (unexpected)
		{
			case ERROR:
			{
				btnWrong.setSelection(true);
				break;
			}
			case WARNING:
			{
				btnWarning.setSelection(true);
				break;
			}
			case PROMPT:
			{
				btnRemind.setSelection(true);
				break;
			}
			case IGNORE:
			{
				btnIgnore.setSelection(true);
				break;
			}
			default:
			{
				btnWrong.setSelection(true);
				break;
			}

		}
	}

	private void makeLost(Composite panel)
	{
		Group group = new Group(panel, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		group.setLayout(layout);
		group.setText(Messages.getString("WHEN_LOST_MSG"));

		Button btnWrong = new Button(group, SWT.RADIO);
		btnWrong.setText(Messages.getString("I18N_WRONG"));
		this.btns.add(btnWrong);
		btnWrong.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				lost = HandlerTypesEnum.ERROR;
			}

		});
		btnWrong.setToolTipText(Messages.getString("ERROR_WHEN_LOST_MSG"));
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		btnWrong.setLayoutData(gridData);

		Button btnWarning = new Button(group, SWT.RADIO);
		btnWarning.setText(Messages.getString("I18N_WARNING"));
		btnWarning.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				lost = HandlerTypesEnum.WARNING;
			}

		});
		btnWarning.setToolTipText(Messages.getString("WARNING_WHEN_LOST_MSG"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnWarning.setLayoutData(gridData);

		Button btnRemind = new Button(group, SWT.RADIO);
		btnRemind.setText(Messages.getString("PROMPT"));
		btnRemind.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				lost = HandlerTypesEnum.PROMPT;
			}

		});
		btnRemind.setToolTipText(Messages.getString("PROMPT_WHEN_LOST_MSG"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnRemind.setLayoutData(gridData);

		Button btnIgnore = new Button(group, SWT.RADIO);
		btnIgnore.setToolTipText(Messages.getString("NOTHING_WHEN_LOST_MSG"));
		btnIgnore.setText(Messages.getString("IGNORE"));
		btnIgnore.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				lost = HandlerTypesEnum.IGNORE;
			}

		});
		gridData = new GridData();
		gridData.widthHint = 80;
		btnIgnore.setLayoutData(gridData);

		switch (lost)
		{
			case ERROR:
			{
				btnWrong.setSelection(true);
				break;
			}
			case WARNING:
			{
				btnWarning.setSelection(true);
				break;
			}
			case PROMPT:
			{
				btnRemind.setSelection(true);
				break;
			}
			case IGNORE:
			{
				btnIgnore.setSelection(true);
				break;
			}
			default:
			{
				btnWrong.setSelection(true);
				break;
			}

		}
	}

	private void makeTimeout(Composite panel)
	{
		Group group = new Group(panel, SWT.NONE);
		group.setText(Messages.getString("WHEN_TIMEOUT"));
		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		group.setLayout(layout);

		Button btnWrong = new Button(group, SWT.RADIO);
		btnWrong.setText(Messages.getString("I18N_WRONG"));
		this.btns.add(btnWrong);
		btnWrong.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				timeout = HandlerTypesEnum.ERROR;

			}

		});
		btnWrong.setToolTipText(Messages.getString("ERROR_WHEN_TIMEOUT"));
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		btnWrong.setLayoutData(gridData);

		Button btnWarning = new Button(group, SWT.RADIO);
		btnWarning.setText(Messages.getString("I18N_WARNING"));
		btnWarning.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				timeout = HandlerTypesEnum.WARNING;
			}

		});
		btnWarning.setToolTipText(Messages.getString("WARNING_WHEN_TIMEOUT"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnWarning.setLayoutData(gridData);

		Button btnRemind = new Button(group, SWT.RADIO);
		btnRemind.setText(Messages.getString("PROMPT"));
		btnRemind.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				timeout = HandlerTypesEnum.PROMPT;
			}

		});
		btnRemind.setToolTipText(Messages.getString("PROMPT_WHEN_TIMEOUT"));
		gridData = new GridData();
		gridData.widthHint = 80;
		btnRemind.setLayoutData(gridData);

		Button btnIgnore = new Button(group, SWT.RADIO);
		btnIgnore.setToolTipText(Messages.getString("IGNORE_WHEN_TIMEOUT"));
		btnIgnore.setText(Messages.getString("IGNORE"));
		btnIgnore.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				timeout = HandlerTypesEnum.IGNORE;
			}

		});
		gridData = new GridData();
		gridData.widthHint = 80;
		btnIgnore.setLayoutData(gridData);

		switch (timeout)
		{
			case ERROR:
			{
				btnWrong.setSelection(true);
				break;
			}
			case WARNING:
			{
				btnWarning.setSelection(true);
				break;
			}
			case PROMPT:
			{
				btnRemind.setSelection(true);
				break;
			}
			case IGNORE:
			{
				btnIgnore.setSelection(true);
				break;
			}
			default:
			{
				btnWrong.setSelection(true);
				break;
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	public void init(IWorkbench workbench)
	{
		IPreferenceStore store = this.doGetPreferenceStore();
		this.unexpected = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.UNEXPECTED.getName()));
		this.lost = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.LOST.getName()));
		this.timeout = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.TIMEOUT.getName()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{

		return CfgPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performApply() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	@Override
	protected void performApply()
	{
		this.getPreferenceStore().setValue(ErrorTypesEnum.UNEXPECTED.getName(), this.unexpected.getNum());
		this.getPreferenceStore().setValue(ErrorTypesEnum.LOST.getName(), this.lost.getNum());
		this.getPreferenceStore().setValue(ErrorTypesEnum.TIMEOUT.getName(), this.timeout.getNum());
		super.performApply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-9
	 */
	@Override
	protected void performDefaults()
	{
		this.doGetPreferenceStore().setValue(ErrorTypesEnum.UNEXPECTED.getName(), HandlerTypesEnum.ERROR.getNum());
		this.doGetPreferenceStore().setValue(ErrorTypesEnum.LOST.getName(), HandlerTypesEnum.ERROR.getNum());
		this.doGetPreferenceStore().setValue(ErrorTypesEnum.TIMEOUT.getName(), HandlerTypesEnum.ERROR.getNum());

		for (Button btn : btns)
		{
			for (Control control : btn.getParent().getChildren())
			{
				if (control instanceof Button)
				{
					((Button) control).setSelection(false);
				}
			}
			btn.setSelection(true);
		}

		super.performDefaults();
	}

}