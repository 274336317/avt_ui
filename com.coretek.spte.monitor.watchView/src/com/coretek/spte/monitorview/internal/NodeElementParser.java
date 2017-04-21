/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.Constants;
import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * ��Ϣ�ڵ������
 * 
 * @author ���� 2012-3-14
 */
public class NodeElementParser
{
	// ��Ϣ�����ͼ
	private MonitorDomainView	viewPart;

	public NodeElementParser(MonitorDomainView viewPart)
	{
		this.viewPart = viewPart;
	}

	/**
	 * ������Ϣ�ڵ���Ϣ
	 * 
	 * @param msgs �ڵ���Ϣ
	 */
	public void parserNodeElementData(SPTEMsg msgs[])
	{
		if (msgs != null && msgs.length > 0)
		{ // ��������Ϣ
			for (SPTEMsg msg : msgs)
			{
				List<NodeElementSet> fields = viewPart.getManager().getAllFields();
				for (NodeElementSet field : fields)
				{ // �û����ù��ܽڵ�
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
