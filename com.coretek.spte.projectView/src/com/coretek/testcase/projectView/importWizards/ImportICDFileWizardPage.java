/************************************************************************
 * Copyright (C) 2000-2012 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.importWizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.coretek.spte.core.util.Utils;

/**
 * @author ZHANG Yi 2012-6-6
 */
public class ImportICDFileWizardPage extends WizardNewFileCreationPage
{

	protected FileFieldEditor	editor;

	/**
	 * @param pageName
	 * @param selection </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-6-6
	 */
	public ImportICDFileWizardPage(String pageName, IStructuredSelection selection)
	{
		super("ICD文件导入", selection);
		setDescription("导入ICD文件");
		setTitle("ICD文件导入");
	}

	protected void createAdvancedControls(Composite parent)
	{
		Composite fileSelectionArea = new Composite(parent, SWT.NONE);
		GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		fileSelectionArea.setLayoutData(fileSelectionData);

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.numColumns = 3;
		fileSelectionLayout.makeColumnsEqualWidth = false;
		fileSelectionLayout.marginWidth = 0;
		fileSelectionLayout.marginHeight = 0;
		fileSelectionArea.setLayout(fileSelectionLayout);

		editor = new FileFieldEditor("fileSelect", "选择导入的ICD文件: ", fileSelectionArea); // NON-NLS-1
		// //NON-NLS-2
		editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				IPath path = new Path(ImportICDFileWizardPage.this.editor.getStringValue());
				setFileName(path.lastSegment());
			}
		});
		String[] extensions = new String[] { "*.xml" }; // NON-NLS-1
		editor.setFileExtensions(extensions);
		fileSelectionArea.moveAbove(null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createLinkTarget()
	 */
	protected void createLinkTarget()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	protected InputStream getInitialContents()
	{
		try
		{
			return new FileInputStream(new File(editor.getStringValue()));
		} catch (FileNotFoundException e)
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getNewFileLabel()
	 */
	protected String getNewFileLabel()
	{
		return "重命名ICD文件:"; // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	protected IStatus validateLinkedResource()
	{
		return new Status(IStatus.OK, "com.coretek.testcase.projectView", IStatus.OK, "", null); // NON-NLS-1
		// //NON-NLS-2
	}

	@Override
	protected boolean validatePage()
	{
		IPath path = getContainerFullPath();
		boolean valid = true;
		if (path != null)
		{
			if (path.segmentCount() < 2)
			{
				setErrorMessage("选择正确ICD文件夹");
				valid = false;
			} else
			{
				for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
				{
					if (Utils.isICDProject(project))
					{
						IFolder folder = project.getFolder(path);
						if (!Utils.isIcdFolder(folder))
						{
							setErrorMessage("选择正确ICD文件夹");
							valid = false;
							break;
						}
					}
				}
			}
		}

		if (valid)
		{
			valid = super.validatePage();
		}
		return valid;
	}

}
