package com.coretek.spte.core.editor.actions.toolbar;

import org.eclipse.jface.action.Action;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.util.ImageManager;

/**
 * 运行测试用例
 * 
 * @author 孙大巍
 * 
 *         2011-3-28
 */
public class RunningAction extends Action
{

	public RunningAction()
	{
		this.setImageDescriptor(ImageManager.getImageDescriptor("icons/running.gif"));
		this.setToolTipText(Messages.getString("I18N_EXCUTE_CASE"));
	}

	@Override
	public void run()
	{
		// TODO
	}
}
