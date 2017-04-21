package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;

import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.ImageManager;

/**
 * __________________________________________________________________________________
 * @Class TerminateAction.java
 * @Description (全部执行Action)
 *
 * @Auther MENDY
 * @Date 2016-5-6 下午04:06:44
 * __________________________________________________________________________________
 */
public class TerminateAction extends Action
{
	private DebugActionsGroup	debugActionGrp;

	public TerminateAction(DebugActionsGroup debugActionGrp)
	{
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/Terminate.gif"));
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/TerminateDisabled.gif"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/102_caseStop.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/102_caseStop.gif"));
		this.setToolTipText("全部执行");
		this.setEnabled(false);

		this.debugActionGrp = debugActionGrp;
	}

	@Override
	public void run()
	{
		this.debugActionGrp.setBtnEnable(DebugCmd.Terminate);

		this.firePropertyChange(SPTEConstants.EVENT_DEBUG_CMD, null, DebugCmd.Terminate);
	}

	public void removeAllListeners()
	{
		super.clearListeners();
	}

	public Object[] getAllListeners()
	{
		return super.getListeners();
	}

	public boolean contains(Object obj)
	{
		for (Object listener : this.getAllListeners())
		{
			if (listener == obj)
			{
				return true;
			}
		}
		return false;
	}
}
