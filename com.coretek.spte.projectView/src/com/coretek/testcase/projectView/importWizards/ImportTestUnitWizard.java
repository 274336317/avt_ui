/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.importWizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.utils.ProjectUtils;

/**
 * 导入测试集向导，当用户点击finish之后，应当对文件夹下的所有.cas文件进行验证和解析
 * 
 * @author 孙大巍
 * @date 2010-11-29
 * 
 */
public class ImportTestUnitWizard extends Wizard implements IImportWizard
{
	private final static Logger			logger	= LoggingPlugin.getLogger(ImportTestUnitWizard.class.getName());

	private ImportTestUnitWizardPage	mainPage;

	public ImportTestUnitWizard()
	{

	}

	@Override
	public boolean performFinish()
	{
		IFolder folder = mainPage.createNewFolder();
		if (folder == null)
			return false;
		if (!(folder.getParent() instanceof IProject))
		{
			return false;
		}
		// 创建xml文件，添加该文件夹属性为testcase，区别其他文件夹
		ProjectUtils.setFolderProperty(folder, ProjectUtils.FODLDER_TYPE, ProjectUtils.FODLDER_TYPE_TEST_SUITE);

		String folderName = mainPage.getLocationPathField().getText();
		File jFolder = new File(folderName);
		File[] jFiles = jFolder.listFiles();
		for (File jFile : jFiles)
		{
			if (jFile.getName().endsWith(SPTEConstants.RESULT_FILE_POST_FIX))
			{
				IFile file = folder.getFile(jFile.getName());
				InputStream in = null;
				try
				{
					in = new FileInputStream(jFile);
					file.create(in, true, null);
					/*
					 * 验证.cas文件
					 */
					if (jFile.getName().endsWith(SPTEConstants.RESULT_FILE_POST_FIX))
					{
						if (!Utils.validateImportedTestCase(file))
						{
							try
							{
								// 删除验证未通过的测试用例
								file.delete(true, null);
							} catch (CoreException e)
							{
								LoggingPlugin.logException(logger, e);
							}
							return false;
						}
					}
				} catch (FileNotFoundException e)
				{
					LoggingPlugin.logException(logger, e);
					mainPage.setMessage("拷贝" + file.getName() + " 时发生错误！", IStatus.ERROR);
					try
					{
						folder.delete(true, null);
					} catch (CoreException e1)
					{
						LoggingPlugin.logException(logger, e);
					}
					return false;
				} catch (CoreException e)
				{
					LoggingPlugin.logException(logger, e);
					try
					{
						folder.delete(true, null);
					} catch (CoreException e1)
					{
						LoggingPlugin.logException(logger, e);
					}
					return false;
				} finally
				{
					try
					{
						if (in != null)
							in.close();
					} catch (IOException e)
					{
						LoggingPlugin.logException(logger, e);
					}
				}
			}
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		setWindowTitle("导入测试用集向导");
		setNeedsProgressMonitor(true);
		mainPage = new ImportTestUnitWizardPage("导入测试集", selection);
	}

	public void addPages()
	{
		super.addPages();
		addPage(mainPage);
	}
}