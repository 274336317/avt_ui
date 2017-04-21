package com.coretek.testcase.projectView.action;

import com.coretek.testcase.projectView.views.ProjectView;
import com.coretek.tools.ide.internal.ui.UIPluginImages;

public class ToggleLinkingAction extends AbstractToggleLinkingAction
{

	private ProjectView	view;

	/**
	 * Constructs a new action.
	 */
	public ToggleLinkingAction(ProjectView view)
	{
		this.view = view;
		this.setToolTipText(Messages.getString("ToggleLinkingAction.toolTip"));
		UIPluginImages.setImageDescriptors(this, UIPluginImages.T_LCL, UIPluginImages.IMG_MENU_TOGGLE_LINK);

		setChecked(view.isLinkingEnabled());
	}

	/**
	 * Runs the action.
	 */
	public void run()
	{
		view.setLinkingEnabled(isChecked());
	}

}