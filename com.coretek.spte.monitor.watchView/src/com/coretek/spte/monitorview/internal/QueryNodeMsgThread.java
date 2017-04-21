/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * ��ѯ�ڵ���Ϣ�߳�
 * 
 * @author ���� 2012-3-14
 */
public class QueryNodeMsgThread extends Thread
{

	// �����ͼ�Ľ���ʱ���
	private long						endTimeStamp;

	// �����ͼ�Ŀ�ʼʱ���
	private long						startTimeStamp;

	// ˢ�¼����ͼ�����߳�
	private RefreshTableTreeDataThread	thread;

	// �����Ϣ��ͼ
	private MonitorDomainView			viewPart;

	public QueryNodeMsgThread(String name, MonitorDomainView viewPart, RefreshTableTreeDataThread thread, long startTimeStamp, long endTimeStamp)
	{
		super(name);
		this.viewPart = viewPart;
		this.thread = thread;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
	}

	public long getEndTimeStamp()
	{
		return endTimeStamp;
	}

	public long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	@Override
	public void run()
	{
		SPTEMsg msgs[] = null;

		// ����û���Ҫ��ѯ����Ϣ��ID���б����б�Ϊ�գ�������ݿ��в�ѯ����Ϣ��ID���б��Ӧ������
		List<String> messageIds = viewPart.getManager().getMessageIds();
		if (messageIds != null && messageIds.size() > 0)
		{

			// �����ݿ��в�ѯ�ӿ�ʼʱ���������ʱ���֮�������
			if (ExecutorSession.getInstance() != null)
			{
				msgs = ExecutorSession.getInstance().querySPTEMsgs(getStartTimeStamp(), (int) (getEndTimeStamp() - getStartTimeStamp()), messageIds.toArray(new String[messageIds.size()]));
				// �����źŷ������Բ�ѯ����Ϣ���з���
				NodeElementParser parser = new NodeElementParser(viewPart);
				parser.parserNodeElementData(msgs);
			}

			// �����ǰ��ͼ���ڼ��״̬����ݵ�ǰ��ѯ��õ����½���ʱ��������µĽ���ʱ���
			if (!viewPart.isTerminated())
			{
				viewPart.setCurrentTimeStamp(getEndTimeStamp());
			}
		}
	}

	@Override
	public String toString()
	{
		return "QueryNodeMsgThread [endTimeStamp=" + endTimeStamp + ", startTimeStamp=" + startTimeStamp + ", thread=" + thread + "]";
	}
}
