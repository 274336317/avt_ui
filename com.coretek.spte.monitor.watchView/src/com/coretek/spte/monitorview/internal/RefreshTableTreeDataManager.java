/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * @author ���� 2012-3-14
 */

public class RefreshTableTreeDataManager
{

	// �����Ϣ��ͼ
	private MonitorDomainView	viewPart;

	public RefreshTableTreeDataManager(MonitorDomainView viewPart)
	{
		this.viewPart = viewPart;
	}

	@SuppressWarnings("deprecation")
	public void refreshData(final List<SPTEMsg> msgList)
	{
		if (viewPart.getTableViewer() == null || viewPart.getTableViewer().getControl().isDisposed())
		{
			return;
		}
		viewPart.getTableViewer().getControl().getDisplay().asyncExec(new Runnable()
		{
			public void run()
			{
				viewPart.getTableViewer().setInput(msgList);
				viewPart.getTableViewer().expandAll();
				viewPart.getTableViewer().refresh();
			}
		});
	}
}
