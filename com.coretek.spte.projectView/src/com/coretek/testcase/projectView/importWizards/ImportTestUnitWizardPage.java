/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.importWizards;

import java.io.InputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.coretek.testcase.projectView.projectwizard.page.TestingProjectWizard;

/**
 * 导入测试集合
 * 
 * @author 孙大巍
 * @date 2012-2-9
 */
public class ImportTestUnitWizardPage extends WizardNewFolderMainPage
{
	private IStructuredSelection	selection;

	private Text					locationPathField;

	public Text getLocationPathField()
	{
		return locationPathField;
	}

	public ImportTestUnitWizardPage(String pageName, IStructuredSelection selection)
	{
		super(pageName, selection);
		setTitle(pageName); // NON-NLS-1
		setDescription("将测试集合从本地文件系统导入到工作空间中"); // NON-NLS-1
		this.selection = selection;
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
		final Composite fileSelectionArea = new Composite(parent, SWT.NONE);
		GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		fileSelectionArea.setLayoutData(fileSelectionData);

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.numColumns = 3;
		fileSelectionLayout.makeColumnsEqualWidth = false;
		fileSelectionLayout.marginWidth = 0;
		fileSelectionLayout.marginHeight = 0;
		fileSelectionArea.setLayout(fileSelectionLayout);

		Label locationLabel = new Label(fileSelectionArea, SWT.NONE);
		locationLabel.setText("测试集路径");

		// project location entry field
		locationPathField = new Text(fileSelectionArea, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 250;
		locationPathField.setLayoutData(data);

		locationPathField.addListener(SWT.Modify, new Listener()
		{
			public void handleEvent(Event e)
			{
				boolean valid = validatePage();
				setPageComplete(valid);
			}
		});

		// browse button
		Button browseButton = new Button(fileSelectionArea, SWT.PUSH);
		browseButton.setText("B&rowse...");
		browseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleICDFilePathLocationBrowseButtonPressed(fileSelectionArea);
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		data.horizontalAlignment = SWT.RIGHT;
		fileSelectionArea.moveAbove(null);

		Button button = new Button(fileSelectionArea, SWT.PUSH);
		button.setText("创建软件测试工程");
		button.setLayoutData(data);
		button.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));
		button.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent e)
			{

			}

			public void mouseDown(MouseEvent e)
			{
				TestingProjectWizard wizard = new TestingProjectWizard();
				wizard.init(PlatformUI.getWorkbench(), ImportTestUnitWizardPage.this.selection);
				if (wizard != null)
				{
					WizardDialog dialog = new WizardDialog(null, wizard);
					dialog.open();
					TreeViewer view = ImportTestUnitWizardPage.this.getResourceGroup().getContainerGroup().getTreeViewer();
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
		return null;
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

	/**
	 * Open an appropriate directory browser
	 */
	protected void handleICDFilePathLocationBrowseButtonPressed(Composite composite)
	{
		String selectedDirectory = null;
		DirectoryDialog dialog = new DirectoryDialog(composite.getShell());
		selectedDirectory = dialog.open();
		if (selectedDirectory != null)
		{
			locationPathField.setText(selectedDirectory);
		}
	}
}