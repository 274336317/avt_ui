/************************************************************************
 * 北京科银京成技术有限公司 版权所有
 * Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.ItemBorderAnchor;

/**
 * 瀑布类型的序列图。图形按照时间的先后顺序从上到下显示。
 * 
 * @author 孙大巍 2012-3-1
 */
public class WaterFallSequence extends SequenceWithTime
{

	private static final Logger	logger	= LoggingPlugin.getLogger(WaterFallSequence.class);

	private CompareResult		compareResult;

	public WaterFallSequence(List<Result> msgs, int scale, CompareResult compareResult)
	{
		super(msgs, scale);
		this.compareResult = compareResult;
	}

	/**
	 * @return the compareResult <br/>
	 * 
	 */
	public CompareResult getCompareResult()
	{
		return compareResult;
	}

	/**
	 * @param caseManager the caseManager to set <br/>
	 * 
	 */
	public void setCaseManager(ClazzManager caseManager)
	{
		this.caseManager = caseManager;
	}

	/**
	 * 返回刻度值
	 * 
	 * @return the scale <br/>
	 */
	public int getScale()
	{
		return scale;
	}

	public void repaint()
	{
		this.msgContainerFgr.removeAllMsgs();
		int startTime = this.currentPageNum * this.pageTime - this.pageTime;
		this.timerFgr.repaint(startTime, this.scale);
		this.paintMsgs(startTime, startTime + this.pageTime);
		this.updatePageInfo();
	}

