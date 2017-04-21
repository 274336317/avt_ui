package com.coretek.testcase.projectView.projectwizard.page;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

import com.coretek.spte.core.util.Constants;
import com.coretek.testcase.projectView.utils.ProjectUtils;

/**
 * ������Թ�����ҳ
 * 
 * @author Lifs
 * @date 2010.09.02
 */
public class TestingProjectWizard extends AbstractProjectWizard
{
	public TestingProjectWizard()
	{
	}

	@Override
	public void addPages()
	{
		super.addPages();
		mainPage = new TestingProjectCreationPage("basicNewProjectPage");
		mainPage.setTitle(WizardMessages.getString(Constants.I18N_SOFTWARE_TESTING_NEWPROJECT_TITLE));
		mainPage.setDescription(WizardMessages.getString(Constants.I18N_SOFTWARE_TESTING_NEWPROJECT_DESCRIPTION));
		this.addPage(mainPage);
	}

	@Override
	protected IProject createNewProject()
	{
		// ����.projectInfo�ļ��������̵���Ϣд���ȥ
		this.newProject = super.createNewProject();
		// IFile file = this.newProject.getFile(".projectInfo");
		// if (!file.isAccessible()) {
		// InputStream input = new
		// ByteArrayInputStream(ProjectInfo.softwareTestingProject.toProperties().getBytes());
		// try {
		// file.create(input, true, null);
		// } catch (CoreException e) {
		// e.printStackTrace();
		// } finally {
		// if (input != null) {
		// try {
		// input.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		// ���testProjectNature
		try
		{
			IProjectDescription description = this.newProject.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = "com.coretek.spte.projectView.testProjectNature";
			description.setNatureIds(newNatures);
			this.newProject.setDescription(description, null);

		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		ProjectUtils.refreshView();
		return this.newProject;
	}
}