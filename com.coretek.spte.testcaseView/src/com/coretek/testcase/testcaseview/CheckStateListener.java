/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

/**
 * 监听分组视图的元素选择状态该事件
 * 
 * @author 孙大巍 2012-3-31
 */
class CheckStateListener implements ICheckStateListener
{
	private CheckboxTreeViewer	treeViewer;

	private TestCaseViewPart	viewPart;

	public CheckStateListener(CheckboxTreeViewer treeViewer, TestCaseViewPart viewPart)
	{
		this.treeViewer = treeViewer;
		this.viewPart = viewPart;
	}

	/**
	 * 当被选中的是“所有列出的用例”时，应当选中所有的测试用例但要排除被禁止的测试用例
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	private void handleAllCasesGroupSelected(CheckStateChangedEvent event)
	{
		if (event.getChecked())
		{
			this.viewPart.getTableViewer().setAllChecked(true);
			List<TestCase> tests = (List<TestCase>) this.viewPart.getTableViewer().getInput();
			for (TestCase test : tests)
			{
				// 将被禁止的测试用例从处于选中状态中排除
				if (!test.isEnabled())
				{
					this.viewPart.getTableViewer().setChecked(test, false);
				}
			}
			treeViewer.setChecked(GroupFactory.getCustomizedList(), false);
			treeViewer.setSubtreeChecked(GroupFactory.getCustomizedList(), false);
		} else
		{
			this.viewPart.getTableViewer().setAllChecked(false);
		}
	}

	/**
	 * 当被选中的是“所有自定义列表”
	 * 
	 * @param event
	 */
	private void handleAllCustomizedGroupsSelected(CheckStateChangedEvent event)
	{
		if (event.getChecked())
		{
			treeViewer.setSubtreeChecked(event.getElement(), event.getChecked());// 将其子节点设置为选中状态
		} else
		{
			treeViewer.setSubtreeChecked(GroupFactory.getCustomizedList(), false);
		}

		for (TestCaseGroup tGroup : GroupFactory.getCustomizedList().getChildGroups())
		{
			// 将包含的测试用例设置为选中状态
			for (TestCase test : tGroup.getTestCases())
			{
				if (test.getGroup() == tGroup)
					this.viewPart.getTableViewer().setChecked(test, event.getChecked());
			}
		}
		if (event.getChecked())
		{
			treeViewer.setChecked(GroupFactory.getAllCases(), false);
		}
	}

	/**
	 * 被选中的是“所有自定义列表”下的子节点
	 */
	private void handleSelectionOfSubNode(CheckStateChangedEvent event, List<TestCaseGroup> groups)
	{
		for (TestCaseGroup group : groups)
		{
			for (TestCase test : group.getTestCases())
			{
				if (test.getGroup() == group)
					this.viewPart.getTableViewer().setChecked(test, true);
			}
			if (event.getChecked())
			{
				treeViewer.setChecked(GroupFactory.getAllCases(), false);
			}
		}
	}

	public void checkStateChanged(CheckStateChangedEvent event)
	{
		if (event.getElement() == GroupFactory.getCustomizedList())
		{
			treeViewer.setSubtreeChecked(event.getElement(), event.getChecked());
		}
		Object[] objs = treeViewer.getCheckedElements();
		List<TestCaseGroup> groups = new ArrayList<TestCaseGroup>();
		for (Object obj : objs)
		{
			groups.add((TestCaseGroup) obj);
		}
		this.viewPart.getTableViewer().setAllChecked(false);
		// 当被选中的是“所有列出的用例”时
		if (event.getElement() == GroupFactory.getAllCases())
		{
			this.handleAllCasesGroupSelected(event);
		} else if (groups.indexOf(GroupFactory.getCustomizedList()) >= 0 && groups.size() == 1)
		{// 当被选中的是“所有自定义列表”
			this.handleAllCustomizedGroupsSelected(event);
		} else
		{// 被选中的是“所有自定义列表”下的子节点
			this.handleSelectionOfSubNode(event, groups);
		}
	}
}
