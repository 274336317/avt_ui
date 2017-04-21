package com.coretek.testcase.testResult.actions;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testResult.TestResultViewPart;

/**
 * È«Ñ¡
 * 
 * @author Ëï´óÎ¡
 * @date 2010-12-4
 * 
 */
public class SelectAllAction extends AbstractAction
{

	public SelectAllAction(TestResultViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_SELECT_ALL"));
	}

	@Override
	public void run()
	{
		int count = this.viewPart.getTableViewer().getCheckedElements().length;
		int allCount = this.viewPart.getTableViewer().getTable().getItemCount();
		if (count < allCount)
		{
			this.viewPart.getTableViewer().setAllChecked(true);
		} else
		{
			this.viewPart.getTableViewer().setAllChecked(false);
		}

	}

}
