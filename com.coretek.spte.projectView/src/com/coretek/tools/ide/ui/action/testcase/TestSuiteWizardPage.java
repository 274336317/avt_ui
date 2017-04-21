package com.coretek.tools.ide.ui.action.testcase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import com.coretek.common.utils.StringUtils;
import com.coretek.testcase.projectView.action.Messages;
import com.coretek.tools.ide.internal.ui.UIPluginImages;

/**
 * 创建测试集
 * 
 * @author 孙大巍
 * @date 2010-12-16
 */
public class TestSuiteWizardPage extends WizardPage
{

	private Text	txtSuiteName;

	private String	suiteName;
	
	private Text projectText;
	
	/**
	 * @param pageName
	 */
	public TestSuiteWizardPage(IResource resource)
	{
		super(Messages.getString("test.suite.wizard.page.create.test.suite"));
		setTitle(Messages.getString("test.suite.wizard.page.create.test.suite"));
		setDescription("请先选择测试工程，再创建测试集");
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public TestSuiteWizardPage(String pageName, String title, ImageDescriptor titleImage)
	{
		super(pageName, title, titleImage);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent)
	{
		initializeDialogUnits(parent);
		
		Composite editorComp = new Composite(parent, SWT.NONE);
		GridLayout viewLayout = new GridLayout(3, false);
		viewLayout.marginHeight = 5;
		viewLayout.marginWidth = 5;
		editorComp.setLayout(viewLayout);
		editorComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label projectLabel= new Label(editorComp, SWT.LEFT | SWT.WRAP);
		projectLabel.setText("测试工程:");
		
		projectText = new Text(editorComp, SWT.SINGLE | SWT.BORDER |SWT.READ_ONLY);
		projectText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		GridData gd= new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalSpan = 1;
		projectText.setLayoutData(gd);
		projectText.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				validataPage();
			}
		});
		
		if(((TestSuiteNewWizard)getWizard()).getResource() !=null) {
			projectText.setText(((TestSuiteNewWizard)getWizard()).getResource().getName());
		}
		
		Button brownsButton = new Button(editorComp, SWT.PUSH);
		brownsButton.setText("浏览...");
		gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 1;
		PixelConverter converter= new PixelConverter(brownsButton);
		int widthHint= converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint, brownsButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		brownsButton.setLayoutData(gd);
		brownsButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				
			}

			public void widgetSelected(SelectionEvent e) {
				buttonPressed();
			}
			
		});
		Label caseNumLabel = new Label(editorComp, SWT.LEFT);
		caseNumLabel.setFont(editorComp.getFont());
		caseNumLabel.setText("测试集:");
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		caseNumLabel.setLayoutData(gridData);

		txtSuiteName = new Text(editorComp, SWT.LEFT | SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 350;
		txtSuiteName.setLayoutData(gridData);
		txtSuiteName.addListener(SWT.Modify, new Listener()
		{

			public void handleEvent(Event event)
			{
				suiteName = txtSuiteName.getText().trim();
				validataPage();
			}

		});
		this.setPageComplete(false);
		setControl(editorComp);
	}

	protected void buttonPressed() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(this.getShell(), new ILabelProvider(){

			public Image getImage(Object element) {
				if(element instanceof IProject) {
					return UIPluginImages.get(UIPluginImages.IMG_OBJS_PROJECT);
				}
				return null;
			}

			public String getText(Object element) {
				if(element instanceof IProject) {
					return ((IProject) element).getName();
				}
				return "";
			}

			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}

			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
		});
		List<IProject> list = new ArrayList<IProject>();
		for(IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				if(p.hasNature("com.coretek.spte.projectView.testProjectNature")) {
					list.add(p);
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dialog.setElements(list.toArray(new IProject[list.size()]));
		dialog.setTitle("选择测试工程");
		dialog.setMessage("请选择该测试集所对应的测试工程");
		if (dialog.open() == Window.OK) {
			IProject p = (IProject) dialog.getFirstResult();
			((TestSuiteNewWizard) getWizard()).setProject(p);
			projectText.setText(p.getName());
			
		}
	}

	/**
	 * To check the unique of suite name </br> <b>Author</b> SunDawei </br>
	 * <b>Date</b> 2012-5-11
	 */
	private void validataPage()
	{
		IWizard wizard = getWizard();

		IProject functionFolder = (IProject) ((TestSuiteNewWizard) wizard).getResource();
		if(functionFolder == null) {
			setErrorMessage("请选择测试工程");
			setPageComplete(false);
			return;
		}
		if (StringUtils.isNull(suiteName))
		{
			setErrorMessage("请输入测试集的名字");
			setPageComplete(false);
			return;
		}

		try
		{
			if (functionFolder.getFolder(suiteName) != null)
			{
				boolean isFolderExist = functionFolder.getFolder(suiteName).exists();
				if (isFolderExist)
				{
					setErrorMessage("已经存在同名的文件夹");
					setPageComplete(false);
					return;
				} else
				{
					setErrorMessage(null);
					setPageComplete(true);
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getSuiteName()
	{
		return suiteName;
	}
	
}