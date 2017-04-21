package com.coretek.testcase.testcaseview.actions;

import java.util.List;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 
 * 全选操作
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class SelectAllAction extends AbstractAction
{

	public SelectAllAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_SELECT_ALL"));
	}

	@Override
	public void run()
	{
		super.run();
		this.viewPart.getTableViewer().setAllChecked(true);
		List<TestCase> tests = (List<TestCase>) this.viewPart.getTableViewer().getInput();
		for (TestCase test : tests)
		{
			if (!test.isEnabled())
			{
				this.viewPart.getTableViewer().setChecked(test, false);
			}
		}
	}

}
