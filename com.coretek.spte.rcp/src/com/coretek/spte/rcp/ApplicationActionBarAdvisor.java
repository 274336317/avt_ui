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
 * �ڴ����н��ļ����༭�����ԡ������˵���ӵ�workbench�ϵ����˵��У� ���ֱ�Ϊ��Щ�˵�����˲˵��
 * 
 * @author ���Ρ
 * @date 2010-11-22
 * 
 */
/**
 * __________________________________________________________________________________
 * 
 * @Class ApplicationActionBarAdvisor.java
 * @Description �������˵��༭��
 * @Auther MENDY
 * @Date 2016-5-17 ����02:51:30
 */
@SuppressWarnings("restriction")
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{

	private IWorkbenchAction exitAction;

	private IWorkbenchAction newAction; // �½�

	private IWorkbenchAction closeEditorAction; // �ر�ĳ��editor

	private IWorkbenchAction closeAllEditorsAction; // �ر����е�editors

	private IWorkbenchAction saveAction; // ����

	private IWorkbenchAction saveAsAction; // ���Ϊ

	private IWorkbenchAction saveAllAction; // ��������

	private IWorkbenchAction switchWorkspaceAction; // �л������ռ�

	private IWorkbenchAction importAction; // ����

	private IWorkbenchAction exportAction; // ����

	private IContributionItem currentFiles; // ����ļ��б�

	private IWorkbenchAction undoAction; // ����

	private IWorkbenchAction redoAction; // ����

	private IWorkbenchAction cutAction; // ����

	private IWorkbenchAction copyAction; // ����

	private IWorkbenchAction pasteAction; // ���

	private IWorkbenchAction deleteAction; // ɾ��

	private IWorkbenchAction selectAllAction; // ȫѡ

	private IWorkbenchAction searchAction; // ����

	private IWorkbenchAction helpContentsAction; // ��������

	private IWorkbenchAction aboutAction; // ���ڲ˵�

	private IWorkbenchAction resetPerspectiveAction; // ��������͸��ͼ����

	private IWorkbenchAction preferencesAction; // ��ѡ��

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
	{
		super(configurer);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationActionBarAdvisor
	 * @Function removeDeuplicatedAction
	 * @Description TODO(ɾ��Ĭ�ϲ˵�)
	 * @Auther MENDY
	 *         void
	 * @Date 2016-5-7 ����11:00:35
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
		// this.newAction.setText("�½�");
		this.register(this.newAction);

		this.closeEditorAction = ActionFactory.CLOSE.create(window);
		// this.closeEditorAction.setText("�ر�");
		this.register(this.closeEditorAction);

		this.closeAllEditorsAction = ActionFactory.CLOSE_ALL.create(window);
		// this.closeAllEditorsAction.setText("�ر�����");
		this.register(this.closeAllEditorsAction);

		this.saveAction = ActionFactory.SAVE.create(window);
		// this.saveAction.setText("����");
		this.register(this.saveAction);

		this.saveAsAction = ActionFactory.SAVE_AS.create(window);
		// this.saveAsAction.setText("���Ϊ");
		this.register(this.saveAsAction);

		this.saveAllAction = ActionFactory.SAVE_ALL.create(window);
		// this.saveAllAction.setText("ȫ������");
		this.register(this.saveAllAction);

		this.switchWorkspaceAction = IDEActionFactory.OPEN_WORKSPACE.create(window);
		// this.switchWorkspaceAction.setText("�л������ռ�");
		this.register(this.switchWorkspaceAction);

		this.importAction = ActionFactory.IMPORT.create(window);
		// this.importAction.setText("����");
		this.register(this.importAction);

		this.exportAction = ActionFactory.EXPORT.create(window);
		// this.exportAction.setText("����");
		this.register(exportAction);

		this.currentFiles = ContributionItemFactory.REOPEN_EDITORS.create(window);

		exitAction = ActionFactory.QUIT.create(window);
		// this.exitAction.setText("�˳�");
		register(exitAction);

		this.undoAction = ActionFactory.UNDO.create(window);
		// this.undoAction.setText("����");
		this.register(this.undoAction);

		this.redoAction = ActionFactory.REDO.create(window);
		// this.redoAction.setText("����");
		this.register(this.redoAction);

		this.cutAction = ActionFactory.CUT.create(window);
		// this.cutAction.setText("����");
		this.register(this.cutAction);

		this.copyAction = ActionFactory.COPY.create(window);
		// this.copyAction.setText("����");
		this.register(this.copyAction);

		this.pasteAction = ActionFactory.PASTE.create(window);
		// this.pasteAction.setText("���");
		this.register(this.pasteAction);

		this.deleteAction = ActionFactory.DELETE.create(window);
		// this.deleteAction.setText("ɾ��");
		this.register(this.deleteAction);

		this.selectAllAction = ActionFactory.SELECT_ALL.create(window);
		// this.selectAllAction.setText("ȫѡ");
		this.register(this.selectAllAction);

		this.searchAction = ActionFactory.FIND.create(window);
		// this.searchAction.setText("����/�滻");
		this.register(this.searchAction);

		this.helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		// this.helpContentsAction.setText("��������");
		this.register(this.helpContentsAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		// this.aboutAction.setText("����");
		register(aboutAction);

		this.resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		// this.resetPerspectiveAction.setText("�ָ�͸��ͼ����");
		this.register(this.resetPerspectiveAction);

		this.preferencesAction = ActionFactory.PREFERENCES.create(window);
		// this.preferencesAction.setText("��ѡ��");
		this.register(this.preferencesAction);
		removeDeuplicatedAction();
	}

	/**
	 * �����˵���������Ӳ˵���
	 */
	protected void fillMenuBar(IMenuManager menuBar)
	{
		// ����ļ��˵�
		MenuManager fileMenu = new MenuManager("�ļ�(&F)", IWorkbenchActionConstants.M_FILE);
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

		// �༭�˵�
		MenuManager editMenu = new MenuManager("�༭(&E)", IWorkbenchActionConstants.M_EDIT);
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

		// ���ڲ˵�
		MenuManager windowMenu = new MenuManager("����(&W)", IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);
		windowMenu.add(new Separator());
		windowMenu.add(this.resetPerspectiveAction);
		windowMenu.add(this.preferencesAction);

		// ����˵�
		MenuManager helpMenu = new MenuManager("����(&H)", IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);

		helpMenu.add(this.helpContentsAction);
		helpMenu.add(this.aboutAction);
	}
	
	/**
	 * ����������������ӹ��߰�ť
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
