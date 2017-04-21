/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import java.util.ArrayList;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.monitorview.MsgWatchViewPlugin;

/**
 * 监控消息首选项配置页面
 * 
 * @author 尹军 2012-3-14
 */
public class MonitorNodePreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage
{

	public static void initDefaults(IPreferenceStore store)
	{
		store.setDefault(MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH, 5000);
		store.setDefault(MonitorNodePreferenceConstants.MAX_PAGE_NUM, 3);
		store.setDefault(MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM, 1000);
		store.setDefault(MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL, 100);
	}

	public MonitorNodePreferencePage()
	{
		super();
		setPreferenceStore(MsgWatchViewPlugin.getDefault().getPreferenceStore());
	}

	protected Control createContents(Composite parent)
	{
		fOverlayStore.load();
		fOverlayStore.start();

		Composite contentAssistComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		contentAssistComposite.setLayout(layout);

		Group enableGroup = addGroupBox(contentAssistComposite, Messages.getString("MonitorNodePreferencePage_Monitor_Field_Config"), 3);

		addTextField(enableGroup, Messages.getString("MonitorNodePreferencePage_Max_Page_Length"), MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH, 6, 0, true);
		Label labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText(Messages.getString("MonitorNodePreferencePage_Max_Page_Length_Advise"));

		addTextField(enableGroup, Messages.getString("MonitorNodePreferencePage_Max_Page_Number"), MonitorNodePreferenceConstants.MAX_PAGE_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText(Messages.getString("MonitorNodePreferencePage_Max_Page_Number_Advise"));

		addTextField(enableGroup, Messages.getString("MonitorNodePreferencePage_Max_Page_Sub"), MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText(Messages.getString("MonitorNodePreferencePage_Max_Page_Sub_Advise"));

		addTextField(enableGroup, Messages.getString("MonitorNodePreferencePage_Refresh_Time"), MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText(Messages.getString("MonitorNodePreferencePage_Refresh_Time_Advise"));

		initializeFields();
		return contentAssistComposite;
	}

	@SuppressWarnings("unchecked")
	protected OverlayPreferenceStore.OverlayKey[] createOverlayStoreKeys()
	{
		ArrayList overlayKeys = new ArrayList();

		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, MonitorNodePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, MonitorNodePreferenceConstants.MAX_PAGE_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, MonitorNodePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, MonitorNodePreferenceConstants.REFRESH_MESSAGE_INTERVAL));

		OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	public void init(IWorkbench workbench)
	{

	}
}