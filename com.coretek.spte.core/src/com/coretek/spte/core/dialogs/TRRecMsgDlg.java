package com.coretek.spte.core.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.ExpResolver;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;

/**
 * 编辑普通消息对话框
 * 
 * @author duys 2011-6-14
 */
public class TRRecMsgDlg extends MsgDialog
{

	private Message			cloneMsg;

	private List<SPTEMsg>	spteMsgsFrommDB	= new ArrayList<SPTEMsg>();

	private SPTEMsg			expectedMsg;

	private SPTEMsg			selectedSpteMsg;

	public TRRecMsgDlg(Shell shell, Result result, SPTEMsg expectedMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD)
	{
		super(shell, result, expectedMsg, icdManager, testedObjectsOfICD);
		this.expectedMsg = result.getSpteMsg();
		this.spteMsgsFrommDB.add(this.expectedMsg);
	}

	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("VIEW_RECV_MSG"));

	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		setTitle(Messages.getString("I18N_CHECK_MSG_RESULT"));
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
		if (this.spteMsgsFrommDB.size() != 0)
		{
			expectedMsg = this.spteMsgsFrommDB.get(0);
		}
		return super.open();
	}

	private void makeDescriptionPanel(Composite area)
	{
		Composite body2 = new Composite(area, SWT.NULL);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		gridLayout2.horizontalSpacing = 30;
		body2.setLayout(gridLayout2);

		Label label = new Label(body2, SWT.RIGHT);
		label.setText(Messages.getString("I18N_DESCRIBE") + ":");
		GridData data = new GridData();
		data.widthHint = 60;
		label.setLayoutData(data);

		Text text = new Text(body2, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.widthHint = 550;
		text.setLayoutData(data);
		String des = StringUtils.EMPTY_STRING;
		if (StringUtils.isNotNull(this.result.getDipicts()))
		{
			des = this.result.getDipicts();
		} else
		{
			des = StringUtils.EMPTY_STRING;
		}

		text.setText(des);
		text.setForeground(ColorConstants.red);
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
			this.showTopicName(body, selectedSpteMsg);

			this.showMsgID(body, selectedSpteMsg);

			this.showBrocast(body, selectedSpteMsg);

			this.showMsgSrc(body, selectedSpteMsg, icdManager, testedObjectsOfICD);

			this.showDestIDs(body, selectedSpteMsg, icdManager, testedObjectsOfICD);

			this.showTransPeriod(body, selectedSpteMsg);

			this.showTransDelay(body, selectedSpteMsg);

			this.showTransType(body, selectedSpteMsg);

			this.makeDescriptionPanel(area);
		} catch (CloneNotSupportedException e1)
		{
			e1.printStackTrace();

		}

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
		new TableColumn(table, SWT.LEFT);

		TableColumn[] columns = table.getColumns();
		columns[0].setText(Messages.getString("I18N_FIELD_NAME"));
		columns[0].setWidth(150);
		columns[1].setText(Messages.getString("I18N_EXPECT_VALUE"));
		columns[1].setWidth(150);
		columns[2].setText(Messages.getString("I18N_ACTUAL_VALUE"));
		columns[2].setWidth(150);
		columns[3].setText(Messages.getString("I18N_BINARY"));
		columns[3].setWidth(100);
		columns[4].setText(Messages.getString("I18N_HEX"));
		columns[4].setWidth(100);
		columns[5].setText(Messages.getString("I18N_UNIT"));
		columns[5].setWidth(100);
		columns[6].setText(Messages.getString("I18N_START_WORD"));
		columns[6].setWidth(100);
		columns[7].setText(Messages.getString("I18N_START_BIT"));
		columns[7].setWidth(100);
		columns[8].setText(Messages.getString("I18N_WIDTH"));
		columns[8].setWidth(100);
		columns[9].setText("LSB");
		columns[9].setWidth(100);
		columns[10].setText(Messages.getString("I18N_TYPE"));
		columns[10].setWidth(100);
		columns[11].setText(Messages.getString("I18N_MAX_VALUE"));
		columns[11].setWidth(100);
		columns[12].setText(Messages.getString("I18N_MIN_VALUE"));
		columns[12].setWidth(100);

		TableItem items[] = this.viewer.getTableTree().getTable().getItems();
		for (TableItem item : items)
		{
			if (!item.getText(1).equals(item.getText(2)) && !item.getText(1).equals("TIME_TAG") && (!StringUtils.EMPTY_STRING.equals(item.getText(2))))
			{

				item.setForeground(ColorConstants.red);
			}
		}

		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider(this.selectedSpteMsg));
		viewer.setInput(fields);
		viewer.expandAll();
	}

	private class LabelProvider implements ITableLabelProvider
	{

		public LabelProvider(final SPTEMsg spteMsg)
		{

		}

		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{
			Field field = (Field) element;

			ICDField icdField = TemplateUtils.getICDField(spteMsg, field);

			switch (columnIndex)
			{
				case 0:
				{// 名字
					return icdField.getAttribute(Constants.ICD_FIELD_NAME).getValue().toString();
				}
				case 1:
				{// 期望值
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
				{// 实际值
					if (null != expectedMsg)
					{
						for (Entity entity : expectedMsg.getMsg().getChildren())
						{
							if (entity instanceof Field)
							{
								if (((Field) entity).getId().equals(field.getId()))
								{
									if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
									{// 枚举类型
										int index = Integer.parseInt(((Field) entity).getValue());
										for (ICDEnum literal : icdField.getIcdEnums())
										{
											if (literal.getValue() == index)
											{
												return literal.getSymbol();
											}

										}
									} else
									{
										return ((Field) entity).getValue();
									}

								}
							}
						}
					}

					return StringUtils.EMPTY_STRING;
				}
				case 3:
				{// 二进制
					TableItem items[] = viewer.getTableTree().getTable().getItems();
					for (TableItem item : items)
					{
						String exceValue = item.getText(1);
						if (StringUtils.isPositiveInteger(exceValue) && (!StringUtils.EMPTY_STRING.equals(item.getText(2))))
						{
							if (!exceValue.equals(item.getText(2)))
							{

								item.setForeground(ColorConstants.red);
							}
						} else if (StringUtils.isPeriodValueRight(exceValue) && (!StringUtils.EMPTY_STRING.equals(item.getText(2))))
						{
							String actualValue = item.getText(2);
							exceValue = exceValue.replace("$value", actualValue);
							String result = (String) ExpResolver.getExpResolver().evaluate(exceValue);
							if (StringUtils.isDouble(result))
							{
								if (!"1.0".equals(result))
								{
									item.setForeground(ColorConstants.red);
								}
							} else
							{
								item.setForeground(ColorConstants.red);
							}

						} else if (StringUtils.EMPTY_STRING.equals(item.getText(2)))
						{
							item.setForeground(ColorConstants.red);
						}

					}
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
				case 4:
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
				case 5:
				{// 单位
					Attribute unitAtt = icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE);
					if (null != unitAtt && null != unitAtt.getValue() && null != icdField.getIcdUnit() && null != icdField.getIcdUnitType() && null != icdField.getIcdUnit().getDisplayName())
					{
						return icdField.getIcdUnit().getDisplayName();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 6:
				{// 起始字
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_START_WORD);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 7:
				{// 起始位
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_START_BIT);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 8:
				{// 宽度
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_LENGTH);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 9:
				{// LSB
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_LSB);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 10:
				{// 类型
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_TYPE);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 11:
				{// 最大值
					Attribute att = icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE);
					if (null != att && null != att.getValue())
					{
						return att.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 12:
				{// 最小值
					Attribute minValueAtt = icdField.getAttribute(Constants.ICD_FIELD_MIN_VALUE);
					if (null != minValueAtt && null != minValueAtt.getValue())
					{
						return minValueAtt.getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 13:
				{
					return StringUtils.EMPTY_STRING;
				}

			}

			return StringUtils.EMPTY_STRING;
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