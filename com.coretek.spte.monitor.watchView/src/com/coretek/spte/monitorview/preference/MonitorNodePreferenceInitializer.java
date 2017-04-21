/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitorview.MsgWatchViewPlugin;

/**
 * ��ʼ����ѡ���ȱʡֵ
 * 
 * @author ���� 2012-3-14
 */
public class MonitorNodePreferenceInitializer extends AbstractPreferenceInitializer
{
	public void initializeDefaultPreferences()
	{
		final IPreferenceStore store = MsgWatchViewPlugin.getDefault().getPreferenceStore();
		MonitorNodePreferencePage.initDefaults(store);
	}
}
