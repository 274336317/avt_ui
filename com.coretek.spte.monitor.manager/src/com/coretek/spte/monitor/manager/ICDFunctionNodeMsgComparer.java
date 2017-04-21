/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import org.eclipse.jface.viewers.IElementComparer;

import com.coretek.common.template.ICDFunctionNodeMsg;

/**
 * 功能节点消息比较，用于在监控配置读取时，选择监控配置文件中的功能节点消息
 * 
 * @author ZHANG Yi
 * @date 2012-07-24
 */
public class ICDFunctionNodeMsgComparer implements IElementComparer
{

	@Override
	public boolean equals(Object a, Object b)
	{
		if (a == null || b == null)
		{
			return false;
		}
		if (a == b)
		{
			return true;
		}
		if (a instanceof ICDFunctionNodeMsg && b instanceof ICDFunctionNodeMsg)
		{
			ICDFunctionNodeMsg a1 = (ICDFunctionNodeMsg) a;
			ICDFunctionNodeMsg b1 = (ICDFunctionNodeMsg) b;
			return a1.getAttributes().toString().equals(b1.getAttributes().toString());
		}
		return a.equals(b);
	}

	@Override
	public int hashCode(Object element)
	{
		if (element instanceof ICDFunctionNodeMsg)
		{
			ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) element;
			return nodeMsg.getAttributes().toString().hashCode();
		}
		return element.hashCode();
	}

}
