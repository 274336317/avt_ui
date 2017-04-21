package com.coretek.spte.core.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.template.Attribute;
import com.coretek.common.template.Helper;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.Constants;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.Field;

public class MessagePropertyView extends ViewPart
{

	private TableTreeViewer		viewer;

	private Helper				helper;

	public static final String	MESSAGE_PROPERTY_VIEW_ID	= "com.coretek.tools.sequence.MessagePropertyView";

	public MessagePropertyView()
	{
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void createPartControl(Composite parent)
	{

		viewer = new TableTreeViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);

		GridLayout layout = new GridLayout();

		GridData gd = new GridData(GridData.FILL_BOTH);

		viewer.getTableTree().setLayout(layout);
		viewer.getTableTree().setLayoutData(gd);

		Table table = viewer.getTableTree().getTable();
		new TableEditor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData gd1 = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd1);

		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);

		TableColumn[] columns = table.getColumns();
		columns[0].setText("属性名");
		columns[0].setWidth(150);
		columns[1].setText("属性值");
		columns[1].setWidth(150);

		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider());

	}

	@Override
	public void setFocus()
	{
		// TODO Auto-generated method stub

	}

	public void setInput(Helper helper)
	{
		this.helper = helper;
		this.viewer.setInput(helper);
		this.viewer.refresh();
	}

	private static class ContentProvider implements ITreeContentProvider
	{

		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof Helper)
			{
				List<Object> eles = new ArrayList<Object>();
				eles.addAll(((Helper) inputElement).getAttributes());
				if (inputElement instanceof ICDFunctionNodeMsg)
				{
					eles.add(((ICDFunctionNodeMsg) inputElement).getSpteMsg().getICDMsg());
					eles.add(((ICDFunctionNodeMsg) inputElement).getSpteMsg().getICDMsg().getFields());
				} else if (inputElement instanceof ICDFunctionCellMsg)
				{
					eles.add(((ICDFunctionCellMsg) inputElement).getSpteMsg().getICDMsg());
					eles.add(((ICDFunctionCellMsg) inputElement).getSpteMsg().getICDMsg().getFields());
				}
				return eles.toArray();
			}

			return new Object[0];
		}

		public void dispose()
		{

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

		public Object[] getChildren(Object parentElement)
		{

			if (parentElement instanceof Attribute)
			{
				return new Object[0];
			} else if (parentElement instanceof ICDMsg)
			{
				return ((ICDMsg) parentElement).getAttributes().toArray();
			} else if (parentElement instanceof ICDField)
			{
				return ((ICDField) parentElement).getAttributes().toArray();
			} else if (parentElement instanceof List<?>)
			{
				return ((List) parentElement).toArray();
			}

			return new Object[0];
		}

		public Object getParent(Object element)
		{

			return null;
		}

		public boolean hasChildren(Object element)
		{

			if (element instanceof ICDMsg)
			{
				if (((ICDMsg) element).getAttributes().size() > 0)
				{
					return true;
				}
			} else if (element instanceof ICDField)
			{
				if (((ICDField) element).getAttributes().size() > 0)
				{
					return true;
				}
			} else if (element instanceof List<?>)
			{
				return true;
			}
			return false;
		}
	}

	private class LabelProvider implements ITableLabelProvider
	{

		public LabelProvider()
		{

		}

		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof Attribute)
			{
				Attribute att = (Attribute) element;
				switch (columnIndex)
				{
					case 0:
					{// 属性 名字
						if (null != att)
						{
							return att.getXmlName();
						}
						return "";
					}
					case 1:
					{// 属性值
						if (null != att && null != att.getValue())
						{
							return att.getValue().toString();
						}
						return "";
					}

				}
			} else if (element instanceof ICDMsg)
			{
				ICDMsg att = (ICDMsg) element;
				switch (columnIndex)
				{
					case 0:
					{// 属性 名字
						return "消息主题属性";
					}
					case 1:
					{// 属性值
						return "";
					}
				}
			} else if (element instanceof List<?>)
			{
				switch (columnIndex)
				{
					case 0:
					{// 属性 名字
						return "包含信号";
					}
					case 1:
					{// 属性值
						return "";
					}
				}
			} else if (element instanceof ICDField)
			{
				ICDField att = (ICDField) element;
				switch (columnIndex)
				{
					case 0:
					{// 属性 名字
						return "信号";
					}
					case 1:
					{// 属性值
						return "";
					}
				}
			}

			return null;
		}

		public void addListener(ILabelProviderListener listener)
		{

		}

		public void dispose()
		{

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
