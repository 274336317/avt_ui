package com.coretek.testcase.projectView.projectwizard.page;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.coretek.spte.core.util.Constants;

/**
 * 创建工程向导页
 * 
 * @author 孙大巍
 * @date 2010-11-26
 * 
 */
public abstract class AbstractNewProjectCreationPage extends WizardNewProjectCreationPage
{

	protected IStructuredSelection	selection;

	protected Label					locationLabel;

	protected Text					locationPathField;

	protected Button				browseButton;

	public AbstractNewProjectCreationPage(String pageName, IStructuredSelection selection)
	{
		super(pageName);
		this.selection = selection;
	}

	public void createControl(Composite parent)
	{
		super.createControl(parent);
		createICDFilePathGroup((Composite) getControl(), selection);

		Dialog.applyDialogFont(getControl());
	}

	protected void createICDFilePathGroup(Composite com, IStructuredSelection selection)
	{
		createUserEntryArea(com, false);
		Dialog.applyDialogFont(getControl());
	}

	/**
	 * 
	 * @param composite
	 * @param defaultEnabled
	 */
	protected void createUserEntryArea(Composite composite, boolean defaultEnabled)
	{
		final Group group = new Group(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;

		group.setLayout(gridLayout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(WizardMessages.getString(Constants.I18N_ICD_FILE));

		locationLabel = new Label(group, SWT.RIGHT);
		locationLabel.setText(WizardMessages.getString(Constants.I18N_PATH) + "：");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 50;
		locationLabel.setLayoutData(data);

		// project location entry field
		locationPathField = new Text(group, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 320;
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
		browseButton = new Button(group, SWT.PUSH);
		browseButton.setText("浏览(&R)...");
		browseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleICDFilePathLocationBrowseButtonPressed(group);
			}
		});
	}

	/**
	 * Open an appropriate directory browser
	 */
	protected void handleICDFilePathLocationBrowseButtonPressed(Composite composite)
	{

		String[] selectedICDFile = new String[0];
		String filterPath = null;
		String[] exten = new String[] { "*.xml", "*.*" };
		FileDialog flDlg = new FileDialog(composite.getShell(), SWT.MULTI);
		flDlg.setFilterExtensions(exten);
		flDlg.open();
		selectedICDFile = flDlg.getFileNames();
		filterPath = flDlg.getFilterPath();
		if (selectedICDFile.length != 0)
		{
			StringBuilder fullPath = new StringBuilder();
			for (String str : selectedICDFile)
			{
				fullPath.append(filterPath);
				fullPath.append("\\");
				fullPath.append(str);
				fullPath.append(";");
			}
			locationPathField.setText(fullPath.toString());
		}

	}

	public String getICDFilePath()
	{
		return locationPathField.getText();
	}

}