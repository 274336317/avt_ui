/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.importWizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.datatransfer.FileSystemImportWizard;

import com.coretek.testcase.projectView.projectwizard.page.TestingProjectWizard;

/**
 * 测试用例导入页
 * 
 * @author 孙大巍
 * @date 2010-11-29
 * 
 */
public class ImportTestCaseWizardPage extends WizardNewFileCreationPage
{

	protected FileFieldEditor		editor;

	private IStructuredSelection	selection;

	public ImportTestCaseWizardPage(String pageName, IStructuredSelection selection)
	{
		super(pageName, selection);
		setTitle(pageName); // NON-NLS-1
		setDescription("将测试用例从本地文件系统导入到工作空间中"); // NON-NLS-1
		this.selection = selection;
	}

	public FileFieldEditor getEditor()
	{
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls
	 * (org.eclipse.swt.widgets.Composite)
	 */
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

		editor = new FileFieldEditor("fileSelect", "选择测试用例文件: ", fileSelectionArea); // NON-NLS-1
		// //NON-NLS-2
		editor.getTextControl(fileSelectionArea).addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				IPath path = new Path(ImportTestCaseWizardPage.this.editor.getStringValue());
				setFileName(path.lastSegment());
			}
		});
		String[] extensions = new String[] { "*.cas" }; // NON-NLS-1
		editor.setFileExtensions(extensions);
		fileSelectionArea.moveAbove(null);

		Button button = new Button(fileSelectionArea, SWT.PUSH);
		button.setText("导入icd文件");
		button.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));

		GridData data = new GridData();
		data.horizontalSpan = 2;
		data.horizontalAlignment = SWT.RIGHT;
		button.setLayoutData(data);
		button.addMouseListener(new MouseListener()
		{
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			public void mouseDown(MouseEvent e)
			{
				FileSystemImportWizard wizard = new FileSystemImportWizard();
				wizard.init(PlatformUI.getWorkbench(), ImportTestCaseWizardPage.this.selection);
				if (wizard != null)
				{
					WizardDialog dialog = new WizardDialog(null, wizard);
					dialog.open();
				}
			}

			public void mouseUp(MouseEvent e)
			{

			}
		});

		button = new Button(fileSelectionArea, SWT.PUSH);
		button.setText("创建软件测试工程");
		button.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));
		data = new GridData();
		data.horizontalAlignment = SWT.RIGHT;
		button.setLayoutData(data);
		button.addMouseListener(new MouseListener()
		{
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			public void mouseDown(MouseEvent e)
			{
				TestingProjectWizard wizard = new TestingProjectWizard();
				wizard.init(PlatformUI.getWorkbench(), ImportTestCaseWizardPage.this.selection);
				if (wizard != null)
				{
					WizardDialog dialog = new WizardDialog(null, wizard);
					dialog.open();
					TreeViewer view = ImportTestCaseWizardPage.this.getResourceGroup().getContainerGroup().getTreeViewer();
					view.setInput(ResourcesPlugin.getWorkspace());
				}

			}

			public void mouseUp(MouseEvent e)
			{

			}
		});

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
		return "新的文件名:"; // NON-NLS-1
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#validateLinkedResource()
	 */
	protected IStatus validateLinkedResource()
	{
		return new Status(IStatus.OK, "com.coretek.tools.ide.ui", IStatus.OK, "", null); // NON-NLS-1
		// //NON-NLS-2
	}

}