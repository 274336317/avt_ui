package com.coretek.testcase.testcaseview.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 测试用例列表视图菜单组
 * 
 * @author 孙大巍
 * @date 2010-12-3
 * 
 */
public class TestCaseViewActionGroup extends ActionGroup
{
	private TestCaseViewPart			viewPart;

	private OpenTestCaseAction			openTestCaseAction;

	private CreationNewListAction		createNewTestCaseAction;

	private DeleteFromListAction		deleteFromListAction;

	private DisableAction				disableAction;

	private EnableAction				enableAction;

	private DeleteAction				deleteAction;

	private SelectAllAction				selectAllAction;

	private SaveAsAction				saveAsAction;

	private RunSelectedTestCaseAction	runSelectedTestCaseAction;

	private RunAllAction				runAllAction;

	private DebugSelectedTestCaseAction	debugSelectedTestCaseAction;

	public TestCaseViewActionGroup(TestCaseViewPart testCaseViewPart)
	{
		this.viewPart = testCaseViewPart;
	}

	@Override
	public void fillActionBars(IActionBars actionBars)
	{
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler("com.coretek.sequence.rcp.runSelectedTest", this.runSelectedTestCaseAction);
		actionBars.setGlobalActionHandler("com.coretek.sequence.rcp.runAllTests", this.runAllAction);
	}

	private void updateStatus(boolean status)
	{
		this.enableAction.setEnabled(status);
		this.disableAction.setEnabled(status);
		this.deleteAction.setEnabled(status);
		this.deleteAction.setEnabled(status);
		this.selectAllAction.setEnabled(status);
		this.saveAsAction.setEnabled(status);
		this.runAllAction.setEnabled(status);
		this.runSelectedTestCaseAction.setEnabled(status);
		this.debugSelectedTestCaseAction.setEnabled(status);
		this.openTestCaseAction.setEnabled(status);
	}

	@Override
	public void fillContextMenu(IMenuManager menu)
	{
		super.fillContextMenu(menu);
		TestCase test = (TestCase) ((StructuredSelection) this.getContext().getSelection()).getFirstElement();

		menu.add(this.openTestCaseAction);
		menu.add(new Separator());
		menu.add(this.createNewTestCaseAction);
		MenuManager manager = new MenuManager(Messages.getString("I18N_ADD_TO_LIST"));
		menu.add(manager);
		if (test != null && test.isEnabled())
		{
			manager.add(new GroupContributionItem(this.viewPart));
		} else
		{
			manager.setVisible(false);
		}

		if (test != null && test.getGroup() == null)
		{
			this.deleteFromListAction.setEnabled(false);
		} else if (test != null)
		{
			this.deleteFromListAction.setEnabled(true);
		} else
		{
			this.deleteFromListAction.setEnabled(false);
		}
		menu.add(this.deleteFromListAction);
		menu.add(new Separator());
		menu.add(new Separator());

		menu.add(this.enableAction);
		menu.add(this.disableAction);
		menu.add(this.deleteAction);
		menu.add(this.selectAllAction);
		menu.add(this.saveAsAction);
		menu.add(new Separator());
		menu.add(this.runAllAction);
		menu.add(this.runSelectedTestCaseAction);
		menu.add(this.debugSelectedTestCaseAction);
		menu.add(new Separator());
		if (test == null)
		{
			this.updateStatus(false);
		} else
		{
			this.updateStatus(true);
		}
		if (test != null && test.isEnabled())
		{
			this.enableAction.setEnabled(false);
			this.disableAction.setEnabled(true);
		} else
		{
			this.disableAction.setEnabled(false);
			this.enableAction.setEnabled(true);
		}
		Object[] objs = this.viewPart.getTableViewer().getCheckedElements();
		if (objs == null || objs.length == 0)
		{
			this.saveAsAction.setEnabled(false);
		} else
		{
			this.saveAsAction.setEnabled(true);
		}

		manager = new MenuManager(Messages.getString("I18N_ADD_DEL_COLUMN"));
		menu.add(manager);
		manager.add(new ColumnContributionItem(this.viewPart));
	}

	public void makeActions()
	{
		this.openTestCaseAction = new OpenTestCaseAction(this.viewPart);
		this.createNewTestCaseAction = new CreationNewListAction(viewPart);
		this.deleteFromListAction = new DeleteFromListAction(viewPart);
		this.disableAction = new DisableAction(viewPart);
		this.enableAction = new EnableAction(viewPart);
		this.deleteAction = new DeleteAction(viewPart);
		this.selectAllAction = new SelectAllAction(viewPart);
		this.saveAsAction = new SaveAsAction(viewPart);
		this.runSelectedTestCaseAction = new RunSelectedTestCaseAction(viewPart);
		this.runAllAction = new RunAllAction(viewPart);
		this.debugSelectedTestCaseAction = new DebugSelectedTestCaseAction(viewPart);
		this.deleteFromListAction = new DeleteFromListAction(viewPart);
	}
}