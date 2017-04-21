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
import com.coretek.common.template.Helper;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionDomainMsg;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDFunctionSubDomainMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
class CheckStateListener implements ICheckStateListener
{

	private CheckboxTreeViewer	viewer;

	private CheckboxTreeViewer	topicViewer;

	public CheckStateListener(CheckboxTreeViewer viewer, CheckboxTreeViewer topicViewer)
	{
		this.viewer = viewer;
		this.topicViewer = topicViewer;
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event)
	{
		viewer.refresh();

		Object obj = event.getElement();
		viewer.setGrayChecked(obj, false);
		viewer.setChecked(obj, event.getChecked());
		viewer.setSubtreeChecked(obj, event.getChecked());
		for (Entity e : (List<Entity>) topicViewer.getInput())
		{
			topicViewer.setSubtreeChecked(e, false);
		}
		for (Object element : viewer.getCheckedElements())
		{
			updateTopicViwer(element);
		}

		CfgDialog.updateParent(obj, viewer);
	}

	/**
	 * 联动所选中的功能节点消息发生改变后更新相应的主题
	 * 
	 * @param objs
	 */
	@SuppressWarnings("unchecked")
	private void updateTopicViwer(Object obj)
	{
		if (obj instanceof Helper)
		{
			Helper helper = (Helper) obj;
			if (helper.getAttribute("msgTopicName") == null)
			{
				return;
			}
			Object value = helper.getAttribute("msgTopicName").getValue();
			List<Entity> list = (List<Entity>) topicViewer.getInput();
			Entity entity = null;
			for (Entity e : list)
			{
				if (e.getAttribute("topicName").getValue().equals(value))
				{
					entity = e;
					break;
				}
			}
			if (entity != null)
			{
				String topicSymbol = entity.getFieldValue(Constants.ICD_TOPIC_SYMBOL).toString();
				List<ICDFunctionDomain> domains = (List<ICDFunctionDomain>) viewer.getInput();
				List<ICDFunctionNodeMsg> nodeMsgs = TemplateUtils.getNodeMsgsByTopicSymbol(domains, topicSymbol);
				List<ICDFunctionCellMsg> cellMsgs = TemplateUtils.getCellMsgsByTopicSymbol(domains, topicSymbol);
				List<ICDFunctionDomainMsg> domainMsgs = TemplateUtils.getDomainMsgsByTopicSymbol(domains, topicSymbol);
				List<ICDFunctionSubDomainMsg> subDomainMsgs = TemplateUtils.getSubDomainMsgsByTopicSymbol(domains, topicSymbol);
				int length = nodeMsgs.size() + cellMsgs.size() + domainMsgs.size() + subDomainMsgs.size();
				int checkLength = 0;
				for (Object o : viewer.getCheckedElements())
				{
					if (o instanceof Helper && ((Helper) o).getAttribute("msgTopicName") != null && ((Helper) o).getAttribute("msgTopicName").getValue().equals(value))
					{
						checkLength++;
					}
				}
				if (checkLength == 0)
				{
					topicViewer.setGrayChecked(entity, false);
					topicViewer.setChecked(entity, false);
				} else if (checkLength < length)
				{
					topicViewer.setChecked(entity, false);
					topicViewer.setGrayChecked(entity, true);
				} else
				{
					topicViewer.setGrayChecked(entity, false);
					topicViewer.setChecked(entity, true);
				}

			}
		}
	}

}