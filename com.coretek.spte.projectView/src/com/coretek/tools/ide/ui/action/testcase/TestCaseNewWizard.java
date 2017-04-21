package com.coretek.tools.ide.ui.action.testcase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.testcase.projectView.action.Messages;
import com.coretek.testcase.projectView.utils.ProjectUtils;

/**
 * To create a new testcase file
 * 
 * @author SunDawei
 * @date 2010-12-16
 */
public class TestCaseNewWizard extends BasicNewResourceWizard
{

	private IFolder						folder;

	private FileNameAndSuiteWizardPage	page;

	private ICDFileWizardPage			icdPage;

	public TestCaseNewWizard()
	{
		super();
		this.setWindowTitle("创建测试用例");
	}

	public TestCaseNewWizard(IFolder folder)
	{
		this();
		this.folder = folder;
	}

	@Override
	public void addPages()
	{
		this.page = new FileNameAndSuiteWizardPage(folder);
		this.icdPage = new ICDFileWizardPage();
		this.addPage(page);
		this.addPage(this.icdPage);
	}

	@Override
	public boolean performFinish()
	{
		String fileBaseName = page.getInputCaseName();
		String caseName = fileBaseName + "." + ProjectUtils.XML_CAS_FILE_EXTENSION;
		this.folder = this.page.getFolder();
		String casePath = folder.getLocation().toString() + "/" + caseName;
		String REQ = page.getREQ();
		String criterion = page.getCriterion();
		String procedure = page.getProcedure();
		String restriction = page.getConstraint();
		String condition = page.getCondition();
		String input = page.getInput();
		String expected = page.getExpected();
		List<Entity> moduleList = this.icdPage.getModuleIdChecked();
		IFile icdFile = this.icdPage.getICDFile();
		// FIXME:层级信息先直接写死
//		String lev = TemplateUtils.FUNCTION_NODE_LEVEL;// page.getLevel();
		String lev = icdPage.getLevel();

		try
		{
			File file = new File(casePath);
			if (!file.createNewFile())
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "创建 " + file.getName() + " 文件失败！");
				return false;
			} else
			{
				String md5 = ICDFileManager.getInstance().getMD5(icdFile);
				StringBuilder sb = new StringBuilder();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
				sb.append("<testCase name=\"" + file.getName() + "\" xsi:noNamespaceSchemaLocation=\"testCaseStructure.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">").append("\n");
				sb.append("	<version>" + this.icdPage.getVersionId() + "</version>\n");
				sb.append("	<description>\n");
				sb.append("		<REQ>" + REQ + "</REQ>\n");
				sb.append("		<condition>" + condition + "</condition>\n");
				sb.append("		<input>" + input + "</input>\n");
				sb.append("		<expected>" + expected + "</expected>\n");
				sb.append("		<criterion>" + criterion + "</criterion>\n");
				sb.append("		<procedure>" + procedure + "</procedure>\n");
				sb.append("		<restriction>" + restriction + "</restriction>\n");
				sb.append("	</description>\n");
				sb.append("	<ICD>\n");
				sb.append("		<file>" + icdFile.getFullPath().toString() + "</file>\n");
				sb.append("		<version>" + this.icdPage.getVersionId() + "</version>\n");
				sb.append("		<md5>" + md5 + "</md5>\n");
				sb.append("	</ICD>\n");
				sb.append("	<testObjects level=\"" + lev + "\">\n");
				for (Entity entity : moduleList)
				{
					String name = entity.getFieldValue("name").toString();
//					String clazz = entity.getClass().getCanonicalName();
					String id = entity.getFieldValue("ID").toString();

					sb.append("		<object name=\"" + name + "\" id=\"" + id + "\"/>\n");
				}

				sb.append("	</testObjects>\n");
				sb.append(" <criterion >\n");
				sb.append("		<unexpected>String</unexpected>\n");
				sb.append("  </criterion>\n");

				sb.append("</testCase>");
				OutputStream os = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				PrintWriter writer = new PrintWriter(osw);
				writer.write(sb.toString());
				writer.close();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			folder.refreshLocal(IResource.DEPTH_ONE, null);
			IFile file = folder.getFile(caseName);
			if (!file.exists())
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "测试用例创建失败！");
				return false;
			}
			IDE.openEditor(EclipseUtils.getActivePage(), file, OpenStrategy.activateOnOpen());
		} catch (CoreException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * Method declared on IWorkbenchWizard.
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		super.init(workbench, currentSelection);
// 测试用例
		setWindowTitle(Messages.getString("test_case"));
		setNeedsProgressMonitor(true);

		Object object = selection.getFirstElement();
		if (object instanceof IFolder)
		{
			folder = (IFolder) object;
		}
	}

}