/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.spte.monitor.ui.MonitorPlugin;

/**
 * 时间戳产生线程首选项配置初始化
 * 
 * @author 尹军 2012-3-14
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
