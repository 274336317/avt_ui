package com.coretek.testcase.testcaseview.actions;

import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.StructuredSelection;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 
 * 从列表中删除
 * 
 * @author 孙大巍
 * @date 2010-12-14
 */
public class DeleteFromListAction extends AbstractAction
{

	public DeleteFromListAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_DELETE_FROM_LIST"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
		super.run();

		CheckboxTableViewer viewer = this.viewPart.getTableViewer();
		StructuredSelection selection = (StructuredSelection) viewer.getSelection();
		List<TestCase> tests = (List<TestCase>) selection.toList();
		for (TestCase test : tests)
		{
			if (test.getGroup().deleteTestCase(test))
			{
				viewer.setChecked(test, false);
				test.setGroup(null);
			}
		}
		this.viewPart.refereshTable();
	}
}
