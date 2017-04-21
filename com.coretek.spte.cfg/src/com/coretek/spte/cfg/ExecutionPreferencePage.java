/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.cfg;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.data.DataPlugin;

/**
 * ����Socket�������Ĳ��������õĲ�������:ͨ�Ŷ˿ںš������С�Լ���С��
 * 
 * @author ���Ρ 2012-3-22
 */
public class ExecutionPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

	/** Ĭ�϶˿ں�Ϊ8888 */
	public final static int		PORT_DEFAULT_VALUE			= 8888;

	/** Ĭ�ϻ����СΪ2MB */
	public final static int		CACHE_SIZE_DEFAULT_VALUE	= 2 * 1024 * 1024;

	/** Ĭ�ϴ�С�˵�ֵΪlittle */
	public final static String	ENDIAN_DEFAULT_VALUE		= "little";

	/** ���ڴ洢�˿ںŵ��ַ���key */
	public final static String	PORT_NAME					= "PORT";

	/** ���ڴ洢�����С���ַ���key */
	public final static String	CACHE_SIZE_NAME				= "CACHE_SIZE";

	/** ���ڴ洢��С�˵��ַ���key */
	public final static String	ENDIAN_NAME					= "ENDIAN_NAME";

	/** ���ڴ洢ֹͣȷ�ϵ��ַ���key */
	public final static String	STOP_CONFIRM				= "STOP_CONFIRM";

	public final static String	EXCUTOR_COMMAND				= "EXCUTOR_COMMAND";

	private int					port						= PORT_DEFAULT_VALUE;		// �˿ں�

	private int					cacheSize					= CACHE_SIZE_DEFAULT_VALUE; // �����С

	private String				endian						= ENDIAN_DEFAULT_VALUE;	// ��С��

	private boolean				noMoreConfirm				= false;					// ֹͣȷ��

	private Text				txtPort;

	private Text				txtCacheSize;

	private Combo				cmbEndian;

	private Text				txtCmdCfg;

	private String				cmdCfg;

	/**
	 * </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-22
	 */
	public ExecutionPreferencePage()
	{
		super(Messages.getString("SETTING_EXECUTION_CFG"));
	}

	/**
	 * @param title </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-22
	 */
	public ExecutionPreferencePage(String title)
	{
		super(title);
	}

	/**
	 * @param title
	 * @param image </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-22
	 */
	public ExecutionPreferencePage(String title, ImageDescriptor image)
	{
		super(title, image);
	}

	/**
	 * ��ʾִֹͣ����������
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-1
	 */
	private void showStopCfg(Composite parent)
	{
		Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.getString("STOP_EXECUTION_CFG"));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);

		Button button = new Button(group, SWT.RADIO);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		button.setLayoutData(gridData);
		button.setSelection(this.noMoreConfirm);
		button.addSelectionListener(new NeverPopBtnSelectionListener());

		Label label = new Label(group, SWT.NONE);
		label.setText(Messages.getString("NOT_POPUP_DIALOG_WHEN_CLICK_STOP_BTN"));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(gridData);

		button = new Button(group, SWT.RADIO);
		gridData = new GridData();
		gridData.widthHint = 20;
		gridData.heightHint = 20;
		button.setLayoutData(gridData);
		button.setSelection(!this.noMoreConfirm);
		button.addSelectionListener(new PopBtnSelectionListener());

		label = new Label(group, SWT.NONE);
		label.setText(Messages.getString("POPUP_DIALOG_WHEN_CLICK_STOP_BTN"));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(gridData);
	}

	private class PopBtnSelectionListener implements SelectionListener
	{

		@Override
		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		@Override
		public void widgetSelected(SelectionEvent e)
		{
			noMoreConfirm = false;

		}

	}

	private class NeverPopBtnSelectionListener implements SelectionListener
	{

		@Override
		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		@Override
		public void widgetSelected(SelectionEvent e)
		{
			noMoreConfirm = true;

		}

	}

	/**
	 * ��ʾSocket������
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-1
	 */
	private void showSocketCfg(Composite parent)
	{
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(Messages.getString("SOCKET_CFG"));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);

		Composite panel = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panel.setLayout(gridLayout);

		/* ���ö˿ں� */
		Label label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("PORT"));
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 100;
		label.setLayoutData(gridData);

		this.txtPort = new Text(panel, SWT.BORDER);
		gridData = new GridData(SWT.LEFT);
		gridData.widthHint = 150;
		gridData.heightHint = 15;
		this.txtPort.setLayoutData(gridData);
		this.txtPort.setText(String.valueOf(this.port));
		this.txtPort.addModifyListener(new NumberModifyListener());

		/* ����SocketServer�Ļ��� */
		label = new Label(panel, SWT.NONE);
		label.setText(Messages.getString("CACHE_SIZE") + "(KB):");
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 100;
		label.setLayoutData(gridData);

		this.txtCacheSize = new Text(panel, SWT.BORDER);
		gridData = new GridData(SWT.LEFT);
		gridData.widthHint = 150;
		gridData.heightHint = 15;
		this.txtCacheSize.setLayoutData(gridData);
		this.txtCacheSize.setText(String.valueOf(this.cacheSize));
		this.txtCacheSize.addModifyListener(new NumberModifyListener());

		/* ���ô�С�� */
		label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("ENDIAN"));
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 100;
		label.setLayoutData(gridData);

		this.cmbEndian = new Combo(panel, SWT.BORDER);
		this.cmbEndian.add("С��");
		this.cmbEndian.add("���");
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 140;
		gridData.heightHint = 15;
		this.cmbEndian.setLayoutData(gridData);
		if ("big".equals(this.endian))
		{
			this.cmbEndian.setText("���");
		} else
		{
			this.cmbEndian.setText("С��");
		}

		this.cmbEndian.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				String str = cmbEndian.getText();
				if ("���".equals(str))
				{
					endian = "big";
				} else
				{
					endian = "little";
				}
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite) <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-22
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		mainPanel.setLayout(gridLayout);
		this.showExcutorCfg(mainPanel);
		this.showSocketCfg(mainPanel);
		this.showStopCfg(mainPanel);
		return mainPanel;
	}

	/**
	 * ��ʾִ���������������
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-11
	 */
	private void showExcutorCfg(Composite parent)
	{
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(Messages.getString("EXECUTOR_START_CMD"));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		group.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);

		Composite panel = new Composite(group, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panel.setLayout(gridLayout);

		Label label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("COMMAND_CFG"));
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 100;
		label.setLayoutData(gridData);

		this.txtCmdCfg = new Text(panel, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		gridData = new GridData(SWT.LEFT);
		gridData.widthHint = 250;
		gridData.heightHint = 35;
		this.txtCmdCfg.setLayoutData(gridData);
		this.txtCmdCfg.setText(this.cmdCfg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 * <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-22
	 */
	@Override
	public void init(IWorkbench workbench)
	{
		String executorPath = EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath();
		StringBuilder sb = new StringBuilder("cmd /c start /b ");
		sb.append(executorPath);
		sb.append(File.separator);
		sb.append("executor");
		sb.append(File.separator);
		sb.append("testParser.exe");

		IPreferenceStore store = this.doGetPreferenceStore();
		store.setDefault(PORT_NAME, PORT_DEFAULT_VALUE);
		store.setDefault(CACHE_SIZE_NAME, CACHE_SIZE_DEFAULT_VALUE);
		store.setDefault(ENDIAN_NAME, ENDIAN_DEFAULT_VALUE);
		store.setDefault(STOP_CONFIRM, false);
		store.setDefault(EXCUTOR_COMMAND, sb.toString());

		this.noMoreConfirm = store.getBoolean(STOP_CONFIRM);
		this.port = store.getInt(PORT_NAME);
		this.cacheSize = store.getInt(CACHE_SIZE_NAME);
		this.endian = store.getString(ENDIAN_NAME);

		this.cmdCfg = sb.toString();
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{

		return CfgPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected void performApply()
	{
		this.saveCfg();
		super.performApply();
	}

	/**
	 * �������� </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-1
	 */
	private void saveCfg()
	{
		IPreferenceStore store = this.doGetPreferenceStore();
		store.setValue(PORT_NAME, this.port);
		store.setValue(CACHE_SIZE_NAME, this.cacheSize);
		store.setValue(ENDIAN_NAME, this.endian);
		store.setValue(STOP_CONFIRM, this.noMoreConfirm);
		store.setValue(EXCUTOR_COMMAND, this.cmdCfg);
	}

	@Override
	protected void performDefaults()
	{
		this.port = PORT_DEFAULT_VALUE;
		this.cacheSize = CACHE_SIZE_DEFAULT_VALUE;
		this.endian = ENDIAN_DEFAULT_VALUE;
		this.noMoreConfirm = false;
		String executorPath = EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath();
		StringBuilder sb = new StringBuilder("cmd /c start /b ");
		sb.append(executorPath);
		sb.append(File.separator);
		sb.append("executor");
		sb.append(File.separator);
		sb.append("testParser.exe");
		this.cmdCfg = sb.toString();

		this.saveCfg();
		super.performDefaults();
	}

	@Override
	public boolean performOk()
	{
		if (this.port <= 0 || this.cacheSize <= 0)
		{
			return false;
		}
		this.saveCfg();
		return super.performOk();
	}

	/**
	 * ȷ�������ֵȫΪ������
	 * 
	 * @author ���Ρ 2012-3-22
	 */
	private class NumberModifyListener implements ModifyListener
	{

		@Override
		public void modifyText(ModifyEvent e)
		{
			Text txt = (Text) e.getSource();
			String input = txt.getText();
			if (!StringUtils.isNumber(input))
			{
				setMessage(Messages.getString("INPUT_MUST_BE_NUMBER"), SWT.ERROR);
				ExecutionPreferencePage.this.getApplyButton().setEnabled(false);
			} else if (Integer.valueOf(input).intValue() <= 0)
			{
				setMessage(Messages.getString("INPUT_CAN_NOT_LESS_THAN_ZERO"), SWT.ERROR);
				ExecutionPreferencePage.this.getApplyButton().setEnabled(false);
			} else
			{
				ExecutionPreferencePage.this.getApplyButton().setEnabled(true);
			}

			port = Integer.valueOf(input);
		}
	}
}
