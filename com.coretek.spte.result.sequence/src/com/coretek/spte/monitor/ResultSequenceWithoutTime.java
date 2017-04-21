/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.ItemBorderAnchor;
import com.coretek.spte.monitor.figures.NodeFgr;

/**
 * 没有时间刻度的序列图
 * 
 * @author 孙大巍 2012-3-1
 */
public class ResultSequenceWithoutTime extends Sequence
{
	private static final Logger	logger	= LoggingPlugin.getLogger(WaterFallSequence.class);

	private CompareResult		compareResult;

	public ResultSequenceWithoutTime(List<Result> msgs, CompareResult compareResult)
	{
		super(msgs);
		this.compareResult = compareResult;
	}

	/**
	 * @return the compareResult <br/>
	 */
	public CompareResult getCompareResult()
	{
		return compareResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.monitor.Sequence#paint() <b>日期</b> 2012-3-1
	 */
	@Override
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
		this.showNodes();
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

		this.paintMsgs(0, this.nodeNumber - 1);
	}

	private void paintMsgs(int startIndex, int endIndex)
	{
		for (int i = startIndex, length = this.resultList.size(); i < length && i < endIndex; i++)
		{
			Result result = this.resultList.get(i);
			SPTEMsg msg = result.getSpteMsg();

			int position = (i % this.nodeNumber);
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
	 * 计算页总数
	 */
	private void computePageSum()
	{
		if (this.resultList.size() != 0)
		{
			// 计算出需要多少页
			this.pageSum = this.resultList.size() / this.nodeNumber;
			if ((this.resultList.size() % this.nodeNumber) > 0)
				this.pageSum++;
			this.updateStatusOfNavigators();
		}
	}

	/**
	 * 更新导航按钮的状态
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
	 * 显示生命线上的节点
	 */
	protected void showNodes()
	{
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.monitor.Sequence#repaint()
	 */
	@Override
	public void repaint()
	{
		this.msgContainerFgr.removeAllMsgs();
		int startIndex = (this.currentPageNum - 1) * this.nodeNumber;
		int endIndex = startIndex + this.nodeNumber;
		this.paintMsgs(startIndex, endIndex);
		this.updatePageInfo();
	}

	@Override
	public void updateCfg(int scale, int timeBound)
	{
		// DO NOTHINGH
	}

	/**
	 * @param caseManager the caseManager to set <br/>
	 */
	public void setCaseManager(ClazzManager caseManager)
	{
		this.caseManager = caseManager;
	}
}