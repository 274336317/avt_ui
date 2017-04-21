/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * ˢ����Ϣ����ͼ
 * 
 * @author ���� 2012-3-14
 */
public class RefreshTableTreeDataThread extends Thread
{

	// �Ƿ�ȴ���ѯ�̵߳�֪ͨ
	private boolean				isWait;

	// ��Ϣ�б�
	private ArrayList<SPTEMsg>	msgs	= new ArrayList<SPTEMsg>();

	// ��Ϣ�����ͼ
	private MonitorDomainView	viewPart;

	/**
	 * @param name
	 * @param viewPart
	 * @param isWait </br>
	 */
	public RefreshTableTreeDataThread(String name, MonitorDomainView viewPart, boolean isWait)
	{
		super(name);
		this.viewPart = viewPart;
		this.isWait = isWait;
	}

	/**
	 * ����Ϣ�б������Ϣ����
	 * 
	 * @param arg0 ��Ϣ����</br>
	 */
	public synchronized boolean add(SPTEMsg arg0)
	{
		return msgs.add(arg0);
	}

	/**
	 * �����Ϣ�б�
	 * 
	 * @return ��Ϣ�б� </br>
	 */
	public synchronized ArrayList<SPTEMsg> getMsgs()
	{
		return msgs;
	}

	/**
	 * �ж��Ƿ�ȴ���ѯ�̵߳�֪ͨ
	 * 
	 * @return �Ƿ�ȴ���ѯ�̵߳�֪ͨ </br>
	 */
	public boolean isWait()
	{
		return isWait;
	}

	@Override
	public void run()
	{
		RefreshTableTreeDataManager manager = new RefreshTableTreeDataManager(viewPart);
		long current = 0;
		long start = viewPart.getStartTimeStamp();

		// ���ڷǼ��״̬�Ĵ���
		if (viewPart.isTerminated())
		{
			handleTerminate(manager, current, start);
		} else if (!viewPart.isTerminated())
		{
			handleNotTerminate(manager, current, start);
		}
	}

	/**
	 * ������Ϣ�б�
	 * 
	 * @param msgs ��Ϣ�б� </br>
	 */
	public synchronized void setMsgs(ArrayList<SPTEMsg> msgs)
	{
		this.msgs = msgs;
	}

	/**
	 * �����Ƿ�ȴ���ѯ�̵߳�֪ͨ
	 * 
	 * @param isWait �Ƿ�ȴ���ѯ�̵߳�֪ͨ </br>
	 */
	public void setWait(boolean isWait)
	{
		this.isWait = isWait;
	}

	/**
	 * ��ִ��δֹͣ�µ�ˢ�´���
	 * 
	 * @param manager
	 * @param current
	 * @param start
	 */
	private void handleNotTerminate(RefreshTableTreeDataManager manager, long current, long start)
	{
		if (!viewPart.isLockMonitor())
		{
			current = viewPart.getCurrentTimeStamp();
			if (current > start)
			{
				List<NodeElementSet> fields = viewPart.getManager().getAllFields();
				if (fields != null && fields.size() > 0)
				{
					for (NodeElementSet field : fields)
					{
						List<SPTEMsg> list = field.getSPTEMsgElementsToShow(start, current);
						if (list != null && list.size() > 0)
						{
							getMsgs().clear();
							getMsgs().add(list.get(list.size() - 1));
						}
					}

					if (getMsgs() != null && getMsgs().size() > 0)
					{
						manager.refreshData(getMsgs());
						start = getMsgs().get(0).getTimeStamp();
					}
				}

			}
		}
	}

	/**
	 * ��ִ�н����󣬼��ڼ�����ʷ��¼ʱ�Ĵ������
	 * 
	 * @param manager
	 * @param current
	 * @param start
	 */
	private void handleTerminate(RefreshTableTreeDataManager manager, long current, long start)
	{
		current = viewPart.getCurrentTimeStamp();
		if (current > start)
		{
			List<NodeElementSet> fields = viewPart.getManager().getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (NodeElementSet field : fields)
				{
					List<SPTEMsg> list = field.getSPTEMsgElementsToShow(start, current);
					if (list != null && list.size() > 0)
					{
						getMsgs().clear();
						getMsgs().add(list.get(list.size() - 1));
					}
				}

				if (getMsgs() != null && getMsgs().size() > 0)
				{
					manager.refreshData(getMsgs());
				}
			}
		} else if (current == start && current <= viewPart.getEndTimeStamp())
		{
			List<NodeElementSet> fields = viewPart.getManager().getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (NodeElementSet field : fields)
				{
					List<SPTEMsg> list = field.getSPTEMsgElementsToShow(start, viewPart.getEndTimeStamp());
					if (list != null && list.size() > 0)
					{
						getMsgs().clear();
						getMsgs().add(list.get(0));
					}
				}

				if (getMsgs() != null && getMsgs().size() > 0)
				{
					manager.refreshData(getMsgs());
				}
			}
		}
	}
}
