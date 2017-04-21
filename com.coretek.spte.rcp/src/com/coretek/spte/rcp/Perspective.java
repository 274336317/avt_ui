package com.coretek.spte.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// layout.addView(MessageView.MESSAGE_VIEW_ID, SWT.RIGHT, 0.2f,
		// layout.getEditorArea());
		// layout.addView("com.coretek.tools.ide.ui.DiagramView", SWT.LEFT,
		// 0.2f, layout.getEditorArea());
	}
}
