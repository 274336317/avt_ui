/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.testcase.curve.CurveViewPlugin;

/**
 * 曲线视图首选项配置初始化
 * 
 * @author 尹军 2012-3-14
 */
public class CurvePreferenceInitializer extends AbstractPreferenceInitializer
{
	public void initializeDefaultPreferences()
	{
		final IPreferenceStore store = CurveViewPlugin.getDefault().getPreferenceStore();
		CurvePreferencePage.initDefaults(store);
	}
}