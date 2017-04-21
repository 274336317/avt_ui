package com.coretek.spte.core.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.actions.ActionFactory;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.commands.AddingTestNodeCmd;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.parts.TestNodePart;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 上下文弹出菜单管理器
 * 
 * @author 孙大巍
 * @date 2010-9-3
 */
public class EditorContextMenuProvider extends ContextMenuProvider
{

	private SPTEEditor editor;

	private ActionRegistry actionRegistry;

	private MenuManager manager;

	private MenuManager backgroundManager;

	private MenuManager addingTestToolNodes;

	public ActionRegistry getActionRegistry()
	{
		return actionRegistry;
	}

	public void setActionRegistry(ActionRegistry actionRegistry)
	{
		this.actionRegistry = actionRegistry;
	}

	public EditorContextMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry, SPTEEditor editor)
	{
		super(viewer);
		this.actionRegistry = actionRegistry;
		this.editor = editor;

	}

	@Override
	public void buildContextMenu(IMenuManager menu)
	{
		IAction action;
		GEFActionConstants.addStandardActionGroups(menu);
		// 撤销
		action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		// 重做
		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		// 剪贴
		action = getActionRegistry().getAction(ActionFactory.CUT.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 复制
		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		action.setEnabled(action.isEnabled());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 黏贴
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		action.setEnabled(action.isEnabled());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 删除
		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 删除所有消息
		action = getActionRegistry().getAction(DelAllAtn.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 添加引用消息
		action = getActionRegistry().getAction(AddRefMsgsAtn.ID);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		// 显示、隐藏背景消息
		if (this.backgroundManager == null)
		{
			this.backgroundManager = new MenuManager(Messages.getString("I18N_BACKGROUND_MSG"));
			IContributionItem item = new BackgroundItem();// 显示、隐藏背景消息
			backgroundManager.add(item);
		}
		else
		{
			IContributionItem[] items = this.backgroundManager.getItems();
			for (IContributionItem item : items)
			{
				BackgroundItem si = (BackgroundItem) item;
				for (MenuItem mi : si.getItems())
				{
					if (editor.getStatus() == TestCaseStatus.Editing)
					{
						mi.setEnabled(true);
					}
					else
					{
						mi.setEnabled(false);
					}
				}
			}
		}
		menu.add(this.backgroundManager);
		if (this.manager == null)
		{
			manager = new MenuManager(Messages.getString("I18N_EDITOR_STATUS"));
			IContributionItem item = new StatusItem();// 编辑器状态
			manager.add(item);
		}
		else
		{
			IContributionItem[] items = this.manager.getItems();
			for (IContributionItem item : items)
			{
				StatusItem si = (StatusItem) item;
				for (MenuItem mi : si.getItems())
				{
					if (mi.getText().equals(Messages.getString("I18N_EDIT_MSG")))
					{
						if (editor.getStatus() == TestCaseStatus.Editing || editor.getStatus() == TestCaseStatus.ViewResult)
						{
							mi.setEnabled(true);
						}
						else
						{
							mi.setEnabled(false);
						}
						if (editor.getStatus() == TestCaseStatus.Editing)
						{
							mi.setSelection(true);
							si.getItems().get(1).setSelection(false);
						}
					}
					else
					{
						if (editor.getStatus() == TestCaseStatus.Editing || editor.getStatus() == TestCaseStatus.ViewResult)
						{
							mi.setEnabled(true);
						}
						else
						{
							mi.setEnabled(false);
						}
						if (editor.getStatus() == TestCaseStatus.ViewResult)
						{
							mi.setSelection(true);
							si.getItems().get(0).setSelection(false);
						}
					}
				}
			}
		}
		menu.add(manager);
		if (this.addingTestToolNodes == null)
		{
			this.addingTestToolNodes = new MenuManager("添加节点");
			IContributionItem item = new AddingTestToolNodesItem();
			this.addingTestToolNodes.add(item);
		}
		menu.add(this.addingTestToolNodes);
	}

	private class TestNodeSelection implements SelectionListener
	{

		private int num;

		public TestNodeSelection(int num)
		{
			this.num = num;
		}

		public void widgetDefaultSelected(SelectionEvent e)
		{
		}

		@SuppressWarnings("unchecked")
		public void widgetSelected(SelectionEvent e)
		{
			TestNodeMdl node = null;
			List list = EditorContextMenuProvider.this.getViewer().getSelectedEditParts();
			if (list != null && list.size() > 0)
			{
				if (list.get(0) instanceof TestNodePart)
					node = (TestNodeMdl) ((TestNodePart) list.get(0)).getModel();
			}
			Command command = new AddingTestNodeCmd(num, editor.getRootContainerMdl(), node);
			editor.getGraphicalViewer().getEditDomain().getCommandStack().execute(command);
		}

	}

	/**
	 * 添加测试节点
	 * 
	 * @author 孙大巍
	 *         2011-4-6
	 */
	private class AddingTestToolNodesItem extends ContributionItem
	{

		@Override
		public void fill(Menu menu, int index)
		{
			super.fill(menu, index);
			MenuItem item = new MenuItem(menu, SWT.NONE, index++);
			item.setText("1个");
			item.addSelectionListener(new TestNodeSelection(1));

			item = new MenuItem(menu, SWT.NONE, index++);
			item.setText("3个");
			item.addSelectionListener(new TestNodeSelection(3));

			item = new MenuItem(menu, SWT.NONE, index++);
			item.setText("5个");
			item.addSelectionListener(new TestNodeSelection(5));
			item = new MenuItem(menu, SWT.NONE, index++);
			item.setText("10个");
			item.addSelectionListener(new TestNodeSelection(10));
		}
	}

	private class BackgroundItem extends ContributionItem
	{

		private List<MenuItem> items = new ArrayList<MenuItem>(2);

		public List<MenuItem> getItems()
		{
			return items;
		}

		@Override
		public void fill(Menu menu, int index)
		{
			super.fill(menu, index);
			MenuItem item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_HIDE_BACKGROUND_MSG"));// 隐藏背景消息
			item.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{

				}

				public void widgetSelected(SelectionEvent e)
				{
					EditorContextMenuProvider.this.editor.setHidingBackgroundMsgs(Boolean.FALSE);
				}
			});

			item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_SHOW_BACKGROUND_MSG"));// 显示背景消息
			item.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{

				}

				public void widgetSelected(SelectionEvent e)
				{
					EditorContextMenuProvider.this.editor.setHidingBackgroundMsgs(Boolean.TRUE);
				}
			});
			item.setSelection(true);
			Separator t = new Separator();
			t.fill(menu, index++);

			item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_EXECUTE"));// 执行
			if (editor.getStatus() == TestCaseStatus.Editing)
			{
				item.setSelection(true);
			}
			else
			{
				item.setSelection(false);
			}
			if (editor.getStatus() == TestCaseStatus.Debug)
			{
				item.setEnabled(false);
			}
			else
			{
				item.setEnabled(true);
			}

			items.add(item);

			item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_NOT_EXECUTE"));// 不执行
			if (editor.getStatus() == TestCaseStatus.Debug)
			{
				item.setEnabled(false);
			}
			else
			{
				item.setEnabled(true);
			}
			items.add(item);
		}
	}

	private class StatusItem extends ContributionItem
	{

		private List<MenuItem> items = new ArrayList<MenuItem>(2);

		public List<MenuItem> getItems()
		{
			return items;
		}

		@Override
		public void fill(Menu menu, int index)
		{
			super.fill(menu, index);
			MenuItem item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_EDIT_MSG"));
			if (editor.getStatus() == TestCaseStatus.Editing)
			{
				item.setSelection(true);
			}
			else
			{
				item.setSelection(false);
			}
			if (editor.getStatus() == TestCaseStatus.Debug)
			{
				item.setEnabled(false);
			}
			else
			{
				item.setEnabled(true);
			}
			item.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{
				}

				public void widgetSelected(SelectionEvent e)
				{
					MenuItem item = (MenuItem) e.getSource();
					if (item.getSelection())
					{
						editor.getRootContainerMdl().setStatus(TestCaseStatus.Editing);
					}
					else
					{
						editor.getRootContainerMdl().setStatus(TestCaseStatus.ViewResult);
					}
				}
			});
			items.add(item);

			item = new MenuItem(menu, SWT.RADIO, index++);
			item.setText(Messages.getString("I18N_VIEW_EXCUTED_RESULT"));
			item.addSelectionListener(new SelectionListener()
			{

				public void widgetDefaultSelected(SelectionEvent e)
				{
				}

				public void widgetSelected(SelectionEvent e)
				{
					MenuItem item = (MenuItem) e.getSource();
					if (item.getSelection())
					{
						editor.getRootContainerMdl().setStatus(TestCaseStatus.ViewResult);
					}
					else
					{
						editor.getRootContainerMdl().setStatus(TestCaseStatus.Editing);
					}
				}
			});
			items.add(item);
			if (editor.getStatus() == TestCaseStatus.Editing)
			{
				item.setEnabled(true);
			}
			else
			{
				item.setEnabled(false);
			}
			if (editor.getStatus() == TestCaseStatus.Debug || editor.getStatus() == TestCaseStatus.ViewResult)
			{
				item.setSelection(true);
			}
			else
			{
				item.setSelection(false);
			}
		}
	}
}
