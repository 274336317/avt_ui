/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview.actions;

import java.util.List;

import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * Run all of enabled testCases in the list.
 * 
 * @author SunDawei 2012-7-25
 */
public class RunAllAction extends AbstractAction
{

	public RunAllAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText("�������в���");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		// ��ʼ��ִ�ж���
		ExecutionQueue excutionQueue = new ExecutionQueue();
		List<TestCase> list = (List<TestCase>) this.getViewPart().getTableViewer().getInput();
		if (list != null && list.size() != 0)
		{
			Object[] elements = list.toArray();
			for (Object object : elements)
			{
				if (object instanceof TestCase)
				{
					TestCase testCase = (TestCase) object;
					excutionQueue.add(testCase);
				}
			}
			// ִ�в���
			excutionQueue.excute();
		}

	}
}
