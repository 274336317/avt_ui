package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.util.ImageManager;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * __________________________________________________________________________________
 * @Class StepReturnAction.java
 * @Description TODO(跳出单步调试Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 下午04:40:05
 * __________________________________________________________________________________
 */
public class StepReturnAction extends Action
{

	public StepReturnAction(DebugActionsGroup debugActionGrp)
	{
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepOut.gif"));
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepOutDisabled.gif"));
		//this.setToolTipText(Messages.getString("I18N_STEPOUT"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/115_stepReturn.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/115_stepReturn.gif"));
		this.setToolTipText("跳出单步调试");
		this.setEnabled(false);
	}

	@Override
	public void run()
	{
		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.StepReturn);
	}

	public void removeAllListeners()
	{
		super.clearListeners();
	}

	public Object[] getAllListeners()
	{
		return super.getListeners();
	}
}