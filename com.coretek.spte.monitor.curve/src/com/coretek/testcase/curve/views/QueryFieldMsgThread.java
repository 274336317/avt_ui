/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * ��ѯ�û�ָ������Ϣ�źŲ������ź�ֵ���߳�,���ݵ�ǰ���ѯ����ʼ������ʱ�����
 * ͨ��ExecutorSession��ѯ����������Ӧ����Ϣ�źŵ����ݣ������������źŵ�ʱ������ź�ֵ��
 * 
 * @author ���� 2012-3-14
 */
public class QueryFieldMsgThread extends Thread
{

	// ������ͼ�Ľ���ʱ���
	private long		endTimeStamp;

	// ������ͼ�Ļ�ͼ�߳�
	private PaintThread	paintThread;

	// ������ͼ�Ŀ�ʼʱ���
	private long		startTimeStamp;

	// ������ͼ
	private CurveView	viewPart;

	private static int	id	= 0;

	public QueryFieldMsgThread(String name, CurveView viewPart, PaintThread paintThread, long startTimeStamp, long endTimeStamp)
	{
		super("��ѯ�߳�" + id++);
		this.viewPart = viewPart;
		this.paintThread = paintThread;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
	}

	/**
	 * ��õ�ǰ�߳�׼����ѯ�Ľ���ʱ���
	 * 
	 * @return ����ʱ��� </br>
	 */
	public long getEndTimeStamp()
	{
		return endTimeStamp;
	}

	/**
	 * ��õ�ǰ�߳�׼����ѯ�Ŀ�ʼʱ���
	 * 
	 * @return ��ʼʱ��� </br>
	 */
	public long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	/*
	 * (non-Javadoc)
	 * ���ݵ�ǰ���ѯ����ʼ������ʱ�����ͨ��ExecutorSession��ѯ����������Ӧ����Ϣ�źŵ����ݣ������������źŵ�ʱ������ź�ֵ��
	 * 
	 * @see java.lang.Thread#run() <br/>
	 */
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
				msgs = ExecutorSession.getInstance().querySPTEMsgs(getStartTimeStamp(), (int) (getEndTimeStamp() - getStartTimeStamp() + 5000L), messageIds.toArray(new String[messageIds.size()]));
			}
			// �����źŷ������Բ�ѯ����Ϣ���з���
			FieldElementParser parser = new FieldElementParser(viewPart);
			parser.parse(msgs);

			// �����ǰ���߻�ͼ���ڼ��״̬����ݵ�ǰ��ѯ��õ����½���ʱ��������µĽ���ʱ���
			if (!viewPart.isTerminated())
			{
				viewPart.setEndTimeStamp(endTimeStamp);

				// �����ǰ���߻�ͼ���ڼ��״̬����ݵ�ǰ��ѯ��õ����½���ʱ��������µĵ�ǰʱ���
				if (viewPart.getCurrentTimeStamp() <= getEndTimeStamp())
				{
					viewPart.setCurrentTimeStamp(getEndTimeStamp());
				}
			}

		}
	}

	/**
	 * ���õ�ǰ�߳�׼����ѯ�Ľ���ʱ���
	 * 
	 * @param endTimeStamp ����ʱ��� </br>
	 */
	public void setEndTimeStamp(long endTimeStamp)
	{
		this.endTimeStamp = endTimeStamp;
	}

	/**
	 * ���õ�ǰ�߳�׼����ѯ�Ŀ�ʼʱ���
	 * 
	 * @param startTimeStamp ��ʼʱ��� </br>
	 */
	public void setStartTimeStamp(long startTimeStamp)
	{
		this.startTimeStamp = startTimeStamp;
	}

	@Override
	public String toString()
	{
		return "QueryFieldMsgThread [" + this.paintThread.toString() + "]";
	}

}
