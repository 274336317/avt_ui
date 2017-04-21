/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor;

import java.util.List;

import org.eclipse.jface.action.Action;

import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.SequenceContainerFgr;

/**
 * 错误导航
 * 
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public class NavigateAction extends Action
{

	private SequenceViewPart	fMonitorViewPart;

	private Sequence			sequence;
	private String				type;

	public NavigateAction(SequenceViewPart view, String type)
	{
		this.fMonitorViewPart = view;
		sequence = fMonitorViewPart.getSequence();
		this.type = type;
		setText(new StringBuilder("上一条").append(fMonitorViewPart.enumType2Str(this.type)).append("消息").toString());
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
			int currentPosition = waterFallSequence.getSequencePanel().getOrigin().y;
			int currentTimestamp = currentPosition * waterFallSequence.pageTime / canvasHeight + waterFallSequence.pageTime * (waterFallSequence.currentPageNum - 1);
			boolean finded = false;
			List<Result> list = waterFallSequence.getResultList();
			outer: for (int i = list.size() - 1; i >= 0; i--)
			{
				Result r = list.get(i);
				if (r.getSpteMsg().getTimeStamp() < currentTimestamp)
				{
					for (ErrorTypesEnum type : r.getErrorTypes())
					{
						if (type.getName().equals(fMonitorViewPart.getErrorTypeLast()))
						{
							finded = true;
							break outer;
						}
					}

				}
			}
			if (finded)
			{
				while (waterFallSequence.currentPageNum >= 1)
				{
					boolean end = false;
					end = findLastMsg(waterFallSequence, currentTimestamp);
					if (end)
					{
						return;
					}
					waterFallSequence.currentPageNum--;
					if (waterFallSequence.currentPageNum == 1)
					{
						waterFallSequence.disablePrevious();
						waterFallSequence.disableFirst();
					}
					if (waterFallSequence.currentPageNum < waterFallSequence.pageSum)
					{
						waterFallSequence.enableLast();
						waterFallSequence.enableNext();
					}
					waterFallSequence.repaint();
				}
			}

		}

	}

	/**
	 * 在当前时序图中,根据当前位置找出上一条错误消息
	 * 
	 * @param sequence
	 * @param currentTimestamp
	 * @return
	 * @author zhangyi@tech.coretek.com.cn
	 */

	private boolean findLastMsg(WaterFallSequence sequence, int currentTimestamp)
	{
		SequenceContainerFgr fgr = sequence.msgContainerFgr;
		List<?> obj = fgr.getChildren();
		for (int i = obj.size() - 1; i >= 0; i--)
		{
			Object f = obj.get(i);
			if (f instanceof ConnFgr)
			{
				ConnFgr conFgr = (ConnFgr) f;
				if (conFgr.getResult().getErrorTypes().size() != 0)
				{
					for (ErrorTypesEnum type : conFgr.getResult().getErrorTypes())
					{
						if (fMonitorViewPart.getErrorTypeLast().equals(type.getName()))
						{
							int tempTimestamp = (int) conFgr.getResult().getSpteMsg().getTimeStamp();
							if (tempTimestamp < currentTimestamp)
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
		}
		return false;
	}

}
