/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ʱ��������߳���ѡ������ҳ��
 * 
 * @author ���� 2012-3-14
 */
public class TimeStampPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage
{

	/**
	 * ��ʼ��ȱʡֵ
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

		Group enableGroup = addGroupBox(contentAssistComposite, "���ʱ��������", 3);

		addTextField(enableGroup, "ʱ������̵߳ļ��", TimeStampPreferenceConstants.TIMESTAMP_INTERVAL_NUM, 6, 0, true);
		Label labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(����ֵ��100--300����)");

		addTextField(enableGroup, "ʱ������߳���Ϣ��ʱ����", TimeStampPreferenceConstants.SLEEP_INTERVAL_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(����ֵ��100--2000����)");

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