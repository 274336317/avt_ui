/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

import org.eclipse.swt.events.MouseListener;

/**
 * @author ���Ρ
 * @date 2012-2-3
 */
public abstract class AbstractMouseListener extends AbstractStatus implements MouseListener
{

	/**
	 * @param disabled </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-3
	 */
	public AbstractMouseListener(boolean disabled)
	{
		super(disabled);
	}

}