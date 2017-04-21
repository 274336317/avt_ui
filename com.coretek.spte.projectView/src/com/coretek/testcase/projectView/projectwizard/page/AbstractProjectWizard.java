package com.coretek.testcase.projectView.projectwizard.page;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.ide.IDEInternalPreferences;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.coretek.spte.core.util.Constants;

/**
 * 工程软件向导的基类
 * 
 * @author 孙大巍
 * @date 2010-11-26
 * 
 */
public abstract class AbstractProjectWizard extends BasicNewResourceWizard implements IExecutableExtension
{

	public static final String					WIZARD_ID				= "com.coretek.tools.ide.projectwizard.new.project";	//$NON-NLS-1$

	public static final String					ICD_DIR					= "icd";												//$NON-NLS-1$

	protected AbstractNewProjectCreationPage	mainPage;

	// cache of newly-created project
	protected IProject							newProject;

	/**
	 * The config element which declares this wizard.
	 */
	protected IConfigurationElement				configElement;

	/**
	 * Extension attribute name for final perspective.
	 */
	protected static final String				FINAL_PERSPECTIVE		= "finalPerspective";									//$NON-NLS-1$

	/**
	 * Extension attribute name for preferred perspectives.
	 */
	protected static final String				PREFERRED_PERSPECTIVES	= "preferredPerspectives";								//$NON-NLS-1$

	/**
	 * Creates a wizard for creating a new project resource in the workspace.
	 */
	public AbstractProjectWizard()
	{
		IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings section = workbenchSettings.getSection("BasicNewProjectResourceWizard");//$NON-NLS-1$
		if (section == null)
		{
			section = workbenchSettings.addNewSection("BasicNewProjectResourceWizard");//$NON-NLS-1$
		}
		setDialogSettings(section);
	}

	/**
	 * Creates a new project resource with the selected name.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on the pages currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this wizard caches the new project once it has been
	 * successfully created; subsequent invocations of this method will answer
	 * the same project resource without attempting to create it again.
	 * </p>
	 * 
	 * @return the created project resource, or <code>null</code> if the project
	 *         was not created
	 */
	protected IProject createNewProject()
	{
		if (newProject != null)
		{
			return newProject;
		}

		// get a project handle
		final IProject newProjectHandle = mainPage.getProjectHandle();

		// get a project descriptor
		URI location = null;
		if (!mainPage.useDefaults())
		{
			location = mainPage.getLocationURI();
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
		description.setLocationURI(location);

		// create the new project operation
		IRunnableWithProgress op = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException
			{
				CreateProjectOperation op = new CreateProjectOperation(description, WizardMessages.getString(Constants.I18N_SOFTWARE_TESTING_NEWPROJECT_TITLE));
				try
				{
					op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
				} catch (ExecutionException e)
				{
					throw new InvocationTargetException(e);
				}
			}
		};

		// run the new project creation operation
		try
		{
			getContainer().run(true, true, op);
		} catch (InterruptedException e)
		{
			return null;
		} catch (InvocationTargetException e)
		{
			Throwable t = e.getTargetException();
			if (t instanceof ExecutionException && t.getCause() instanceof CoreException)
			{
				CoreException cause = (CoreException) t.getCause();
				StatusAdapter status;
				if (cause.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS)
				{
					status = new StatusAdapter(StatusUtil.newStatus(IStatus.WARNING, NLS.bind(WizardMessages.getString("NewProject_caseVariantExistsError"), newProjectHandle.getName()), cause));
				} else
				{
					status = new StatusAdapter(StatusUtil.newStatus(cause.getStatus().getSeverity(), WizardMessages.getString("NewProject_errorMessage"), cause));
				}
				status.setProperty(StatusAdapter.TITLE_PROPERTY, WizardMessages.getString("NewProject_errorMessage"));
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
			} else
			{
				StatusAdapter status = new StatusAdapter(new Status(IStatus.WARNING, IDEWorkbenchPlugin.IDE_WORKBENCH, 0, NLS.bind(WizardMessages.getString("NewProject_internalError"), t.getMessage()), t));
				status.setProperty(StatusAdapter.TITLE_PROPERTY, WizardMessages.getString("NewProject_errorMessage"));
				StatusManager.getManager().handle(status, StatusManager.LOG | StatusManager.BLOCK);
			}
			return null;
		}

		newProject = newProjectHandle;
		return newProject;
	}

	/**
	 * Returns the newly created project.
	 * 
	 * @return the created project, or <code>null</code> if project not created
	 */
	public IProject getNewProject()
	{
		return newProject;
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		super.init(workbench, currentSelection);
		setNeedsProgressMonitor(true);
		setWindowTitle(WizardMessages.getString("NewProject_windowTitle"));
	}

