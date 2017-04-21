/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ICDField;
import com.coretek.testcase.curve.internal.model.FieldElementSet;
import com.coretek.testcase.curve.views.CurveView;

/**
 * 配置监控消息信号的属性
 * 
 * @author 尹军 2012-3-14
 */
public class ConfigFieldsPropertyDialog extends TitleAreaDialog
{
	// 信号名称
	public static final String		ATTR_FIELD_NAME	= "name";

	// 信号连线类型
	public static final String		ATTR_LINE_TYPE	= "type";

	// 信号最大值
	public static final String		ATTR_MAX_VALUE	= "max";

	// 信号最小值
	public static final String		ATTR_MIN_VALUE	= "min";

	// 连线类型属性
	public static final String[]	LINE_PROPERTIES	= { "方波线", "直连线" };

	// 信号属性
	public static final String[]	PROPERTIES		= { ATTR_FIELD_NAME, ATTR_MIN_VALUE, ATTR_MAX_VALUE, ATTR_LINE_TYPE };

	// 曲线视图
	private CurveView				curveView;

	// 表空间
	private Table					table;

	// 表视图
	private TableViewer				tableViewer;

	/**
	 * @param parentShell
	 * @param curveView
	 */
	public ConfigFieldsPropertyDialog(Shell parentShell, CurveView curveView)
	{
		super(parentShell);
		this.curveView = curveView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("ConfigFieldsPropertyDialog_Setting_Field_Property_For_Monitor"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createContents(org.eclipse.
	 * swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		this.setTitle(Messages.getString("ConfigFieldsPropertyDialog_Setting_Need_Field_Property_For_Monitor"));
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
	 * .swt.widgets.Composite
	 */
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

		Label label = new Label(panel, SWT.NONE);
		label.setText(Messages.getString("ConfigFieldsPropertyDialog_Config_Field_Property"));
		label.setAlignment(SWT.LEFT);
		layoutData = new GridData();
		layoutData.widthHint = 100;
		label.setLayoutData(layoutData);

		table = new Table(panel, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn callNameColumn = new TableColumn(table, SWT.LEFT, 0);
		callNameColumn.setText(Messages.getString("ConfigFieldsPropertyDialog_Field_Name"));

		final TableColumn callerColumn = new TableColumn(table, SWT.LEFT, 1);
		callerColumn.setText(Messages.getString("ConfigFieldsPropertyDialog_MinValue"));

		final TableColumn typeColumn = new TableColumn(table, SWT.LEFT, 2);
		typeColumn.setText(Messages.getString("ConfigFieldsPropertyDialog_MaxValue"));

		final TableColumn expectColumn = new TableColumn(table, SWT.LEFT, 3);
		expectColumn.setText(Messages.getString("ConfigFieldsPropertyDialog_LineType"));

		tableViewer = new TableViewer(table);

		this.tableViewer.setContentProvider(new FieldTableContentProvider());
		this.tableViewer.setLabelProvider(new FieldTableLabelProvider());

		tableViewer.setInput(curveView.getManager().getAllFields());

		new TableEditor(table);
		CellEditor[] editors = new CellEditor[PROPERTIES.length];
		editors[0] = null;
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);
		ComboBoxCellEditor modeEditor = new ComboBoxCellEditor(table, LINE_PROPERTIES, SWT.READ_ONLY);
		editors[3] = modeEditor;

		tableViewer.setColumnProperties(PROPERTIES);
		FieldElementTableCellModifier cellModifer = new FieldElementTableCellModifier(tableViewer);
		tableViewer.setCellModifier(cellModifer);
		tableViewer.setCellEditors(editors);

		for (int i = 0; i < table.getColumnCount(); i++)
		{
			table.getColumn(i).setAlignment(SWT.CENTER);
			table.getColumn(i).setWidth(100);
		}

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		return panel;
	}

	private static class FieldTableContentProvider implements IStructuredContentProvider
	{

		public void dispose()
		{

		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				List<FieldElementSet> list = (List<FieldElementSet>) inputElement;
				return list.toArray(new FieldElementSet[list.size()]);
			}
			return (FieldElementSet[]) inputElement;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}

	private static class FieldTableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{

			if (element instanceof FieldElementSet)
			{
				FieldElementSet result1 = (FieldElementSet) element;

				ICDField result = result1.getField();

				switch (columnIndex)
				{

					// 信号名称
					case 0:
					{
						return result.getAttribute("signalName").getValue().toString();
					}

						// 最小值
					case 1:
					{
						return Integer.toString(result1.getMinValue());
					}

						// 最大值
					case 2:
					{
						return Integer.toString(result1.getMaxValue());
					}

						// 连线类型
					case 3:
					{
						if (result1.getLineType() == 0)
						{// 方波线
							return LINE_PROPERTIES[0];
						} else
						{ // 直连线
							return LINE_PROPERTIES[1];
						}
					}
				}
			}

			return "";
		}
	}

}