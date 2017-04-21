/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.ui.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 时间戳产生线程首选项配置页面
 * 
 * @author 尹军 2012-3-14
 */
public class TimeStampPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * 初始化缺省值
	 * 
	 * @param store
	 */
	public static void initDefaults(IPreferenceStore store)
	{
		store.setDefault(TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM, 400);
		store.setDefault(TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM, 100);
	}

	public TimeStampPreferencePage()
	{
		super();
	}

	protected Control createContents(Composite parent)
	{
		fOverlayStore.load();
		fOverlayStore.start();

		Composite contentAssistComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		contentAssistComposite.setLayout(layout);

		Group enableGroup = addGroupBox(contentAssistComposite, "监控时间间隔配置", 3);

		addTextField(enableGroup, "时间产生线程的间隔", TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM, 6, 0, true);
		Label labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：100--300毫秒)");

		addTextField(enableGroup, "时间产生线程休息的时间间隔", TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：100--2000毫秒)");

		initializeFields();
		return contentAssistComposite;
	}

	@SuppressWarnings("unchecked")
	protected OverlayPreferenceStore.OverlayKey[] createOverlayStoreKeys()
	{
		List overlayKeys = new ArrayList();

		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM));

		OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	public void init(IWorkbench workbench)
	{

	}
}