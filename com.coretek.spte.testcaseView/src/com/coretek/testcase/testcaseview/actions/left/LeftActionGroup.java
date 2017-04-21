package com.coretek.testcase.testcaseview.actions.left;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;

import com.coretek.testcase.testcaseview.TestCaseViewPart;

public class LeftActionGroup extends ActionGroup
{
	private NewListCreationAction	creationNewAction;
	private DeleteAction			deleteAction;
	private TestCaseViewPart		viewPart;
	private SaveGroupAction			saveGroupAction;

	public LeftActionGroup(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
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
		this.creationNewAction = new NewListCreationAction(this.viewPart);
		this.deleteAction = new DeleteAction(this.viewPart);
		this.saveGroupAction = new SaveGroupAction(this.viewPart);
		Object[] objs = this.viewPart.getTreeViewer().getCheckedElements();
		menu.add(this.creationNewAction);
		menu.add(this.saveGroupAction);
		menu.add(this.deleteAction);
		if (objs == null || objs.length == 0)
		{
			this.saveGroupAction.setEnabled(false);
		} else
		{
			this.saveGroupAction.setEnabled(true);
		}
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

}
