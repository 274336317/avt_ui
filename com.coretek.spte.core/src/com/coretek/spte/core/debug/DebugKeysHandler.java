package com.coretek.spte.core.debug;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.editor.actions.toolbar.SPTETBContributor;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.util.TestCaseStatus;
import com.coretek.spte.core.util.Utils;

/**
 * 监听F6、F5、F7、F8、F11键
 * 
 * @author 孙大巍
 * 
 *         1/ corrector : Sim.Wang @ 2011-07-19 (键盘按键要在画面按钮有效的情况下才有效)
 */
public class DebugKeysHandler extends GraphicalViewerKeyHandler
{

	private RootContainerMdl	rootContainer;

	private SPTEEditor			currentEditor;	// 正在被debug的editor

	private DebugActionsGroup	actionsGroup;

	public DebugKeysHandler(GraphicalViewer viewer, RootContainerMdl rootContainer, SPTEEditor currentEditor)
	{
		super(viewer);
		this.rootContainer = rootContainer;
		this.currentEditor = currentEditor;
		this.actionsGroup = (DebugActionsGroup) ((SPTETBContributor) this.currentEditor.getEditorSite().getActionBarContributor()).getActionsGroup();
	}

	public static DebugKeysHandler newInstance(GraphicalViewer viewer, RootContainerMdl rootContainer, SPTEEditor currentEditor)
	{
		return new DebugKeysHandler(viewer, rootContainer, currentEditor);
	}

	@Override
	public boolean keyPressed(KeyEvent event)
	{
		switch (event.keyCode)
		{
			case SWT.F5:
			{// 将F5转换为StepInto命令
				if (this.isDebugStatus() && actionsGroup.getStepIntoAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepInto.getCmd());
					actionsGroup.getStepIntoAction().run();
				}
				break;
			}
			case SWT.F6:
			{// 将F6转换为StepOver命令
				if (this.isDebugStatus() && actionsGroup.getStepOverAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepOver.getCmd());
					actionsGroup.getStepOverAction().run();
				}
				break;
			}
			case SWT.F7:
			{// 将F7转换为StepReturn
				if (this.isDebugStatus() && actionsGroup.getStepReturnAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepOver.getCmd());
					actionsGroup.getStepReturnAction().run();
				}
				break;
			}
			case SWT.F8:
			{// 发送Terminate命令
				if (this.isDebugStatus() && actionsGroup.getTerminateAction().isEnabled())
				{
					Utils.addWarningToStatusLine(Messages.getString("I18N_DEBUG_MODEL_END"));
					actionsGroup.getTerminateAction().run();
				}
				break;
			}
			case SWT.F11:
			{// 开启调试功能
				if (actionsGroup.getDebugAction().isEnabled())
				{
					this.rootContainer.setStatus(TestCaseStatus.Debug);
					Utils.addMsgToStatusLine(Messages.getString("I18N_DEBUG_MODEL_STARTED"));
					actionsGroup.getDebugAction().run();
				}
				break;
			}
			default:
			{
				if (this.rootContainer.getStatus() == TestCaseStatus.Debug)
					Utils.addErrorMsgToStatusLine(Messages.getString("I18N_WRONG_KEY_PRESSED"));
				return false;
			}
		}
		return true;
	}

	private boolean isDebugStatus()
	{

		return this.rootContainer.getStatus() == TestCaseStatus.Debug;
	}
}
