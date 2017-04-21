/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * @author 尹军 2012-3-14
 */

public class RefreshTableTreeDataManager
{

	// 监控消息视图
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
