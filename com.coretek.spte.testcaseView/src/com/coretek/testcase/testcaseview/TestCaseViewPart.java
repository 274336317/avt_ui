package com.coretek.testcase.testcaseview;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.actions.TestCaseViewActionGroup;
import com.coretek.testcase.testcaseview.actions.left.LeftActionGroup;

/**
 * 测试用例列表视图
 * 
 * @author 孙大巍
 * @date 2010-12-2
 * 
 */
public class TestCaseViewPart extends ViewPart
{

	public static final String		ID							= "com.coretek.tools.sequence.testcaseList";

	private static final String		TAG_CUSTOMIZED_LIST			= "customizedList";

	private static final String		TAG_GROUP					= "group";

	private static final String		TAG_GROUP_NAME				= "name";

	private static final String		TAG_GROUP_UUID				= "uuid";

	private static final String		TAG_TESTCASE				= "testCase";

	private static final String		TAG_TESTCASE_PROJECT		= "project";

	private static final String		TAG_TESTCASE_SUITE			= "suite";

	private static final String		TAG_TESTCASE_PATH			= "path";

	private static final String		TAG_TESTCASE_CASE			= "case";

	private static final String		TAG_TESTCASE_ENABLED		= "enabled";

	private static final String		TAG_TESTCASE_ENABLED_CASE	= "enabledCases";

	/**
	 * 列表视图
	 */
	private CheckboxTreeViewer		treeViewer;

	/**
	 * 用例视图
	 */
	private CheckboxTableViewer		tableViewer;

	private TestCaseViewActionGroup	actionGroup;

	private LeftActionGroup			leftActionGroup;

	private Image					runImage;

	private Image					refreshImage;

	private Image					stopImage;

	private Image					stopDiabledImage;

	private List<TestCase>			list;

	private Combo					cmb_AllSuites;

	private Combo					cmb_KeyWords;

	private ToolItem				stopItem;

//	private List<Button>			cfgButList					= new ArrayList<Button>();
	private List<Link>              cfgLinkList                 = new ArrayList<Link>();

	private Image					upImage;

	private Image					downImage;

	private IResourceChangeListener	listener;

	public TestCaseViewPart()
	{
		this.listener = new CaseChangeListener(this);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(listener);
	}

	@Override
	public void saveState(IMemento memento)
	{
		this.saveCustomized(memento);

		this.saveDisabledCases(memento);
	}

	/**
	 * 保存用户自定义列表
	 */
	private void saveCustomized(IMemento memento)
	{
		// 保存用户自定义列表
		IMemento customized = memento.createChild(TAG_CUSTOMIZED_LIST);
		TestCaseGroup customizedGroup = GroupFactory.getCustomizedList();
		for (TestCaseGroup group : customizedGroup.getChildGroups())
		{
			IMemento groupM = customized.createChild(TAG_GROUP);
			groupM.putString(TAG_GROUP_NAME, group.getName());
			groupM.putString(TAG_GROUP_UUID, group.getUUID());
			for (TestCase testCase : group.getTestCases())
			{
				IMemento testM = groupM.createChild(TAG_TESTCASE);
				testM.putString(TAG_TESTCASE_PROJECT, testCase.getProjectName());
				testM.putString(TAG_TESTCASE_SUITE, testCase.getSuiteName());
				testM.putString(TAG_TESTCASE_PATH, testCase.getPath());
				testM.putString(TAG_TESTCASE_CASE, testCase.getCaseName());
				testM.putBoolean(TAG_TESTCASE_ENABLED, testCase.isEnabled());

			}
		}
	}

	/**
	 * 保存被用户禁用的测试用例
	 * 
	 * @param memento
	 */
	@SuppressWarnings("unchecked")
	private void saveDisabledCases(IMemento memento)
	{
		// 保存被用户禁用的测试用例
		IMemento enabledCases = memento.createChild(TAG_TESTCASE_ENABLED_CASE);
		List<TestCase> cases = (List<TestCase>) this.tableViewer.getInput();
		for (TestCase testCase : cases)
		{
			if (testCase.isEnabled())
				continue;
			IMemento testM = enabledCases.createChild(TAG_TESTCASE);
			testM.putString(TAG_TESTCASE_PROJECT, testCase.getProjectName());
			testM.putString(TAG_TESTCASE_SUITE, testCase.getSuiteName());
			testM.putString(TAG_TESTCASE_PATH, testCase.getPath());
			testM.putString(TAG_TESTCASE_CASE, testCase.getCaseName());
			testM.putBoolean(TAG_TESTCASE_ENABLED, testCase.isEnabled());
		}
	}

