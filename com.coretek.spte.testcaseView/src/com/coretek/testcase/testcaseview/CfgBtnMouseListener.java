/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.spte.monitor.cfg.CfgDialog;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * @author SunDawei 2012-6-7
 */
public class CfgBtnMouseListener implements MouseListener
{
	private final static Logger	logger	= LoggingPlugin.getLogger(CfgBtnMouseListener.class);

	private String				icdPath;

	public CfgBtnMouseListener(String icdPath)
	{
		this.icdPath = icdPath;
	}

	public void mouseDoubleClick(MouseEvent e)
	{

	}

	public void mouseDown(MouseEvent e)
	{
//		Button btn = (Button) e.getSource();
		Link btn = (Link)e.getSource();
		if (new File(icdPath).exists())
		{
			CfgDialog dialog = CfgDialog.getInstance();
			dialog.setICDFile(this.icdPath);
			if (dialog.open() == Window.CANCEL)
			{
				logger.info("�û������CANCEL��ť�����Ա��μ�ز������á�");
			} else
			{
				logger.info("�û������˼�ز�����");
				ExecutorSession.setRunning(true);
				ExecutorSession manager = ExecutorSession.getInstanceForBoth(dialog.getIcdPath());
				if (manager == null)
				{
					logger.warning("��ʼ��MonitorManagerʧ�ܣ�");
					Shell shell = Display.getCurrent().getActiveShell();
					MessageDialog.openError(shell, "����", "�������ü�ز���ʧ�ܣ�������ִ�в�������ʱ�޷�������ء�");
				} else
				{
					manager.setMonitorCfg(dialog.getSelectedMsgs(), dialog.getIcdPath(), dialog.getEndian());
				}
			}
		} else
		{
			MessageDialog.openError(Display.getDefault().getActiveShell(), "����", "����������������ICD�ļ������ڣ�");
			btn.setEnabled(false);
		}

	}

	public void mouseUp(MouseEvent e)
	{

	}
}
