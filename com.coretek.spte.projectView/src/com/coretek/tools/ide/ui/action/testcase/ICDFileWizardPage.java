/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.tools.ide.ui.action.testcase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.FunctionCell;
import com.coretek.spte.FunctionDomain;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.FunctionSubDomain;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.views.SPTEDecoratingLabelProvider;

/**
 * @author SunDawei 2012-5-8
 */
public class ICDFileWizardPage extends WizardPage
{
	private Combo				cmbModulesType;
	private Label				lblFighter;
	private Label				lblVersion;
	private CheckboxTableViewer	ctvModules;

	private String				modulesType;
	// 选择的某个icd文件
	private IFile				targetFile;

	// 版本号
	private String				versionId;
	private List<String>		moduleTypesList	= new ArrayList<String>();

	private String				level;

	// 被测对象节点列表
	private List<Entity>		moduleIds;
	private List<Entity>		moduleIdChecked	= new ArrayList<Entity>();

	public ICDFileWizardPage()
	{
		super("选择ICD文件");
		this.setTitle("ICD信息");
		this.setMessage("选择ICD文件与被测对象");
		setPageComplete(false);
	}

	public String getVersionId()
	{
		return this.versionId;
	}

	public String getLevel()
	{
//		return this.level;
		String l = "";
		if(cmbModulesType == null) { return l;}
		if("功能单元".equals(cmbModulesType.getText())){
			return "21";
		}
		if("功能节点".equals(cmbModulesType.getText())) {
			return "22";
		}
		return l;
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ICDFileWizardPage(String pageName, String title, ImageDescriptor titleImage)
	{
		super(pageName, title, titleImage);
	}

	public IFile getICDFile()
	{
		return this.targetFile;
	}

	public List<Entity> getModuleIdChecked()
	{
		return moduleIdChecked;
	}

	public void createControl(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite panel = new Composite(parent, SWT.BORDER);
		panel.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);
		Composite icdPanel = new Composite(panel, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 1;
		icdPanel.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		icdPanel.setLayoutData(gridData);
		this.showICDFileChoose(icdPanel);
		this.lblFighter = this.showFighterType(panel);
		this.lblVersion = this.showVersion(panel);
		this.cmbModulesType = this.showFunctionType(panel);
		this.ctvModules = this.showTestedObjects(panel);
		this.setControl(panel);
	}

	private Combo showFunctionType(Composite parent)
	{
		Label label = new Label(parent, SWT.LEFT);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		label.setText("被测对象类型:");
		label.setLayoutData(gd);

		Combo cmbType = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = 130;
		cmbType.setLayoutData(gd);

		cmbType.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				Combo cmb = (Combo) e.getSource();
				modulesType = cmb.getText();
				Entity entity = TemplateEngine.getEngine().parseICD(targetFile.getLocation().toFile()).getFighter();
				moduleIds = new ArrayList<Entity>();
				moduleIds = TemplateUtils.getSelectTypeofFighter(entity, modulesType);
				level = TemplateUtils.getLevelOfFigther(entity, modulesType);

				ctvModules.setInput(moduleIds);
				ctvModules.refresh();

			}

		});

		return cmbType;
	}

	private CheckboxTableViewer showTestedObjects(Composite parent)
	{
		Label label = new Label(parent, SWT.LEFT);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		label.setText("被测对象节点号:");
		label.setLayoutData(gd);

		CheckboxTableViewer ctv = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		gd.heightHint = 130;
		ctv.getTable().setLayoutData(gd);
		ctv.setContentProvider(new ModulesContentProvider());
		ctv.setLabelProvider(new ModulesLabelProvider());
		ctv.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent event)
			{
				updateOkButtonStatus();
				validatePage();
			}
		});

		return ctv;
	}

	private Label showVersion(Composite parent)
	{
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);
		panel.setLayoutData(gd);

		Label label = new Label(panel, SWT.RIGHT);
		gd = new GridData();
		label.setText("版本:");
		label.setLayoutData(gd);

		label = new Label(panel, SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(gd);

		return label;
	}

	private Label showFighterType(Composite parent)
	{
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);
		panel.setLayoutData(gd);

		Label label = new Label(panel, SWT.RIGHT);
		gd = new GridData();
		label.setText("机型:");
		label.setLayoutData(gd);

		label = new Label(panel, SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(gd);

		return label;
	}

	private CheckboxTreeViewer showICDFileChoose(Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Group group = new Group(parent, SWT.NONE);
		group.setText("选择ICD文件");
		group.setLayout(new FillLayout());
		group.setLayoutData(gridData);
		CheckboxTreeViewer ctv = new CheckboxTreeViewer(group);
		ctv.addCheckStateListener(new CheckStateListener());
		IDecoratorManager manager = IDEWorkbenchPlugin.getDefault().getWorkbench().getDecoratorManager();
		SPTEDecoratingLabelProvider labelProvider = new SPTEDecoratingLabelProvider(new WorkbenchLabelProvider(), manager, "ICD");
		ctv.setLabelProvider(labelProvider);
		ctv.setContentProvider(new TreeContentProvider());
		ctv.setInput(this.getAllICDProject());
		ctv.expandAll();

		return ctv;
	}

	private IProject[] getAllICDProject()
	{
		IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> list = new ArrayList<IProject>(prjs.length);
		for (IProject prj : prjs)
		{
			if (Utils.isICDProject(prj))
			{
				list.add(prj);
			}
		}
		return list.toArray(new IProject[list.size()]);
	}

	public boolean validatePage()
	{
		if (moduleIdChecked.size() == 0)
		{
			this.setErrorMessage("至少需要选择 一个测试对象");
			this.setPageComplete(false);
			return false;
		}
		this.setErrorMessage(null);
		this.setPageComplete(true);
		return true;
	}

	private void updateOkButtonStatus()
	{
		moduleIdChecked.clear();
		for (Object test : ctvModules.getCheckedElements())
		{
			if (test instanceof FunctionSubDomain)
			{
				moduleIdChecked.addAll(TemplateUtils.getAllFunctionNodesOfFunctionSubDomain((FunctionSubDomain) test));
			} else if (test instanceof FunctionDomain)
			{
				moduleIdChecked.addAll(TemplateUtils.getAllFunctionNodes((FunctionDomain) test));
			} else if (test instanceof FunctionNode)
			{
				moduleIdChecked.add((FunctionNode) test);
			} else if (test instanceof FunctionCell) {
				moduleIdChecked.add((FunctionCell)test);
			}

		}

	}

	private class ModulesContentProvider implements IStructuredContentProvider
	{
		public void dispose()
		{
			// Nothing to dispose
		}

		public Object[] getElements(Object inputElement)
		{
			// inputElement, the input data, is myList
			return ((List) inputElement).toArray();
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{
			// Nothing to do
		}
	}

	private class ModulesLabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof Entity)
			{
				Entity entity = (Entity) element;

				switch (columnIndex)
				{
					case 0:
						return (String) entity.getFieldValue("name");
				}

			}
			return null;
		}

		public void addListener(ILabelProviderListener listener)
		{

		}

		public void dispose()
		{

		}

		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		public void removeListener(ILabelProviderListener listener)
		{

		}

	}

	private class CheckStateListener implements ICheckStateListener
	{

		public void checkStateChanged(CheckStateChangedEvent event)
		{
			CheckboxTreeViewer ctv = (CheckboxTreeViewer) event.getSource();
			ctv.setAllChecked(false);
			ctv.setChecked(event.getElement(), true);
			IResource resource = (IResource) event.getElement();
			if (resource instanceof IFile)
			{
				targetFile = (IFile) resource;
				ParsingICDWithProgress progress = new ParsingICDWithProgress(targetFile.getLocation().toFile());
				try
				{
					new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, progress);
				} catch (InvocationTargetException e1)
				{
					e1.printStackTrace();
				} catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				Entity fighter = progress.getICDManager().getFighter();
				moduleTypesList = TemplateUtils.getAllLayersNameOfFigther(fighter);
				cmbModulesType.removeAll();
				for (String str : moduleTypesList)
				{
//					if (str.equals("功能单元"))
//						continue;
					cmbModulesType.add(str);
				}

				cmbModulesType.select(cmbModulesType.getItemCount() - 1);
				modulesType = cmbModulesType.getText();
				lblFighter.setText(fighter.getFieldValue("fighterDescription").toString());

				lblVersion.setText(TemplateUtils.getVersionIDOfFighter(fighter));
				moduleIds = TemplateUtils.getSelectTypeofFighter(fighter, modulesType);
				versionId = TemplateUtils.getVersionIDOfFighter(fighter);
				ctvModules.setInput(moduleIds);
			} else
			{
				cmbModulesType.removeAll();
				lblFighter.setText("");
				lblVersion.setText("");
				ctvModules.setInput(new ArrayList<Entity>(0));
			}
			moduleIdChecked.clear();
			setPageComplete(validatePage());

		}

	}
}