/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

import org.eclipse.swt.events.MouseTrackListener;

/**
 * @author ���Ρ
 * @date 2012-2-3
 */
public abstract class AbstractMouseTrackListener extends AbstractStatus implements MouseTrackListener
{

	/**
	 * @param disabled </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-3
	 */
	public AbstractMouseTrackListener(boolean disabled)
	{
		super(disabled);
	}

}