	/**
	 * 画出消息 </br>
	 */
	public void paint(Composite panel)
	{
		if (this.resultList.size() == 0)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "警告", "没有执行结果！");
			return;
		}
		super.paint(panel);
		this.showHeaderPanel(headerPanel);
		this.showSequencePanel(sequencePanel);

		// 计算出每页所能表示的时间长度
		this.pageTime = this.scale * this.nodeNumber;
		this.showGotoPanel(timePanel);

		this.computePageSum();

		if (this.pageSum > 1)
		{
			// 激活last
			this.enableLast();
			// 激活next
			this.enableNext();
		}
		this.updatePageInfo();
		// 画出第一页
		this.paintMsgs(this.resultList.get(0).getSpteMsg().getTimeStamp(), this.nodeNumber * this.scale);

	}

	/**
	 * 根据用户设置的新参数重新画界面
	 * 
	 * @param scale 每个刻度所代表的值
	 * @param timeBound 时间范围</br>
	 */
	public void updateCfg(int scale, int timeBound)
	{
		boolean flag = false;
		if (this.scale != scale)
		{
			flag = true;

			this.scale = scale;
			this.msgContainerFgr.removeAllMsgs();
			// 计算出每页所表示的时间
			this.pageTime = this.nodeNumber * this.scale;
		}

		if (flag)
		{
			// 默认跳转到第一页开始重画
			this.currentPageNum = 1;
			this.computePageSum();
			this.repaint();
		}

	}

	/**
	 * 计算页总数 </br>
	 */
	private void computePageSum()
	{
		if (this.resultList.size() != 0)
		{
			SPTEMsg tailMsg = this.resultList.get(this.resultList.size() - 1).getSpteMsg();
			// 计算出需要多少页
			this.pageSum = (int) tailMsg.getTimeStamp() / (this.scale * this.nodeNumber);
			if (tailMsg.getTimeStamp() % (this.scale * this.nodeNumber) > 0)
				this.pageSum++;
			this.updateStatusOfNavigators();
		}
	}

	/**
	 * 更新导航按钮的状态 </br>
	 */
	private void updateStatusOfNavigators()
	{
		if (pageSum < 2)
		{
			// 将last标签禁止
			this.disableLast();
			// 将previous标签禁止
			this.disablePrevious();
			// 将first标签禁止
			this.disableFirst();
			// 将next标签禁止
			this.disableNext();
		} else
		{
			// 将last标签激活
			this.enableLast();
			// 将previous标签禁止
			this.disablePrevious();
			// 将first标签禁止
			this.disableFirst();
			// 将next标签激活
			this.enableNext();
		}
	}

	/**
	 * 更新页信息
	 */
	private void updatePageInfo()
	{
		StringBuilder sb = new StringBuilder("第");
		sb.append(this.currentPageNum);
		sb.append("页");
		sb.append("/");
		sb.append(this.pageSum);
		sb.append("页");
		this.txtPageNum.setText(sb.toString());
	}

	/**
	 * 画出时间在开始时间到结束时间之内的所有消息
	 * 
	 * @param startTime 开始时间
	 * @param endTime 结束时间 </br>
	 */
	private void paintMsgs(long startTime, long endTime)
	{
		for (int i = 0; i < this.resultList.size(); i++)
		{
			Result result = this.resultList.get(i);
			SPTEMsg msg = result.getSpteMsg();
			if (msg.getTimeStamp() >= startTime && msg.getTimeStamp() < endTime)
			{
				int relativeTime = (int) msg.getTimeStamp() - (currentPageNum - 1) * this.nodeNumber * this.scale;
				int position = relativeTime / this.scale;
				if (msg.getICDMsg().getAttribute("sourceFunctionID") == null)
				{
					logger.warning(StringUtils.concat("无法获取消息的源ID。msg=\n", msg.getICDMsg()));
					return;
				}
				boolean foundSrc = false;
				// 查找出源功能对象
				for (Object child : this.headerContainerFgr.getChildren())
				{
					if (child instanceof HeaderFgr)
					{
						int srcId = (Integer) msg.getICDMsg().getAttribute("sourceFunctionID").getValue();
						HeaderFgr fgr = (HeaderFgr) child;
						if (srcId == fgr.getFunctionId())
						{
							foundSrc = true;
							Attribute att = msg.getICDMsg().getAttribute("msgTransType");
							Color arrowColor = null;
							if (!TemplateUtils.MSG_TRANS_TYPE_PERIOD.equals(att.getValue().toString()))
							{
								arrowColor = ColorConstants.blue;
							} else
							{
								arrowColor = ColorConstants.darkGreen;
							}
							ConnFgr conn = new ConnFgr(ColorConstants.darkGreen, arrowColor, result, caseManager, testedObjects, icdManager, this.compareResult);
							List<Integer> destIDs = msg.getICDMsg().getDestIDs();
							if (destIDs == null || destIDs.size() == 0)
							{
								logger.warning(StringUtils.concat("消息为点播消息，却找不到目的ID。msg=\n", msg.getICDMsg()));
								return;
							} else if (destIDs.size() != 1)
							{
								logger.warning(StringUtils.concat("消息为点播消息，却发现有多个目的ID。msg=\n", msg.getICDMsg()));
								return;
							}
							boolean foundDest = false;
							Integer destID = destIDs.get(0);
							for (Object kid : this.headerContainerFgr.getChildren())
							{
								if (kid instanceof HeaderFgr)
								{
									HeaderFgr hf = (HeaderFgr) kid;
									if (destID == hf.getFunctionId())
									{
										foundDest = true;
										conn.setSourceAnchor(new ItemBorderAnchor(fgr.getNodes().get(position)));
										conn.setTargetAnchor(new ItemBorderAnchor(hf.getNodes().get(position)));
										this.msgContainerFgr.add(conn);
										break;
									}
								}
							}
							if (!foundDest)
							{
								logger.warning(StringUtils.concat("无法找到消息的目的功能节点对象。msg=\n", msg.getICDMsg()));
								return;
							}
						}
					}
				}
				// 找不到源功能节点
				if (!foundSrc)
				{
					logger.warning(StringUtils.concat("找不到源功能节点。msg=\n", msg.getICDMsg()));
					return;
				}

			}
		}
	}

	@Override
	protected void showGotoPanelLeft(Composite timePanel)
	{
		super.showGotoPanelLeft(timePanel);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 15;
		leftPlaceHolder.setLayout(layout);
		Label label = new Label(leftPlaceHolder, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2));
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		label.setText("消息总数:" + compareResult.getReslutList().size());
		int loss = compareResult.getAllLostCount();
		showCompareResultItem(leftPlaceHolder, "丢失个数", loss);
		int timeout = compareResult.getTimeOut();
		showCompareResultItem(leftPlaceHolder, "超时个数", timeout);
		int error = compareResult.getErrorValue();
		showCompareResultItem(leftPlaceHolder, "错误个数", error);
		int unexpected = compareResult.getUnexpected();
		showCompareResultItem(leftPlaceHolder, "未期望个数", unexpected);
	}

	/**
	 * 在消息面板的左部分显示每种错误消息的类型及其对应的数量
	 * 
	 * @param leftPlaceHolder
	 * @param type
	 * @param count
	 * @author zhangyi@tech.coretek.com.cn
	 */
	private void showCompareResultItem(Composite leftPlaceHolder, String type, int count)
	{
		StyledText text = new StyledText(leftPlaceHolder, SWT.READ_ONLY);
		StyleRange range = new StyleRange();
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		text.setLayoutData(gridData);
		text.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND));
		text.setToolTipText(StringUtils.concat("显示第一条", type.substring(0, type.lastIndexOf("个数")), "消息"));
		if (type.length() < 5)
		{
			type = StringUtils.concat("  ", type);
		}
		text.setText(StringUtils.concat(type, ":", count));
		if (type.startsWith("  "))
		{
			range.start = 2;
		} else
		{
			range.start = 0;
		}
		range.length = text.getText().trim().length();
		range.underline = true;
		range.underlineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		text.setStyleRange(range);
		text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		text.setBlockSelection(false);
		text.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseUp(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				StyledText label = (StyledText) e.widget;
				if (label.getText().trim().startsWith("丢失个数") && !label.getText().endsWith(":0"))
				{
					LossInfoDialog dialog = new LossInfoDialog(label.getShell(), compareResult.getAllLost());
					dialog.open();
				}
				if (label.getText().trim().startsWith("超时个数") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.TIMEOUT.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.TIMEOUT.getName());
					view.setErrorTypeLast(ErrorTypesEnum.TIMEOUT.getName());
					changeActionTooltip(view, "超时");
				}
				if (label.getText().trim().startsWith("错误个数") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.WRONGVALUE.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.WRONGVALUE.getName());
					view.setErrorTypeLast(ErrorTypesEnum.WRONGVALUE.getName());
					changeActionTooltip(view, "错误");
				}
				if (label.getText().trim().startsWith("未期望个数") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.UNEXPECTED.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.UNEXPECTED.getName());
					view.setErrorTypeLast(ErrorTypesEnum.UNEXPECTED.getName());
					changeActionTooltip(view, "未期望");
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}
		});
	}

	/**
	 * 在错误消息面板中监听用户点击所属错误消息类型的事件， 修改时序图视图导航错误消息导航工具栏的Tooltip
	 * 
	 * @param view
	 * @param str </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-10
	 */
	private void changeActionTooltip(SequenceViewPart view, String str)
	{
		ActionContributionItem item1 = (ActionContributionItem) view.getViewSite().getActionBars().getToolBarManager().find(LastErrorDelegate.ID);
		item1.getAction().setToolTipText(new StringBuilder("上一条").append(str).append("消息").toString());
		ActionContributionItem item2 = (ActionContributionItem) view.getViewSite().getActionBars().getToolBarManager().find(NextErrorDelegate.ID);
		item2.getAction().setToolTipText(new StringBuilder("下一条").append(str).append("消息").toString());
	}

	/**
	 * 在时序图面板中显示各种消息类型的第一条错误消息
	 * 
	 * @param type 错误消息类型
	 */
	private void showFirstErrorMsg(String type)
	{
		Result result = null;
		for (Result r : resultList)
		{
			if (r.getErrorTypes().size() != 0)
			{
				for (ErrorTypesEnum errorType : r.getErrorTypes())
				{
					if (type.equals(errorType.getName()))
					{
						if (result == null)
						{
							result = r;
						}
						if (result != null && r.getSpteMsg().getTimeStamp() < result.getSpteMsg().getTimeStamp())
						{
							result = r;
						}
						break;
					}
				}

			}
		}
		if (result == null)
		{
			return;
		}
		long time = result.getSpteMsg().getTimeStamp();
		int page = (int) (time / pageTime) + 1;
		currentPageNum = page;
		enableAll();
		if (currentPageNum == pageSum)
		{
			disableNext();
			disableLast();
		}
		if (currentPageNum == 1)
		{
			disableFirst();
			disablePrevious();
		}
		repaint();
		int position = (int) result.getSpteMsg().getTimeStamp() - (currentPageNum - 1) * this.nodeNumber * this.scale;
		int canvasHeight = getSequencePanel().getContent().getBounds().height;
		sequencePanel.setOrigin(0, (position - scale) * canvasHeight / pageTime);
	}

	/**
	 * 启用页面导航中的所有按钮 <b>
	 */
	public void enableAll()
	{
		enableFirst();
		enableLast();
		enableLast();
		enablePrevious();
	}

}