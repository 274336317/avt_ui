/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionDomainMsg;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDFunctionSubDomainMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import static com.coretek.spte.monitor.cfg.CfgDialog.updateParent;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
class TopicViewerCheckStateListener implements ICheckStateListener
{
	private CheckboxTreeViewer	topicViewer;

	private CheckboxTreeViewer	viewer;

	public TopicViewerCheckStateListener(CheckboxTreeViewer topicViewer, CheckboxTreeViewer viewer)
	{
		this.topicViewer = topicViewer;
		this.viewer = viewer;
	}

	private void doCheck(Object[] objs, boolean checked)
	{
		for (Object obj : objs)
		{
			viewer.setChecked(obj, checked);
			viewer.setSubtreeChecked(obj, checked);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void checkStateChanged(CheckStateChangedEvent event)
	{
		// 当任何一个主题被选中之后，都需要选中引用它的消息
		topicViewer.setGrayChecked(event.getElement(), false);
		topicViewer.setChecked(event.getElement(), event.getChecked());
		Entity entity = (Entity) event.getElement();
		if (viewer.getInput() instanceof List<?>)
		{
			List<ICDFunctionDomain> domains = (List<ICDFunctionDomain>) viewer.getInput();
			String topicSymbol = entity.getFieldValue(Constants.ICD_TOPIC_SYMBOL).toString();
			List<ICDFunctionCellMsg> cellMsgs = TemplateUtils.getCellMsgsByTopicSymbol(domains, topicSymbol);
			this.doCheck(cellMsgs.toArray(), event.getChecked());

			List<ICDFunctionNodeMsg> nodeMsgs = TemplateUtils.getNodeMsgsByTopicSymbol(domains, topicSymbol);
			this.doCheck(nodeMsgs.toArray(), event.getChecked());

			List<ICDFunctionDomainMsg> domainMsgs = TemplateUtils.getDomainMsgsByTopicSymbol(domains, topicSymbol);
			this.doCheck(domainMsgs.toArray(), event.getChecked());

			List<ICDFunctionSubDomainMsg> subDomainMsgs = TemplateUtils.getSubDomainMsgsByTopicSymbol(domains, topicSymbol);
			this.doCheck(subDomainMsgs.toArray(), event.getChecked());

			for (ICDFunctionCellMsg msg : cellMsgs)
			{
				updateParent(msg, viewer);
			}
			for (ICDFunctionNodeMsg msg : nodeMsgs)
			{
				updateParent(msg, viewer);
			}
			for (ICDFunctionDomainMsg msg : domainMsgs)
			{
				updateParent(msg, viewer);
			}
			for (ICDFunctionSubDomainMsg msg : subDomainMsgs)
			{
				updateParent(msg, viewer);
			}
		}

	}

}