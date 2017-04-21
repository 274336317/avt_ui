/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.action;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitor.ui.manager.TimeStampManager;

/**
 * ֹͣ���
 * 
 * @author ���� 2012-3-14
 */
public class StopMonitorAction extends ActionDelegate implements IWorkbenchWindowActionDelegate
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

				// ������������¼������д���
				case Event.EVENT_START:
				{
					action.setEnabled(true);
					break;
				}

				case Event.EVENT_TIME_GENERATED:
				{
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
					action.setEnabled(false);
					break;
				}

					// �����û�ѡ��ĳ��ʱ����¼������д���
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
	 * ֹͣ���
	 */
	private IAction				action;

	/**
	 * �Ƿ���ʱ���������ע��
	 */
	private volatile boolean	isRegister	= false;

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

	public void init(IAction action)
	{
		this.action = action;
		this.action.setEnabled(false);
		if (!this.isRegister)
		{
			// ������ע��Ϊ������
			MonitorEventManager.getMonitorEventManager().addObserver(observable);
			observable.addObserver(MonitorEventManager.getMonitorEventManager());
			setRegister(true);
		}
	}

	@Override
	public void init(IWorkbenchWindow window)
	{
		if (!isRegister())
		{
			MonitorEventManager.getMonitorEventManager().addObserver(observable);
			observable.addObserver(MonitorEventManager.getMonitorEventManager());
			setRegister(true);
		}
	}

	public boolean isRegister()
	{
		return isRegister;
	}

	@Override
	public void run(IAction action)
	{
		this.action.setEnabled(false);
		if (ExecutorSession.getInstance() != null)
		{
			try
			{
				ExecutorSession.shutDown();
				ExecutorSession.dispose();
			} catch (TimeoutException e)
			{
				e.printStackTrace();
			}
		}

		TimeStampManager.getTimeStampManager().stopMonitor();
		try
		{
			if (observable.countObservers() > 0)
			{
				observable.notifyObservers(new Event(Event.EVENT_STOP));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}
}