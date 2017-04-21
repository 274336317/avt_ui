/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.internal;

import java.util.ArrayList;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitorlogview.views.MonitorLogViewPart;

/**
 * ��ѯ�����Ϣ�߳�
 * 
 * @author ���� 2012-3-14
 */
public class QueryLogMsgThread extends Thread
{

	// ��ǰʱ���
	private long				current;

	// ����ʱ���
	private long				end;

	// ��ʼʱ���
	private long				start;

	// �����־��ͼ
	private MonitorLogViewPart	viewPart;

	public QueryLogMsgThread(String name, MonitorLogViewPart viewPart)
	{
		super(name);
		this.viewPart = viewPart;
	}

	public QueryLogMsgThread(String name, MonitorLogViewPart viewPart, long start, long end, long current)
	{
		super(name);
		this.viewPart = viewPart;
		this.current = current;
		this.start = start;
		this.end = end;
	}

	@Override
	public void run()
	{
		start = viewPart.getStartTimeStamp();

		// ���ڼ��״̬
		if (!viewPart.isTerminated())
		{
			this.handleMonitor();

		} else
		{// ���ڷǼ��״̬
			this.handleHistory();
		}
	}

	/**
	 * ������״̬�µ����ݼ���
	 */
	private void handleMonitor()
	{
		SPTEMsg msgs[] = null;
		if (!viewPart.isLockMonitor())
		{
			// �����ݿ��в�ѯ�ӿ�ʼʱ���������ʱ���֮�������
			if (current > start)
			{
				if (ExecutorSession.getInstance() != null)
				{
					msgs = ExecutorSession.getInstance().querySPTEMsgs(start, (current - start));
				}
			}
			// ��Ӳ�ѯ������Ϣ��ˢ����ͼ��ʾ
			if (msgs != null && msgs.length > 0)
			{
				long previosMsgTime = 0;
				for (SPTEMsg msg : msgs)
				{
					long currentMsgTime = msg.getTimeStamp();
					if (previosMsgTime < currentMsgTime)
					{
						viewPart.getMsgs().add(msg);
						previosMsgTime = currentMsgTime;
					}
				}
				viewPart.setStartTimeStamp(previosMsgTime + 1);

				// ���ݵ�ǰ��ͼ�Ŀ�ʼ����ǰ������ʱ���������ͼ��ʾ�ĵ�ǰ��
				RefreshTableManager manager = new RefreshTableManager(viewPart, start, end, current);
				manager.refreshData();
			}
		}
	}

	/**
	 * ����Ǽ��״̬�µ����ݼ���
	 */
	private void handleHistory()
	{
		SPTEMsg msgs[] = new SPTEMsg[0];
		if (start >= viewPart.getStartTimeStamp())
		{

			// �����ݿ��в�ѯ�ӿ�ʼʱ���������ʱ���֮�������
			if (end > start)
			{
				// �����źŷ������Բ�ѯ����Ϣ���з���
				if (ExecutorSession.getInstance() != null)
				{
					msgs = ExecutorSession.getInstance().querySPTEMsgs(start, (current - start));
				}
			}

			// ��Ӳ�ѯ������Ϣ��ˢ����ͼ��ʾ
			if (msgs != null && msgs.length > 0)
			{
				for (SPTEMsg msg : msgs)
				{
					ArrayList<SPTEMsg> list = viewPart.getMsgs();

					// ֻ��ӱ���Ϣ�б������һ��ʱ��������Ϣ��
					if (list != null && list.size() > 0)
					{
						SPTEMsg m = viewPart.getMsgs().get(list.size() - 1);
						if (msg.getTimeStamp() > m.getTimeStamp())
						{
							viewPart.getMsgs().add(msg);
						}
					} else
					{
						viewPart.getMsgs().add(msg);
					}
				}

				// ���ݵ�ǰ��ͼ�Ŀ�ʼ����ǰ������ʱ���������ͼ��ʾ�ĵ�ǰ��
				RefreshTableManager manager = new RefreshTableManager(viewPart, start, end, current);
				manager.refreshData();
			}
		}
	}

	@Override
	public String toString()
	{
		return StringUtils.concat("QueryLogMsgThread [+��ʼʱ��:", start, "==����ʱ�䣺", end, "==��ǰʱ��", current, "]");
	}
}
