/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import java.io.File;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public class ICDFilesModifyListener implements ModifyListener
{
	private CheckboxTreeViewer	viewer;

	private CheckboxTreeViewer	topicViewer;

	private CfgDialog			dlg;

	public ICDFilesModifyListener(CfgDialog dlg, CheckboxTreeViewer viewer, CheckboxTreeViewer topicViewer)
	{
		this.dlg = dlg;
		this.viewer = viewer;
		this.topicViewer = topicViewer;

	}

	@Override
	public void modifyText(ModifyEvent e)
	{
		if (StringUtils.isNotNull(this.dlg.getICDPath()))
		{
			ClazzManager icdManager = TemplateEngine.getEngine().parseICD(new File(this.dlg.getICDPath()));
			if (icdManager != null)
			{
				List<ICDFunctionDomain> domains = TemplateUtils.getAllFunctionDomains(icdManager);
				viewer.setInput(domains);
				viewer.refresh();
				viewer.expandAll();
				topicViewer.setInput(icdManager.getAllTopics());
				topicViewer.refresh();
				topicViewer.expandAll();
			}
		}
	}

}
