/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * ���͸��ͼ����
 * 
 * @author ���� 2012-3-14
 */
public class MonitorPerspectiveFactory implements IPerspectiveFactory
{

	public MonitorPerspectiveFactory()
	{
		super();
	}

	private void addCWizardShortcuts(IPageLayout layout)
	{

	}

	public void createInitialLayout(IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// �����־��ͼ
		IFolderLayout folder1 = layout.createFolder("LEFT", IPageLayout.LEFT, 0.25f, editorArea);
		folder1.addView("com.coretek.spte.monitorLog.view");

		// �����Ϣ��ͼ
		IFolderLayout folder2 = layout.createFolder("LeftBottom", IPageLayout.BOTTOM, (float) 0.60, "com.coretek.spte.monitorLog.view"); //$NON-NLS-1$
		folder2.addView("com.coretek.testcase.monitorView.monitorDomainView");

		// ��ض�����ͼ
		IFolderLayout folder3 = layout.createFolder("RIGHT", IPageLayout.RIGHT, 0.7f, editorArea);
		folder3.addView("com.coretek.spte.monitor.monitorView");

		// ���������ͼ
		IFolderLayout folder4 = layout.createFolder("RightBottom", IPageLayout.BOTTOM, (float) 0.7, editorArea); //$NON-NLS-1$
		folder4.addView("com.coretek.tools.curve.views.CurveView");

		layout.addActionSet("com.coretek.testcase.curve.actionSet");
	}
}
