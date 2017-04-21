package com.coretek.spte.core.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;

/**
 * 编辑普通消息对话框
 * 
 * @author duys 2011-6-14
 */

public class TRSendMsgDlg extends MsgDialog
{

	private SPTEMsg	selectedSpteMsg;

	public TRSendMsgDlg(Shell shell, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD)
	{
		super(shell, null, spteMsg, icdManager, testedObjectsOfICD);
		this.type = SPTEConstants.MESSAGE_TYPE_SEND;
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("VIEW_SEND_MSG"));
	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		setTitle(Messages.getString("VIEW_SEND_MSG"));
		if (hasLieError)
		{
			if (null != getButton(IDialogConstants.OK_ID))
			{
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
		return control;
	}

	protected Control createDialogArea(Composite parent)
	{
		Composite area = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		area.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 850;
		gd.heightHint = 450;
		area.setLayoutData(gd);

		createMessageAttrItems(area);
		createMessageInfoTabItems(area);
		return area;
	}

	@Override
	public int open()
	{
		return super.open();
	}

	private void createMessageAttrItems(Composite area)
	{
		Composite body = new Composite(area, SWT.NULL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.horizontalSpacing = 30;
		body.setLayout(gridLayout);
		GridData data = new GridData();
		data.widthHint = 750;
		body.setLayoutData(data);

		this.showMsgName(body, spteMsg);
		try
		{
			cloneMsg = (Message) spteMsg.getMsg().clone();
			selectedSpteMsg = new SPTEMsg(cloneMsg, spteMsg.getICDMsg());
		} catch (CloneNotSupportedException e1)
		{
			e1.printStackTrace();
			return;
		}

		this.showTopicName(body, selectedSpteMsg);

		this.showMsgID(body, selectedSpteMsg);

		this.showBrocast(body, selectedSpteMsg);

		this.showMsgSrc(body, selectedSpteMsg, icdManager, testedObjectsOfICD);

		this.showDestIDs(body, selectedSpteMsg, icdManager, testedObjectsOfICD);

		this.showTransPeriod(body, selectedSpteMsg);

		this.showTransDelay(body, selectedSpteMsg);

		this.showTransType(body, selectedSpteMsg);

	}

	/**
	 * 消息编辑tab
	 * 
	 * @author duys
	 * @date 2011-6-8
	 * @param area
	 */
	private void createMessageInfoTabItems(Composite area)
	{
		List<Entity> icdFieldList = cloneMsg.getChildren();
		if (cloneMsg.isPeriodMessage())
		{
			icdFieldList = cloneMsg.getChildren().get(0).getChildren();
		}
		getTabControl(area, icdFieldList);
	}

	/**
	 * 设置tabItem中的控件
	 * 
	 * @param tabFolder
	 * @param fields
	 * @author duys
	 * @date 2011-6-8
	 * @return
	 */
	private void getTabControl(Composite com, List<Entity> fields)
	{

		viewer = new TableTreeViewer(com, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);

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
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);
		new TableColumn(table, SWT.LEFT);

		TableColumn[] columns = table.getColumns();
		columns[0].setText(Messages.getString("I18N_FIELD_NAME"));
		columns[0].setWidth(150);
		if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
		{
			columns[1].setText(Messages.getString("I18N_SEND_VALUE"));
		} else
		{
			columns[1].setText(Messages.getString("I18N_EXPECT_VALUE"));
		}
		columns[1].setWidth(150);
		columns[2].setText(Messages.getString("I18N_BINARY"));
		columns[2].setWidth(100);
		columns[3].setText(Messages.getString("I18N_HEX"));
		columns[3].setWidth(100);
		columns[4].setText(Messages.getString("I18N_UNIT"));
		columns[4].setWidth(100);
		columns[5].setText(Messages.getString("I18N_START_WORD"));
		columns[5].setWidth(100);
		columns[6].setText(Messages.getString("I18N_START_BIT"));
		columns[6].setWidth(100);
		columns[7].setText(Messages.getString("I18N_WIDTH"));
		columns[7].setWidth(100);
		columns[8].setText("LSB");
		columns[8].setWidth(100);
		columns[9].setText(Messages.getString("I18N_TYPE"));
		columns[9].setWidth(100);
		columns[10].setText(Messages.getString("I18N_MAX_VALUE"));
		columns[10].setWidth(100);
		columns[11].setText(Messages.getString("I18N_MIN_VALUE"));
		columns[11].setWidth(100);

		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(fields);
		viewer.expandAll();

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

			Field field = (Field) element;

			ICDField icdField = TemplateUtils.getICDField(selectedSpteMsg, field);

			switch (columnIndex)
			{
				case 0:
				{// 名字
					return icdField.getAttribute(Constants.ICD_FIELD_NAME).getValue().toString();
				}
				case 1:
				{// 值
					if (null != field.getValue())
					{
						if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
						{// 枚举类型
							int index = Integer.parseInt(field.getValue());
							for (ICDEnum literal : icdField.getIcdEnums())
							{
								if (literal.getValue() == index)
								{
									return literal.getSymbol();
								}

							}
						} else
						{
							return field.getValue();
						}

					}
					return StringUtils.EMPTY_STRING;
				}
				case 2:
				{// 二进制

					String text = this.getColumnText(element, 1);
					if (text.equals(StringUtils.EMPTY_STRING))
					{
						return StringUtils.EMPTY_STRING;
					} else if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
					{
						int index = Integer.parseInt(field.getValue());
						for (ICDEnum literal : icdField.getIcdEnums())
						{

							if (literal.getValue() == index)
							{
								return Integer.toBinaryString(literal.getValue());
							}

						}
					} else if (StringUtils.isNumber(text))
					{
						return Integer.toBinaryString(Integer.valueOf(text));
					} else
					{
						return StringUtils.EMPTY_STRING;
					}
				}
				case 3:
				{// 十六进制
					String text = this.getColumnText(element, 1);
					if (text.equals(StringUtils.EMPTY_STRING))
					{
						return StringUtils.EMPTY_STRING;
					} else if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
					{
						int index = Integer.parseInt(field.getValue());
						for (ICDEnum literal : icdField.getIcdEnums())
						{

							if (literal.getValue() == index)
							{
								return "0x" + Integer.toHexString(literal.getValue());
							}

						}
					} else if (StringUtils.isNumber(text))
					{
						return "0x" + Integer.toHexString(Integer.valueOf(text));
					} else
					{
						return StringUtils.EMPTY_STRING;
					}

				}
				case 4:
				{// 单位
					if (null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE) && null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue() && null != icdField.getIcdUnit() && null != icdField.getIcdUnitType() && null != icdField.getIcdUnit().getDisplayName())
					{
						return icdField.getIcdUnit().getDisplayName();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 5:
				{// 起始字
					if (null != icdField.getAttribute(Constants.ICD_FIELD_START_WORD) && null != icdField.getAttribute(Constants.ICD_FIELD_START_WORD).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_WORD).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 6:
				{// 起始位
					if (null != icdField.getAttribute(Constants.ICD_FIELD_START_BIT) && null != icdField.getAttribute(Constants.ICD_FIELD_START_BIT).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_BIT).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 7:
				{// 宽度
					if (null != icdField.getAttribute(Constants.ICD_FIELD_LENGTH) && null != icdField.getAttribute(Constants.ICD_FIELD_LENGTH).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_LENGTH).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 8:
				{// LSB
					if (null != icdField.getAttribute(Constants.ICD_FIELD_LSB) && null != icdField.getAttribute(Constants.ICD_FIELD_LSB).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_LSB).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 9:
				{// 类型
					if (null != icdField.getAttribute(Constants.ICD_FIELD_TYPE) && null != icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 10:
				{// 最大值
					if (null != icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE) && null != icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 11:
				{// 最小值
					if (null != icdField.getAttribute(Constants.ICD_FIELD_MIN_VALUE) && null != icdField.getAttribute(Constants.ICD_FIELD_MIN_VALUE).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_MIN_VALUE).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 12:
				{
					return StringUtils.EMPTY_STRING;
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

	private static class ContentProvider implements ITreeContentProvider
	{

		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				List<Field> sections = (List<Field>) inputElement;
				return sections.toArray();
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
			if (parentElement instanceof Field)
			{
				if (((Field) parentElement).getChildren().size() == 0)
				{
					return new Object[0];
				} else
				{
					return ((Field) parentElement).getChildren().toArray();
				}
			}

			return new Object[0];
		}

		public Object getParent(Object element)
		{
			if (element instanceof Field)
			{
				if (null != ((Field) element).getParent())
				{
					if (((Field) element).getParent() instanceof Field)
					{
						return ((Field) element).getParent();
					}
				}
			}
			return null;
		}

		public boolean hasChildren(Object element)
		{
			if (element instanceof Field)
			{
				if (((Field) element).getChildren().size() != 0)
				{
					return true;
				}
			}
			return false;
		}
	}
}
