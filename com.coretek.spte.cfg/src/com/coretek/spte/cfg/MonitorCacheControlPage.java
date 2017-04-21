package com.coretek.spte.cfg;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;

public class MonitorCacheControlPage extends PreferencePage implements IWorkbenchPreferencePage
{

	/** 监控时，缓存中存放消息的最大条数 */
	public final static int		CACHE_MAX			= 50000;

	/** 每次向数据库批量写入的消息条数 */
	public final static int		ONETIME_INSERT_DB	= 100;

	public final static String	CACHE_MAX_KEY		= "CACHEMAX";

	public final static String	ONETIME_INSERT_KEY	= "ONETIMENO";

	private Text				txtCacheMax;

	private Text				txtInsetDBOnetime;

	private int					cacheMax;
	private int					insertOneTime;

	public MonitorCacheControlPage()
	{
	}

	public MonitorCacheControlPage(String title)
	{
		super(title);
	}

	public MonitorCacheControlPage(String title, ImageDescriptor image)
	{
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		panel.setLayout(gridLayout);

		/* 缓存大小 */
		Label label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("CACHED_MSG_NUM"));
		GridData gridData = new GridData(SWT.NONE);
		gridData.widthHint = 120;
		label.setLayoutData(gridData);

		this.txtCacheMax = new Text(panel, SWT.BORDER);
		gridData = new GridData(SWT.LEFT);
		gridData.widthHint = 150;
		this.txtCacheMax.setLayoutData(gridData);
		this.txtCacheMax.setText(String.valueOf(cacheMax));
		this.txtCacheMax.addModifyListener(new NumberModifyListener());

		/* 向数据库每次写入条数 */
		label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("BATCH_SAVE_MSG_NUM"));
		gridData = new GridData(SWT.NONE);
		gridData.widthHint = 120;
		label.setLayoutData(gridData);

		this.txtInsetDBOnetime = new Text(panel, SWT.BORDER);
		gridData = new GridData(SWT.LEFT);
		gridData.widthHint = 150;
		this.txtInsetDBOnetime.setLayoutData(gridData);
		this.txtInsetDBOnetime.setText(String.valueOf(insertOneTime));
		this.txtInsetDBOnetime.addModifyListener(new NumberModifyListener());

		return panel;
	}

	@Override
	public void init(IWorkbench workbench)
	{
		IPreferenceStore store = doGetPreferenceStore();
		cacheMax = store.getInt(CACHE_MAX_KEY) > 0 ? store.getInt(CACHE_MAX_KEY) : CACHE_MAX;
		insertOneTime = store.getInt(ONETIME_INSERT_KEY) > 0 ? store.getInt(ONETIME_INSERT_KEY) : ONETIME_INSERT_DB;
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{
		return CfgPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected void performDefaults()
	{

		IPreferenceStore store = this.doGetPreferenceStore();
		store.setDefault(CACHE_MAX_KEY, CACHE_MAX);
		store.setDefault(ONETIME_INSERT_KEY, ONETIME_INSERT_DB);

		txtCacheMax.setText(String.valueOf(CACHE_MAX));
		txtInsetDBOnetime.setText(String.valueOf(ONETIME_INSERT_DB));

		super.performDefaults();
	}

	@Override
	protected void performApply()
	{
		IPreferenceStore store = this.doGetPreferenceStore();
		store.setValue(CACHE_MAX_KEY, txtCacheMax.getText());
		store.setValue(ONETIME_INSERT_KEY, txtInsetDBOnetime.getText());

		super.performApply();
	}

	/**
	 * 确保输入的值全为正整数
	 * 
	 * @author 孙大巍 2012-3-22
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
				MonitorCacheControlPage.this.getApplyButton().setEnabled(false);
			} else if (Integer.valueOf(input).intValue() <= 0)
			{
				setMessage(Messages.getString("INPUT_CAN_NOT_LESS_THAN_ZERO"), SWT.ERROR);
				MonitorCacheControlPage.this.getApplyButton().setEnabled(false);
			} else
			{
				MonitorCacheControlPage.this.getApplyButton().setEnabled(true);
			}
		}
	}
}
