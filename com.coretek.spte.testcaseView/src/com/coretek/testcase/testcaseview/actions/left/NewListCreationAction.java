package com.coretek.testcase.testcaseview.actions.left;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.testcase.testcaseview.GroupFactory;
import com.coretek.testcase.testcaseview.TestCaseGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPart;
import com.coretek.testcase.testcaseview.actions.AbstractAction;
import com.coretek.testcase.testcaseview.dialogs.NewGroupDialog;

/**
 * 新建测试用例组
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class NewListCreationAction extends AbstractAction
{

	public NewListCreationAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_NEW"));
	}

	@Override
	public void run()
	{
		super.run();
		NewGroupDialog dialog = new NewGroupDialog();
		if (dialog.open() == Window.OK)
		{
			TreeViewer viewer = this.viewPart.getTreeViewer();
			String name = dialog.getText();
			TestCaseGroup group = new TestCaseGroup();
			group.setName(name);
			group.setParentGroup(GroupFactory.getCustomizedList());
			group.setUUID(StringUtils.getUUID());
			GroupFactory.getCustomizedList().addChildGroup(group);
			viewer.refresh(true);
			viewer.expandAll();
		}

	}

}
