/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.curve.figures;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.coretek.spte.common.service.IPaintService;

/**
 * ����ʱ��ͼ
 * 
 * @author SunDawei
 * 
 * @date 2012-9-24
 */
public class PaintJob extends Job
{
	private IPaintService	service;

	private long			time;

	/**
	 * @param name
	 */
	public PaintJob(IPaintService service, long time)
	{
		super("��ʾ���");
		this.service = service;
		this.time = time;
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
		monitor.beginTask("���ڻ���ͼ��", IProgressMonitor.UNKNOWN);
		this.service.paint(time);
		return Status.OK_STATUS;
	}

}
