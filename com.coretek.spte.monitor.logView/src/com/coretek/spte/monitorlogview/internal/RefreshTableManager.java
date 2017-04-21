/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.internal;

import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorlogview.views.MonitorLogViewPart;

/**
 * 刷新表视图
 * 
 * @author 尹军 2012-3-14
 */

public class RefreshTableManager
{

	// 当前时间戳
	private long				current;

	// 结束时间戳
	private long				end;

	// 开始时间戳
	private long				start;

	// 监控日志视图
	private MonitorLogViewPart	viewPart;

	/**
	 * @param viewPart
	 * @param start
	 * @param end
	 * @param current </br>
	 */
	public RefreshTableManager(MonitorLogViewPart viewPart, long start, long end, long current)
	{
		super();
		this.viewPart = viewPart;
		this.start = start;
		this.end = end;
		this.current = current;
	}

	/**
	 * 获得当前时间戳
	 * 
	 * @return 当前时间戳</br>
	 */
	public long getCurrent()
	{
		return current;
	}

	/**
	 * 获得结束时间戳
	 * 
	 * @return 结束时间戳 </br>
	 */
	public long getEnd()
	{
		return end;
	}

	/**
	 * 获得开始时间戳
	 * 
	 * @return 开始时间戳 </br>
	 */
	public long getStart()
	{
		return start;
	}

	/**
	 * 根据当前视图的开始、当前、结束时间戳决定视图显示的当前项 </br>
	 */
	public void refreshData()
	{
		Display display = null;
		if (viewPart.getTableViewer() != null)
		{
			display = viewPart.getTableViewer().getControl().getDisplay();
		} else if (viewPart.getTreeViewer() != null)
		{
			display = viewPart.getTreeViewer().getControl().getDisplay();
		} else
		{
			return;
		}
		display.asyncExec(new Runnable()
		{
			public void run()
			{
				if (viewPart.getTableViewer() != null)
				{
					viewPart.getTableViewer().setInput(viewPart.getMsgs());
				}
				if (viewPart.getTreeViewer() != null)
				{
					viewPart.getTreeViewer().setInput(viewPart.getMsgs());
					return;
				}

				// 开始时间戳等于当前时间戳、当前时间戳小于等于结束时间戳
				if (getStart() == getCurrent() && getCurrent() <= getEnd())
				{
					List<SPTEMsg> lists = (List<SPTEMsg>) viewPart.getMsgs();
					for (int i = 0; i < lists.size() - 1; i++)
					{
						if (lists.get(i).getTimeStamp() >= getStart() && lists.get(i).getTimeStamp() <= getEnd())
						{
							viewPart.getTableViewer().getTable().setSelection(i);
							viewPart.getTableViewer().refresh();
							break;

						}
					}
				} else if (getStart() < getCurrent() // 开始时间戳小于当前时间戳、当前时间戳等于结束时间戳
						&& getCurrent() == getEnd())
				{
					List<SPTEMsg> lists = (List<SPTEMsg>) viewPart.getMsgs();
					for (int i = lists.size() - 1; i > 0; i--)
					{
						if (lists.get(i).getTimeStamp() >= getStart() && lists.get(i).getTimeStamp() <= getEnd())
						{
							viewPart.getTableViewer().getTable().setSelection(i);
							viewPart.getTableViewer().refresh();
							break;
						}
					}
				} else if (getStart() < getCurrent()// 开始时间戳小于当前时间戳、当前时间戳小于结束时间戳
						&& getCurrent() < getEnd())
				{
					List<SPTEMsg> lists = (List<SPTEMsg>) viewPart.getMsgs();
					for (int i = lists.size() - 1; i > 0; i--)
					{
						if (lists.get(i).getTimeStamp() >= getStart() && lists.get(i).getTimeStamp() <= getCurrent())
						{
							viewPart.getTableViewer().getTable().setSelection(i);
							viewPart.getTableViewer().refresh();
							break;
						}
					}
				}
			}
		});
	}
}
