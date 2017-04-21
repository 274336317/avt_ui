/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �������������
 * 
 * @author ���� 2012-3-14
 */
public class LockMonitorAction extends Observable implements IWorkbenchWindowActionDelegate, Observer
{

	/**
	 * �Ƿ��������
	 */
	private volatile boolean	isLockMonitor	= false;

	/**
	 * �Ƿ���ʱ���������ע��
	 */
	private volatile boolean	isRegister		= false;

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
			// ������ע��Ϊ������
			MonitorEventManager.getMonitorEventManager().addObserver(this);
			this.addObserver(MonitorEventManager.getMonitorEventManager());
			setLockMonitor(false);
			setRegister(true);
		}
	}

	/**
	 * @return �Ƿ��������
	 */
	public boolean isLockMonitor()
	{
		return isLockMonitor;
	}

	/**
	 * @return �Ƿ���ʱ���������ע��
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

					// ֪ͨ�۲��߶��������������¼�
					if (this.countObservers() > 0)
					{
						this.setChanged();
						this.notifyObservers(new Event(Event.EVENT_LOCK));
						this.clearChanged();
					}
				} else
				{
					setLockMonitor(false);

					// ֪ͨ�۲��߶��������˽����¼�
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
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>����</b> ���� </br>
	 * <b>����</b> 2012-3-13
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

	/**
	 * �����Ƿ��������
	 * 
	 */
	/**
	 * �����Ƿ��������
	 * 
	 * @param isLockMonitor �Ƿ��������
	 */
	public void setLockMonitor(boolean isLockMonitor)
	{
		this.isLockMonitor = isLockMonitor;
	}

	/**
	 * @param isRegister �Ƿ���ʱ���������ע��
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}

	/*
	 * (non-Javadoc) ͨ���¼����¼�����
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

			// ������������¼������д���
			case Event.EVENT_START:
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
				break;
			}

			default:
				break;
		}
	}
}
