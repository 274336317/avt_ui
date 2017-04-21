/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.internal.PluginActionContributionItem;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.common.service.IPaintService;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 时序图监控视图
 * 
 * @author 孙大巍
 * @date 2012-1-6
 */
public class SequenceViewPart extends ViewPart implements Observer, IPaintService
{
	public final static String	ID				= "com.coretek.spte.monitor.monitorView";

	private static final Logger	logger			= LoggingPlugin.getLogger(SequenceViewPart.class.getName());

	private Sequence			sequence;

	private Composite			rootPanel;

	private String				errorTypeNext	= ErrorTypesEnum.WRONGVALUE.getName();							// 导航中的下一条错误消息

	private String				errorTypeLast	= ErrorTypesEnum.WRONGVALUE.getName();							// 导航中的上一条错误消息

	public SequenceViewPart()
	{
		if (ExecutorSession.getInstance() != null)
			ExecutorSession.getInstance().registerListener(this);
	}

	/**
	 * @return the sequence <br/>
	 * 
	 */
	public Sequence getSequence()
	{
		return sequence;
	}

	/**
	 * 根据用户设置的新参数重新画界面
	 * 
	 * @param scale 每个刻度所代表的值
	 * @param timeBound 时间范围</br>
	 */
	public void updateCfg(int scale, int timeBound)
	{
		this.sequence.updateCfg(scale, timeBound);
	}

	/**
	 * 显示序列图
	 * 
	 * @param sequence </br>
	 */
	public void showSequence(Sequence sequence)
	{
		Control[] children = this.rootPanel.getChildren();
		for (Control control : children)
		{
			control.dispose();
		}

		this.sequence = sequence;
		this.sequence.paint(this.rootPanel);
		Rectangle rect = this.rootPanel.getBounds();
		this.rootPanel.setBounds(rect.x, rect.y, rect.width - 1, rect.height);
		this.rootPanel.setBounds(rect.x, rect.y, rect.width + 1, rect.height);

	}

	public void updateErrorNavigatorStatus(boolean enableNext, boolean enableLast)
	{
		IActionBars bars = this.getViewSite().getActionBars();
		IToolBarManager toolManager = bars.getToolBarManager();
		IContributionItem item = toolManager.find("com.coretek.spte.monitor.NextError");
		PluginActionContributionItem paci = (PluginActionContributionItem) item;
		paci.getAction().setEnabled(enableNext);

		item = toolManager.find("com.coretek.spte.monitor.LastError");
		paci = (PluginActionContributionItem) item;
		paci.getAction().setEnabled(enableNext);
	}

	public void updateCfgStatus(boolean enabled)
	{
		IActionBars bars = this.getViewSite().getActionBars();
		IToolBarManager toolManager = bars.getToolBarManager();
		IContributionItem item = toolManager.find("com.coretek.spte.monitor.configAction");
		PluginActionContributionItem paci = (PluginActionContributionItem) item;
		paci.getAction().setEnabled(enabled);
	}

