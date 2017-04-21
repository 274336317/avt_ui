/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

import org.eclipse.swt.events.MouseTrackListener;

/**
 * @author 孙大巍
 * @date 2012-2-3
 */
public abstract class AbstractMouseTrackListener extends AbstractStatus implements MouseTrackListener
{

	/**
	 * @param disabled </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-3
	 */
	public AbstractMouseTrackListener(boolean disabled)
	{
		super(disabled);
	}

}
