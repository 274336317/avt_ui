/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.manager;

import org.eclipse.jface.viewers.IElementComparer;

import com.coretek.spte.Topic;

/**
 * 
 * @author ZHANG Yi
 * @date 2012-07-24
 */
public class TopicComparer implements IElementComparer
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
		if (a instanceof Topic && b instanceof Topic)
		{
			Topic a1 = (Topic) a;
			Topic b1 = (Topic) b;
			return a1.toString().equals(b1.toString());
		}
		return a.equals(b);
	}

	@Override
	public int hashCode(Object element)
	{
		if (element instanceof Topic)
		{
			return element.toString().hashCode();
		}
		return element.hashCode();
	}

}
