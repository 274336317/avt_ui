/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.coretek.testcase.projectView.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import com.coretek.tools.ide.internal.ui.UIPluginImages;
import com.coretek.tools.ide.internal.ui.IHelpContextIds;

/**
 * This is an action template for actions that toggle whether it links its
 * selection to the active editor.
 * 
 * @since 3.0
 */
public abstract class AbstractToggleLinkingAction extends Action
{

	/**
	 * Constructs a new action.
	 */
	public AbstractToggleLinkingAction()
	{
		super(Messages.getString("ToggleLinkingAction.label")); //$NON-NLS-1$
		setDescription(Messages.getString("ToggleLinkingAction.description")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ToggleLinkingAction.tooltip")); //$NON-NLS-1$
		UIPluginImages.setImageDescriptors(this, UIPluginImages.T_LCL, "synced.gif"); //$NON-NLS-1$		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IHelpContextIds.LINK_EDITOR_ACTION);
	}

	/**
	 * Runs the action.
	 */
	public abstract void run();
}
