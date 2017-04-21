/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.internal;

import java.util.ArrayList;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitorlogview.views.MonitorLogViewPart;

/**
 * 查询监控消息线程
 * 
 * @author 尹军 2012-3-14
 */
public class QueryLogMsgThread extends Thread
{

	// 当前时间戳
	private long				current;

	// 结束时间戳
	private long				end;

	// 开始时间戳
	private long				start;

	// 监控日志视图
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

		// 处于监控状态
		if (!viewPart.isTerminated())
		{
			this.handleMonitor();

		} else
		{// 处于非监控状态
			this.handleHistory();
		}
	}

	/**
	 * 处理监控状态下的数据加载
	 */
	private void handleMonitor()
	{
		SPTEMsg msgs[] = null;
		if (!viewPart.isLockMonitor())
		{
			// 从数据库中查询从开始时间戳到结束时间戳之间的数据
			if (current > start)
			{
				if (ExecutorSession.getInstance() != null)
				{
					msgs = ExecutorSession.getInstance().querySPTEMsgs(start, (current - start));
				}
			}
			// 添加查询到的消息并刷新视图显示
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

				// 根据当前视图的开始、当前、结束时间戳决定视图显示的当前项
				RefreshTableManager manager = new RefreshTableManager(viewPart, start, end, current);
				manager.refreshData();
			}
		}
	}

	/**
	 * 处理非监控状态下的数据加载
	 */
	private void handleHistory()
	{
		SPTEMsg msgs[] = new SPTEMsg[0];
		if (start >= viewPart.getStartTimeStamp())
		{

			// 从数据库中查询从开始时间戳到结束时间戳之间的数据
			if (end > start)
			{
				// 调用信号分析器对查询的消息进行分析
				if (ExecutorSession.getInstance() != null)
				{
					msgs = ExecutorSession.getInstance().querySPTEMsgs(start, (current - start));
				}
			}

			// 添加查询到的消息并刷新视图显示
			if (msgs != null && msgs.length > 0)
			{
				for (SPTEMsg msg : msgs)
				{
					ArrayList<SPTEMsg> list = viewPart.getMsgs();

					// 只添加比消息列表中最后一个时间戳大的消息项
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

				// 根据当前视图的开始、当前、结束时间戳决定视图显示的当前项
				RefreshTableManager manager = new RefreshTableManager(viewPart, start, end, current);
				manager.refreshData();
			}
		}
	}

	@Override
	public String toString()
	{
		return StringUtils.concat("QueryLogMsgThread [+开始时间:", start, "==结束时间：", end, "==当前时间", current, "]");
	}
}
