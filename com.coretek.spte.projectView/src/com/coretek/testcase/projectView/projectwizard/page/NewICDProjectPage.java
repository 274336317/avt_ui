/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.projectwizard.page;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Constants;
import com.coretek.tools.ide.ui.action.testcase.ParsingICDWithProgress;

/**
 * 创建ICD工程页面
 * 
 * @author 孙大巍 2012-4-26
 */
public class NewICDProjectPage extends AbstractNewProjectCreationPage
{

	/**
	 * @param pageName
	 */
	public NewICDProjectPage(String pageName, IStructuredSelection selection)
	{
		super(pageName, selection);

	}

	@Override
	protected boolean validatePage()
	{
		if (super.validatePage())
		{
			if (locationPathField != null && StringUtils.isNotNull(this.locationPathField.getText()))
			{
				String prjName = this.locationPathField.getText();
				if (StringUtils.isNotNull(prjName))
				{
					IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
					for (IProject prj : prjs)
					{
						if (prj.getName().equals(prjName))
						{
							this.setErrorMessage("已经存在同名的工程");
							return false;
						}
					}
				}
				for (String path : locationPathField.getText().trim().split(";"))
				{
					if (StringUtils.isNull(path))
						continue;
					if (!new File(path).exists())
					{
						setErrorMessage(WizardMessages.getString(Constants.I18N_ERROR_ICD_NOT_EXIST));
						return false;
					} else
					{
						if (TemplateEngine.getEngine() == null)
						{
							setErrorMessage(WizardMessages.getString("ERROR_NULL_PARSER"));
							return false;
						}
						ParsingICDWithProgress progress = new ParsingICDWithProgress(new File(path));
						try
						{
							new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, progress);
						} catch (InvocationTargetException e1)
						{
							e1.printStackTrace();
							return false;
						} catch (InterruptedException e1)
						{
							e1.printStackTrace();
							return false;
						} catch(Exception e)
						{
							e.printStackTrace();
							setErrorMessage(WizardMessages.getString("ERROR_CAN_NOT_PARSE_ICD") + path + "。\n错误原因:" + e.getMessage());
							return false;
						}
						ClazzManager icdManager = progress.getICDManager();
						if (icdManager == null)
						{
							setErrorMessage(WizardMessages.getString("ERROR_CAN_NOT_PARSE_ICD") + path);
							return false;
						}
						Entity fighter = icdManager.getFighter();
						if (null == fighter)
						{
							setErrorMessage(WizardMessages.getString("ERROR_ILLEGAL_ICD"));
							return false;
						}
					}
				}

			}
		} else
		{
			return false;
		}
		setErrorMessage(null);
		return true;
	}
}
