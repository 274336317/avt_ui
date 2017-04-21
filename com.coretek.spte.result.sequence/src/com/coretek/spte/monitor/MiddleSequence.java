/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.ItemBorderAnchor;
import com.coretek.spte.monitor.figures.MiddleConnFgr;
import com.coretek.spte.monitor.figures.NodeFgr;
import com.coretek.spte.monitor.figures.SequenceContainerFgr;

/**
 * 从中间往两边画的序列图。此图用于响应曲线视图上用户的选择操作
 * 
 * @author 孙大巍 2012-3-1
 */
public class MiddleSequence extends SequenceWithTime
{

	private static final Logger	logger		= LoggingPlugin.getLogger(MiddleSequence.class);

	// 用户选择的中间时间
	private int					selectedTime;

	// 时间范围，默认为+/-5000毫秒
	private int					timeBound	= 5000;

	public MiddleSequence(List<Result> msgs, int scale, int selectedTime, int timeBound)
	{
		super(msgs, scale);
		this.selectedTime = selectedTime;
		this.timeBound = timeBound;
	}

	/**
	 * @return the selectedTime <br/>
	 * 
	 */
	public int getSelectedTime()
	{
		return selectedTime;
	}

	/**
	 * 设置被选中的时间
	 * 
	 * @param selectedTime 被选中的时间
	 * @param msgs 执行结果</br>
	 */
	public void updateSelectedTime(int selectedTime, List<Result> msgs)
	{
		this.selectedTime = selectedTime;
		this.resultList = Collections.unmodifiableList(msgs);
		this.repaint();
	}

	/**
	 * 获取时间范围
	 * 
	 * @return the timeBound <br/>
	 * 
	 */
	public int getTimeBound()
	{
		return timeBound;
	}

