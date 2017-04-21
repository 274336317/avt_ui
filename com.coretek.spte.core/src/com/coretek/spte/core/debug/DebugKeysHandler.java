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
 * ����F6��F5��F7��F8��F11��
 * 
 * @author ���Ρ
 * 
 *         1/ corrector : Sim.Wang @ 2011-07-19 (���̰���Ҫ�ڻ��水ť��Ч������²���Ч)
 */
public class DebugKeysHandler extends GraphicalViewerKeyHandler
{

	private RootContainerMdl	rootContainer;

	private SPTEEditor			currentEditor;	// ���ڱ�debug��editor

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
			{// ��F5ת��ΪStepInto����
				if (this.isDebugStatus() && actionsGroup.getStepIntoAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepInto.getCmd());
					actionsGroup.getStepIntoAction().run();
				}
				break;
			}
			case SWT.F6:
			{// ��F6ת��ΪStepOver����
				if (this.isDebugStatus() && actionsGroup.getStepOverAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepOver.getCmd());
					actionsGroup.getStepOverAction().run();
				}
				break;
			}
			case SWT.F7:
			{// ��F7ת��ΪStepReturn
				if (this.isDebugStatus() && actionsGroup.getStepReturnAction().isEnabled())
				{
					Utils.addMsgToStatusLine(DebugCmd.StepOver.getCmd());
					actionsGroup.getStepReturnAction().run();
				}
				break;
			}
			case SWT.F8:
			{// ����Terminate����
				if (this.isDebugStatus() && actionsGroup.getTerminateAction().isEnabled())
				{
					Utils.addWarningToStatusLine(Messages.getString("I18N_DEBUG_MODEL_END"));
					actionsGroup.getTerminateAction().run();
				}
				break;
			}
			case SWT.F11:
			{// �������Թ���
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
