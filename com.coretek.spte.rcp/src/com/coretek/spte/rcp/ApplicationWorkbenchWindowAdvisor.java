package com.coretek.spte.rcp;

import java.lang.reflect.Method;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.part.EditorPart;

import com.coretek.common.utils.ImageAndColor;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.ImageManager;

/**
 * 设置整个RCP的界面外观
 * 
 * @author 孙大巍
 * @date 2010-11-23
 */
@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/**
	 * 创建菜单栏对象
	 */
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/*
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * 
	 * @Function preWindowOpen
	 * 
	 * @Description 打开窗口前调用该方法，对窗口初始化设置
	 * 
	 * @Auther MENDY
	 * 
	 * @Date 2016-5-30 下午03:39:22
	 */
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		// 设置界面区域大小，宽、高
		configurer.setInitialSize(new Point(Display.getCurrent().getClientArea().width, Display.getCurrent().getClientArea().height));
		// 显示工具条区域
		configurer.setShowCoolBar(true);
		// 显示状态栏区域
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowPerspectiveBar(true);
		// 显示进度条区域
		configurer.setShowProgressIndicator(true);
		configurer.setShowMenuBar(true);
		//configurer.setTitle("航空电子系统验证工具");
		configurer.setTitle("平台 配置项测试");
	}

	@Override
	public void postWindowOpen() {
		// 初始化ICD解析器
		ICDFileManager manager = ICDFileManager.getInstance();
		manager.collectAllICDFile();
		init_load();
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * 
	 * @Function init_load 加载rcp背景色设置
	 * 
	 * @Description
	 * 
	 * @Auther MENDY
	 * 
	 * 
	 *         void
	 * 
	 * @Date 2016-6-3 下午02:02:33
	 */
	private void init_load() {
		// 设置菜单按钮背景色
		setMenuColor(ImageAndColor.getDefault().getColor(1));
		// 设置EditorArea背景色
		setEditorAreaTabFolderColor(ImageAndColor.getDefault().getColor(1));
		// 设置RCP程序背景色
		setRCPColor(ImageAndColor.getDefault().getColor(1));
	}

	@Override
	public void postWindowClose() {
		ImageManager.dispose();
		
		PrefUtil.getAPIPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_INTRO, true);
		PrefUtil.saveAPIPrefs();
	}

	// -----------------------------------------------------------------------------------------
	// Menu类提供可见的方法为setBackground、setBackgroundImage、setForeground。
	// 必须通过反射调用这些方法来设置。
	// -----------------------------------------------------------------------------------------

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setMenuBG
	 * @Description 给菜单栏设置背景图片
	 * @Auther MENDY
	 * @param m_MenuImage
	 *            void
	 * @Date 2016-5-26 上午09:10:23
	 * 
	 *       try { Method m_Method =
	 *       m_Menu.getClass().getDeclaredMethod("setBackground", Color.class);
	 *       m_Method.setAccessible(true); m_Method.invoke(m_Menu, m_MenuColor);
	 *       } catch (Exception e) { System.out.println("===>>>" +
	 *       e.getMessage()); }
	 */
	public void setMenuBG(Image m_MenuImage) {
		// 获取RCP框架默认生成的菜单区域对象
		MenuManager m_MenuManage = (MenuManager) getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		Menu m_Menu = m_MenuManage.getMenu();
		// 通过反射给菜单区域设置图片
		invoke("setBackgroundImage", m_Menu, new Class[] { Image.class }, new Image[] { m_MenuImage });
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function invoke
	 * @Description 反射私有方法
	 * @Auther MENDY
	 * @param m_MethodName
	 * @param m_Obj
	 * @param m_ClazzTypes
	 * @param m_ObjArgs
	 *            void
	 * @Date 2016-5-25 下午05:30:53
	 */
	private void invoke(String m_MethodName, Object m_Obj, Class<?>[] m_ClazzTypes, Object[] m_ObjArgs) {
		try {
			Method m_Method = m_Obj.getClass().getDeclaredMethod(m_MethodName, m_ClazzTypes);
			m_Method.setAccessible(true);
			m_Method.invoke(m_Obj, m_ObjArgs);
		} catch (Exception e) {
			System.out.println("===>>>" + e.getMessage());
		}
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setMenuColor
	 * @Description 给菜单栏设置背景色
	 * @Auther MENDY
	 * @param m_MenuColor
	 *            void
	 * @Date 2016-5-25 下午05:34:19
	 */
	public void setMenuColor(Color m_MenuColor) {
		// 获取RCP框架默认生成的菜单区域对象
		MenuManager m_MenuManage = (MenuManager) getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		Menu m_Menu = m_MenuManage.getMenu();
		invoke("setBackground", m_Menu, new Class[] { Color.class }, new Color[] { m_MenuColor });
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setToorbarBG
	 * @Description 给工具条设置背景图片
	 * @Auther MENDY
	 * @param m_ToorbarMage
	 *            void
	 * @Date 2016-5-25 下午06:17:14
	 */
	public void setToorbarBG(Image m_ToorbarMage) {
		Control[] m_Controls = getWindowConfigurer().getWindow().getShell().getChildren();
		for (int i = 0; i < m_Controls.length; i++) {
			String m_ClazzName = m_Controls[i].getClass().getName();
			// 获取RCP框架默认生成的工具条对象
			if (m_ClazzName.endsWith("CBanner")) {
				// 为工具栏设置图片
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackgroundImage(m_ToorbarMage);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
		}
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setEditorAreaTabFolderColor
	 * @Description 给Editorarea区域设置背景色
	 * @Auther MENDY
	 * @param m_EditorAreaColor
	 *            void
	 * @Date 2016-5-25 下午06:19:06
	 */
	public void setEditorAreaTabFolderColor(Color m_EditorAreaColor) {
		if (getWindowConfigurer().getWindow() == null) {
			return;
		}
		if (getWindowConfigurer().getWindow().getActivePage() == null) {
			return;
		}
		WorkbenchPage m_Page = (WorkbenchPage) getWindowConfigurer().getWindow().getActivePage();
		Composite m_ClientEditor = m_Page.getClientComposite();
		Control[] m_ClientControls = m_ClientEditor.getChildren();
		for (int i = 0; i < m_ClientControls.length; i++) {
			if (i == 0) {
				ToolBar m_0 = (ToolBar) m_ClientControls[0];
				m_0.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 1) {
				Composite m_1 = (Composite) m_ClientControls[1];
				m_1.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 2) {
				ToolBar m_2 = (ToolBar) m_ClientControls[2];
				m_2.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 3) {
				Composite m_3 = (Composite) m_ClientControls[3];
				m_3.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 4) {
				ToolBar m_4 = (ToolBar) m_ClientControls[4];
				m_4.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 5) {
				Composite m_5 = (Composite) m_ClientControls[5];
				m_5.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 6) {
				ToolBar m_6 = (ToolBar) m_ClientControls[6];
				m_6.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 7) {
				Composite m_7 = (Composite) m_ClientControls[7];
				m_7.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 8) {
				Composite m_8 = (Composite) m_ClientControls[8];
				m_8.setBackground(ImageAndColor.getDefault().getColor(1)); // 设置Editor区域四角颜色
			}
			if (i == 9) {
				CTabFolder m_9 = (CTabFolder) m_ClientControls[9];
				m_9.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 10) {
				CTabFolder m_10 = (CTabFolder) m_ClientControls[10];
				m_10.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 11) {
				CTabFolder m_11 = (CTabFolder) m_ClientControls[11];
				m_11.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 12) {
				CTabFolder m_12 = (CTabFolder) m_ClientControls[12];
				m_12.setBackground(ImageAndColor.getDefault().getColor(2));
			}
			if (i == 13) {
				Sash m_13 = (Sash) m_ClientControls[13];
				m_13.setBackground(ImageAndColor.getDefault().getColor(1));
			}
			if (i == 14) {
				Sash m_14 = (Sash) m_ClientControls[14];
				m_14.setBackground(ImageAndColor.getDefault().getColor(1));
			}
			if (i == 15) {
				Sash m_15 = (Sash) m_ClientControls[15];
				m_15.setBackground(ImageAndColor.getDefault().getColor(1));
			}
			if (i == 16) {
				Sash m_16 = (Sash) m_ClientControls[16];
				m_16.setBackground(ImageAndColor.getDefault().getColor(1));
			}
		}

	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setRCPColor
	 * @Description 设置RCP框架背景色
	 * @Auther MENDY
	 * @param m_Color
	 *            void
	 * @Date 2016-5-26 上午11:29:54
	 */
	public void setRCPColor(Color m_Color) {
		Object[] m_Controls = getWindowConfigurer().getWindow().getShell().getChildren();
		for (int i = 0; i < m_Controls.length; i++) {
			String m_Clazz = m_Controls[i].getClass().getName();
			// 获取RCP框架二级菜单栏区域对象
			if (m_Clazz.endsWith("CBanner")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
			// 获取RCP框架默认生成的状态栏区域对象
			if (m_Clazz.endsWith("StatusLine")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
			if (m_Clazz.endsWith("FastViewBar$4")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
			if (m_Clazz.endsWith("ProgressRegion$1")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
			if (m_Clazz.endsWith("TrimCommonUIHandle")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
		}
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function addPartLinstener
	 * @Description
	 * @Auther MENDY
	 * @param color
	 *            void
	 * @Date 2016-5-27 下午02:50:02
	 */
	public void addPartLinstener(final Color color) {
		getWindowConfigurer().getWindow().getActivePage().addPartListener(new IPartListener() {

			@Override
			public void partOpened(IWorkbenchPart part) {
				if (part instanceof EditorPart) {
					setEditorAreaTabFolderColor(color);
				}
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				if (part instanceof EditorPart) {
					setEditorAreaTabFolderColor(color);
				}
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				if (part instanceof EditorPart) {
					setEditorAreaTabFolderColor(color);
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				if (part instanceof EditorPart) {
					setEditorAreaTabFolderColor(color);
				}
			}

			@Override
			public void partActivated(IWorkbenchPart part) {
				if (part instanceof EditorPart) {
					setEditorAreaTabFolderColor(color);
				}
			}
		});
	}

}
