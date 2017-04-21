package com.coretek.testcase.projectView.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import com.coretek.testcase.projectView.views.ProjectView;
import com.coretek.tools.ide.internal.ui.UIPluginImages;
import com.coretek.tools.ide.internal.ui.IHelpContextIds;

public class CollapseAllAction extends Action
{

	private ProjectView	view;

	public CollapseAllAction(ProjectView view)
	{
		super(Messages.getString("CollapseAllAction.label")); //$NON-NLS-1$
		this.view = view;

		setDescription(Messages.getString("CollapseAllAction.description")); //$NON-NLS-1$
		setToolTipText(Messages.getString("CollapseAllAction.tooltip")); //$NON-NLS-1$
		UIPluginImages.setImageDescriptors(this, UIPluginImages.T_LCL, UIPluginImages.IMG_MENU_COLLAPSE_ALL);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.COLLAPSE_ALL_ACTION);
	}

	public void run()
	{
		view.collapseAll();
	}
}