	public CheckboxTreeViewer getTreeViewer()
	{
		return treeViewer;
	}

	public CheckboxTableViewer getTableViewer()
	{
		return tableViewer;
	}

	/**
	 * 恢复用户自定义的列表
	 * 
	 * @param customized
	 */
	private void restoreCustomizedList(IMemento customized)
	{
		IMemento childGroups[] = customized.getChildren(TAG_GROUP);
		TestCaseGroup customizedGroup = GroupFactory.getCustomizedList();
		customizedGroup.clearChildGroups();
		// 获取列表
		for (IMemento groupM : childGroups)
		{
			TestCaseGroup group = new TestCaseGroup();
			group.setUUID(groupM.getString(TAG_GROUP_UUID));
			group.setName(groupM.getString(TAG_GROUP_NAME));
			customizedGroup.addChildGroup(group);
			group.setParentGroup(customizedGroup);
			IMemento testCases[] = groupM.getChildren(TAG_TESTCASE);
			// 获取列表中的测试用例
			for (IMemento testM : testCases)
			{
				TestCase testCase = new TestCase();
				testCase.setCaseName(testM.getString(TAG_TESTCASE_CASE));
				testCase.setSuiteName(testM.getString(TAG_TESTCASE_SUITE));
				testCase.setPath(testM.getString(TAG_TESTCASE_PATH));
				testCase.setProjectName(testM.getString(TAG_TESTCASE_PROJECT));
				testCase.setGroup(group);
				testCase.setEnabled(testM.getBoolean(TAG_TESTCASE_ENABLED));
				group.addTestCase(testCase);
				// 防止添加已经不存在的测试用例
				if (list.contains(testCase))
				{
					list.set(list.indexOf(testCase), testCase);
				}
			}
		}
	}

	/**
	 * 恢复被精致的测试用例列表
	 * 
	 * @param enabledCases
	 */
	private void restoreDisabledList(IMemento enabledCases)
	{
		IMemento testCases[] = enabledCases.getChildren(TAG_TESTCASE);
		if (testCases != null)
		{
			for (IMemento testM : testCases)
			{
				TestCase testCase = new TestCase();
				testCase.setCaseName(testM.getString(TAG_TESTCASE_CASE));
				testCase.setSuiteName(testM.getString(TAG_TESTCASE_SUITE));
				testCase.setPath(testM.getString(TAG_TESTCASE_PATH));
				testCase.setProjectName(testM.getString(TAG_TESTCASE_PROJECT));
				testCase.setGroup(null);
				testCase.setEnabled(false);
				// 防止添加已经不存在的测试用例
				if (list.contains(testCase))
				{
					list.set(list.indexOf(testCase), testCase);
				}
			}
		}
	}

	/**
	 * 获取工作空间下的所有测试用例
	 * 
	 * @return
	 */
	private List<TestCase> getAllCases()
	{
		IFile[] files = Utils.getAllCasesInWorkspace();
		List<TestCase> list = new ArrayList<TestCase>();
		for (IFile file : files)
		{
			TestCase test = new TestCase();
			test.setCaseName(file.getName());
			test.setSuiteName(file.getParent().getName());
			test.setPath(file.getProjectRelativePath().toOSString());
			test.setProjectName(file.getProject().getName());
			list.add(test);
		}

		return list;
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException
	{
		super.init(site, memento);

		this.list = this.getAllCases();
		if (memento != null)
		{
			// 获取被保存的所有自定义列表的内容
			IMemento customized = memento.getChild(TAG_CUSTOMIZED_LIST);
			if (customized != null)
			{
				this.restoreCustomizedList(customized);
			}
			// 获取被用户禁止的测试用例
			IMemento enabledCases = memento.getChild(TAG_TESTCASE_ENABLED_CASE);
			if (enabledCases != null)
			{
				this.restoreDisabledList(enabledCases);
			}
		}
	}

	@Override
	public void dispose()
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(listener);
		if (this.runImage != null)
		{
			this.runImage.dispose();
			this.runImage = null;
		}
		if (this.refreshImage != null)
		{
			this.refreshImage.dispose();
			this.refreshImage = null;
		}
		if (this.stopImage != null)
			this.stopImage.dispose();
		if (this.stopDiabledImage != null)
			this.stopDiabledImage.dispose();
		if (downImage != null)
		{
			downImage.dispose();
			downImage = null;
		}
		if (upImage != null)
		{
			upImage.dispose();
			upImage = null;
		}

		super.dispose();
	}

