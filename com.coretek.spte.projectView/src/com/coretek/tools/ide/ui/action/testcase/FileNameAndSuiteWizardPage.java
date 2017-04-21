package com.coretek.tools.ide.ui.action.testcase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.views.SPTEDecoratingLabelProvider;

/**
 * Create TestCase
 * 
 * @author SunDawei
 * @date 2010-12-16
 */
public class FileNameAndSuiteWizardPage extends WizardPage
{
	// 输入的用例名称
	private String						inputCaseName;

	private IFolder						folder;

	private TestCaseDescriptionWidget	widget;

	private Text						text;

	protected FileNameAndSuiteWizardPage(IFolder folder)
	{
		super(Messages.getString("I18N_CREATE_TEST_CASE"));
		this.setTitle(Messages.getString("I18N_CREATE_TEST_CASE"));
		this.setDescription(Messages.getString("I18N_INPUT_TESTCASE_NAME"));
		this.folder = folder;
	}

	public IFolder getFolder()
	{
		return this.folder;
	}

	public String getREQ()
	{
		return this.widget.getREQ();
	}

	public String getCondition()
	{
		return this.widget.getCondition();
	}

	public String getCriterion()
	{
		return this.widget.getCriterion();
	}

	public String getProcedure()
	{
		return this.widget.getProcedure();
	}

	public String getExpected()
	{
		return this.widget.getExpected();
	}

	public String getConstraint()
	{
		return this.widget.getConstraint();
	}

	/**
	 * To show suite choose widget
	 * 
	 * @param parent
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-8
	 */
	private CheckboxTreeViewer showSuiteFolder(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(gridData);
		panel.setLayout(new FillLayout());
		CheckboxTreeViewer ctv = new CheckboxTreeViewer(panel, SWT.BORDER);

		IDecoratorManager manager = IDEWorkbenchPlugin.getDefault().getWorkbench().getDecoratorManager();
		SPTEDecoratingLabelProvider labelProvider = new SPTEDecoratingLabelProvider(new WorkbenchLabelProvider(), manager, "");
		ctv.setLabelProvider(labelProvider);
		ctv.setContentProvider(new TreeContentProvider());
		ctv.setInput(this.getAllTestProject());
		ctv.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent event)
			{
				Object obj = event.getElement();
				if (obj instanceof IFolder)
				{
					IFolder selected = (IFolder) obj;
					if (Utils.isTestSuite(selected))
					{
						folder = selected;
						setErrorMessage(null);
						validatePage();
					} else
					{
						setErrorMessage("请选择一个测试集！");
					}
				} else
				{
					setErrorMessage("请选择一个测试集！");
				}
			}

		});
		ctv.expandAll();
		return ctv;
	}

	public void createControl(Composite parent)
	{
		final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		TabItem one = new TabItem(tabFolder, SWT.NONE);
		one.setText(Messages.getString("I18N_REQ_MSG"));
		one.setToolTipText(Messages.getString("I18N_CASE_REQ_MSG"));
		one.setControl(showTabOne(tabFolder));

		widget = new TestCaseDescriptionWidget();

		TabItem two = new TabItem(tabFolder, SWT.NONE);
		two.setText(Messages.getString("I18N_DESCRIBE"));
		two.setToolTipText(Messages.getString("I18N_CASE_DES_MSG"));
		two.setControl(widget.showWidget(tabFolder));
		this.setControl(tabFolder);
		text.setFocus();
		tabFolder.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				TabItem item = (TabItem) e.item;
				if (item.getText().equals(Messages.getString("I18N_REQ_MSG")))
				{
					text.setFocus();
				}
				if (item.getText().equals(Messages.getString("I18N_DESCRIBE")))
				{
					widget.getTxtRequireNum().setFocus();
				}
			}

		});
		this.setPageComplete(false);

	}

	private IProject[] getAllTestProject()
	{
		IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> list = new ArrayList<IProject>(prjs.length);
		for (IProject prj : prjs)
		{
			if (!Utils.isICDProject(prj))
			{
				list.add(prj);
			}
		}
		return list.toArray(new IProject[list.size()]);
	}

	/**
	 * To show case's name input text.
	 * 
	 * @param parent
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-7
	 */
	private Text showCaseName(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		panel.setLayout(gridLayout);
		panel.setLayoutData(gridData);

		Label label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("I18N_TESTCASE_NAME") + ":");
		gridData = new GridData();
		gridData.widthHint = 80;
		label.setLayoutData(gridData);

		text = new Text(panel, SWT.BORDER);

		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 15;
		text.setLayoutData(gridData);
		text.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				Text text = (Text) e.getSource();
				inputCaseName = text.getText();
				validatePage();
			}

		});
		text.setFocus();
		return text;
	}

	/**
	 * To show icd info
	 * 
	 * @param tabFolder
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-7
	 */
	private Control showTabOne(TabFolder tabFolder)
	{
		Composite panel = new Composite(tabFolder, SWT.NONE);
		// panel.setFocus();
		GridLayout layout = new GridLayout(1, true);
		panel.setLayout(layout);
		this.showCaseName(panel);
		if (this.folder == null || !Utils.isTestSuite(folder))
			this.showSuiteFolder(panel);
		return panel;
	}

	public boolean validatePage()
	{
		if (StringUtils.isNull(this.inputCaseName))
		{
			this.setErrorMessage("请输入测试用例的名字");
			this.setPageComplete(false);
			return false;
		}
		if (this.folder == null || !Utils.isTestSuite(folder))
		{
			this.setErrorMessage("请选择一个测试集合！");
			this.setPageComplete(false);
			return false;
		}
		if (!Utils.isUniqueCaseInFolder(inputCaseName + SPTEConstants.RESULT_FILE_POST_FIX, this.folder))
		{
			this.setErrorMessage("测试集当中已经存在同名的测试用例");
			this.setPageComplete(false);
			return false;
		}

		this.setErrorMessage(null);
		this.setPageComplete(true);
		
		return true;
	}

	public String getInputCaseName()
	{
		return inputCaseName;
	}

	public void setInputCaseName(String inputCaseName)
	{
		this.inputCaseName = inputCaseName;
	}

	public String getInput()
	{
		return this.widget.getInput();
	}
}