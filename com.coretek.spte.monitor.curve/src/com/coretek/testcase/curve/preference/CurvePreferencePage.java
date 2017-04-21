/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.preference;

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

/**
 * 曲线视图首选项配置页面
 * 
 * @author 尹军 2012-3-14
 */
public class CurvePreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage
{

	public static void initDefaults(IPreferenceStore store)
	{
		store.setDefault(CurvePreferenceConstants.MAX_FieldElement_NUM, 6);
		store.setDefault(CurvePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH, 5000);
		store.setDefault(CurvePreferenceConstants.MAX_PAGE_NUM, 5);
		store.setDefault(CurvePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM, 1000);
		store.setDefault(CurvePreferenceConstants.COL_WIDTH, 30);
		store.setDefault(CurvePreferenceConstants.X_INTERVAL_NUM, 20);
		store.setDefault(CurvePreferenceConstants.Y_INTERVAL_NUM, 10);
		store.setDefault(CurvePreferenceConstants.PAINT_CURVE_INTERVAL, 100);
	}

	public CurvePreferencePage()
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

		Group enableGroup = addGroupBox(contentAssistComposite, "曲线配置", 3);

		addTextField(enableGroup, "最大支持信号的个数", CurvePreferenceConstants.MAX_FieldElement_NUM, 6, 0, true);
		Label labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：1--6个)");

		addTextField(enableGroup, "最大支持页长度", CurvePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：4000--8000毫秒)");

		addTextField(enableGroup, "最大支持页数", CurvePreferenceConstants.MAX_PAGE_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：3--10页)");

		addTextField(enableGroup, "最大支持页子项数", CurvePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：500--1000项)");

		addTextField(enableGroup, "值轴列间的宽度", CurvePreferenceConstants.COL_WIDTH, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：30--40像素)");

		addTextField(enableGroup, "时间轴的刻度个数", CurvePreferenceConstants.X_INTERVAL_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：5--15个)");

		addTextField(enableGroup, "值轴的刻度个数", CurvePreferenceConstants.Y_INTERVAL_NUM, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：5--15个)");

		addTextField(enableGroup, "曲线图刷新的间隔时间", CurvePreferenceConstants.PAINT_CURVE_INTERVAL, 6, 0, true);
		labelControl = new Label(enableGroup, SWT.NONE);
		labelControl.setText("(建议值：50--150毫秒)");

		initializeFields();
		return contentAssistComposite;
	}

	@SuppressWarnings("unchecked")
	protected OverlayPreferenceStore.OverlayKey[] createOverlayStoreKeys()
	{
		ArrayList overlayKeys = new ArrayList();

		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.MAX_FieldElement_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.MAX_TIMESTAMP_PAGE_LENGTH));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.MAX_PAGE_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.MAX_PAGE_SUB_ITEM_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.COL_WIDTH));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.X_INTERVAL_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.Y_INTERVAL_NUM));
		overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.INT, CurvePreferenceConstants.PAINT_CURVE_INTERVAL));

		OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	public void init(IWorkbench workbench)
	{
	}
}