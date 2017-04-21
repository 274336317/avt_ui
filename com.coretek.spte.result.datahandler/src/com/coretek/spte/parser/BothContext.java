/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

import java.util.Observer;

/**
 * @author 孙大巍 2012-3-27
 */
public class BothContext extends ExecutionContext implements IBothContext
{
	public BothContext(Observer observer, boolean needCache)
	{
		super(observer, needCache);
	}

	@Override
	public void setCasePath(String casePath)
	{
		this.getMessageParser().setCasePath(casePath);
	}

}