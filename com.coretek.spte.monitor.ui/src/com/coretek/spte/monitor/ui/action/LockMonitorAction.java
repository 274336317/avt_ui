/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.action;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitor.ui.manager.TimeStampManager;

/**
 * 锁定、解锁监控
 * 
 * @author 尹军 2012-3-14
 */
public class LockMonitorAction extends Observable implements IWorkbenchWindowActionDelegate, Observer
{

	/**
	 * 是否锁定监控
	 */
	private volatile boolean	isLockMonitor	= false;

	/**
	 * 是否向时间戳管理器注册
	 */
	private volatile boolean	isRegister		= false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose() <br/>
	 * <b>作者</b> 尹军 </br> <b>日期</b> 2012-3-13
	 */
	@Override
	public void dispose()
	{
		if (isRegister())
		{
			MonitorEventManager.getMonitorEventManager().deleteObserver(this);
			this.deleteObserver(MonitorEventManager.getMonitorEventManager());
			setRegister(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window)
	{
		if (!isRegister())
		{
			// 将自身注册为监听者
			MonitorEventManager.getMonitorEventManager().addObserver(this);
			this.addObserver(MonitorEventManager.getMonitorEventManager());
			setLockMonitor(false);
			setRegister(true);
		}
	}

	/**
	 * @return 是否锁定监控
	 */
	public boolean isLockMonitor()
	{
		return isLockMonitor;
	}

	/**
	 * @return 是否向时间戳管理器注册
	 */
	public boolean isRegister()
	{
		return isRegister;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action)
	{
		try
		{
			if (TimeStampManager.getTimeStampManager() != null)
			{

				if (!isLockMonitor())
				{
					setLockMonitor(true);

					// 通知观察者对象设置了锁定事件
					if (this.countObservers() > 0)
					{
						this.setChanged();
						this.notifyObservers(new Event(Event.EVENT_LOCK));
						this.clearChanged();
					}
				} else
				{
					setLockMonitor(false);

					// 通知观察者对象设置了解锁事件
					if (this.countObservers() > 0)
					{
						this.setChanged();
						this.notifyObservers(new Event(Event.EVENT_UNLOCK));
						this.clearChanged();
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>作者</b> 尹军 </br>
	 * <b>日期</b> 2012-3-13
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

	/**
	 * 设置是否锁定监控
	 * 
	 */
	/**
	 * 设置是否锁定监控
	 * 
	 * @param isLockMonitor 是否锁定监控
	 */
	public void setLockMonitor(boolean isLockMonitor)
	{
		this.isLockMonitor = isLockMonitor;
	}

	/**
	 * @param isRegister 是否向时间戳管理器注册
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}

	/*
	 * (non-Javadoc) 通过事件更新监听者
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object arg)
	{
		Event event = (Event) arg;
		switch (event.getEventType())
		{
			case Event.EVENT_LOCK:
				break;

			case Event.EVENT_UNLOCK:
				break;

			// 接受启动监控事件并进行处理
			case Event.EVENT_START:
			{
				break;
			}

				// 接受加载监控历史记录事件并进行处理
			case Event.EVENT_LOAD:
			{
				break;
			}

				// 接受停止监控事件并进行处理
			case Event.EVENT_STOP:
			{
				break;
			}

			default:
				break;
		}
	}
}
