package com.coretek.testcase.testcaseview.actions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.widgets.TableItem;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 启用某个被禁止了的用例
 * 
 * @author 孙大巍
 * @date 2010-12-14
 */
public class EnableAction extends AbstractAction
{

	public EnableAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_ENABLE"));
	}

	@Override
	public void run()
	{
		super.run();
		TableItem[] items = this.viewPart.getTableViewer().getTable().getSelection();
		for (TableItem item : items)
		{
			item.setForeground(ColorConstants.black);
			TestCase test = (TestCase) item.getData();
			test.setEnabled(true);
			this.viewPart.getTableViewer().setChecked(test, false);
		}
	}
}
