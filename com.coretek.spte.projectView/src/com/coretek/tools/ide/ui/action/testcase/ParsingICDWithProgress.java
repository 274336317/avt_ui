/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.tools.ide.ui.action.testcase;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;

/**
 * To show the progress of parsing icd file
 * 
 * @author SunDawei 2012-5-8
 */
public class ParsingICDWithProgress implements IRunnableWithProgress
{
	private File			icdFile;

	private ClazzManager	icdManager;

	public ParsingICDWithProgress(File file)
	{
		this.icdFile = file;
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
	{
		monitor.beginTask("���ڽ���" + this.icdFile.getAbsolutePath() + "����һ�ν����Ứ�ѽϳ�ʱ�䣬�����ĵȴ���", IProgressMonitor.UNKNOWN);
		icdManager = TemplateEngine.getEngine().parseICD(this.icdFile);
		monitor.done();
	}

	public ClazzManager getICDManager()
	{
		return this.icdManager;
	}

}