	/**
	 * 显示停止按钮
	 * 
	 * @param toolBar
	 * @return
	 */
	private ToolItem showStopImage(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText("停止正在执行的测试用例");
		this.stopImage = TestCaseViewPlugin.getImageDescriptor("icons/stop.gif").createImage();
		this.stopDiabledImage = TestCaseViewPlugin.getImageDescriptor("icons/602_stop.gif").createImage();
		item.setDisabledImage(this.stopDiabledImage);
		item.setImage(this.stopImage);
		item.setEnabled(false);
		// item.addSelectionListener(new StopItemSelectionListener());
		return item;
	}

	/**
	 * 设置工具栏的停止执行按钮的状态
	 * 
	 * @param enabled </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-1
	 */
	public void enableStop(boolean enabled)
	{
		this.stopItem.setEnabled(enabled);
	}

	/**
	 * 注册监听停止按钮的监听器 </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-9-3
	 */
	public void registerStopListener(SelectionListener listener)
	{
		this.stopItem.addSelectionListener(listener);
	}

	/**
	 * 取消注册的停止按钮监听器
	 * 
	 * @param listener </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-9-3
	 */
	public void cancelStopListener(SelectionListener listener)
	{
		this.stopItem.removeSelectionListener(listener);
	}


	/**
	 * 显示刷新按钮
	 * 
	 * @param toolBar
	 * @return
	 */
	private ToolItem showRefreshImage(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("I18N_REFRESH_CASE_VIEW"));
		// TODO (测试用例列表中刷新按钮)
		//this.refreshImage = TestCaseViewPlugin.getImageDescriptor("icons/refresh.gif").createImage();
		this.refreshImage = TestCaseViewPlugin.getImageDescriptor("icons/504_fresh.gif").createImage();
		item.setImage(this.refreshImage);
		item.addSelectionListener(new RefreshImageSelectionListener(this));
		return item;
	}

	/**
	 * 显示左边视图左边的内容
	 * 
	 * @param parent
	 * @return
	 */
	private Composite showLeftPanel(Composite parent)
	{
		Composite leftPanel = new Composite(parent, SWT.NONE);
		GridData gd = new GridData();
		gd.widthHint = 80;
		leftPanel.setLayoutData(gd);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		leftPanel.setLayout(grid);

		ToolBar toolBar = new ToolBar(leftPanel, SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		toolBar.setLayoutData(gd);

		this.showRunImage(toolBar);
		this.stopItem = this.showStopImage(toolBar);
		this.showRefreshImage(toolBar);

		return leftPanel;
	}

	/**
	 * 显示视图的右边的内容
	 * 
	 * @param parent
	 * @return
	 */
	private Composite showRightPanel(Composite parent)
	{
		Composite rightPanel = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		rightPanel.setLayoutData(gd);
		GridLayout grid = new GridLayout();
		grid.numColumns = 6;
		rightPanel.setLayout(grid);

		Label label = new Label(rightPanel, SWT.NONE);
		label.setText(Messages.getString("I18N_PICK_UP"));
		label.setAlignment(SWT.LEFT);

		showAllProjectsCombo(rightPanel);

		this.cmb_AllSuites = this.showSuitesCombo(rightPanel);

		this.cmb_KeyWords = this.showKeyWordsCmb(rightPanel);

		this.showOKButton(rightPanel);

		ToolBar toolBar = new ToolBar(rightPanel, SWT.HORIZONTAL);
		showUpImage(toolBar);
		showDownImage(toolBar);

		return rightPanel;
	}

	/**
	 * 显示关键字下拉框
	 * 
	 * @param rightPanel
	 * @return
	 */
	private Combo showKeyWordsCmb(Composite rightPanel)
	{
		Combo combo = new Combo(rightPanel, SWT.DROP_DOWN);
		combo.add(Messages.getString("I18N_FILTER_WORDS"));
		combo.select(0);
		GridData gd = new GridData();
		gd.widthHint = 100;
		combo.setLayoutData(gd);
		combo.addKeyListener(new KeyWordsCmbKeyListener());

		return combo;
	}

	/**
	 * 监听关键字下拉框的键盘事件
	 * 
	 * @author 孙大巍 2012-3-31
	 */
	private class KeyWordsCmbKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if (e.keyCode == 13)
			{// Enter键被按下
				recordKeyWords();

			}
		}

		public void keyReleased(KeyEvent e)
		{

		}
	}

	/**
	 * 显示测试集合下拉框
	 * 
	 * @param rightPanel
	 * @return
	 */
	private Combo showSuitesCombo(Composite rightPanel)
	{
		Combo combo = new Combo(rightPanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.add(Messages.getString("I18N_ALL_SUITES"));
		GridData gd = new GridData();
		gd.widthHint = 100;
		combo.setLayoutData(gd);
		combo.select(0);
		combo.addSelectionListener(new SuitesCmbSelectionListener());

		return combo;
	}

	/**
	 * 监听测试集合下拉框的选择事件
	 * 
	 * @author 孙大巍 2012-3-31
	 */
	private class SuitesCmbSelectionListener implements SelectionListener
	{
		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		public void widgetSelected(SelectionEvent e)
		{
			Combo combo = (Combo) e.getSource();
			String name = combo.getText();
			if (Messages.getString("I18N_ALL_SUITES").equals(name))
			{
				cmb_KeyWords.setText(Messages.getString("I18N_FILTER_WORDS"));
				Filter.getInstance().setSuiteName(null);
				Filter.getInstance().setKeyWords(null);
				refereshTable();
				return;
			}
			Filter.getInstance().setSuiteName(name);
			refereshTable();
		}
	}

	/**
	 * 显示项目下拉框
	 * 
	 * @param rightPanel
	 * @return
	 */
	private Combo showAllProjectsCombo(Composite rightPanel)
	{
		Combo combo = new Combo(rightPanel, SWT.DROP_DOWN);
		combo.add(Messages.getString("I18N_ALL_PROJECTS"));
		GridData gd = new GridData();
		gd.widthHint = 100;
		combo.setLayoutData(gd);
		for (IProject project : EclipseUtils.getAllProjects())
		{
			try
			{
				if (project.hasNature("com.coretek.spte.projectView.testProjectNature"))
				{// 在工程筛选是只对测试工程进行筛选，过滤掉ICDg工程
					combo.add(project.getName());
				}
			} catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		combo.select(0);
		combo.addSelectionListener(new AllProjectsCmbSelectionListener());

		return combo;
	}

	/**
	 * 监听项目下拉框的选中事件
	 * 
	 * @author 孙大巍 2012-3-31
	 */
	private class AllProjectsCmbSelectionListener implements SelectionListener
	{
		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		public void widgetSelected(SelectionEvent e)
		{
			Combo combo = (Combo) e.getSource();
			String name = combo.getText();
			if (Messages.getString("I18N_ALL_PROJECTS").equals(name))
			{
				cmb_AllSuites.removeAll();
				cmb_AllSuites.add(Messages.getString("I18N_ALL_SUITES"));
				cmb_AllSuites.select(0);
				cmb_KeyWords.setText(Messages.getString("I18N_FILTER_WORDS"));
				Filter.getInstance().setSuiteName(null);
				Filter.getInstance().setKeyWords(null);
				Filter.getInstance().setProjectName(null);
				refereshTable();
			} else
			{
				IProject project = EclipseUtils.getProject(name);
				if (project == null)
				{
					return;
				}
				Filter.getInstance().setProjectName(project.getName());
				Filter.getInstance().setSuiteName(null);
				Filter.getInstance().setKeyWords(null);
				refereshTable();
				cmb_AllSuites.removeAll();
				cmb_AllSuites.add(Messages.getString("I18N_ALL_SUITES"));
				cmb_AllSuites.select(0);
				for (IFolder folder : Utils.getTestSuite(project))
				{
					cmb_AllSuites.add(folder.getFullPath().removeFirstSegments(1).toOSString());
				}
			}
		}
	}

	/**
	 * 显示确定按钮
	 * 
	 * @param rightPanel
	 * @return
	 */
	private Button showOKButton(Composite rightPanel)
	{
		Button button = new Button(rightPanel, SWT.PUSH);
		GridData gd = new GridData();
		gd.widthHint = 50;
		button.setLayoutData(gd);
		button.setText("确定");
		button.addSelectionListener(new OKBtnSelectionListener());

		return button;
	}

	/**
	 * 监听确定按钮的选中事件
	 * 
	 * @author 孙大巍 2012-3-31
	 */
	private class OKBtnSelectionListener implements SelectionListener
	{
		public void widgetDefaultSelected(SelectionEvent e)
		{

		}

		public void widgetSelected(SelectionEvent e)
		{
			recordKeyWords();
		}
	}

	/**
	 * 显示上移的工具栏按钮
	 * 
	 * @param toolBar
	 * @return </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-6-5
	 */
	private ToolItem showUpImage(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText("上移");
		// TODO (测试用例列表中上移的工具栏按钮)
		//upImage = TestCaseViewPlugin.getImageDescriptor("icons/up.png").createImage();
		upImage = TestCaseViewPlugin.getImageDescriptor("icons/604_moveUp.gif").createImage();
		item.setImage(upImage);
		item.addSelectionListener(new UpBtnSelectionListener(this));
		return item;
	}

	/**
	 * 显示下移的工具栏按钮
	 * 
	 * @param toolBar
	 * @return </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-6-5
	 */
	private ToolItem showDownImage(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText("下移");
		// TODO (测试用例列表中下移的工具栏按钮)
		//downImage = TestCaseViewPlugin.getImageDescriptor("icons/down.png").createImage();
		downImage = TestCaseViewPlugin.getImageDescriptor("icons/605_moveDown.gif").createImage();
		item.setImage(downImage);
		item.addSelectionListener(new DownBtnSelectionListener(this));
		return item;
	}

	/**
	 * 显示运行按钮
	 * 
	 * @param toolBar
	 * @return
	 */
	private ToolItem showRunImage(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("I18N_EXCUTION"));
		// TODO (测试用例列表中运行按钮)
		//this.runImage = TestCaseViewPlugin.getImageDescriptor("icons/running.gif").createImage();
		this.runImage = TestCaseViewPlugin.getImageDescriptor("icons/601_do.gif").createImage();
		item.setImage(this.runImage);
		item.addSelectionListener(new RunImageSelectionListener(this));
		return item;
	}
	/**
	 * 显示树形分组视图
	 * 
	 * @param sashForm
	 * @return
	 */
	private CheckboxTreeViewer showTreeViewer(SashForm sashForm)
	{
		CheckboxTreeViewer treeViewer = new CheckboxTreeViewer(sashForm, SWT.BORDER);
		treeViewer.setContentProvider(new TreeViewContentProvider());
		treeViewer.setLabelProvider(new TreeViewLabelProvider());

		Set<TestCaseGroup> parentGroups = new HashSet<TestCaseGroup>(2);
		parentGroups.add(GroupFactory.getCustomizedList());
		parentGroups.add(GroupFactory.getAllCases());

		treeViewer.setInput(parentGroups);
		treeViewer.addCheckStateListener(new CheckStateListener(treeViewer, this));

		return treeViewer;
	}

	/**
	 * 监听测试用例列表中的选中事件
	 * 
	 * @author 孙大巍 2012-3-31
	 */
	private class CasesCheckStateListener implements ICheckStateListener
	{
		public void checkStateChanged(CheckStateChangedEvent event)
		{
			TestCase test = (TestCase) event.getElement();
			// 不可以选中被禁止了的测试用例
			if (!test.isEnabled())
			{
				tableViewer.setChecked(test, false);
			}
		}
	}

	/**
	 * 为用例列表视图添加显示的列
	 * 
	 * @param tableViewer
	 */
	private void addColumns(CheckboxTableViewer tableViewer)
	{
		TableColumn column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(StringUtils.EMPTY_STRING);
		column.setWidth(50);

		column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(Messages.getString("I18N_CASE_MARKER"));
		column.setWidth(100);

		column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(Messages.getString("I18N_TEST_LIST"));
		column.setWidth(100);

		column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(Messages.getString("I18N_SUITE"));
		column.setWidth(100);

		column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(Messages.getString("I18N_PROJECT"));
		column.setWidth(100);

		column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
		column.setText(Messages.getString("I18N_CONFIG"));
		column.setWidth(100);
		
//		column.
		
	}

	/**
	 * 为每一个测试用例添加配置按钮
	 * 
	 * @param tableViewer
	 * @param list
	 */
	private void addCfgBtn(CheckboxTableViewer tableViewer, List<TestCase> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			TestCase testCase = list.get(i);
			TableEditor editor = new TableEditor(tableViewer.getTable());

			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.grabVertical = true;
			
			/**将原本的Button修改为Link*/
			Link cfgLink = new Link(tableViewer.getTable(), SWT.NULL);
			cfgLink.setText("<A>选择配置</A>");
			cfgLink.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			editor.setEditor(cfgLink, tableViewer.getTable().getItem(i), 5);
			IPath path = new Path(testCase.toString());

			IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			File file = iFile.getLocation().toFile();
			if (file.exists())
			{
				String icdPath = Utils.getICDFilePath(file);
				path = new Path(icdPath);
				iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				if (iFile.exists() && testCase.isEnabled())
				{
					cfgLink.addMouseListener(new CfgBtnMouseListener(iFile.getLocation().toFile().getAbsolutePath()));
				} else
				{
					cfgLink.setEnabled(false);
				}
				cfgLinkList.add(cfgLink);
			}
		}
	}

	/**
	 * 显示测试用例列表视图
	 * 
	 * @return
	 */
	private CheckboxTableViewer showCases(SashForm sashForm)
	{
		CheckboxTableViewer tableViewer = CheckboxTableViewer.newCheckList(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		
		tableViewer.addCheckStateListener(new CasesCheckStateListener());
		tableViewer.getTable().addPaintListener(new TablePaintListener());
		tableViewer.addFilter(Filter.getInstance());
		this.addColumns(tableViewer);

		tableViewer.setContentProvider(new TableContentsProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setInput(this.list);
		sashForm.setWeights(new int[] { 3, 7 });

		this.addCfgBtn(tableViewer, this.list);

		return tableViewer;
	}

	/**
	 * 显示视图的底部部分
	 * 
	 * @param parent
	 * @return
	 */
	private SashForm showSashForm(Composite parent)
	{
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		sashForm.setLayoutData(gd);

		this.treeViewer = this.showTreeViewer(sashForm);

		this.tableViewer = this.showCases(sashForm);

		return sashForm;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new GridLayout());

		Composite mainPanel = new Composite(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		mainPanel.setLayoutData(gridData);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		mainPanel.setLayout(grid);

		this.showLeftPanel(mainPanel);

		this.showRightPanel(mainPanel);

		this.showSashForm(parent);

		this.initContextMenu();

		this.makeActions();

	}

	private void recordKeyWords()
	{
		String keyWords = cmb_KeyWords.getText();
		Filter.getInstance().setKeyWords(keyWords);
		refereshTable();
		if (cmb_KeyWords.indexOf(keyWords) < 0)
		{
			cmb_KeyWords.add(keyWords, 0);
		}
	}

	@SuppressWarnings("unchecked")
	public void refereshTable()
	{
		Display.getDefault().syncExec(new Runnable()
		{

			public void run()
			{
				Object[] selection = treeViewer.getCheckedElements();
				List<TestCaseGroup> groups = new ArrayList<TestCaseGroup>(selection.length);
				for (Object object : selection)
				{
					groups.add((TestCaseGroup) object);
				}
				int i = 0;
				if (!cfgLinkList.isEmpty())
				{
					for (Link link : cfgLinkList)
					{
						link.dispose();
						link = null;
					}
				}
				cfgLinkList.clear();
				IFile[] files = Utils.getAllCasesInWorkspace();
				List<TestCase> input = new ArrayList<TestCase>(files.length);
				List<TestCase> old = (List<TestCase>) getTableViewer().getInput();
				for (IFile file : files)
				{
					TestCase test = new TestCase();
					test.setCaseName(file.getName());
					test.setSuiteName(file.getParent().getName());
					test.setPath(file.getProjectRelativePath().toOSString());
					test.setProjectName(file.getProject().getName());
					if (old.contains(test))
					{
						for (TestCase oldTest : old)
						{
							if (oldTest.equals(test))
							{
								test.setEnabled(oldTest.isEnabled());
								if (oldTest.getGroup() != null)
								{
									test.setGroup(oldTest.getGroup());
									oldTest.getGroup().deleteTestCase(oldTest);
									oldTest.getGroup().addTestCase(test);
								}
							}
						}
					}

					input.add(test);
				}
				tableViewer.setInput(input);
				tableViewer.refresh(true);

				TableItem[] aitems = tableViewer.getTable().getItems();
				for (TableItem item : aitems)
				{
					// 将被禁止的测试用例从处于选中状态中排除
					TestCase test = (TestCase) item.getData();
					if (!test.isEnabled())
					{
						tableViewer.setChecked(test, false);
					}
					TableEditor editor = new TableEditor(tableViewer.getTable());

					editor.horizontalAlignment = SWT.LEFT;
					editor.grabHorizontal = true;
					editor.grabVertical = true;
					
					/**将原本的Button修改为Link*/
					Link cfgLink = new Link(tableViewer.getTable(), SWT.WRAP);
					cfgLink.setText("<A>选择配置</A>");
					cfgLink.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					editor.setEditor(cfgLink, tableViewer.getTable().getItem(i), 5);
					IPath path = new Path(test.toString());

					IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					File file = iFile.getLocation().toFile();
					if (file.exists())
					{
						String icdPath = Utils.getICDFilePath(file);
						path = new Path(icdPath);
						iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
						if (iFile.exists() && test.isEnabled())
						{
							cfgLink.addMouseListener(new CfgBtnMouseListener(iFile.getLocation().toFile().getAbsolutePath()));
						} else
						{
							cfgLink.setEnabled(false);
						}
						cfgLinkList.add(cfgLink);
					}

					i++;
				}

				tableViewer.refresh(true);
				for (TestCaseGroup group : groups)
				{
					if (group == GroupFactory.getAllCases())
					{
						tableViewer.setAllChecked(true);
						List<TestCase> tests2 = (List<TestCase>) tableViewer.getInput();
						for (TestCase test : tests2)
						{
							// 将被禁止的测试用例从处于选中状态中排除
							if (!test.isEnabled())
							{
								tableViewer.setChecked(test, false);
							}
						}
					} else
					{
						TableItem[] items = tableViewer.getTable().getItems();
						for (TableItem item : items)
						{
							TestCase test = (TestCase) item.getData();
							if (test.getGroup() == group && test.isEnabled())
							{
								item.setChecked(true);
							} else
							{
								item.setChecked(treeViewer.getChecked(test.getGroup()));
							}
						}
					}
				}

			}

		});

	}

	protected void initContextMenu()
	{
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				TestCaseViewPart.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(this.tableViewer.getTable());
		this.tableViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.tableViewer);

		menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				TestCaseViewPart.this.fillLeftContextMenu(manager);
			}
		});
		menu = menuMgr.createContextMenu(this.treeViewer.getTree());
		this.treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.treeViewer);
	}

	protected void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection selection = (IStructuredSelection) this.tableViewer.getSelection();
		this.actionGroup.setContext(new ActionContext(selection));
		this.actionGroup.fillContextMenu(menu);
	}

	private void fillLeftContextMenu(IMenuManager menu)
	{
		ISelection selection = this.treeViewer.getSelection();
		this.leftActionGroup.setContext(new ActionContext(selection));
		this.leftActionGroup.fillContextMenu(menu);
	}

	@Override
	public void setFocus()
	{

	}

	private void makeActions()
	{
		this.actionGroup = new TestCaseViewActionGroup(this);
		this.actionGroup.makeActions();
		this.actionGroup.fillActionBars(this.getViewSite().getActionBars());
		this.leftActionGroup = new LeftActionGroup(this);
	}
}