	/*
	 * (non-Javadoc) Method declared on BasicNewResourceWizard.
	 */
	protected void initializeDefaultPageImageDescriptor()
	{
		ImageDescriptor desc = IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/newprj_wiz.png");//$NON-NLS-1$
		setDefaultPageImageDescriptor(desc);
	}

	/*
	 * (non-Javadoc) Opens a new window with a particular perspective and input.
	 */
	protected static void openInNewWindow(IPerspectiveDescriptor desc)
	{

		// Open the page.
		try
		{
			PlatformUI.getWorkbench().openWorkbenchWindow(desc.getId(), ResourcesPlugin.getWorkspace().getRoot());
		} catch (WorkbenchException e)
		{
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null)
			{
				ErrorDialog.openError(window.getShell(), WizardMessages.getString("NewProject_errorOpeningWindow"), e.getMessage(), e.getStatus());
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish()
	{
		if (createNewProject() == null)
		{

			return false;
		}

		IWorkingSet[] workingSets = mainPage.getSelectedWorkingSets();
		getWorkbench().getWorkingSetManager().addToWorkingSets(newProject, workingSets);

		updatePerspective();
		selectAndReveal(newProject);

		try
		{
			getNewProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e)
		{

			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc) Replaces the current perspective with the new one.
	 */
	protected static void replaceCurrentPerspective(IPerspectiveDescriptor persp)
	{

		// Get the active page.
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
		{
			return;
		}
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
		{
			return;
		}

		// Set the perspective.
		page.setPerspective(persp);
	}

	/**
	 * Stores the configuration element for the wizard. The config element will
	 * be used in <code>performFinish</code> to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
	{
		configElement = cfig;
	}

	/**
	 * Updates the perspective for the active page within the window.
	 */
	protected void updatePerspective()
	{
		updatePerspective(configElement);
	}

	/**
	 * Updates the perspective based on the current settings in the
	 * Workbench/Perspectives preference page.
	 * 
	 * Use the setting for the new perspective opening if we are set to open in
	 * a new perspective.
	 * <p>
	 * A new project wizard class will need to implement the
	 * <code>IExecutableExtension</code> interface so as to gain access to the
	 * wizard's <code>IConfigurationElement</code>. That is the configuration
	 * element to pass into this method.
	 * </p>
	 * 
	 * @param configElement - the element we are updating with
	 * 
	 * @see IPreferenceConstants#OPM_NEW_WINDOW
	 * @see IPreferenceConstants#OPM_ACTIVE_PAGE
	 * @see IWorkbenchPreferenceConstants#NO_NEW_PERSPECTIVE
	 */
	public static void updatePerspective(IConfigurationElement configElement)
	{
		// Do not change perspective if the configuration element is
		// not specified.
		if (configElement == null)
		{
			return;
		}

		// Retrieve the new project open perspective preference setting
		String perspSetting = PrefUtil.getAPIPreferenceStore().getString(IDE.Preferences.PROJECT_OPEN_NEW_PERSPECTIVE);

		String promptSetting = IDEWorkbenchPlugin.getDefault().getPreferenceStore().getString(IDEInternalPreferences.PROJECT_SWITCH_PERSP_MODE);

		// Return if do not switch perspective setting and are not prompting
		if (!(promptSetting.equals(MessageDialogWithToggle.PROMPT)) && perspSetting.equals(IWorkbenchPreferenceConstants.NO_NEW_PERSPECTIVE))
		{
			return;
		}

		// Read the requested perspective id to be opened.
		String finalPerspId = configElement.getAttribute(FINAL_PERSPECTIVE);
		if (finalPerspId == null)
		{
			return;
		}

		// Map perspective id to descriptor.
		IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();

		// leave this code in - the perspective of a given project may map to
		// activities other than those that the wizard itself maps to.
		IPerspectiveDescriptor finalPersp = reg.findPerspectiveWithId(finalPerspId);
		if (finalPersp != null && finalPersp instanceof IPluginContribution)
		{
			IPluginContribution contribution = (IPluginContribution) finalPersp;
			if (contribution.getPluginId() != null)
			{
				IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI.getWorkbench().getActivitySupport();
				IActivityManager activityManager = workbenchActivitySupport.getActivityManager();
				IIdentifier identifier = activityManager.getIdentifier(WorkbenchActivityHelper.createUnifiedId(contribution));
				Set<?> idActivities = identifier.getActivityIds();

				if (!idActivities.isEmpty())
				{
					Set enabledIds = new HashSet(activityManager.getEnabledActivityIds());

					if (enabledIds.addAll(idActivities))
					{
						workbenchActivitySupport.setEnabledActivityIds(enabledIds);
					}
				}
			}
		} else
		{
			IDEWorkbenchPlugin.log("Unable to find persective " //$NON-NLS-1$
					+ finalPerspId + " in BasicNewProjectResourceWizard.updatePerspective"); //$NON-NLS-1$
			return;
		}

		// gather the preferred perspectives
		// always consider the final perspective (and those derived from it)
		// to be preferred
		ArrayList preferredPerspIds = new ArrayList();
		addPerspectiveAndDescendants(preferredPerspIds, finalPerspId);
		String preferred = configElement.getAttribute(PREFERRED_PERSPECTIVES);
		if (preferred != null)
		{
			StringTokenizer tok = new StringTokenizer(preferred, " \t\n\r\f,"); //$NON-NLS-1$
			while (tok.hasMoreTokens())
			{
				addPerspectiveAndDescendants(preferredPerspIds, tok.nextToken());
			}
		}

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null)
		{
			IWorkbenchPage page = window.getActivePage();
			if (page != null)
			{
				IPerspectiveDescriptor currentPersp = page.getPerspective();

				// don't switch if the current perspective is a preferred
				// perspective
				if (currentPersp != null && preferredPerspIds.contains(currentPersp.getId()))
				{
					return;
				}
			}

			// prompt the user to switch
			if (!confirmPerspectiveSwitch(window, finalPersp))
			{
				return;
			}
		}

		int workbenchPerspectiveSetting = WorkbenchPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.OPEN_PERSP_MODE);

		// open perspective in new window setting
		if (workbenchPerspectiveSetting == IPreferenceConstants.OPM_NEW_WINDOW)
		{
			openInNewWindow(finalPersp);
			return;
		}

		// replace active perspective setting otherwise
		replaceCurrentPerspective(finalPersp);
	}

	/**
	 * Adds to the list all perspective IDs in the Workbench who's original ID
	 * matches the given ID.
	 * 
	 * @param perspectiveIds the list of perspective IDs to supplement.
	 * @param id the id to query.
	 * @since 3.0
	 */
	protected static void addPerspectiveAndDescendants(List perspectiveIds, String id)
	{
		IPerspectiveRegistry registry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		IPerspectiveDescriptor[] perspectives = registry.getPerspectives();
		for (int i = 0; i < perspectives.length; i++)
		{
			// @issue illegal ref to workbench internal class;
			// consider adding getOriginalId() as API on IPerspectiveDescriptor
			PerspectiveDescriptor descriptor = ((PerspectiveDescriptor) perspectives[i]);
			if (descriptor.getOriginalId().equals(id))
			{
				perspectiveIds.add(descriptor.getId());
			}
		}
	}

	/**
	 * Prompts the user for whether to switch perspectives.
	 * 
	 * @param window The workbench window in which to switch perspectives; must
	 *            not be <code>null</code>
	 * @param finalPersp The perspective to switch to; must not be
	 *            <code>null</code>.
	 * 
	 * @return <code>true</code> if it's OK to switch, <code>false</code>
	 *         otherwise
	 */
	protected static boolean confirmPerspectiveSwitch(IWorkbenchWindow window, IPerspectiveDescriptor finalPersp)
	{
		IPreferenceStore store = IDEWorkbenchPlugin.getDefault().getPreferenceStore();
		String pspm = store.getString(IDEInternalPreferences.PROJECT_SWITCH_PERSP_MODE);
		if (!IDEInternalPreferences.PSPM_PROMPT.equals(pspm))
		{
			// Return whether or not we should always switch
			return IDEInternalPreferences.PSPM_ALWAYS.equals(pspm);
		}
		String desc = finalPersp.getDescription();
		String message;
		if (desc == null || desc.length() == 0)
			message = NLS.bind(WizardMessages.getString("NewProject_perspSwitchMessage"), finalPersp.getLabel());
		else
			message = NLS.bind(WizardMessages.getString("NewProject_perspSwitchMessageWithDesc"), new String[] { finalPersp.getLabel(), desc });

		MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(window.getShell(), WizardMessages.getString("NewProject_perspSwitchTitle"), message, null /*
																																											 * use
																																											 * the
																																											 * default
																																											 * message
																																											 * for
																																											 * the
																																											 * toggle
																																											 */, false /*
																																														 * toggle
																																														 * is
																																														 * initially
																																														 * unchecked
																																														 */, store, IDEInternalPreferences.PROJECT_SWITCH_PERSP_MODE);
		int result = dialog.getReturnCode();

		// If we are not going to prompt anymore propogate the choice.
		if (dialog.getToggleState())
		{
			String preferenceValue;
			if (result == IDialogConstants.YES_ID)
			{
				// Doesn't matter if it is replace or new window
				// as we are going to use the open perspective setting
				preferenceValue = IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_REPLACE;
			} else
			{
				preferenceValue = IWorkbenchPreferenceConstants.NO_NEW_PERSPECTIVE;
			}

			// update PROJECT_OPEN_NEW_PERSPECTIVE to correspond
			PrefUtil.getAPIPreferenceStore().setValue(IDE.Preferences.PROJECT_OPEN_NEW_PERSPECTIVE, preferenceValue);
		}
		return result == IDialogConstants.YES_ID;
	}

}
