/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.Constants;
import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * 消息节点分析器
 * 
 * @author 尹军 2012-3-14
 */
public class NodeElementParser
{
	// 消息监控视图
	private MonitorDomainView	viewPart;

	public NodeElementParser(MonitorDomainView viewPart)
	{
		this.viewPart = viewPart;
	}

	/**
	 * 分析消息节点信息
	 * 
	 * @param msgs 节点信息
	 */
	public void parserNodeElementData(SPTEMsg msgs[])
	{
		if (msgs != null && msgs.length > 0)
		{ // 待分析消息
			for (SPTEMsg msg : msgs)
			{
				List<NodeElementSet> fields = viewPart.getManager().getAllFields();
				for (NodeElementSet field : fields)
				{ // 用户配置功能节点
					SPTEMsg spteMsg = field.getMonitorMsgNode();
					if (spteMsg instanceof SPTEMsg)
					{
						if (spteMsg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString().equals(msg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString()))
						{
							field.addSPTEMsgElement(msg);
						}
					}
				}
			}
		}
	}
}
