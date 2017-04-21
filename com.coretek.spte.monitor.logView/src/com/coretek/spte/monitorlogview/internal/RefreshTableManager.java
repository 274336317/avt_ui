/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.internal;

import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitorlogview.views.MonitorLogViewPart;

/**
 * ˢ�±���ͼ
 * 
 * @author ���� 2012-3-14
 */

public class RefreshTableManager
{

	// ��ǰʱ���
	private long				current;

	// ����ʱ���
	private long				end;

	// ��ʼʱ���
	private long				start;

	// �����־��ͼ
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
	 * ��õ�ǰʱ���
	 * 
	 * @return ��ǰʱ���</br>
	 */
	public long getCurrent()
	{
		return current;
	}

	/**
	 * ��ý���ʱ���
	 * 
	 * @return ����ʱ��� </br>
	 */
	public long getEnd()
	{
		return end;
	}

	/**
	 * ��ÿ�ʼʱ���
	 * 
	 * @return ��ʼʱ��� </br>
	 */
	public long getStart()
	{
		return start;
	}

	/**
	 * ���ݵ�ǰ��ͼ�Ŀ�ʼ����ǰ������ʱ���������ͼ��ʾ�ĵ�ǰ�� </br>
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

				// ��ʼʱ������ڵ�ǰʱ�������ǰʱ���С�ڵ��ڽ���ʱ���
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
				} else if (getStart() < getCurrent() // ��ʼʱ���С�ڵ�ǰʱ�������ǰʱ������ڽ���ʱ���
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
				} else if (getStart() < getCurrent()// ��ʼʱ���С�ڵ�ǰʱ�������ǰʱ���С�ڽ���ʱ���
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
