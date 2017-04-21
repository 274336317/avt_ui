/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.action;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;

/**
 * 加载历史记录
 * 
 * @author 尹军 2012-3-14
 */
public class LoadHistroyAction extends ActionDelegate implements IWorkbenchWindowActionDelegate
{

	private class ActionObservable extends Observable implements Observer
	{
		/**
		 * 通过事件更新监听者
		 */
		/*
		 * (non-Javadoc) 通过事件更新监听者
		 * 
		 * @see java.util.Observer#update(java.util.Observable,
		 * java.lang.Object) <br/>
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
					action.setEnabled(false);
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
					action.setEnabled(true);
					break;
				}

				default:
					break;
			}
		}

		public void notifyObservers(Object arg)
		{
			this.setChanged();
			super.notifyObservers(arg);
			this.clearChanged();
		}
	}

	private ActionObservable	observable	= new ActionObservable();

	/**
	 * 加载历史记录
	 */
	private IAction				action;

	/**
	 * 是否向时间戳管理器注册
	 */
	private boolean				isRegister	= false;

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
			MonitorEventManager.getMonitorEventManager().deleteObserver(observable);
			observable.deleteObserver(MonitorEventManager.getMonitorEventManager());
			setRegister(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow) <br/>
	 */
	public void init(IAction action)
	{
		this.action = action;
		this.action.setEnabled(true);
		if (!isRegister())
		{
			// 将自身注册为监听者
			MonitorEventManager.getMonitorEventManager().addObserver(observable);
			observable.addObserver(MonitorEventManager.getMonitorEventManager());
			setRegister(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow) <br/>
	 */
	@Override
	public void init(IWorkbenchWindow window)
	{

	}

	/**
	 * @return 是否向时间戳管理器注册 </br>
	 */
	public boolean isRegister()
	{
		return isRegister;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 * <br/>
	 */
	@Override
	public void run(IAction action)
	{
		// 打开文件对话框，让用户选择数据库文件
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.db" });
		String res = dialog.open();
		if (res != null)
		{
			this.action.setEnabled(false);

			// 获得数据库文件路径
			String path = dialog.getFilterPath();
			path = StringUtils.concat(path, "/", dialog.getFileName());
			path = path.replaceAll("\\\\", "/");

			// 通知观察者对象设置了加载事件，并通知获得的数据库文件路径
			try
			{
				if (observable.countObservers() > 0)
				{
					observable.notifyObservers(new Event(Event.EVENT_LOAD));
				}

				ExecutorSession.setRunning(true);
				LoadJob job = new LoadJob(path);
				job.setUser(true);
				job.schedule();

				MonitorEventManager.getMonitorEventManager().registerObserver();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			action.setEnabled(true);
		}
	}

	/**
	 * @param isRegister 是否向时间戳管理器注册
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}
}
