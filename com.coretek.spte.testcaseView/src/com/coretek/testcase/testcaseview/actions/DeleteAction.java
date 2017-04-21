package com.coretek.testcase.testcaseview.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * 
 * ɾ����������
 * 
 * @author ���Ρ
 * @date 2010-12-14
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
		StructuredSelection selection = (StructuredSelection) this.viewPart.getTableViewer().getSelection();
		List<TestCase> list = (List<TestCase>) selection.toList();
		for (TestCase test : list)
		{
			if (MessageDialog.openConfirm(Utils.getShell(), "ȷ��", StringUtils.concat("��ȷ��Ҫɾ����������", test.getProjectName(), "/", test.getSuitePath(), "/", test.getCaseName(), "��?")))
			{
				IFile file = Utils.getCaseByName(test.getProjectName(), test.getSuitePath(), test.getCaseName());
				try
				{
					if (!file.exists())
					{
						MessageDialog.openError(Utils.getShell(), "����", StringUtils.concat("��������", test.getProjectName(), "/", test.getSuitePath(), "/", test.getCaseName(), "�����ڣ�"));
						continue;
					}
					file.delete(true, null);
					this.viewPart.getTableViewer().remove(test);
				} catch (CoreException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}