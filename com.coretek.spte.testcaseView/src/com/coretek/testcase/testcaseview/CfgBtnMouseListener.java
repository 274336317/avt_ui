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
				logger.info("用户点击了CANCEL按钮，忽略本次监控参数设置。");
			} else
			{
				logger.info("用户配置了监控参数。");
				ExecutorSession.setRunning(true);
				ExecutorSession manager = ExecutorSession.getInstanceForBoth(dialog.getIcdPath());
				if (manager == null)
				{
					logger.warning("初始化MonitorManager失败！");
					Shell shell = Display.getCurrent().getActiveShell();
					MessageDialog.openError(shell, "错误", "本次设置监控参数失败！将导致执行测试用例时无法启动监控。");
				} else
				{
					manager.setMonitorCfg(dialog.getSelectedMsgs(), dialog.getIcdPath(), dialog.getEndian());
				}
			}
		} else
		{
			MessageDialog.openError(Display.getDefault().getActiveShell(), "错误", "测试用例所依赖的ICD文件不存在！");
			btn.setEnabled(false);
		}

	}

	public void mouseUp(MouseEvent e)
	{

	}
}
