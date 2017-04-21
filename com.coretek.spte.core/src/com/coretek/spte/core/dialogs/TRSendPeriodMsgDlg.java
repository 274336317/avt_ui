package com.coretek.spte.core.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

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
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * 编辑周期消息对话框
 * 
 * @author duys 2011-6-14
 * 
 */
public class TRSendPeriodMsgDlg extends PeriodResultDialog
{

	/** 修正值 */
	private Label						lblRevise;

	private Text						txtRevise;

	/** 发送数量 */
	private Label						lblNum;

	private Text						txtNum;

	/** 发送编号 */
	private Label						lblSendID;

	private Combo						cmbSendID;

	/** 保存发送编号和对应Field链表的哈希表 */
	private Map<String, List<Entity>>	TCFieldMap	= new HashMap<String, List<Entity>>();

	/** 记录发送编号的选择 */
	private static int					sendIDoldSelect;

	private SPTEMsg						selectedSpteMsg;

	private Message						cloneMsg;

	private Period						clonePeriod;

	private Result						result;

	public TRSendPeriodMsgDlg(Shell shell, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD, CompareResult compareResult, Result result)
	{
		super(shell, null, spteMsg, icdManager, testedObjectsOfICD, compareResult);
		this.compareResult = compareResult;
		this.result = result;
		spteMsgFormDB = this.result.getSpteMsg();

	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(Messages.getString("VIEW_PERIOD_SEND_MSG"));
	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		setTitle(Messages.getString("I18N_CHECK_MSG_RESULT"));

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

	private void makeReviseLabel(Composite body)
	{
		this.lblRevise = new Label(body, SWT.RIGHT);
		this.lblRevise.setText(Messages.getString("I18N_PERIOD_RIVISE") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblRevise.setLayoutData(data);

		this.txtRevise = new Text(body, SWT.LEFT | SWT.BORDER);
		this.txtRevise.setEditable(false);
		data = new GridData();
		data.widthHint = 175;
		this.txtRevise.setLayoutData(data);
		if (null != this.selectedSpteMsg.getMsg())
		{
			if (null != this.selectedSpteMsg.getMsg().getAmendValue())
			{
				txtRevise.setText(this.selectedSpteMsg.getMsg().getAmendValue().toString());
			} else
			{
				txtRevise.setText(StringUtils.EMPTY_STRING);
			}
		}
	}

	private void makeRecvLabel(Composite body)
	{
		this.lblNum = new Label(body, SWT.RIGHT);
		this.lblNum.setText(Messages.getString("I18N_SEND_CONUT") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblNum.setLayoutData(data);

		this.txtNum = new Text(body, SWT.LEFT | SWT.BORDER);
		this.txtNum.setEditable(false);
		data = new GridData();
		data.widthHint = 175;
		this.txtNum.setLayoutData(data);
		Message msg = this.selectedSpteMsg.getMsg();
		if (null != msg)
		{
			if (null != msg.getPeriodCount())
			{
				txtNum.setText(String.valueOf(msg.getPeriodCount()));
			} else
			{
				txtNum.setText(StringUtils.EMPTY_STRING);
			}

		}
	}

	private void makeSendLabel(Composite body)
	{
		this.lblSendID = new Label(body, SWT.RIGHT);
		this.lblSendID.setText(Messages.getString("I18N_SEND_ID") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblSendID.setLayoutData(data);

		this.cmbSendID = new Combo(body, SWT.DROP_DOWN | SWT.BORDER | SWT.LEFT | SWT.READ_ONLY);
		data = new GridData();
		data.widthHint = 175;
		this.cmbSendID.setLayoutData(data);
		Period period = (Period) this.selectedSpteMsg.getMsg().getChildren().get(0);
		cmbSendID.add(period.getValue().toString());
		cmbSendID.select(0);
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
			TCFieldMap = TemplateUtils.getTestCaseFieldsMap(selectedSpteMsg.getMsg());
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

		this.showCycle(body, selectedSpteMsg);

		this.makeReviseLabel(body);

		this.makeRecvLabel(body);

		this.makeSendLabel(body);

		Composite body2 = new Composite(area, SWT.NULL);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		gridLayout2.horizontalSpacing = 30;
		body2.setLayout(gridLayout2);

		Label label = new Label(body2, SWT.RIGHT);
		label.setText(Messages.getString("I18N_DESCRIBE") + ":");
		data = new GridData();
		data.widthHint = 60;
		label.setLayoutData(data);

		Text text = new Text(body2, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.widthHint = 550;
		text.setLayoutData(data);
		if (StringUtils.isNotNull(this.result.getDipicts()))
		{
			text.setText(this.result.getDipicts());
		}
		sendIDoldSelect = this.cmbSendID.getSelectionIndex();
		this.cmbSendID.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{// 记录当前选择的combo
				Combo sendIdCmb = (Combo) e.getSource();
				int selectIndex = sendIdCmb.getSelectionIndex();
				if (selectIndex != sendIDoldSelect && selectIndex != -1)
				{
					sendIDoldSelect = selectIndex;
					List<Entity> fields = TCFieldMap.get(cmbSendID.getItem(sendIDoldSelect));
					List<Entity> Fields = new ArrayList<Entity>();
					if (null != fields && fields.size() != 0)
					{
						if (fields.get(0).getParent() instanceof Period)
						{
							Period pd = (Period) fields.get(0).getParent();
							Fields.addAll(pd.getChildren());
						}
					} else
					{
						SPTEMsg msg = selectedSpteMsg;

						List<Entity> periodList = msg.getMsg().getChildren();

						Entity entity = null;
						int sendID = Integer.parseInt(cmbSendID.getText());
						int size = periodList.size();
						for (int i = 0; i < size; i++)
						{
							if (i == (size - 1) || (((Period) periodList.get(i)).getValue() <= sendID && ((Period) periodList.get(i + 1)).getValue() > sendID))
							{
								entity = periodList.get(i);
								break;
							}
						}
						if (null != entity && entity instanceof Period)
						{
							Period per = (Period) entity;
							Fields.addAll(per.getChildren());
						}

					}
					viewer.setInput(Fields);
					viewer.expandAll();
					viewer.refresh();
				}

				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		});
	}

	/**
	 * 消息编辑tab
	 * 
	 * @author duys
	 * @date 2011-6-10
	 * @param area
	 */
	private void createMessageInfoTabItems(Composite area)
	{
		List<Entity> Fields = new ArrayList<Entity>();
		SPTEMsg msg = this.selectedSpteMsg;
		List<Entity> periodList = msg.getMsg().getChildren();

		Entity entity = null;
		int sendID = Integer.parseInt(cmbSendID.getText());
		int size = periodList.size();
		for (int i = 0; i < size; i++)
		{
			if (i == (size - 1) || (((Period) periodList.get(i)).getValue() <= sendID && ((Period) periodList.get(i + 1)).getValue() > sendID))
			{
				entity = periodList.get(i);
				break;
			}
		}
		if (null != entity && entity instanceof Period)
		{
			Period per = (Period) entity;
			Fields.addAll(per.getChildren());
		}

		getTabControl(area, Fields);

	}

	/**
	 * 设置tabItem中的控件
	 * 
	 * @param tabFolder
	 * @param icdFields
	 * @author duys
	 * @date 2011-6-10
	 * @return
	 */
	public void getTabControl(Composite com, List<Entity> fields)
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

	/**
	 * 通过通过tabItem获取TCSignalBean的链表 记录测试用例中TCSignalBean的信息
	 * 
	 * @return
	 */
	public List<Entity> getFieldList()
	{
		List<Entity> tcFieldList = new ArrayList<Entity>();
		tcFieldList.addAll(this.clonePeriod.getChildren());
		return tcFieldList;
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
				{// 发送值
					for (Entity entity : spteMsgFormDB.getMsg().getChildren().get(0).getChildren())
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
