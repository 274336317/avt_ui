package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.editors.text.TextEditor;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.util.ImageManager;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * __________________________________________________________________________________
 * @Class DebugAction.java
 * @Description TODO(��ʼִ�е�������Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 ����03:58:57
 * __________________________________________________________________________________
 */
public class DebugAction extends Action
{
	private DebugActionsGroup	debugActionGrp;

	public DebugAction(DebugActionsGroup debugActionGrp)
	{
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/Resume.gif"));
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/ResumeDisabled.gif"));
		//this.setToolTipText(Messages.getString("I18N_DEBUG_TESTCASE"));
		// TODO (debug����ģʽ��ʼ���԰�ť)
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/101_caseStart.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/101_caseStart.gif"));
		this.setToolTipText("��ʼִ�е�������"); // ���Բ�������

		this.debugActionGrp = debugActionGrp;
	}

	@Override
	public void run()
	{
		this.debugActionGrp.setBtnEnable(DebugCmd.Begin);
		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.Begin);
		// �����з�SPTEEditor�򿪵Ĳ�������ȫ���رյ�
		IEditorReference[] ers = EclipseUtils.getAllEditors();
		for (IEditorReference er : ers)
		{
			IEditorPart editorPart = er.getEditor(true);
			if (editorPart instanceof TextEditor)
			{
				TextEditor te = (TextEditor) editorPart;
				te.close(true);
			}
		}
	}

	public void removeAllListeners()
	{
		super.clearListeners();
	}

	public Object[] getAllListeners()
	{
		return this.getListeners();
	}
}
