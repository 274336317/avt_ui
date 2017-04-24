/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.i18n;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;

/**
 * 配置语言
 * 
 * @author 孙大巍
 * @date 2012-2-18
 */
@SuppressWarnings("restriction")
public class I18NWorkbenchPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

	public final static String	LANGUAGE	= "language";

	private Combo				cmbLanguage;

	private Language			language	= Language.Chinese;

	private Language			oldLanguage	= null;

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	public I18NWorkbenchPreferencePage()
	{

	}

	/**
	 * @param title </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	public I18NWorkbenchPreferencePage(String title)
	{
		super(title);
	}

	/**
	 * @param title
	 * @param image </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	public I18NWorkbenchPreferencePage(String title, ImageDescriptor image)
	{
		super(title, image);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse
	 * .swt.widgets.Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite panel = new Composite(parent, SWT.BORDER);
		panel.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);
		Label label = new Label(panel, SWT.RIGHT);
		gridData = new GridData();
		gridData.widthHint = 100;
		label.setLayoutData(gridData);
		label.setText(Messages.getString("SELECT_LAGUAGE"));
		this.cmbLanguage = new Combo(panel, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 100;
		this.cmbLanguage.setLayoutData(gridData);
		this.cmbLanguage.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				String name = cmbLanguage.getText();
				for (Language lan : Language.values())
				{
					if (lan.getDisplayName().equals(name))
						language = lan;
				}

			}

		});
		for (Language lan : Language.values())
		{
			this.cmbLanguage.add(lan.getDisplayName());
		}
		this.cmbLanguage.setText(this.language.getDisplayName());

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	@Override
	public void init(IWorkbench workbench)
	{
		String name = this.getPreferenceStore().getString(LANGUAGE);
		if (StringUtils.isNull(name))
			name = Language.Chinese.getName();
		this.language = Language.valueOf(name);
		this.oldLanguage = Language.valueOf(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-20
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{

		return I18NPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-18
	 */
	@Override
	public boolean performOk()
	{

		return super.performOk();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performApply() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-20
	 */
	@Override
	protected void performApply()
	{
		this.getPreferenceStore().setValue(LANGUAGE, language.getName());
		//FrameworkProperties.setProperty("osgi.nl", language.getLocale().toString());
		super.performApply();
		if (this.oldLanguage != this.language)
		{
			PlatformUI.getWorkbench().saveAllEditors(true);
			if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.getString("CONFIRM"), Messages.getString("DO_YOU_WANT_TO_RESTART")))
			{
				PlatformUI.getWorkbench().restart();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-20
	 */
	@Override
	protected void performDefaults()
	{
		this.doGetPreferenceStore().setValue(LANGUAGE, Language.Chinese.getName());
		super.performDefaults();
	}

}