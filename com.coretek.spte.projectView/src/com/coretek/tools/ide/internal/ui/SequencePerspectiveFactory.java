package com.coretek.tools.ide.internal.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.coretek.spte.core.views.MessageView;
import com.coretek.testcase.projectView.views.ProjectView;
import com.coretek.testcase.testResult.TestResultViewPart;
import com.coretek.testcase.testcaseview.TestCaseViewPart;

/**
 * ����͸��ͼ�еĸ��齨�Ĳ���λ��
 * 
 * @author ���Ρ
 * @date 2010-12-4
 * 
 */
public class SequencePerspectiveFactory implements IPerspectiveFactory
{

	/**
	 * Constructs a new Default layout engine.
	 */
	public SequencePerspectiveFactory()
	{

	}

	/**
	 * @see IPerspectiveFactory#createInitialLayout
	 */
	public void createInitialLayout(IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();

		IFolderLayout rightFolder = layout.createFolder("topRight", IPageLayout.RIGHT, (float) 0.76, editorArea); //$NON-NLS-1$
		rightFolder.addView(MessageView.MESSAGE_VIEW_ID); // ��Ϣ��ͼ��ͼ
		//rightFolder.addView(IPageLayout.ID_OUTLINE);  // Outline��ͼ

		
		IFolderLayout bottomFolder = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, (float) 0.75, editorArea); //$NON-NLS-1$
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW); // Console��ͼ
		bottomFolder.addView(TestResultViewPart.ID); // ���Խ����ͼ  com.coretek.tools.testResult.view
		bottomFolder.addView(TestCaseViewPart.ID); // ���������б���ͼ   com.coretek.tools.sequence.testcaseList
		bottomFolder.addView("com.coretek.spte.monitor.monitorView"); //ʱ��ͼ�����ͼ SwquenceViewPart.ID
		//bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);  // ������ͼ

		
		IFolderLayout leftFolder = layout.createFolder("BOTTOM", IPageLayout.LEFT, 0.20f, editorArea);
		leftFolder.addView(ProjectView.ID); // ��Ŀ��ͼ  com.coretek.tools.ide.ui.DiagramView
		leftFolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - build console
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW); // console ��ͼ
		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE); // Outine��ͼ
		//layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);  // ������ͼ
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET); 
		
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.7f, layout.getEditorArea());
		
		addCWizardShortcuts(layout);
	}

	private void addCWizardShortcuts(IPageLayout layout)
	{
		layout.addNewWizardShortcut("com.coretek.tools.ide.projectWizard.testingWizard");
		layout.addNewWizardShortcut("com.coretek.testcase.projectView.TestSuiteNewWizard");
		layout.addNewWizardShortcut("com.coretek.testcase.projectView.TestCaseNewWizard");
		layout.addNewWizardShortcut("com.coretek.testcase.projectViewi.newICDProject");
		layout.addNewWizardShortcut("com.coretek.testcase.projectView.NewFolder");
	}
}
