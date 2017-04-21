package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.Action;

import com.coretek.spte.core.util.ImageManager;

/**
 * 终止背景周期消息
 * 
 * @author duys 2011-04-11
 * 
 */
public class TerminateCycleAction extends Action
{
	private DebugActionsGroup	debugActionGrp;

	public TerminateCycleAction(DebugActionsGroup debugActionGrp)
	{
		//this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/terminate_cycle.gif"));
		//this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/terminate_cycle_disabled.gif"));
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/debug/116_stopBGMsg.gif"));
		this.setDisabledImageDescriptor(ImageManager.getImageDescriptor("icons/debug/116_stopBGMsg.gif"));
		this.setToolTipText("终止背景周期消息");
		this.setEnabled(false);

		this.debugActionGrp = debugActionGrp;
	}

	public void run()
	{

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
