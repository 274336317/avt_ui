/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor;

import org.eclipse.jface.action.Action;

import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.SequenceContainerFgr;

public class NextAction extends Action
{
	private SequenceViewPart	fMonitorViewPart;
	private Sequence			sequence;
	private String				type;

	public NextAction(SequenceViewPart view, String type)
	{
		this.fMonitorViewPart = view;
		sequence = fMonitorViewPart.getSequence();
		this.type = type;
		setText(new StringBuilder("下一条").append(fMonitorViewPart.enumType2Str(this.type)).append("消息").toString());
	}

	@Override
	public void run()
	{
		fMonitorViewPart.setErrorTypeNext(type);
		fMonitorViewPart.setErrorTypeLast(type);
		fMonitorViewPart.changeActionTooltip(fMonitorViewPart.enumType2Str(type));
		if (sequence == null)
			return;
		int canvasHeight = sequence.getSequencePanel().getContent().getBounds().height;
		if (sequence instanceof WaterFallSequence)
		{
			WaterFallSequence waterFallSequence = (WaterFallSequence) sequence;
			int currentPosition = waterFallSequence.getSequencePanel().getOrigin().y + waterFallSequence.getSequencePanel().getVerticalBar().getSize().y;
			int currentTimestamp = currentPosition * waterFallSequence.pageTime / canvasHeight + waterFallSequence.pageTime * (waterFallSequence.currentPageNum - 1);

			outer: for (Result r : waterFallSequence.getResultList())
			{
				if (r.getSpteMsg().getTimeStamp() > currentTimestamp)
				{
					for (ErrorTypesEnum type : r.getErrorTypes())
					{
						if (fMonitorViewPart.getErrorTypeNext().equals(type.getName()))
						{
							while (waterFallSequence.currentPageNum <= waterFallSequence.pageSum)
							{
								boolean end = false;
								end = findNextMsg(waterFallSequence, currentTimestamp);
								if (!end)
								{
									waterFallSequence.currentPageNum++;
									if (waterFallSequence.currentPageNum == waterFallSequence.pageSum)
									{
										// 禁止next
										waterFallSequence.disableNext();
										// 将last标签禁止
										waterFallSequence.disableLast();
									}
									if (waterFallSequence.currentPageNum > 1)
									{
										waterFallSequence.enablePrevious();
										waterFallSequence.enableFirst();
									}
									waterFallSequence.repaint();
								}

							}
							break outer;
						}
					}

				}
			}

		}
	}

	/**
	 * 查找下一条消息
	 * 
	 * @param sequence
	 * @param currentTimestamp
	 * @return
	 */
	private boolean findNextMsg(WaterFallSequence sequence, int currentTimestamp)
	{
		SequenceContainerFgr fgr = sequence.msgContainerFgr;
		for (Object f : fgr.getChildren())
		{
			if (f instanceof ConnFgr)
			{
				ConnFgr conFgr = (ConnFgr) f;
				for (ErrorTypesEnum type : conFgr.getResult().getErrorTypes())
				{
					if (fMonitorViewPart.getErrorTypeNext().equals(type.getName()))
					{
						int tempTimestamp = (int) conFgr.getResult().getSpteMsg().getTimeStamp();
						if (tempTimestamp > currentTimestamp)
						{
							int tempPosition = tempTimestamp - (sequence.currentPageNum - 1) * sequence.nodeNumber * sequence.scale;
							int canvasHeight = sequence.getSequencePanel().getContent().getBounds().height;
							sequence.getSequencePanel().setOrigin(0, (tempPosition - sequence.scale) * canvasHeight / sequence.pageTime);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}