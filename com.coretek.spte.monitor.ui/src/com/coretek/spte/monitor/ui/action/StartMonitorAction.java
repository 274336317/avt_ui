/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.action;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.data.DataPlugin;
import com.coretek.spte.monitor.cfg.CfgDialog;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitor.ui.manager.TimeStampManager;

/**
 * 启动监控
 * 
 * @author 尹军 2012-3-14
 */
public class StartMonitorAction extends ActionDelegate implements IWorkbenchWindowActionDelegate
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
		 * java.lang.Object)
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

					// 接受用户选择某个时间戳事件并进行处理
				case Event.EVENT_TIME_SELECTED:
				{
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
	 * 启动监控
	 */
	private IAction				action;

	/**
	 * 是否向时间戳管理器注册
	 */
	private volatile boolean	isRegister	= false;

	private IWorkbenchWindow	window;

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
	 * IWorkbenchWindow)
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
	 * IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window)
	{
		this.window = window;
	}

	/**
	 * @return 是否向时间戳管理器注册</br> <b>作者</b> 尹军 </br> <b>日期</b> 2012-3-13
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
		CfgDialog dialog = CfgDialog.getInstance();
		if (dialog.open() == Window.OK)
		{
			this.action.setEnabled(false);
			final List<SPTEMsg> msgs = dialog.getSelectedMsgs();
			final String icdPath = dialog.getIcdPath();
			final String endian = dialog.getEndian();
			Job job = new Job("纯监控执行")
			{

				protected IStatus run(IProgressMonitor monitor)
				{
					ExecutorSession.setRunning(true);
					ExecutorSession manager = ExecutorSession.getInstanceForMonitor(msgs.toArray(new SPTEMsg[msgs.size()]));
					if (manager != null)
					{
						manager.setMonitorCfg(msgs, icdPath, endian);
						try
						{
							String executorPath = EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath();
							String command = StringUtils.concat("cmd /c start /b ", executorPath, File.separator, "executor", File.separator, "testParser.exe");
							manager.launchMonitor(command, null);
						} catch (TimeoutException e)
						{
							e.printStackTrace();
						}
					}

					TimeStampManager.getTimeStampManager().startMonitor();
					try
					{
						// 通过当前系统时间生成数据库的文件名
						if (TimeStampManager.getTimeStampManager() != null)
						{
							if (observable.countObservers() > 0)
							{
								// 通知观察者对象监控开始事件，并通知数据库的路径
								observable.notifyObservers(new Event(Event.EVENT_START));
							}
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					return Status.OK_STATUS;
				}
			};
			job.addJobChangeListener(new JobChangeAdapter()
			{

				@Override
				public void done(IJobChangeEvent event)
				{
//					System.out.println("hhhhh");

				}

			});
			job.schedule();
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
