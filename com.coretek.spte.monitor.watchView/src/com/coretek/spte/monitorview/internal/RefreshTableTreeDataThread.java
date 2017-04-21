/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * 刷新消息表视图
 * 
 * @author 尹军 2012-3-14
 */
public class RefreshTableTreeDataThread extends Thread
{

	// 是否等待查询线程的通知
	private boolean				isWait;

	// 消息列表
	private ArrayList<SPTEMsg>	msgs	= new ArrayList<SPTEMsg>();

	// 消息监控视图
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
	 * 向消息列表添加消息对象
	 * 
	 * @param arg0 消息对象</br>
	 */
	public synchronized boolean add(SPTEMsg arg0)
	{
		return msgs.add(arg0);
	}

	/**
	 * 获得消息列表
	 * 
	 * @return 消息列表 </br>
	 */
	public synchronized ArrayList<SPTEMsg> getMsgs()
	{
		return msgs;
	}

	/**
	 * 判断是否等待查询线程的通知
	 * 
	 * @return 是否等待查询线程的通知 </br>
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

		// 处于非监控状态的处理
		if (viewPart.isTerminated())
		{
			handleTerminate(manager, current, start);
		} else if (!viewPart.isTerminated())
		{
			handleNotTerminate(manager, current, start);
		}
	}

	/**
	 * 设置消息列表
	 * 
	 * @param msgs 消息列表 </br>
	 */
	public synchronized void setMsgs(ArrayList<SPTEMsg> msgs)
	{
		this.msgs = msgs;
	}

	/**
	 * 设置是否等待查询线程的通知
	 * 
	 * @param isWait 是否等待查询线程的通知 </br>
	 */
	public void setWait(boolean isWait)
	{
		this.isWait = isWait;
	}

	/**
	 * 在执行未停止下的刷新处理
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
	 * 在执行结束后，即在加载历史记录时的处理策略
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
