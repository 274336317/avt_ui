/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

/**
 * 绘图线程，根据当前曲线视图的起始时间戳、结束时间戳、当前时间戳绘制当前帧的曲线图。
 * 
 * @author 尹军 2012-3-14
 */
public class PaintThread extends Thread
{

	// 是否等待查询线程的唤起通知
	private boolean		isWait;

	// 曲线视图
	private CurveView	viewPart;

	private static int	id	= 0;

	public PaintThread(String name, CurveView viewPart, boolean isWait)
	{
		super("绘制线程" + id);
		this.viewPart = viewPart;
		this.isWait = isWait;
		if (isWait)
			id++;
	}

	/**
	 * 查询是否等待查询线程的唤起通知
	 * 
	 * @return 是否等待查询线程的唤起通知 </br>
	 */
	public boolean isWait()
	{
		return isWait;
	}

	/*
	 * (non-Javadoc) 在线程中调用曲线绘图管理器进行画曲线图
	 * 
	 * @see java.lang.Thread#run() <br/>
	 */
	@Override
	public void run()
	{
		PaintManager paintManager = new PaintManager(viewPart);

		if (viewPart.isTerminated())
		{
			paintManager.paint();
		} else
		{
			// 当前曲线视图处于监控状态，则循环进行刷新曲线绘图界面。
			if (!viewPart.isTerminated())
			{
				if (!viewPart.isLockMonitor())
				{
					paintManager.paint();

					try
					{
						Thread.sleep(viewPart.getPaintTime());
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					setTimeStamp();
				}
			}
		}
		return;
	}

	/**
	 * 设置曲线视图的开始时间戳、结束时间戳、当前时间戳。 </br>
	 */
	private void setTimeStamp()
	{
		long current = viewPart.getCurrentTimeStamp();
		long start = viewPart.getStartTimeStamp();
		long end = viewPart.getEndTimeStamp();

		if (end - start > viewPart.getPreferenceManager().getTimestampLengthEachPage())
		{
			viewPart.setStartTimeStamp(current - viewPart.getPreferenceManager().getTimestampLengthEachPage());
			viewPart.setEndTimeStamp(current);
		} else if (end - start == viewPart.getPreferenceManager().getTimestampLengthEachPage())
		{
			viewPart.setStartTimeStamp(start + current - end);
			viewPart.setEndTimeStamp(current);
		} else if (end - start > 0)
		{
			viewPart.setEndTimeStamp(current);
		} else
		{
			if (current >= viewPart.getPreferenceManager().getTimestampLengthEachPage())
			{
				viewPart.setStartTimeStamp(current - viewPart.getPreferenceManager().getTimestampLengthEachPage());
				viewPart.setEndTimeStamp(current);
			} else
			{
				if (!viewPart.isTerminated())
				{
					viewPart.setEndTimeStamp(current);
				}
			}
		}
	}

	/**
	 * 设置是否等待查询线程的唤起通知
	 * 
	 * @param isWait 是否等待查询线程的唤起通知 </br>
	 */
	public void setWait(boolean isWait)
	{
		this.isWait = isWait;
	}
}
