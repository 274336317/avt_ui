/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitorview.internal.NodeElementSet;
import com.coretek.spte.monitorview.views.MonitorDomainView;

/**
 * 配置监视视图所要的监听功能节点
 * 
 * @author Sim.Wang 2012-01-13
 * 
 */
public class CfgFunctionNodeDialog extends TitleAreaDialog
{

	private static class ViewContentProvider implements ITreeContentProvider
	{

		public void dispose()
		{

		}

		public Object[] getChildren(Object parentElement)
		{
			return null;
		}

		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				List<?> list = (List<?>) inputElement;
				if (list.size() != 0)
				{
					return list.toArray();
				}
			} else if (inputElement instanceof SPTEMsg[])
			{
				return (Object[]) inputElement;
			}
			return new Object[0];
		}

		public Object getParent(Object element)
		{
			return null;
		}

		public boolean hasChildren(Object element)
		{

			return false;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}

	/**
	 * @author 尹军 2012-3-14
	 */
	private static class ViewLabelProvider implements ILabelProvider
	{

		public void addListener(ILabelProviderListener listener)
		{

		}

		public void dispose()
		{

		}

		public Image getImage(Object obj)
		{

			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

		public String getText(Object element)
		{

			if (element instanceof SPTEMsg)
			{// 功能单元消息
				return ((SPTEMsg) element).getMsg().getName();

			}

			return "";
		}

		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		public void removeListener(ILabelProviderListener listener)
		{

		}
	}

	private CheckboxTreeViewer	checkTreeViewer;

	private MonitorDomainView	view;

	public CfgFunctionNodeDialog(Shell parentShell, MonitorDomainView view)
	{
		super(parentShell);
		this.view = view;
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("CfgFunctionNodeDialog_Setting_Field_For_Monitor"));
	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		this.setTitle(Messages.getString("CfgFunctionNodeDialog_Setting_Need_Field_For_Monitor"));
		return control;
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);

		Composite panel = new Composite(parent, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(layoutData);
		layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);

		checkTreeViewer = new CheckboxTreeViewer(panel);
		checkTreeViewer.setAutoExpandLevel(CheckboxTreeViewer.ALL_LEVELS);
		layoutData = new GridData();
		layoutData.widthHint = 480;
		layoutData.heightHint = 300;
		layoutData.horizontalSpan = 4;
		checkTreeViewer.getTree().setLayoutData(layoutData);
		checkTreeViewer.setLabelProvider(new ViewLabelProvider());
		checkTreeViewer.setContentProvider(new ViewContentProvider());

		checkTreeViewer.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent event)
			{
				checkTreeViewer.setSubtreeChecked(event.getElement(), event.getChecked());
			}
		});

		if (ExecutorSession.getInstance() != null && ExecutorSession.getInstance().getStatus() == ExecutorSession.LOADING)
		{
			checkTreeViewer.setInput(ExecutorSession.getInstance().getAllKindOfSPTEMsgs());
		} else if (ExecutorSession.getInstance() != null && (ExecutorSession.getInstance().getStatus() == ExecutorSession.EXECUTING || ExecutorSession.getInstance().getStatus() == ExecutorSession.MONITROING))
		{
			checkTreeViewer.setInput(ExecutorSession.getInstance().getCfgSPTEMsgs());
		}

		return parent;
	}

	@Override
	protected void okPressed()
	{
		view.getManager().clear();
		TreeItem items[] = checkTreeViewer.getTree().getItems();
		for (TreeItem item : items)
		{
			if (item.getData() instanceof SPTEMsg)
			{
				if (item.getChecked())
				{
					NodeElementSet cfb = new NodeElementSet(view.getManager());
					cfb.setMonitorMsgNode((SPTEMsg) item.getData());
					view.getManager().addField(cfb);
				}
			}
		}

		MonitorEventManager.getMonitorEventManager().addObserver(view);
		super.okPressed();
	}

}
