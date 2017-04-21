package com.coretek.spte.monitor.ui.action;

/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.dialog.ChangeMonitoredTopicsDialog;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;

/**
 * @author 孙大巍 2012-4-10
 */
public class ChangeMonitoredTopicsAction extends ActionDelegate implements IWorkbenchWindowActionDelegate
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
					action.setEnabled(true);
					break;
				}

				case Event.EVENT_TIME_GENERATED:
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
					action.setEnabled(false);
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

	private final static Logger	logger		= LoggingPlugin.getLogger(ChangeMonitoredTopicsAction.class);

	private ActionObservable	observable	= new ActionObservable();

	private IAction				action;

	private boolean				isRegister	= false;

	public ChangeMonitoredTopicsAction()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-10
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

	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}

	public boolean isRegister()
	{
		return isRegister;
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
		this.action.setEnabled(false);
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action)
	{
		ExecutorSession session = ExecutorSession.getInstance();
		if (session != null && ExecutorSession.isRunning())
		{
			String icdPath = session.getCfgICDPath();
			if (StringUtils.isNull(icdPath))
				return;
			ClazzManager icdManager = TemplateEngine.getEngine().parseICD(new File(icdPath));
			ChangeMonitoredTopicsDialog dialog = new ChangeMonitoredTopicsDialog(Display.getCurrent().getActiveShell(), icdManager);
			if (Window.CANCEL != dialog.open())
			{
				List<SPTEMsg> topics = dialog.getSelectedTopics();
				new ChangeJob(topics).schedule();
			}
		} else
		{
			logger.warning("ExecutorSession对象已经被销毁，无法执行更改监控主题。");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		// TODO Auto-generated method stub

	}

	private class ChangeJob extends Job
	{

		private List<SPTEMsg>	topicIds;

		public ChangeJob(List<SPTEMsg> topicIds)
		{
			super(Messages.getString("ChangeMonitoredTopicsAction_Change_Monitor_Topics"));
			this.topicIds = topicIds;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
		 * IProgressMonitor)
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor)
		{
			monitor.beginTask(Messages.getString("ChangeMonitoredTopicsAction_Change_Monitor_Topics"), IProgressMonitor.UNKNOWN);
			ExecutorSession session = ExecutorSession.getInstance();
			if (session.changeMonitoredTopics(this.topicIds))
			{
				return Status.OK_STATUS;
			}
			return new Status(Status.ERROR, "", Messages.getString("ChangeMonitoredTopicsAction_Change_Monitor_Topics_Failure"));
		}

	}

}