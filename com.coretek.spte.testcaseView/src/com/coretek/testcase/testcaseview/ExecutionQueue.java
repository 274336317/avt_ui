package com.coretek.testcase.testcaseview;

import java.util.ArrayList;
import java.util.List;

import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * ִ�ж���
 * 
 * @author ���Ρ
 * @date 2010-12-31
 */
public class ExecutionQueue
{
	// ִ�ж���
	private List<TestCase>	queue	= new ArrayList<TestCase>();

	public void add(TestCase testCase)
	{
		this.queue.add(testCase);
	}

	/**
	 * ִ�в�������
	 */
	public void excute()
	{
		ExecutionJob ej = new ExecutionJob(queue.get(0).getPath(), ExecutorSession.getInstance(), this.queue);
		ej.setUser(true);
		ej.schedule();
	}

	public List<TestCase> getQueue()
	{
		return queue;
	}

	public void setQueue(List<TestCase> queue)
	{
		this.queue = queue;
	}

}