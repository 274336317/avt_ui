package com.coretek.testcase.testResult.actions;

import org.eclipse.jface.action.Action;

import com.coretek.testcase.testResult.TestResultViewPart;

public abstract class AbstractAction extends Action
{
	protected TestResultViewPart	viewPart;

	public AbstractAction(TestResultViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

}
