package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;

import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.ImageManager;

/**
 * __________________________________________________________________________________
 * @Class StepOverAction.java
 * @Description TODO(单步调试Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 下午04:33:30
 * __________________________________________________________________________________
 */
public class StepOverAction extends Action
{
	private DebugActionsGroup	debugActionGrp;

	public StepOverAction(DebugActionsGroup debugActionGrp)
	{
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepOver.gif"));
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/StepOverDisabled.gif"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/114_stepOver.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/114_stepOver.gif"));
		this.setToolTipText("单步调试");
		this.setEnabled(false);

		this.debugActionGrp = debugActionGrp;
	}

	@Override
	public void run()
	{
		this.debugActionGrp.setBtnEnable(DebugCmd.StepOver);

		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.StepOver);
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
