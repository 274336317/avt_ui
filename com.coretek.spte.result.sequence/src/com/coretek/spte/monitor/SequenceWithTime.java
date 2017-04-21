/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.NodeFgr;

/**
 * ӵ��ʱ��̶ȵ�����ͼ
 * 
 * @author ���Ρ 2012-3-1
 */
public abstract class SequenceWithTime extends Sequence
{

	// ʱ��̶ȣ�Ĭ������£�һ���̶ȴ���5����
	protected int		scale		= 5;

	// ʱ����ͼ��
	protected TimerFgr	timerFgr;

	// ÿһҳ����ʾ��ʱ��
	protected int		pageTime	= 0;

	public SequenceWithTime(List<Result> msgs, int scale)
	{
		super(msgs);
		this.scale = scale;
	}

	/**
	 * ��ȡʱ��̶�ֵ
	 * 
	 * @return
	 */
	public int getScale()
	{
		return scale;
	}

	/**
	 * ����ʱ��̶�ֵ
	 * 
	 * @param scale
	 */
	public void setScale(int scale)
	{
		this.scale = scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.spte.monitor.Sequence#paint(org.eclipse.swt.widgets.Composite
	 */
	@Override
	public void paint(Composite panel)
	{
		super.paint(panel);

	}

	/**
	 * ��ʾ�������ϵĽڵ�
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
	 * ��װʱ��ͼ����
	 * 
	 * @param sequencePanel
	 */
	protected void showSequencePanel(final ScrolledComposite sequencePanel)
	{
		super.showSequencePanel(sequencePanel);
		// ��ʱ����
		timerFgr = this.showTimerFgr();
		msgContainerFgr.add(timerFgr);
		this.showNodes();
	}

	/**
	 * ��ʱ����
	 * 
	 * @return
	 */
	protected TimerFgr showTimerFgr()
	{
		// ��ʱ����
		TimerFgr timerFgr = new TimerFgr(0);
		timerFgr.setBounds(new Rectangle(0, 0, 50, msgContainerFgr.getBounds().height));

		return timerFgr;
	}
}