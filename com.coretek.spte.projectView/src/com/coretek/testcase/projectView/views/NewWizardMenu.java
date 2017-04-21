package com.coretek.testcase.projectView.views;

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.BaseNewWizardMenu;
import org.eclipse.ui.internal.actions.NewWizardShortcutAction;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.utils.ProjectUtils;

/**
 * A <code>NewWizardMenu</code> augments <code>BaseNewWizardMenu</code> with
 * IDE-specific actions: New Project... (always shown) and New Example... (shown
 * only if there are example wizards installed).
 */
public class NewWizardMenu extends BaseNewWizardMenu
{

	IStructuredSelection	selection;

	public IStructuredSelection getSelection()
	{
		return selection;
	}

	public void setSelection(IStructuredSelection selection)
	{
		this.selection = selection;
	}

	private boolean	enabled	= true;

	/**
	 * Creates a new wizard shortcut menu for the IDE.
	 * 
	 * @param window the window containing the menu
	 */
	public NewWizardMenu(IWorkbenchWindow window)
	{
		this(window, null);

	}

	/**
	 * Creates a new wizard shortcut menu for the IDE.
	 * 
	 * @param window the window containing the menu
	 * @param id the identifier for this contribution item
	 */
	public NewWizardMenu(IWorkbenchWindow window, String id)
	{
		super(window, id);
	}

	/**
	 * Create a new wizard shortcut menu.
	 * <p>
	 * If the menu will appear on a semi-permanent basis, for instance within a
	 * toolbar or menubar, the value passed for <code>register</code> should be
	 * true. If set, the menu will listen to perspective activation and update
	 * itself to suit. In this case clients are expected to call
	 * <code>deregister</code> when the menu is no longer needed. This will
	 * unhook any perspective listeners.
	 * </p>
	 * 
	 * @param innerMgr the location for the shortcut menu contents
	 * @param window the window containing the menu
	 * @param register if <code>true</code> the menu listens to perspective
	 *            changes in the window
	 * @deprecated use NewWizardMenu(IWorkbenchWindow) instead
	 */
	public NewWizardMenu(IMenuManager innerMgr, IWorkbenchWindow window, boolean register)
	{
		this(window, null);
		fillMenu(innerMgr);
		// Must be done after constructor to ensure field initialization.
	}

	/*
	 * (non-Javadoc) Fills the menu with New Wizards.
	 */
	private void fillMenu(IContributionManager innerMgr)
	{
		// Remove all.
		innerMgr.removeAll();

		IContributionItem[] items = getContributionItems();
		for (int i = 0; i < items.length; i++)
		{
			innerMgr.add(items[i]);
		}
	}

	/**
	 * Removes all listeners from the containing workbench window.
	 * <p>
	 * This method should only be called if the shortcut menu is created with
	 * <code>register = true</code>.
	 * </p>
	 * 
	 * @deprecated has no effect
	 */
	public void deregisterListeners()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.actions.BaseNewWizardMenu#addItems(org.eclipse.jface.action
	 * .IContributionManager)
	 */
	protected void addItems(List list)
	{
		List<Object> shortCuts = new ArrayList<Object>();
		addShortcuts(shortCuts);
		for (Iterator iterator = shortCuts.iterator(); iterator.hasNext();)
		{
			Object curr = iterator.next();
			if (curr instanceof ActionContributionItem && isNewProjectWizardAction(((ActionContributionItem) curr).getAction()))
			{
				iterator.remove();

				ActionContributionItem actionConItm = (ActionContributionItem) curr;
				NewWizardShortcutAction action = (NewWizardShortcutAction) actionConItm.getAction();
				WorkbenchWizardElement wizardElement = (WorkbenchWizardElement) action.getWizardDescriptor();
				IResource resource = (IResource) selection.getFirstElement();
				if ("com.coretek.testcase.projectViewi.newICDProject".equals(wizardElement.getId()))
				{
					// ICD工程
					list.add(curr);
				} else if ("com.coretek.tools.ide.projectWizard.testingWizard".equals(wizardElement.getId()))
				{
					// 测试工程
					list.add(curr);
				} else if ("com.coretek.testcase.projectView.TestSuiteNewWizard".equals(wizardElement.getId()))
				{
					// 测试集
					if (resource instanceof IProject && Utils.isSoftwareTestingProject(resource.getProject()))
					{
						list.add(curr);
					} else if (selection.getFirstElement() instanceof IFolder)
					{
						String folderType = ProjectUtils.getFolderProperty((IFolder) selection.getFirstElement(), ProjectUtils.FODLDER_TYPE);
						if (folderType.equalsIgnoreCase(ProjectUtils.FODLDER_TYPE_TEST_SUITE))
						{
							list.add(curr);
						}
					}
				} else if ("com.coretek.testcase.projectView.TestCaseNewWizard".equals(wizardElement.getId()))
				{
					// 测试用例
					if (resource instanceof IFolder && Utils.isTestSuite((IFolder) resource))
					{
						String folderType = ProjectUtils.getFolderProperty((IFolder) selection.getFirstElement(), ProjectUtils.FODLDER_TYPE);
						if (folderType.equalsIgnoreCase(ProjectUtils.FODLDER_TYPE_TEST_SUITE))
						{
							list.add(curr);
						}
					}
				} else if ("com.coretek.testcase.projectView.NewFolder".equals(wizardElement.getId()))
				{
					list.add(curr);
				}
			}
		}
	}

	private boolean isNewProjectWizardAction(IAction action)
	{
		if (action instanceof NewWizardShortcutAction)
		{
			IWizardDescriptor wizardDescriptor = ((NewWizardShortcutAction) action).getWizardDescriptor();
			String[] tags = wizardDescriptor.getTags();
			for (int i = 0; i < tags.length; i++)
			{
				if (WorkbenchWizardElement.TAG_PROJECT.equals(tags[i]))
				{
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc) Method declared on IContributionItem.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Sets the enabled state of the receiver.
	 * 
	 * @param enabledValue if <code>true</code> the menu is enabled; else it is
	 *            disabled
	 */
	public void setEnabled(boolean enabledValue)
	{
		this.enabled = enabledValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.BaseNewWizardMenu#getContributionItems()
	 */
	protected IContributionItem[] getContributionItems()
	{
		if (isEnabled())
		{
			return super.getContributionItems();
		}
		return new IContributionItem[0];
	}
}