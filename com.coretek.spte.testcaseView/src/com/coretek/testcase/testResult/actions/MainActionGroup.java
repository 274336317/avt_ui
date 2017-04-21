package com.coretek.testcase.testResult.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;

import com.coretek.testcase.testResult.TestResultViewPart;

/**
 * 测试结果视图菜单组
 * 
 * @author 孙大巍
 * @date 2010-12-4
 * 
 */
public class MainActionGroup extends ActionGroup
{

	private OpenTestCaseAction	openTestCaseAction;

	private SelectAllAction		selectAllAction;

	private TestResultViewPart	viewPart;

	public MainActionGroup(TestResultViewPart viewPart)
	{
		this.viewPart = viewPart;
		this.makeActions();
	}

	@Override
	public void dispose()
	{

		super.dispose();
	}

	@Override
	public void fillActionBars(IActionBars actionBars)
	{

		super.fillActionBars(actionBars);
	}

	@Override
	public void fillContextMenu(IMenuManager menu)
	{
		super.fillContextMenu(menu);
		menu.add(this.openTestCaseAction);
		menu.add(this.selectAllAction);

	}

	@Override
	public ActionContext getContext()
	{

		return super.getContext();
	}

	@Override
	public void setContext(ActionContext context)
	{

		super.setContext(context);
	}

	@Override
	public void updateActionBars()
	{

		super.updateActionBars();
	}

	private void makeActions()
	{
		this.openTestCaseAction = new OpenTestCaseAction(viewPart);
		this.selectAllAction = new SelectAllAction(viewPart);
	}

}