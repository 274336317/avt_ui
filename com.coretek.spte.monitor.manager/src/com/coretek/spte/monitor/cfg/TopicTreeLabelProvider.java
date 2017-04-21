/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.coretek.common.template.Constants;
import com.coretek.common.template.build.codeTemplate.Entity;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
class TopicTreeLabelProvider implements ILabelProvider
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element)
	{
		if (element instanceof Entity)
		{
			Entity entity = (Entity) element;
			return entity.getFieldValue(Constants.ICD_TOPIC_NAME).toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose() <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-1-12
	 */
	@Override
	public void dispose()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
	 * .lang.Object, java.lang.String) 2012-1-12
	 */
	@Override
	public boolean isLabelProperty(Object element, String property)
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener) <b>日期</b> 2012-1-12
	 */
	@Override
	public void removeListener(ILabelProviderListener listener)
	{

	}

}
