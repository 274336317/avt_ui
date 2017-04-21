package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;

import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.ImageManager;

/**
 * __________________________________________________________________________________
 * @Class StepIntoAction.java
 * @Description TODO(单步进入调试Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 下午04:17:01
 * __________________________________________________________________________________
 */
public class StepIntoAction extends Action
{

	private DebugActionsGroup	debugActionGrp;

	public StepIntoAction(DebugActionsGroup debugActionGrp)
	{
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepIntoDisabled.gif"));
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepInto.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/113_stepInto.gif"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/113_stepInto.gif"));
		this.setToolTipText("单步进入调试");
		this.setEnabled(false);

		this.debugActionGrp = debugActionGrp;
	}

	@Override
	public void run()
	{
		this.debugActionGrp.setBtnEnable(DebugCmd.StepInto);

		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.StepInto);
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
