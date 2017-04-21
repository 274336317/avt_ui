/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

/**
 * @author ���Ρ
 * @date 2012-2-3
 */
public abstract class AbstractStatus
{

	public AbstractStatus(boolean disabled)
	{
		this.disabled = disabled;
	}

	private volatile boolean	disabled	= false;

	/**
	 * @param disabled the disabled to set <br/>
	 * 
	 */
	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

	/**
	 * @return the disabled <br/>
	 */
	public boolean isDisabled()
	{
		return disabled;
	}

}
