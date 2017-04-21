/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;

/**
 * @author SunDawei 2012-5-19
 */
public class ParseCaseFileJob implements IRunnableWithProgress
{

	private IFile			caseFile;

	private ClazzManager	caseManager;

	public ParseCaseFileJob(IFile caseFile)
	{
		this.caseFile = caseFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core
	 * .runtime.IProgressMonitor) <br/> <b>Author</b> SunDawei </br> <b>Date</b>
	 * 2012-5-19
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
	{
		monitor.beginTask("½âÎö" + this.caseFile.getLocation(), 100);
		monitor.worked(20);
		this.caseManager = TemplateEngine.getEngine().parseCase(this.caseFile.getLocation().toFile());
		monitor.done();
	}

	public ClazzManager getCaseManager()
	{
		return this.caseManager;
	}

}
