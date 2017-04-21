/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @author ���Ρ 2012-4-6
 */
public class SwitchToNoTimeSequenceActionDelegate implements IViewActionDelegate
{

	private SequenceViewPart	viewPart;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 * <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-6
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
	 * <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-6
	 */
	@Override
	public void run(IAction action)
	{
		Sequence sequence = viewPart.getSequence();
		if (sequence != null && sequence instanceof WaterFallSequence)
		{
			WaterFallSequence wfs = (WaterFallSequence) sequence;
			Sequence seq = SequenceBuilderImpl.getInstance().buildSequence(sequence.getResultList(), wfs.getCompareResult());
			seq.setResultList(sequence.getResultList(), sequence.getIcdManager(), sequence.getCaseManager(), sequence.getTestedObjects(), sequence.getTestCase());
			this.viewPart.showSequence(seq);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>����</b> ���Ρ </br>
	 * <b>����</b> 2012-4-6
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		// TODO Auto-generated method stub

	}

}
