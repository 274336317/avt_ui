/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

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
 * @author 孙大巍 2012-4-6
 */
public class LastErrorDelegate implements IViewActionDelegate, IMenuCreator
{

	private SequenceViewPart	viewPart;

	private Menu				fMenu;

	public static final String	ID	= "com.coretek.spte.monitor.LastError";

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
		int canvasHeight = sequence.getSequencePanel().getContent().getBounds().height;
		if (sequence == null)
			return;
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
						if (type.getName().equals(viewPart.getErrorTypeLast()))
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
				for (ErrorTypesEnum type : conFgr.getResult().getErrorTypes())
				{
					if (viewPart.getErrorTypeLast().equals(type.getName()))
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
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <b>日期</b> 2012-4-6
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		action.setMenuCreator(this);
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
		NavigateAction action1 = new NavigateAction(viewPart, ErrorTypesEnum.WRONGVALUE.getName());
		NavigateAction action2 = new NavigateAction(viewPart, ErrorTypesEnum.TIMEOUT.getName());
		NavigateAction action3 = new NavigateAction(viewPart, ErrorTypesEnum.UNEXPECTED.getName());
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
