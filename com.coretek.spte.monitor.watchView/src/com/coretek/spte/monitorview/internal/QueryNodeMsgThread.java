/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * 查询节点消息线程
 * 
 * @author 尹军 2012-3-14
 */
public class QueryNodeMsgThread extends Thread
{

	// 监控视图的结束时间戳
	private long						endTimeStamp;

	// 监控视图的开始时间戳
	private long						startTimeStamp;

	// 刷新监控视图表树线程
	private RefreshTableTreeDataThread	thread;

	// 监控消息视图
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

		// 获得用户需要查询的消息的ID的列表，当列表不为空，则从数据库中查询该消息的ID的列表对应的数据
		List<String> messageIds = viewPart.getManager().getMessageIds();
		if (messageIds != null && messageIds.size() > 0)
		{

			// 从数据库中查询从开始时间戳到结束时间戳之间的数据
			if (ExecutorSession.getInstance() != null)
			{
				msgs = ExecutorSession.getInstance().querySPTEMsgs(getStartTimeStamp(), (int) (getEndTimeStamp() - getStartTimeStamp()), messageIds.toArray(new String[messageIds.size()]));
				// 调用信号分析器对查询的消息进行分析
				NodeElementParser parser = new NodeElementParser(viewPart);
				parser.parserNodeElementData(msgs);
			}

			// 如果当前视图处于监控状态则根据当前查询获得的最新结束时间戳设置新的结束时间戳
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
