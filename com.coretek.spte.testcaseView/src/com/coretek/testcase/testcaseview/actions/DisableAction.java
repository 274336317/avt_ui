package com.coretek.testcase.testcaseview.actions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.widgets.TableItem;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 禁止测试用例被添加到其它列中或者被选中
 * 
 * @author 孙大巍
 * @date 2010-12-14
 */
public class DisableAction extends AbstractAction
{
	public DisableAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_DISABLE"));
	}

	@Override
	public void run()
	{
		super.run();
		TableItem[] items = this.viewPart.getTableViewer().getTable().getSelection();
		for (TableItem item : items)
		{
			item.setForeground(ColorConstants.gray);
			TestCase test = (TestCase) item.getData();
			test.setEnabled(false);
			TestCaseGroup group = test.getGroup();
			// 被禁用的测试用例不能被添加到用户自定义的测试用例列表中
			if (group != null)
			{
				group.deleteTestCase(test);
				test.setGroup(null);
			}
			this.viewPart.getTableViewer().setChecked(test, false);
			this.viewPart.refereshTable();
		}
	}
}
