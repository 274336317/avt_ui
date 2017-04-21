/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 查询用户指定的消息信号并解析信号值的线程,根据当前需查询的起始、结束时间戳，
 * 通过ExecutorSession查询缓冲区中相应的消息信号的数据，并解析生成信号的时间戳、信号值。
 * 
 * @author 尹军 2012-3-14
 */
public class QueryFieldMsgThread extends Thread
{

	// 曲线视图的结束时间戳
	private long		endTimeStamp;

	// 曲线视图的绘图线程
	private PaintThread	paintThread;

	// 曲线视图的开始时间戳
	private long		startTimeStamp;

	// 曲线视图
	private CurveView	viewPart;

	private static int	id	= 0;

	public QueryFieldMsgThread(String name, CurveView viewPart, PaintThread paintThread, long startTimeStamp, long endTimeStamp)
	{
		super("查询线程" + id++);
		this.viewPart = viewPart;
		this.paintThread = paintThread;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
	}

	/**
	 * 获得当前线程准备查询的结束时间戳
	 * 
	 * @return 结束时间戳 </br>
	 */
	public long getEndTimeStamp()
	{
		return endTimeStamp;
	}

	/**
	 * 获得当前线程准备查询的开始时间戳
	 * 
	 * @return 开始时间戳 </br>
	 */
	public long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 根据当前需查询的起始、结束时间戳，通过ExecutorSession查询缓冲区中相应的消息信号的数据，并解析生成信号的时间戳、信号值。
	 * 
	 * @see java.lang.Thread#run() <br/>
	 */
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
				msgs = ExecutorSession.getInstance().querySPTEMsgs(getStartTimeStamp(), (int) (getEndTimeStamp() - getStartTimeStamp() + 5000L), messageIds.toArray(new String[messageIds.size()]));
			}
			// 调用信号分析器对查询的消息进行分析
			FieldElementParser parser = new FieldElementParser(viewPart);
			parser.parse(msgs);

			// 如果当前曲线绘图处于监控状态则根据当前查询获得的最新结束时间戳设置新的结束时间戳
			if (!viewPart.isTerminated())
			{
				viewPart.setEndTimeStamp(endTimeStamp);

				// 如果当前曲线绘图处于监控状态则根据当前查询获得的最新结束时间戳设置新的当前时间戳
				if (viewPart.getCurrentTimeStamp() <= getEndTimeStamp())
				{
					viewPart.setCurrentTimeStamp(getEndTimeStamp());
				}
			}

		}
	}

	/**
	 * 设置当前线程准备查询的结束时间戳
	 * 
	 * @param endTimeStamp 结束时间戳 </br>
	 */
	public void setEndTimeStamp(long endTimeStamp)
	{
		this.endTimeStamp = endTimeStamp;
	}

	/**
	 * 设置当前线程准备查询的开始时间戳
	 * 
	 * @param startTimeStamp 开始时间戳 </br>
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
