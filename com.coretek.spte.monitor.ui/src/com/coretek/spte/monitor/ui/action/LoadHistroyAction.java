/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ������ʷ��¼
 * 
 * @author ���� 2012-3-14
 */
public class LoadHistroyAction extends ActionDelegate implements IWorkbenchWindowActionDelegate
{

	private class ActionObservable extends Observable implements Observer
	{
		/**
		 * ͨ���¼����¼�����
		 */
		/*
		 * (non-Javadoc) ͨ���¼����¼�����
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

				// ������������¼������д���
				case Event.EVENT_START:
				{
					action.setEnabled(false);
					break;
				}

					// ���ܼ��ؼ����ʷ��¼�¼������д���
				case Event.EVENT_LOAD:
				{

					break;
				}

					// ����ֹͣ����¼������д���
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
	 * ������ʷ��¼
	 */
	private IAction				action;

	/**
	 * �Ƿ���ʱ���������ע��
	 */
	private boolean				isRegister	= false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose() <br/>
	 * <b>����</b> ���� </br> <b>����</b> 2012-3-13
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
			// ������ע��Ϊ������
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
	 * @return �Ƿ���ʱ���������ע�� </br>
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
		// ���ļ��Ի������û�ѡ�����ݿ��ļ�
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.db" });
		String res = dialog.open();
		if (res != null)
		{
			this.action.setEnabled(false);

			// ������ݿ��ļ�·��
			String path = dialog.getFilterPath();
			path = StringUtils.concat(path, "/", dialog.getFileName());
			path = path.replaceAll("\\\\", "/");

			// ֪ͨ�۲��߶��������˼����¼�����֪ͨ��õ����ݿ��ļ�·��
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
	 * @param isRegister �Ƿ���ʱ���������ע��
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}
}
