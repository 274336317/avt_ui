package com.coretek.spte.rcp;

import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;

/**
 * 
 * @author 孙大巍
 * @date 2010-11-23
 * 
 */
@SuppressWarnings("restriction")
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	// 工作区默认的透视图ID
	private static final String PERSPECTIVE_ID = "com.coretek.tools.ide.ui.DiagramPerspective";

	/**
	 * 创建工作区窗口
	 */
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/**
	 * 获得默认透视图的ID
	 */
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/**
	 * 初始化工作区设置
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
		
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		configurer.declareImage(IDEInternalWorkbenchImages.IMG_OBJS_WARNING_PATH, SPTEPlugin.getImageDescriptor("/icons/warning.gif"), true);
		configurer.declareImage(IDEInternalWorkbenchImages.IMG_OBJS_ERROR_PATH, SPTEPlugin.getImageDescriptor("/icons/error.gif"), true);
	}
}