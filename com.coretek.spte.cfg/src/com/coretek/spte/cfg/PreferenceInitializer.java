/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.cfg;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * @author ZHANG Yi 2012-05-23
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
{

	public void initializeDefaultPreferences()
	{
		IPreferenceStore store = CfgPlugin.getDefault().getPreferenceStore();
		store.setDefault(ReportPreferencePage.OPEN, true);
		// String workspacePath =
		// ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();
		store.setDefault(ReportPreferencePage.SAVE, System.getProperty("user.home"));
	}

}