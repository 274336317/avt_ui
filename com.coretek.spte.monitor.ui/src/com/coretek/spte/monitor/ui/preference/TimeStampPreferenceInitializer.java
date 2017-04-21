/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitor.ui.MonitorPlugin;

/**
 * ʱ��������߳���ѡ�����ó�ʼ��
 * 
 * @author ���� 2012-3-14
 */
public class TimeStampPreferenceInitializer extends AbstractPreferenceInitializer
{

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences() <br/>
	 */
	public void initializeDefaultPreferences()
	{
		final IPreferenceStore store = MonitorPlugin.getDefault().getPreferenceStore();
		TimeStampPreferencePage.initDefaults(store);
	}
}
