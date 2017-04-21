/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
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

import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDFunctionCell;
import com.coretek.common.template.ICDFunctionNode;
import com.coretek.common.template.ICDFunctionSubDomainMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.testcase.curve.internal.model.ColorFactory;
import com.coretek.testcase.curve.internal.model.FieldElementSet;
import com.coretek.testcase.curve.views.CurveView;

import com.coretek.common.i18n.messages.Messages;

/**
 * 配置监控消息对话框
 * 
 * @author 尹军 2012-3-14
 */
public class ConfigFieldsDialog extends TitleAreaDialog
{
	// 监控消息树视图
	private CheckboxTreeViewer	ctv;

	// 曲线监控视图
	private CurveView			curveView;

	public ConfigFieldsDialog(Shell parentShell, CurveView curveView)
	{
		super(parentShell);
		this.curveView = curveView;
	}

	@Override
	protected void buttonPressed(int buttonId)
	{
		if (Window.CANCEL == buttonId)
		{
			super.buttonPressed(buttonId);
			return;
		}
		curveView.getManager().clear();
		TreeItem items[] = ctv.getTree().getItems();
		for (TreeItem item : items)
		{
			TreeItem items2[] = item.getItems();
			for (TreeItem item2 : items2)
			{
				if (item2.getData() instanceof ICDField)
				{
					Object obj = item2.getData();
					if (obj instanceof ICDField && item2.getChecked())
					{
						ICDField field = (ICDField) obj;
						int currentLen = curveView.getManager().getAllFields().size();
						int fieldElementSetLength = curveView.getManager().getFieldElementSetLength();
						if (currentLen < fieldElementSetLength)
						{
							FieldElementSet cfb = new FieldElementSet(curveView.getManager());
							cfb.setField(field);
							cfb.setMonitorMsgNode((SPTEMsg) item.getData());
							cfb.setColor(ColorFactory.getColor());
							cfb.setMaxValue(10000);
							cfb.setMinValue(0);
							cfb.setLineType(0);
							curveView.getManager().addField(cfb);
						}
					}
				}
			}
		}

		MonitorEventManager.getMonitorEventManager().addObserver(curveView.getObservable());
		curveView.getObservable().addObserver(MonitorEventManager.getMonitorEventManager());
		super.buttonPressed(buttonId);
	}

	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("ConfigFieldsDialog_Setting_Field_For_Monitor"));
	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		this.setTitle(Messages.getString("ConfigFieldsDialog_Setting_Need_Field_For_Monitor"));
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
		layout.numColumns = 4;
		panel.setLayout(layout);

		ctv = new CheckboxTreeViewer(panel);
		ctv.setAutoExpandLevel(CheckboxTreeViewer.ALL_LEVELS);
		layoutData = new GridData();
		layoutData.widthHint = 480;
		layoutData.heightHint = 300;
		layoutData.horizontalSpan = 4;
		ctv.getTree().setLayoutData(layoutData);
		ctv.setLabelProvider(new ViewLabelProvider());
		ctv.setContentProvider(new ViewContentProvider());

		ctv.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent event)
			{
				ctv.setSubtreeChecked(event.getElement(), event.getChecked());
			}
		});

		if (ExecutorSession.getInstance() != null && ExecutorSession.getInstance().getStatus() == ExecutorSession.LOADING)
		{
			ctv.setInput(ExecutorSession.getInstance().getAllKindOfSPTEMsgs());
		} else if (ExecutorSession.getInstance() != null && (ExecutorSession.getInstance().getStatus() == ExecutorSession.EXECUTING || ExecutorSession.getInstance().getStatus() == ExecutorSession.MONITROING))
		{
			ctv.setInput(ExecutorSession.getInstance().getCfgSPTEMsgs());
		}

		return panel;
	}

	/**
	 * @author 尹军 2012-3-14
	 */
	private static class ViewContentProvider implements ITreeContentProvider
	{

		public void dispose()
		{

		}

		public Object[] getChildren(Object parentElement)
		{
			if (parentElement instanceof SPTEMsg)
			{
				List<ICDField> helpers = new ArrayList<ICDField>();
				helpers.addAll(((SPTEMsg) parentElement).getICDMsg().getFields());
				return helpers.toArray();
			} else if (parentElement instanceof List<?>)
			{
				if (((List<?>) parentElement).size() != 0)
				{
					return ((List<?>) parentElement).toArray();
				}
			}
			return new Object[0];

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
			if (element instanceof ICDField)
			{
				return true;
			} else if (element instanceof List<?>)
			{
				if (((List<?>) element).size() != 0)
				{
					if (((List<?>) element).get(0) instanceof ICDFunctionSubDomainMsg)
					{// 功能子域消息
						((ICDFunctionSubDomainMsg) ((List<?>) element).get(0)).getParent();
					} else if (((List<?>) element).get(0) instanceof ICDFunctionCell)
					{// 功能单元
						((ICDFunctionCell) ((List<?>) element).get(0)).getParent();
					} else if (((List<?>) element).get(0) instanceof ICDFunctionNode)
					{// 功能节点
						((ICDFunctionNode) ((List<?>) element).get(0)).getParent();
					}
				}
			}
			return null;
		}

		public boolean hasChildren(Object element)
		{
			if (element instanceof SPTEMsg)
			{
				return true;
			} else if (element instanceof List<?>)
			{
				if (((List<?>) element).size() != 0)
				{
					return true;
				}
			}
			return false;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}

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
				return ((SPTEMsg) element).getICDMsg().getAttribute("msgName").getValue().toString();
			} else if (element instanceof ICDField)
			{// 信号
				return ((ICDField) element).getAttribute("signalName").getValue().toString() + "(信号)";
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
}