	public void updateSaveStatus(boolean enabled)
	{
		IActionBars bars = this.getViewSite().getActionBars();
		IToolBarManager toolManager = bars.getToolBarManager();
		IContributionItem item = toolManager.find("com.coretek.spte.monitor.action1");
		PluginActionContributionItem paci = (PluginActionContributionItem) item;
		paci.getAction().setEnabled(enabled);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		// 根容器
		rootPanel = new Composite(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		rootPanel.setLayoutData(gridData);

		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		rootPanel.setLayout(gridLayout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus() </br> <b>日期</b>
	 * 2012-1-6
	 */
	@Override
	public void setFocus()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 * <br/>
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		Event event = (Event) arg;
		if (event.getEventType() == Event.EVENT_TIME_SELECTED)
		{
			long time = event.getStartTime();
			if (this.sequence instanceof MiddleSequence)
			{
				MiddleSequence ms = (MiddleSequence) this.sequence;
				ExecutorSession manager = ExecutorSession.getInstance();
				if (manager == null)
				{
					logger.warning("ExecutorSession并未被初始化。将放弃处理本次的Event.EVENT_TIME_SELECTED事件。");
					return;
				}

				long startTime = time - ms.getTimeBound();
				if (startTime < 0)
					startTime = 0;
				if (ExecutorSession.getInstance() == null)
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "无法获取数据源！");
					return;
				}
				SPTEMsg[] msgs = ExecutorSession.getInstance().querySPTEMsgs(startTime, 2 * ms.getTimeBound());
				List<Result> list = new ArrayList<Result>(msgs.length);
				for (SPTEMsg msg : msgs)
				{
					Result result = new Result(new ArrayList<ErrorTypesEnum>(0), null, msg, null);
					list.add(result);
				}
				ms.updateSelectedTime((int) time, list);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose()
	{
		if (ExecutorSession.getInstance() != null)
			ExecutorSession.getInstance().unRegisterListener(this);
	}

	/**
	 * 获取当前监控视图下一条错误消息的类型
	 * 
	 * @return
	 */
	public String getErrorTypeNext()
	{
		return errorTypeNext;
	}

	/**
	 * 设置当前监控视图下一条错误消息的类型
	 * 
	 * @param errorTypeNext 所要设置的错误消息类型
	 */
	public void setErrorTypeNext(String errorTypeNext)
	{
		this.errorTypeNext = errorTypeNext;
	}

	/**
	 * 获取当前监控视图上一条错误消息的类型
	 * 
	 * @return
	 */
	public String getErrorTypeLast()
	{
		return errorTypeLast;
	}

	/**
	 * 设置当前监控视图下一条错误消息的类型
	 * 
	 * @param errorTypeLast 所要设置的错误消息类型
	 */
	public void setErrorTypeLast(String errorTypeLast)
	{
		this.errorTypeLast = errorTypeLast;
	}

	/**
	 * 根据当前监控视图下错误导航图标的Tooltip
	 * 
	 * @param str
	 */
	public void changeActionTooltip(String str)
	{
		ActionContributionItem item1 = (ActionContributionItem) getViewSite().getActionBars().getToolBarManager().find(LastErrorDelegate.ID);
		item1.getAction().setToolTipText(StringUtils.concat("上一条", str, "消息"));
		ActionContributionItem item2 = (ActionContributionItem) getViewSite().getActionBars().getToolBarManager().find(NextErrorDelegate.ID);
		item2.getAction().setToolTipText(StringUtils.concat("下一条", str, "消息"));
	}

	/**
	 * 根据ENUM中的错误消息类型转化为普通汉字
	 * 
	 * @param type
	 * @return
	 */
	public String enumType2Str(String type)
	{
		String str = "丢失";
		if (type.equals(ErrorTypesEnum.LOST.getName()))
		{
			str = "丢失";
		} else if (type.equals(ErrorTypesEnum.TIMEOUT.getName()))
		{
			str = "超时";
		} else if (type.equals(ErrorTypesEnum.UNEXPECTED.getName()))
		{
			str = "未期望";
		} else if (type.equals(ErrorTypesEnum.WRONGVALUE.getName()))
		{
			str = "错误";
		}
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.common.IPaintService#paint(long)
	 */
	@Override
	public void paint(final long time)
	{
		Display.getDefault().syncExec(new Runnable()
		{

			@Override
			public void run()
			{
				ExecutorSession manager = ExecutorSession.getInstance();
				if (manager == null)
				{
					logger.warning("ExecutorSession并未被初始化。将放弃处理本次的Event.EVENT_TIME_SELECTED事件。");
					return;
				}

				long startTime = time - 5000;
				if (startTime < 0)
					startTime = 0;
				if (ExecutorSession.getInstance() == null)
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "无法获取数据源！");
					return;
				}
				SPTEMsg[] msgs = ExecutorSession.getInstance().querySPTEMsgs(startTime, 2 * 5000);
				List<Result> list = new ArrayList<Result>(msgs.length);
				for (SPTEMsg msg : msgs)
				{
					Result result = new Result(new ArrayList<ErrorTypesEnum>(0), null, msg, null);
					list.add(result);
				}
				SequenceViewPart.this.sequence = null;
				System.gc();
				SequenceViewPart.this.sequence = new MiddleSequence(list, 5, (int) time, 5000);
				String icdPath = ExecutorSession.getInstance().getICDPath();
				ClazzManager cm = TemplateEngine.getEngine().parseICD(new File(icdPath));
				SequenceViewPart.this.sequence.setIcdManager(cm);
				SequenceViewPart.this.showSequence(sequence);
				SequenceViewPart.this.updateErrorNavigatorStatus(true, true);
				SequenceViewPart.this.updateCfgStatus(true);
				SequenceViewPart.this.updateSaveStatus(true);

			}

		});

	}

}