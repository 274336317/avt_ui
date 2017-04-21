package com.coretek.spte.core.debug;

import org.eclipse.core.resources.IFile;

import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 调试执行器
 * 
 * @author 孙大巍
 * 
 *         2011-3-11
 */
public class DebugExcutor
{

	private IFile				testCaseFile;

	private DebugActionsGroup	actionsGroup;

	public DebugExcutor(IFile testCaseFile, DebugActionsGroup actionsGroup)
	{
		this.testCaseFile = testCaseFile;
		this.actionsGroup = actionsGroup;
	}

	/**
	 * 执行调试
	 * 
	 * @param startUUID
	 * @param endUUID </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-11
	 */
	public void excute(String startUUID, String endUUID)
	{
		ExecutorSession.setRunning(true);
		String file = testCaseFile.getLocation().toFile().getAbsolutePath();
		ExecutorSession session = ExecutorSession.getInstanceForExecutionOnly(file);
		// 执行消息
		DebugExecutionJob job = new DebugExecutionJob(session, testCaseFile, startUUID, endUUID, this.actionsGroup);
		job.setUser(true);
		job.schedule();
	}
}