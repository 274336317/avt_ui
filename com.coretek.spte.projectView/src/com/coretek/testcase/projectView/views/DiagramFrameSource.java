package com.coretek.testcase.projectView.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.views.framelist.TreeFrame;
import org.eclipse.ui.views.framelist.TreeViewerFrameSource;

import com.coretek.testcase.projectView.action.Messages;

public class DiagramFrameSource extends TreeViewerFrameSource
{
	private ProjectView	diagramView;

	/**
	 * Constructs a new frame source for the specified resource diagramView.
	 * 
	 * @param diagramView the resource diagramView
	 */
	public DiagramFrameSource(ProjectView diagramView)
	{
		super(diagramView.getTreeViewer());
		this.diagramView = diagramView;
	}

	/**
	 * Returns a new frame. This implementation extends the super implementation
	 * by setting the frame's tool tip text to show the full path for the input
	 * element.
	 */
	protected TreeFrame createFrame(Object input)
	{
		TreeFrame frame = super.createFrame(input);
		frame.setName(diagramView.getFrameName(input));
		frame.setToolTipText(diagramView.getFrameToolTipText(input));
		return frame;
	}

	/**
	 * Also updates the diagramView's title.
	 */
	protected void frameChanged(TreeFrame frame)
	{
		IResource resource = (IResource) frame.getInput();
		IProject project = resource.getProject();

		if (project != null && project.isOpen() == false)
		{
			MessageDialog.openInformation(diagramView.getViewSite().getShell(), Messages.getString("DiagramFrameSource_closedProject_title"), NLS.bind(Messages.getString("DiagramFrameSource_closedProject_message"), project.getName()));
			diagramView.getFrameList().back();
		} else
		{
			super.frameChanged(frame);
			diagramView.updateTitle();
		}
	}
}
