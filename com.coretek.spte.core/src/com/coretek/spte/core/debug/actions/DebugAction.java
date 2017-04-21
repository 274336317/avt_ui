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
 * @Description TODO(开始执行单步调试Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 下午03:58:57
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
		// TODO (debug调试模式开始调试按钮)
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/101_caseStart.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/101_caseStart.gif"));
		this.setToolTipText("开始执行单步调试"); // 调试测试用例

		this.debugActionGrp = debugActionGrp;
	}

	@Override
	public void run()
	{
		this.debugActionGrp.setBtnEnable(DebugCmd.Begin);
		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.Begin);
		// 将所有非SPTEEditor打开的测试用例全部关闭掉
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
