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
 * ��������RCP�Ľ������
 * 
 * @author ���Ρ
 * @date 2010-11-23
 */
@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/**
	 * �����˵�������
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
	 * @Description �򿪴���ǰ���ø÷������Դ��ڳ�ʼ������
	 * 
	 * @Auther MENDY
	 * 
	 * @Date 2016-5-30 ����03:39:22
	 */
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		// ���ý��������С������
		configurer.setInitialSize(new Point(Display.getCurrent().getClientArea().width, Display.getCurrent().getClientArea().height));
		// ��ʾ����������
		configurer.setShowCoolBar(true);
		// ��ʾ״̬������
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowPerspectiveBar(true);
		// ��ʾ����������
		configurer.setShowProgressIndicator(true);
		configurer.setShowMenuBar(true);
		//configurer.setTitle("���յ���ϵͳ��֤����");
		configurer.setTitle("ƽ̨ ���������");
	}

	@Override
	public void postWindowOpen() {
		// ��ʼ��ICD������
		ICDFileManager manager = ICDFileManager.getInstance();
		manager.collectAllICDFile();
		init_load();
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * 
	 * @Function init_load ����rcp����ɫ����
	 * 
	 * @Description
	 * 
	 * @Auther MENDY
	 * 
	 * 
	 *         void
	 * 
	 * @Date 2016-6-3 ����02:02:33
	 */
	private void init_load() {
		// ���ò˵���ť����ɫ
		setMenuColor(ImageAndColor.getDefault().getColor(1));
		// ����EditorArea����ɫ
		setEditorAreaTabFolderColor(ImageAndColor.getDefault().getColor(1));
		// ����RCP���򱳾�ɫ
		setRCPColor(ImageAndColor.getDefault().getColor(1));
	}

	@Override
	public void postWindowClose() {
		ImageManager.dispose();
		
		PrefUtil.getAPIPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_INTRO, true);
		PrefUtil.saveAPIPrefs();
	}

	// -----------------------------------------------------------------------------------------
	// Menu���ṩ�ɼ��ķ���ΪsetBackground��setBackgroundImage��setForeground��
	// ����ͨ�����������Щ���������á�
	// -----------------------------------------------------------------------------------------

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setMenuBG
	 * @Description ���˵������ñ���ͼƬ
	 * @Auther MENDY
	 * @param m_MenuImage
	 *            void
	 * @Date 2016-5-26 ����09:10:23
	 * 
	 *       try { Method m_Method =
	 *       m_Menu.getClass().getDeclaredMethod("setBackground", Color.class);
	 *       m_Method.setAccessible(true); m_Method.invoke(m_Menu, m_MenuColor);
	 *       } catch (Exception e) { System.out.println("===>>>" +
	 *       e.getMessage()); }
	 */
	public void setMenuBG(Image m_MenuImage) {
		// ��ȡRCP���Ĭ�����ɵĲ˵��������
		MenuManager m_MenuManage = (MenuManager) getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		Menu m_Menu = m_MenuManage.getMenu();
		// ͨ��������˵���������ͼƬ
		invoke("setBackgroundImage", m_Menu, new Class[] { Image.class }, new Image[] { m_MenuImage });
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function invoke
	 * @Description ����˽�з���
	 * @Auther MENDY
	 * @param m_MethodName
	 * @param m_Obj
	 * @param m_ClazzTypes
	 * @param m_ObjArgs
	 *            void
	 * @Date 2016-5-25 ����05:30:53
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
	 * @Description ���˵������ñ���ɫ
	 * @Auther MENDY
	 * @param m_MenuColor
	 *            void
	 * @Date 2016-5-25 ����05:34:19
	 */
	public void setMenuColor(Color m_MenuColor) {
		// ��ȡRCP���Ĭ�����ɵĲ˵��������
		MenuManager m_MenuManage = (MenuManager) getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		Menu m_Menu = m_MenuManage.getMenu();
		invoke("setBackground", m_Menu, new Class[] { Color.class }, new Color[] { m_MenuColor });
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ApplicationWorkbenchWindowAdvisor
	 * @Function setToorbarBG
	 * @Description �����������ñ���ͼƬ
	 * @Auther MENDY
	 * @param m_ToorbarMage
	 *            void
	 * @Date 2016-5-25 ����06:17:14
	 */
	public void setToorbarBG(Image m_ToorbarMage) {
		Control[] m_Controls = getWindowConfigurer().getWindow().getShell().getChildren();
		for (int i = 0; i < m_Controls.length; i++) {
			String m_ClazzName = m_Controls[i].getClass().getName();
			// ��ȡRCP���Ĭ�����ɵĹ���������
			if (m_ClazzName.endsWith("CBanner")) {
				// Ϊ����������ͼƬ
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
	 * @Description ��Editorarea�������ñ���ɫ
	 * @Auther MENDY
	 * @param m_EditorAreaColor
	 *            void
	 * @Date 2016-5-25 ����06:19:06
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
				m_8.setBackground(ImageAndColor.getDefault().getColor(1)); // ����Editor�����Ľ���ɫ
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
	 * @Description ����RCP��ܱ���ɫ
	 * @Auther MENDY
	 * @param m_Color
	 *            void
	 * @Date 2016-5-26 ����11:29:54
	 */
	public void setRCPColor(Color m_Color) {
		Object[] m_Controls = getWindowConfigurer().getWindow().getShell().getChildren();
		for (int i = 0; i < m_Controls.length; i++) {
			String m_Clazz = m_Controls[i].getClass().getName();
			// ��ȡRCP��ܶ����˵����������
			if (m_Clazz.endsWith("CBanner")) {
				Composite m_Control = (Composite) m_Controls[i];
				m_Control.setBackground(m_Color);
				m_Control.setBackgroundMode(SWT.INHERIT_FORCE);
			}
			// ��ȡRCP���Ĭ�����ɵ�״̬���������
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
	 * @Date 2016-5-27 ����02:50:02
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
