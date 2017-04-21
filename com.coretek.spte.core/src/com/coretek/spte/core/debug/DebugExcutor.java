package com.coretek.spte.core.debug;

import org.eclipse.core.resources.IFile;

import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * ����ִ����
 * 
 * @author ���Ρ
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
	 * ִ�е���
	 * 
	 * @param startUUID
	 * @param endUUID </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-11
	 */
	public void excute(String startUUID, String endUUID)
	{
		ExecutorSession.setRunning(true);
		String file = testCaseFile.getLocation().toFile().getAbsolutePath();
		ExecutorSession session = ExecutorSession.getInstanceForExecutionOnly(file);
		// ִ����Ϣ
		DebugExecutionJob job = new DebugExecutionJob(session, testCaseFile, startUUID, endUUID, this.actionsGroup);
		job.setUser(true);
		job.schedule();
	}
}