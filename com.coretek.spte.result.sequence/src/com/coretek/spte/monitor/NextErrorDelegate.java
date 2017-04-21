/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.SequenceContainerFgr;

/**
 * 下一条未符合预期期望的消息
 * 
 * @author 孙大巍 2012-4-6
 */
public class NextErrorDelegate implements IViewActionDelegate, IMenuCreator
{
	private SequenceViewPart	viewPart;

	private Menu				fMenu;

	public static final String	ID	= "com.coretek.spte.monitor.NextError";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	@Override
	public void init(IViewPart view)
	{
		this.viewPart = (SequenceViewPart) view;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action)
	{
		Sequence sequence = viewPart.getSequence();
		if (sequence == null)
			return;
		int canvasHeight = sequence.getSequencePanel().getContent().getBounds().height;
		if (sequence instanceof WaterFallSequence)
		{
			WaterFallSequence waterFallSequence = (WaterFallSequence) sequence;
			int currentPosition = waterFallSequence.getSequencePanel().getOrigin().y + waterFallSequence.getSequencePanel().getVerticalBar().getSize().y;
			int currentTimestamp = currentPosition * waterFallSequence.pageTime / canvasHeight + waterFallSequence.pageTime * (waterFallSequence.currentPageNum - 1);
			boolean finded = false;
			outer: for (Result r : waterFallSequence.getResultList())
			{
				if (r.getSpteMsg().getTimeStamp() > currentTimestamp)
				{
					for (ErrorTypesEnum type : r.getErrorTypes())
					{
						if (viewPart.getErrorTypeNext().equals(type.getName()))
						{
							finded = true;
							break outer;
						}
					}

				}
			}
			if (finded)
			{
				while (waterFallSequence.currentPageNum <= waterFallSequence.pageSum)
				{
					boolean end = false;
					end = findNextMsg(waterFallSequence, currentTimestamp);
					if (end)
					{
						return;
					}
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

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2012-4-6
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		action.setMenuCreator(this);

	}

	/**
	 * 在当前时序图中,根据当前位置找出下一条错误消息
	 * 
	 * @param sequence
	 * @param currentTimestamp
	 * @return </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-10
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
					if (viewPart.getErrorTypeNext().equals(type.getName()))
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

	@Override
	public void dispose()
	{

	}

	@Override
	public Menu getMenu(Control parent)
	{
		if (fMenu != null)
		{
			return fMenu;
		}
		fMenu = new Menu(parent);
		NextAction action1 = new NextAction(viewPart, ErrorTypesEnum.WRONGVALUE.getName());
		NextAction action2 = new NextAction(viewPart, ErrorTypesEnum.TIMEOUT.getName());
		NextAction action3 = new NextAction(viewPart, ErrorTypesEnum.UNEXPECTED.getName());
		addActionToMenu(fMenu, action1);
		addActionToMenu(fMenu, action2);
		addActionToMenu(fMenu, action3);
		return fMenu;
	}

	protected void addActionToMenu(Menu parent, IAction action)
	{
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	@Override
	public Menu getMenu(Menu parent)
	{

		return null;
	}

}