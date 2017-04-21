package com.coretek.testcase.testcaseview.actions.left;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.GroupFactory;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPart;
import com.coretek.testcase.testcaseview.actions.AbstractAction;

/**
 * 
 * ɾ����������
 * 
 * @author ���Ρ
 * @date 2010-12-13
 */
public class DeleteAction extends AbstractAction
{

	public DeleteAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_DELETE"));

	}

	@Override
	public void run()
	{
		super.run();
		StructuredSelection selection = (StructuredSelection) this.viewPart.getTreeViewer().getSelection();
		if (selection.getFirstElement() == GroupFactory.getCustomizedList())
		{
			return;
		}
		TestCaseGroup group = (TestCaseGroup) selection.getFirstElement();
		if (MessageDialog.openConfirm(Utils.getShell(), "ȷ��", StringUtils.concat("��ȷ��Ҫɾ�������б� ", group.getName(), " ��")))
		{
			if (!GroupFactory.getCustomizedList().deleteChildGroup(group))
			{
				MessageDialog.openError(Utils.getShell(), "����", StringUtils.concat("ɾ�������б� ", group.getName(), " ʧ��"));
			} else
			{
				this.viewPart.getTreeViewer().refresh(true);
				for (TestCase test : group.getTestCases())
				{
					test.setGroup(null);
				}
				this.viewPart.getTableViewer().refresh();
			}
		}
	}

}
