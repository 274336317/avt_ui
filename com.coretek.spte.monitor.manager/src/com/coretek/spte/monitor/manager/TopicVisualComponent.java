/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDFunctionCell;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionDomainMsg;
import com.coretek.common.template.ICDFunctionNode;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDFunctionSubDomain;
import com.coretek.common.template.ICDFunctionSubDomainMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;

/**
 * 显示主题的可视化组件
 * 
 * @author 孙大巍 2012-4-10
 */
public class TopicVisualComponent
{
	private Composite			parent;

	private CheckboxTreeViewer	topicViewer;

	public TopicVisualComponent(Composite parent)
	{
		this.parent = parent;
	}

	/**
	 * 展示可视化的内容
	 * 
	 * @param topics 设置组件的展示内容
	 */
	public void showContents(List<Entity> topics)
	{
		topicViewer = new CheckboxTreeViewer(this.parent, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.widthHint = 370;
		gridData.heightHint = 300;
		topicViewer.getTree().setLayoutData(gridData);
		topicViewer.setContentProvider(new ContentProvider());
		topicViewer.setLabelProvider(new TopicTreeLabelProvider());
		this.updateInput(topics);
	}

	/**
	 * 设置被选中的主题
	 * 
	 * @param topics
	 */
	public void check(List<Entity> topics)
	{
		for (Entity topic : topics)
		{
			this.topicViewer.setChecked(topic, true);
		}

	}

	/**
	 * 设置组件的展示内容
	 * 
	 * @param topics
	 */
	public void updateInput(List<Entity> topics)
	{
		if (this.topicViewer == null)
		{
			this.showContents(topics);
		}
		this.topicViewer.setInput(topics);
		topicViewer.refresh();
		topicViewer.expandAll();

	}

	/**
	 * 获取选择的消息主题
	 * 
	 * @return
	 */
	public List<SPTEMsg> getSelectedTopics()
	{
		Object[] elements = this.topicViewer.getCheckedElements();
		List<SPTEMsg> msgs = new ArrayList<SPTEMsg>();
		for (Object element : elements)
		{
			msgs.add((SPTEMsg) element);
		}

		return msgs;
	}

	public void addCheckStateListener(ICheckStateListener listener)
	{
		this.topicViewer.addCheckStateListener(listener);
	}

	private static class ContentProvider implements ITreeContentProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang
		 * .Object) <br/>
		 */
		@Override
		public Object[] getChildren(Object parentElement)
		{
			if (parentElement instanceof ICDFunctionDomain)
			{
				List<Object> objs = new ArrayList<Object>();
				objs.addAll(((ICDFunctionDomain) parentElement).getDomainMsg());
				objs.addAll(((ICDFunctionDomain) parentElement).getSubDomains());

				return objs.toArray();
			}
			if (parentElement instanceof ICDFunctionDomainMsg)
			{
				ICDFunctionDomainMsg domainMsg = (ICDFunctionDomainMsg) parentElement;
				List<Object> objs = new ArrayList<Object>();
				objs.addAll(domainMsg.getCellMsgs());
				objs.addAll(domainMsg.getNodeMsgs());

				return objs.toArray();
			}
			if (parentElement instanceof ICDFunctionSubDomain)
			{
				List<Object> objs = new ArrayList<Object>();
				objs.addAll(((ICDFunctionSubDomain) parentElement).getSubDomainMsgs());
				objs.addAll(((ICDFunctionSubDomain) parentElement).getCells());
				objs.addAll(((ICDFunctionSubDomain) parentElement).getNodes());

				return objs.toArray();
			}
			if (parentElement instanceof ICDFunctionSubDomainMsg)
			{
				List<Object> objs = new ArrayList<Object>();
				ICDFunctionSubDomainMsg subMsg = (ICDFunctionSubDomainMsg) parentElement;
				objs.addAll(subMsg.getCellMsgs());
				objs.addAll(subMsg.getNodeMsgs());

				return objs.toArray();
			}

			if (parentElement instanceof ICDFunctionNode)
			{

				return ((ICDFunctionNode) parentElement).getNodeMsgs().toArray();
			}
			if (parentElement instanceof ICDFunctionCell)
			{

				return ((ICDFunctionCell) parentElement).getCellMsgs().toArray();
			}

			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang
		 * .Object) <br/>
		 */
		@Override
		public Object getParent(Object element)
		{
			if (element instanceof ICDFunctionSubDomain)
			{

				return ((ICDFunctionSubDomain) element).getParent();
			} else if (element instanceof ICDFunctionNode)
			{

				return ((ICDFunctionNode) element).getParent();
			} else if (element instanceof ICDFunctionCell)
			{

				return ((ICDFunctionCell) element).getParent();
			} else if (element instanceof ICDFunctionNodeMsg)
			{
				ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) element;

				return (nodeMsg.getParentDomain() != null ? nodeMsg.getParentDomain() : (nodeMsg.getParentNode() != null ? nodeMsg.getParentNode() : (nodeMsg.getParentSubDomain() != null ? nodeMsg.getParentSubDomain() : null)));
			} else if (element instanceof ICDFunctionDomainMsg)
			{

				return ((ICDFunctionDomainMsg) element).getParent();
			} else if (element instanceof ICDFunctionSubDomainMsg)
			{

				return ((ICDFunctionSubDomainMsg) element).getParent();
			} else if (element instanceof ICDFunctionCellMsg)
			{

				ICDFunctionCellMsg nodeMsg = (ICDFunctionCellMsg) element;

				return (nodeMsg.getParentDomain() != null ? nodeMsg.getParentDomain() : (nodeMsg.getParentCell() != null ? nodeMsg.getParentCell() : (nodeMsg.getParentSubDomain() != null ? nodeMsg.getParentSubDomain() : null)));
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang
		 * .Object) <br/>
		 */
		@Override
		public boolean hasChildren(Object element)
		{
			if (element instanceof ICDFunctionDomain)
			{
				ICDFunctionDomain domain = (ICDFunctionDomain) element;

				return (domain.getDomainMsg().size() > 0 || domain.getSubDomains().size() > 0);
			} else if (element instanceof ICDFunctionSubDomain)
			{
				ICDFunctionSubDomain subDomain = (ICDFunctionSubDomain) element;

				return (subDomain.getNodes().size() > 0 || subDomain.getSubDomainMsgs().size() > 0);
			} else if (element instanceof ICDFunctionNode)
			{
				ICDFunctionNode node = (ICDFunctionNode) element;

				return (node.getNodeMsgs().size() > 0);
			} else if (element instanceof ICDFunctionCell)
			{
				ICDFunctionCell cell = (ICDFunctionCell) element;

				return (cell.getCellMsgs().size() > 0);
			} else if (element instanceof ICDFunctionSubDomainMsg)
			{
				ICDFunctionSubDomainMsg subMsg = (ICDFunctionSubDomainMsg) element;

				return (subMsg.getNodeMsgs().size() > 0 || subMsg.getCellMsgs().size() > 0);
			} else if (element instanceof ICDFunctionDomainMsg)
			{
				ICDFunctionDomainMsg domainMsg = (ICDFunctionDomainMsg) element;

				return (domainMsg.getCellMsgs().size() > 0 || domainMsg.getNodeMsgs().size() > 0);
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		@Override
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List)
			{
				List list = (List) inputElement;
				return list.toArray();
			}
			return new Object[] { inputElement };
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose() <br/>
		 */
		@Override
		public void dispose()
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object) <br/>
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

	}

	private static class TopicTreeLabelProvider implements ILabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
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
		 * @see
		 * org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
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
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
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
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
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
		 * .jface.viewers.ILabelProviderListener)
		 */
		@Override
		public void removeListener(ILabelProviderListener listener)
		{

		}

	}
}