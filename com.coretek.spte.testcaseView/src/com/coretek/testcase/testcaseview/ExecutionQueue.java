package com.coretek.testcase.testcaseview;

import java.util.ArrayList;
import java.util.List;

import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 执行队列
 * 
 * @author 孙大巍
 * @date 2010-12-31
 */
public class ExecutionQueue
{
	// 执行队列
	private List<TestCase>	queue	= new ArrayList<TestCase>();

	public void add(TestCase testCase)
	{
		this.queue.add(testCase);
	}

	/**
	 * 执行测试用例
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