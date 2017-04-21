/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.testcase.curve.CurveViewPlugin;

/**
 * ������ͼ��ѡ�����ó�ʼ��
 * 
 * @author ���� 2012-3-14
 */
public class CurvePreferenceInitializer extends AbstractPreferenceInitializer
{
	public void initializeDefaultPreferences()
	{
		final IPreferenceStore store = CurveViewPlugin.getDefault().getPreferenceStore();
		CurvePreferencePage.initDefaults(store);
	}
}