	/**
	 * 更新时间范围
	 * 
	 * @param timeBound the timeBound to set <br/>
	 * 
	 */
	public void updateTimeBound(int timeBound, List<Result> msgs)
	{
		this.timeBound = timeBound;
		this.resultList = Collections.unmodifiableList(msgs);
		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.monitor.Sequence#paint() <br/>
	 */
	@Override
	public void paint(Composite panel)
	{
		super.paint(panel);

		this.showHeaderPanel(headerPanel);

		// 计算出每页所能表示的时间长度
		this.pageTime = this.scale * this.nodeNumber;
		// 计算出被选中的时间所在的页序号
		int targetNo = this.selectedTime / this.pageTime;
		if (this.selectedTime % this.pageTime > 0)
		{
			++targetNo;
		}

		this.currentPageNum = targetNo;
		this.createSequencePanel(sequencePanel);
		this.showGotoPanel(timePanel);

		this.computePageSum();

		if (targetNo == this.pageSum && targetNo != 1)
		{
			this.enablePrevious();
			this.enableFirst();
		} else if (targetNo != this.pageSum && targetNo != 1)
		{
			this.enablePrevious();
			this.enableFirst();
			this.enableLast();
			this.enableNext();
		} else if (targetNo != this.pageSum && targetNo == 1)
		{
			this.enableNext();
			this.enableLast();
		}

		this.updatePageInfo();
		if (targetNo < 1)
			targetNo = 1;
		// 画出目标页
		this.paintMsgs(targetNo * this.pageTime - this.pageTime, targetNo * this.pageTime);

	}

	/**
	 * 组装时序图容器
	 * 
	 * @param sequencePanel </br>
	 */
	private void createSequencePanel(final ScrolledComposite sequencePanel)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		sequencePanel.setLayout(gridLayout);
		final Canvas canvas = new Canvas(sequencePanel, SWT.NONE);
		GridData gridData = new GridData();
		if (sequencePanel.getBounds().height > CANVAS_DEFAULT_HEIGHT)
		{
			gridData.heightHint = sequencePanel.getBounds().height;
		} else
		{
			gridData.heightHint = CANVAS_DEFAULT_HEIGHT;
		}

		gridData.widthHint = this.headerSum * (HEADER_WIDTH + HEADER_DISTANCE) + 100;
		canvas.setSize(gridData.widthHint, gridData.heightHint);
		canvas.setLayoutData(gridData);
		canvas.setBackground(ColorConstants.black);
		sequencePanel.setContent(canvas);
		canvas.addControlListener(new ControlListener()
		{
			int	x	= canvas.getBounds().x;

			@Override
			public void controlMoved(ControlEvent e)
			{
				if (x == canvas.getBounds().x)
				{
					return;
				}
				this.x = canvas.getBounds().x;
				headerCanvas.setBounds(canvas.getBounds().x, 0, canvas.getBounds().width, canvas.getBounds().height);
				headerCanvas.redraw();
			}

			@Override
			public void controlResized(ControlEvent e)
			{
			}

		});
		LightweightSystem lws = new LightweightSystem(canvas);
		// 序列图容器
		msgContainerFgr = new SequenceContainerFgr();
		msgContainerFgr.setFill(true);
		msgContainerFgr.setOpaque(true);
		msgContainerFgr.setOutline(true);
		msgContainerFgr.setBackgroundColor(ColorConstants.white);
		msgContainerFgr.setBounds(new Rectangle(0, 0, CANVAS_DEFAULT_WIDTH, CANVAS_DEFAULT_HEIGHT));
		lws.setContents(msgContainerFgr);
		// 画时间轴
		timerFgr = new TimerFgr((this.currentPageNum - 1) * this.pageTime);

		timerFgr.setBounds(new Rectangle(0, 0, 50, msgContainerFgr.getBounds().height));
		msgContainerFgr.add(timerFgr);
		List<?> children = this.headerContainerFgr.getChildren();
		int counter = 0;
		for (Object obj : children)
		{
			if (obj instanceof HeaderFgr)
			{
				HeaderFgr hcf = (HeaderFgr) obj;
				counter++;

				Rectangle rect = hcf.getBounds();
				this.nodeNumber = msgContainerFgr.getBounds().height / (NODE_HEIGHT + NODE_INTERVAL);
				this.pageTime = this.nodeNumber * this.scale;
				for (int i = 0; i < this.nodeNumber; i++)
				{
					NodeFgr nodeFgr = new NodeFgr(counter, i);
					nodeFgr.setForegroundColor(ColorConstants.darkGreen);
					nodeFgr.setBackgroundColor(ColorConstants.darkGreen);
					nodeFgr.setFill(true);
					nodeFgr.setOpaque(false);
					nodeFgr.setOutline(true);
					nodeFgr.setBounds(new Rectangle(rect.x + HEADER_WIDTH / 2, (NODE_HEIGHT + NODE_INTERVAL) * i, NODE_WIDTH, NODE_HEIGHT));
					msgContainerFgr.add(nodeFgr);
					hcf.addNode(nodeFgr);
				}

			}
		}

	}

	/**
	 * 计算页总数 </br>
	 */
	private void computePageSum()
	{
		if (this.resultList.size() != 0)
			// 计算出需要多少页
			this.pageSum = (this.selectedTime + 2 * this.timeBound) / this.pageTime;
		if ((this.selectedTime + 2 * this.timeBound) % this.pageTime > 0)
			this.pageSum++;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.monitor.Sequence#repaint()
	 */
	@Override
	public void repaint()
	{
		this.msgContainerFgr.removeAllMsgs();
		int startTime = this.currentPageNum * this.pageTime - this.pageTime;
		if (startTime < 0)
			startTime = 0;
		this.timerFgr.repaint(startTime, this.scale);
		this.paintMsgs(startTime, startTime + this.pageTime);
		this.updatePageInfo();

	}

	@Override
	public void updateCfg(int scale, int timeBound)
	{

	}

	private void updatePageInfo()
	{
		this.txtPageNum.setText(StringUtils.concat("第", this.currentPageNum, "页", "/", this.pageSum, "页"));
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
							MiddleConnFgr conn = new MiddleConnFgr(ColorConstants.darkGreen, arrowColor, result, icdManager);
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
}