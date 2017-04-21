package com.coretek.testcase.testcaseview.actions;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

public class DebugSelectedTestCaseAction extends AbstractAction
{

	public DebugSelectedTestCaseAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_DEBUG_SELECTED_CASES"));
	}
}
