/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.projectwizard.page;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkingSet;

import com.coretek.common.utils.FileUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.utils.ProjectUtils;

/**
 * 创建ICD管理工程的向导
 * 
 * @author 孙大巍 2012-4-26
 */
public class NewICDProjectWizard extends AbstractProjectWizard
{
	public NewICDProjectWizard()
	{
	}

	@Override
	public void addPages()
	{
		super.addPages();
		this.mainPage = new NewICDProjectPage("新建ICD管理工程", this.getSelection());
		this.mainPage.setTitle("创建ICD管理工程");
		this.addPage(this.mainPage);
	}

	@Override
	protected IProject createNewProject()
	{
		// 创建.projectInfo文件，将工程的信息写入进去
		this.newProject = super.createNewProject();
		// 添加testProjectNature
		try
		{
			IProjectDescription description = this.newProject.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = "com.coretek.spte.projectView.icdProjectNature";
			description.setNatureIds(newNatures);
			this.newProject.setDescription(description, null);

		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		ProjectUtils.refreshView();
		return this.newProject;
	}

	@Override
	public boolean performFinish()
	{
		createNewProject();
		this.createNewFile();
		if (newProject == null)
		{
			return false;
		}

		IWorkingSet[] workingSets = mainPage.getSelectedWorkingSets();
		getWorkbench().getWorkingSetManager().addToWorkingSets(newProject, workingSets);

		updatePerspective();
		selectAndReveal(newProject);

		try
		{
			getNewProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			//Utils.parseAllICDFilesInProject2(newProject);
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected void createNewFile()
	{
		if (newProject == null)
			return;
		String dirPath = newProject.getLocation().toOSString() + File.separator + "ICD";
		File dir = new File(dirPath);
		dir.mkdir();

		String icdPaths = mainPage.getICDFilePath();
		if (StringUtils.isNotNull(icdPaths))
		{
			// 拷贝所选择的icd文件
			for (String icdPath : icdPaths.split(";"))
			{
				try
				{
					File srcFile = new File(icdPath);
					File desFile = new File(dirPath + File.separator + srcFile.getName());
					FileUtils.copyFile(srcFile, desFile);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}
}