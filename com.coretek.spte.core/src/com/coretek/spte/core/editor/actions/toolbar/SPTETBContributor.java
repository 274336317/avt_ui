package com.coretek.spte.core.editor.actions.toolbar;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import com.coretek.spte.core.debug.DebugCmdHanlder;
import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.core.editor.SPTEEditor;

/**
 * 将与editor相关的菜单贡献到workbench菜单栏中
 * 
 * @author 孙大巍
 * @date 2010-8-30
 * 
 */
public class SPTETBContributor extends ActionBarContributor
{

	private DebugActionsGroup	actionsGroup;

	// private RunningAction runningAction;

	private SPTEEditor			editor;

	@Override
	public void contributeToCoolBar(ICoolBarManager coolBarManager)
	{
		super.contributeToCoolBar(coolBarManager);
	}

	@Override
	public void setActiveEditor(IEditorPart editor)
	{
		super.setActiveEditor(editor);
		this.editor = (SPTEEditor) editor;
		this.actionsGroup.setEditor(this.editor);
		Object[] objs = this.actionsGroup.getDebugAction().getAllListeners();
		for (Object obj : objs)
		{
			if (obj instanceof DebugCmdHanlder)
			{
				this.actionsGroup.getDebugAction().removePropertyChangeListener((DebugCmdHanlder) obj);
			}
		}
		this.actionsGroup.getDebugAction().addPropertyChangeListener(this.editor.getDebugResposne());
	}

	@Override
	protected void buildActions()
	{
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		IWorkbenchWindow window = getPage().getWorkbenchWindow();
		this.addRetargetAction((RetargetAction) ActionFactory.COPY.create(window));
		this.addRetargetAction((RetargetAction) ActionFactory.PASTE.create(window));
		this.addRetargetAction((RetargetAction) ActionFactory.CUT.create(window));
		this.addAction(ActionFactory.BACKWARD_HISTORY.create(window));
		this.addAction(ActionFactory.FORWARD_HISTORY.create(window));

		// this.runningAction = new RunningAction();
		this.actionsGroup = new DebugActionsGroup();
	}

	protected void declareGlobalActionKeys()
	{
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager)
	{
		super.contributeToMenu(menuManager);
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager)
	{
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		IWorkbenchAction saveAction = ActionFactory.SAVE.create(PlatformUI.getWorkbench().getWorkbenchWindows()[0]);
		toolBarManager.add(saveAction);
		toolBarManager.add(new Separator("zoom"));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		toolBarManager.add(new Separator("debug"));
		this.actionsGroup.contributeToToolBar(toolBarManager);

		toolBarManager.add(new Separator("running"));
		// toolBarManager.add(this.runningAction);
		toolBarManager.add(new Separator("history"));
		toolBarManager.add(getAction(ActionFactory.BACKWARD_HISTORY.getId()));
		toolBarManager.add(getAction(ActionFactory.FORWARD_HISTORY.getId()));
	}

	public void fireDebugCmd()
	{
		this.actionsGroup.getDebugAction().run();
	}

	public void fireTerminateDebugCmd()
	{
		this.actionsGroup.fireTerminateDebugCmd();
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	public DebugActionsGroup getActionsGroup()
	{
		return actionsGroup;
	}
}