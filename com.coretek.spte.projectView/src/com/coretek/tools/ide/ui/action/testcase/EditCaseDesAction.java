package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.TemplateUtils;
import com.coretek.spte.core.xml.parser.TestCaseParser;
import com.coretek.spte.testcase.TestCase;
import com.coretek.testcase.projectView.utils.ProjectUtils;

public class EditCaseDesAction extends Action
{

	private IFile		file;

	private TestCase	testCase;

	public EditCaseDesAction(IFile file)
	{
		this.file = file;
		this.setText(Messages.getString("I18N_EDITING_DES"));
	}

	public void run()
	{
		this.testCase = (TestCase) TestCaseParser.getInstance(TemplateUtils.getTestCaseSchemaFile(), this.file).doParse();
		EditCaseDesDlg editCaseDlg = new EditCaseDesDlg(ProjectUtils.getShell(), testCase, file);
		editCaseDlg.open() ;
	}

}
