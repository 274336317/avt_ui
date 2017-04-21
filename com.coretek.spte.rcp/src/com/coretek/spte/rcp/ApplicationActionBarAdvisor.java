package com.coretek.spte.rcp;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

/**
 * 在此类中将文件、编辑、测试、帮助菜单添加到workbench上的主菜单中， 并分别为这些菜单添加了菜单项。
 * 
 * @author 孙大巍
 * @date 2010-11-22
 * 
 */
/**
 * __________________________________________________________________________________
 * 
 * @Class ApplicationActionBarAdvisor.java
 * @Description 工具栏菜单编辑类
 * @Auther MENDY
 * @Date 2016-5-17 下午02:51:30
 */
@SuppressWarnings("restriction")
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{

	private IWorkbenchAction exitAction;

	private IWorkbenchAction newAction; // 新建

	private IWorkbenchAction closeEditorAction; // 关闭某个editor

	private IWorkbenchAction closeAllEditorsAction; // 关闭所有的editors

	private IWorkbenchAction saveAction; // 保存

	private IWorkbenchAction saveAsAction; // 另存为

	private IWorkbenchAction saveAllAction; // 保存所有

	private IWorkbenchAction switchWorkspaceAction; // 切换工作空间

	private IWorkbenchAction importAction; // 导入

	private IWorkbenchAction exportAction; // 导出

	private IContributionItem currentFiles; // 最近文件列表

	private IWorkbenchAction undoAction; // 撤销

	private IWorkbenchAction redoAction; // 重做

	private IWorkbenchAction cutAction; // 剪贴

	private IWorkbenchAction copyAction; // 复制

	private IWorkbenchAction pasteAction; // 黏贴

	private IWorkbenchAction deleteAction; // 删除

	private IWorkbenchAction selectAllAction; // 全选

	private IWorkbenchAction searchAction; // 查找

	private IWorkbenchAction helpContentsAction; // 帮助内容

	private IWorkbenchAction aboutAction; // 关于菜单

	private IWorkbenchAction resetPerspectiveAction; // 重新设置透视图布局

	private IWorkbenchAction preferencesAction; // 首选项

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
	{
		super(configurer);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationActionBarAdvisor
	 * @Function removeDeuplicatedAction
	 * @Description TODO(删除默认菜单)
	 * @Auther MENDY
	 *         void
	 * @Date 2016-5-7 上午11:00:35
	 *       __________________________________________________________________________________
	 */
	public void removeDeuplicatedAction()
	{
		ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
		IActionSetDescriptor[] actionSets = reg.getActionSets();
		String actionSetId2 = "org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo";
		String actionSetId4 = "org.eclipse.ui.actionSet.openFiles";
		String actionSetId = "org.eclipse.ui.edit.text.actionSet.annotationNavigation";
		String actionSetId1 = "org.eclipse.ui.edit.text.actionSet.navigation";
		for (IActionSetDescriptor item : actionSets)
		{
			if (item.getId().equals(actionSetId2) || item.getId().equals(actionSetId4) || item.getId().equals(actionSetId) || item.getId().equals(actionSetId1))
			{
				IExtension ext = item.getConfigurationElement().getDeclaringExtension();
				reg.removeExtension(ext, new Object[]
				{ item });
			}
		}
	}

	protected void makeActions(final IWorkbenchWindow window)
	{
		this.newAction = ActionFactory.NEW.create(window);
		// this.newAction.setText("新建");
		this.register(this.newAction);

		this.closeEditorAction = ActionFactory.CLOSE.create(window);
		// this.closeEditorAction.setText("关闭");
		this.register(this.closeEditorAction);

		this.closeAllEditorsAction = ActionFactory.CLOSE_ALL.create(window);
		// this.closeAllEditorsAction.setText("关闭所有");
		this.register(this.closeAllEditorsAction);

		this.saveAction = ActionFactory.SAVE.create(window);
		// this.saveAction.setText("保存");
		this.register(this.saveAction);

		this.saveAsAction = ActionFactory.SAVE_AS.create(window);
		// this.saveAsAction.setText("另存为");
		this.register(this.saveAsAction);

		this.saveAllAction = ActionFactory.SAVE_ALL.create(window);
		// this.saveAllAction.setText("全部保存");
		this.register(this.saveAllAction);

		this.switchWorkspaceAction = IDEActionFactory.OPEN_WORKSPACE.create(window);
		// this.switchWorkspaceAction.setText("切换工作空间");
		this.register(this.switchWorkspaceAction);

		this.importAction = ActionFactory.IMPORT.create(window);
		// this.importAction.setText("导入");
		this.register(this.importAction);

		this.exportAction = ActionFactory.EXPORT.create(window);
		// this.exportAction.setText("导出");
		this.register(exportAction);

		this.currentFiles = ContributionItemFactory.REOPEN_EDITORS.create(window);

		exitAction = ActionFactory.QUIT.create(window);
		// this.exitAction.setText("退出");
		register(exitAction);

		this.undoAction = ActionFactory.UNDO.create(window);
		// this.undoAction.setText("撤销");
		this.register(this.undoAction);

		this.redoAction = ActionFactory.REDO.create(window);
		// this.redoAction.setText("重做");
		this.register(this.redoAction);

		this.cutAction = ActionFactory.CUT.create(window);
		// this.cutAction.setText("剪贴");
		this.register(this.cutAction);

		this.copyAction = ActionFactory.COPY.create(window);
		// this.copyAction.setText("复制");
		this.register(this.copyAction);

		this.pasteAction = ActionFactory.PASTE.create(window);
		// this.pasteAction.setText("黏贴");
		this.register(this.pasteAction);

		this.deleteAction = ActionFactory.DELETE.create(window);
		// this.deleteAction.setText("删除");
		this.register(this.deleteAction);

		this.selectAllAction = ActionFactory.SELECT_ALL.create(window);
		// this.selectAllAction.setText("全选");
		this.register(this.selectAllAction);

		this.searchAction = ActionFactory.FIND.create(window);
		// this.searchAction.setText("查找/替换");
		this.register(this.searchAction);

		this.helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		// this.helpContentsAction.setText("帮助内容");
		this.register(this.helpContentsAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		// this.aboutAction.setText("关于");
		register(aboutAction);

		this.resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		// this.resetPerspectiveAction.setText("恢复透视图布局");
		this.register(this.resetPerspectiveAction);

		this.preferencesAction = ActionFactory.PREFERENCES.create(window);
		// this.preferencesAction.setText("首选项");
		this.register(this.preferencesAction);
		removeDeuplicatedAction();
	}

	/**
	 * 创建菜单栏，并添加菜单项
	 */
	protected void fillMenuBar(IMenuManager menuBar)
	{
		// 添加文件菜单
		MenuManager fileMenu = new MenuManager("文件(&F)", IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);

		fileMenu.add(this.newAction);
		fileMenu.add(this.closeEditorAction);
		fileMenu.add(this.closeAllEditorsAction);
		fileMenu.add(new Separator("0"));
		fileMenu.add(this.saveAction);
		fileMenu.add(this.saveAsAction);
		fileMenu.add(this.saveAllAction);
		fileMenu.add(new Separator("1"));
		fileMenu.add(this.switchWorkspaceAction);
		fileMenu.add(new Separator("2"));
		fileMenu.add(this.importAction);
		fileMenu.add(this.exportAction);
		fileMenu.add(this.currentFiles);
		fileMenu.add(new Separator("3"));
		fileMenu.add(exitAction);

		// 编辑菜单
		MenuManager editMenu = new MenuManager("编辑(&E)", IWorkbenchActionConstants.M_EDIT);
		editMenu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
		menuBar.add(editMenu);

		editMenu.add(this.undoAction);
		editMenu.add(this.redoAction);

		editMenu.add(new Separator("1"));
		editMenu.add(this.cutAction);
		editMenu.add(this.copyAction);
		editMenu.add(this.pasteAction);
		editMenu.add(new Separator("2"));
		editMenu.add(this.deleteAction);
		editMenu.add(this.selectAllAction);
		editMenu.add(this.searchAction);

		// 窗口菜单
		MenuManager windowMenu = new MenuManager("窗口(&W)", IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);
		windowMenu.add(new Separator());
		windowMenu.add(this.resetPerspectiveAction);
		windowMenu.add(this.preferencesAction);

		// 帮组菜单
		MenuManager helpMenu = new MenuManager("帮助(&H)", IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);

		helpMenu.add(this.helpContentsAction);
		helpMenu.add(this.aboutAction);
	}
	
	/**
	 * 创建工具栏，并添加工具按钮
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar)
	{
	}
	
	// ------------------------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------------------------
	
	static ApplicationActionBarAdvisor _instance;
	static IMenuManager _menuBar;
	
	public static ApplicationActionBarAdvisor getDefault() {
		return _instance;
	}
	
	public static IMenuManager getMenuBar() {
		return _menuBar;
	}
	
}
