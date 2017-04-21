package com.coretek.testcase.testcaseview.actions;

import org.eclipse.jface.action.Action;

import com.coretek.testcase.testcaseview.TestCaseViewPart;

public abstract class AbstractAction extends Action
{

	protected TestCaseViewPart	viewPart;

	public TestCaseViewPart getViewPart()
	{
		return viewPart;
	}

	public void setViewPart(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public AbstractAction(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}
}
