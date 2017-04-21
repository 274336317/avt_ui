/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview.actions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.GroupFactory;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * @author SunDawei 2012-6-7
 */
public class GroupContributionItem extends ContributionItem
{

	private TestCaseViewPart	viewPart;

	public GroupContributionItem(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public void dispose()
	{

	}

	public void fill(Composite parent)
	{

	}

	public void fill(Menu parent, int index)
	{
		super.fill(parent, index);
		TestCaseGroup group = GroupFactory.getCustomizedList();
		if (group.getChildGroups().size() == 0)
		{
			MenuItem item = new MenuItem(parent, SWT.PUSH, index++);
			item.setText(Messages.getString("I18N_NO_GROUP"));
			item.setEnabled(false);
		} else
		{
			for (TestCaseGroup test : group.getChildGroups())
			{
				MenuItem item = new MenuItem(parent, SWT.PUSH, index++);
				item.setText(test.getName());
				item.addSelectionListener(new Listener(test));
			}
		}
	}

	private class Listener implements SelectionListener
	{

		private TestCaseGroup	group;

		public Listener(TestCaseGroup group)
		{
			this.group = group;
		}

		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		public void widgetSelected(SelectionEvent e)
		{
			StructuredSelection selection = (StructuredSelection) viewPart.getTableViewer().getSelection();
			Object tests[] = (Object[]) selection.toArray();
			for (Object obj : tests)
			{
				TestCase test = (TestCase) obj;
				// ֻ�б�����Ĳ���������������ӵ��б���
				if (test.isEnabled())
				{
					// ��Ҫ��������������ǰ���б���ɾ��
					if (test.getGroup() != null)
					{
						test.getGroup().deleteTestCase(test);
					}
					if (!group.addTestCase(test))
					{
						MessageDialog.openError(Utils.getShell(), Messages.getString("I18N_WRONG"), StringUtils.concat("���������� ", test.getCaseName(), " ��ӵ��Զ����б�", group.getName(), "ʧ�ܣ�"));
					} else
					{
						test.setGroup(group);
						if (viewPart.getTreeViewer().getChecked(group))
						{
							viewPart.getTableViewer().setChecked(test, true);
						}
					}
				}
			}
			viewPart.refereshTable();
		}
	}
}
