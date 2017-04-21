/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.ui.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * @author SunDawei
 * 
 * @date 2012-9-25
 */
public class LoadJob extends Job
{

	private String	dbPath;

	/**
	 * @param name
	 */
	public LoadJob(String dbPath)
	{
		super("加载历史记录");
		this.dbPath = dbPath;
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
		monitor.beginTask("加载历史记录...", IProgressMonitor.UNKNOWN);
		ExecutorSession manager = ExecutorSession.getInstanceForReplay();
		if (manager != null)
		{
			manager.loadHistory(this.dbPath);
		}
		return Status.OK_STATUS;
	}

}
