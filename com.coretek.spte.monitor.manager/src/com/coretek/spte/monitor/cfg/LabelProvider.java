package com.coretek.spte.monitor.cfg;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDFunctionCell;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionDomainMsg;
import com.coretek.common.template.ICDFunctionNode;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDFunctionSubDomain;
import com.coretek.common.template.ICDFunctionSubDomainMsg;

class LabelProvider implements ILabelProvider
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element)
	{

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
		if (element instanceof ICDFunctionDomain)
		{
			ICDFunctionDomain domain = (ICDFunctionDomain) element;

			return domain.getAttribute(Constants.ICD_FUNCTION_NAME).getValue().toString() + "(������)";
		} else if (element instanceof ICDFunctionSubDomain)
		{
			ICDFunctionSubDomain subDomain = (ICDFunctionSubDomain) element;

			return subDomain.getAttribute(Constants.ICD_FUNCTION_NAME).getValue().toString() + "(��������)";
		} else if (element instanceof ICDFunctionNode)
		{
			ICDFunctionNode node = (ICDFunctionNode) element;

			return node.getAttribute(Constants.ICD_FUNCTION_NAME).getValue().toString() + "(���ܽڵ�)";
		} else if (element instanceof ICDFunctionCell)
		{
			ICDFunctionCell cell = (ICDFunctionCell) element;

			return cell.getAttribute(Constants.ICD_FUNCTION_NAME).getValue().toString() + "(���ܵ�Ԫ)";
		} else if (element instanceof ICDFunctionDomainMsg)
		{
			ICDFunctionDomainMsg domainMsg = (ICDFunctionDomainMsg) element;

			return domainMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(��������Ϣ)";
		} else if (element instanceof ICDFunctionSubDomainMsg)
		{
			ICDFunctionSubDomainMsg subDomainMsg = (ICDFunctionSubDomainMsg) element;

			return subDomainMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(����������Ϣ)";
		} else if (element instanceof ICDFunctionNodeMsg)
		{
			ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) element;
			if ("BROADCAST".equals(nodeMsg.getAttribute("brocast").getValue()))
			{
				return nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(���ܽڵ�㲥��Ϣ)";
			} else if ("GROUP".equals(nodeMsg.getAttribute("brocast").getValue()))
			{
				return nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(���ܽڵ��鲥��Ϣ)";
			}
			return nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(���ܽڵ���Ϣ)";
		} else if (element instanceof ICDFunctionCellMsg)
		{
			ICDFunctionCellMsg cellMsg = (ICDFunctionCellMsg) element;

			return cellMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString() + "(���ܵ�Ԫ��Ϣ)";
		}

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener) <br/> <b>����</b> ���Ρ </br>
	 * <b>����</b> 2012-1-11
	 */
	@Override
	public void addListener(ILabelProviderListener listener)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
	 * .lang.Object, java.lang.String) 2012-1-11
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
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener)
	{

	}

}
