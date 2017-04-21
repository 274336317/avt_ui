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
 * ����������ͼ��Ԫ��ѡ��״̬���¼�
 * 
 * @author ���Ρ 2012-3-31
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
	 * ����ѡ�е��ǡ������г���������ʱ��Ӧ��ѡ�����еĲ���������Ҫ�ų�����ֹ�Ĳ�������
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
				// ������ֹ�Ĳ��������Ӵ���ѡ��״̬���ų�
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
	 * ����ѡ�е��ǡ������Զ����б�
	 * 
	 * @param event
	 */
	private void handleAllCustomizedGroupsSelected(CheckStateChangedEvent event)
	{
		if (event.getChecked())
		{
			treeViewer.setSubtreeChecked(event.getElement(), event.getChecked());// �����ӽڵ�����Ϊѡ��״̬
		} else
		{
			treeViewer.setSubtreeChecked(GroupFactory.getCustomizedList(), false);
		}

		for (TestCaseGroup tGroup : GroupFactory.getCustomizedList().getChildGroups())
		{
			// �������Ĳ�����������Ϊѡ��״̬
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
	 * ��ѡ�е��ǡ������Զ����б��µ��ӽڵ�
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
		// ����ѡ�е��ǡ������г���������ʱ
		if (event.getElement() == GroupFactory.getAllCases())
		{
			this.handleAllCasesGroupSelected(event);
		} else if (groups.indexOf(GroupFactory.getCustomizedList()) >= 0 && groups.size() == 1)
		{// ����ѡ�е��ǡ������Զ����б�
			this.handleAllCustomizedGroupsSelected(event);
		} else
		{// ��ѡ�е��ǡ������Զ����б��µ��ӽڵ�
			this.handleSelectionOfSubNode(event, groups);
		}
	}
}
