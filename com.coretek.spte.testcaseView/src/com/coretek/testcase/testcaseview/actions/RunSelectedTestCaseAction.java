package com.coretek.testcase.testcaseview.actions;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.ExecutionQueue;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * Run all of selected testCases in the list.
 * 
 * @author SunDawei 2012-7-25
 */
public class RunSelectedTestCaseAction extends AbstractAction
{

	public RunSelectedTestCaseAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_RUN_SELECTED_TESTCASE"));
	}

	@Override
	public void run()
	{
		// 初始化执行队列
		ExecutionQueue excutionQueue = new ExecutionQueue();
		Object[] elements = getViewPart().getTableViewer().getCheckedElements();
		if (elements != null && elements.length != 0)
		{
			for (Object object : elements)
			{
				if (object instanceof TestCase)
				{
					TestCase testCase = (TestCase) object;
					excutionQueue.add(testCase);
				}
			}
			// 执行测试
			excutionQueue.excute();
		}

	}
}