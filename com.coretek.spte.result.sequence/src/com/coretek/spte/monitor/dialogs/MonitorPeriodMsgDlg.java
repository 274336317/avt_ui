/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * @author SunDawei
 * 
 * @date 2012-9-25
 */
public class MonitorPeriodMsgDlg extends TitleAreaDialog
{

	/** ����ֵ */
	private Label						lblRevise;

	private Text						txtRevise;

	/** �������� */
	private Label						lblNum;

	private Text						txtNum;

	/** ���ͱ�� */
	private Label						lblSendID;

	private Combo						cmbSendID;

	/** ���淢�ͱ�źͶ�ӦField����Ĺ�ϣ�� */
	private Map<String, List<Entity>>	TCFieldMap	= new HashMap<String, List<Entity>>();

	/** ��¼���ͱ�ŵ�ѡ�� */
	private static int					sendIDoldSelect;

	private Period						clonePeriod;

	protected TableTreeViewer			viewer;

	private SPTEMsg						spteMsg;

	private ClazzManager				icdManager;

	private Result						result;

	public MonitorPeriodMsgDlg(Shell shell, ClazzManager icdManager, Result result)
	{
		super(shell);
		this.result = result;
		this.spteMsg = result.getSpteMsg();

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
		if (null != this.spteMsg.getMsg())
		{
			if (null != this.spteMsg.getMsg().getAmendValue())
			{
				txtRevise.setText(this.spteMsg.getMsg().getAmendValue().toString());
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
		Message msg = this.spteMsg.getMsg();
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
		Period period = (Period) this.spteMsg.getMsg().getChildren().get(0);
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
		TCFieldMap = TemplateUtils.getTestCaseFieldsMap(this.spteMsg.getMsg());

		this.showTopicName(body, this.spteMsg);

		this.showMsgID(body, this.spteMsg);

		this.showBrocast(body, this.spteMsg);

		this.showMsgSrc(body, this.spteMsg, icdManager);

		this.showDestIDs(body, this.spteMsg, icdManager);

		this.showTransPeriod(body, this.spteMsg);

		this.showTransDelay(body, this.spteMsg);

		this.showTransType(body, this.spteMsg);

		this.showCycle(body, this.spteMsg);

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
			{// ��¼��ǰѡ���combo
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
						List<Entity> periodList = spteMsg.getMsg().getChildren();

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
	 * ��Ϣ�༭tab
	 * 
	 * @author duys
	 * @date 2011-6-10
	 * @param area
	 */
	private void createMessageInfoTabItems(Composite area)
	{
		List<Entity> Fields = new ArrayList<Entity>();
		SPTEMsg msg = this.spteMsg;
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
	 * ����tabItem�еĿؼ�
	 * 
	 * @param tabFolder
	 * @param icdFields
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
		columns[1].setText(Messages.getString("I18N_SEND_VALUE"));
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
	 * ͨ��ͨ��tabItem��ȡTCSignalBean������ ��¼����������TCSignalBean����Ϣ
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

			ICDField icdField = TemplateUtils.getICDField(spteMsg, field);

			switch (columnIndex)
			{
				case 0:
				{// ����
					return icdField.getAttribute(Constants.ICD_FIELD_NAME).getValue().toString();
				}
				case 1:
				{// ����ֵ
					for (Entity entity : spteMsg.getMsg().getChildren().get(0).getChildren())
					{
						if (entity instanceof Field)
						{
							if (((Field) entity).getId().equals(field.getId()))
							{

								if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
								{// ö������
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
				{// ������
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
				{// ʮ������
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
				{// ��λ
					if (null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE) && null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue() && null != icdField.getIcdUnit() && null != icdField.getIcdUnitType() && null != icdField.getIcdUnit().getDisplayName())
					{
						return icdField.getIcdUnit().getDisplayName();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 5:
				{// ��ʼ��
					if (null != icdField.getAttribute(Constants.ICD_FIELD_START_WORD) && null != icdField.getAttribute(Constants.ICD_FIELD_START_WORD).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_WORD).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 6:
				{// ��ʼλ
					if (null != icdField.getAttribute(Constants.ICD_FIELD_START_BIT) && null != icdField.getAttribute(Constants.ICD_FIELD_START_BIT).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_BIT).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 7:
				{// ���
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
				{// ����
					if (null != icdField.getAttribute(Constants.ICD_FIELD_TYPE) && null != icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 10:
				{// ���ֵ
					if (null != icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE) && null != icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE).getValue())
					{
						return icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE).getValue().toString();
					}
					return StringUtils.EMPTY_STRING;
				}
				case 11:
				{// ��Сֵ
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

	/**
	 * ��ʾ��Ϣ�Ĵ�������
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showTransType(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_TRANS_TYPE")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblMsgTransType = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		lblMsgTransType.setLayoutData(data);
		ICDMsg icdMsg = spteMsg.getICDMsg();
		Attribute transType = icdMsg.getAttribute(Constants.ICD_MSG_TRANS_TYPE);
		if (null != spteMsg.getICDMsg())
		{
			if (null != transType && null != transType.getValue())
			{
				lblMsgTransType.setText(transType.getValue().toString());
			} else
			{
				lblMsgTransType.setText(StringUtils.EMPTY_STRING);
			}

		}

		return lblMsgTransType;
	}

	/**
	 * ��ʾ��Ϣ�Ĵ����ӳ�����
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showTransDelay(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_MAX_TRANS_DELAY")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblMaxTrans = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		lblMaxTrans.setLayoutData(data);
		ICDMsg icdMsg = spteMsg.getICDMsg();
		if (null != icdMsg)
		{
			Attribute delayAtt = icdMsg.getAttribute(Constants.ICD_MSG_MAX_TRANS_DELAY);
			Attribute delayUnitAtt = icdMsg.getAttribute(Constants.ICD_MSG_DELAY_UNIT);
			if (null != delayAtt && null != delayAtt.getValue() && null != delayUnitAtt && null != delayUnitAtt.getValue())
			{
				String unit = delayUnitAtt.getValue().toString();
				lblMaxTrans.setText(delayAtt.getValue() + StringUtils.SPACE_STRING + unit);
			} else
			{
				lblMaxTrans.setText(StringUtils.EMPTY_STRING);
			}
		}

		return lblMaxTrans;
	}

	/**
	 * ��ʾ��Ϣ�Ĵ�����ʱ��
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showTransPeriod(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_TRANS_PERIOD")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblMsgTransPeriod = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		lblMsgTransPeriod.setLayoutData(data);
		ICDMsg icdMsg = spteMsg.getICDMsg();
		if (null != icdMsg)
		{
			Attribute transPeriodAtt = icdMsg.getAttribute(Constants.ICD_MSG_TRANS_PERIOD);
			Attribute periodUnitAtt = icdMsg.getAttribute(Constants.ICD_MSG_PERIOD_UNIT);
			if (null != transPeriodAtt && null != transPeriodAtt.getValue() && null != periodUnitAtt && null != periodUnitAtt.getValue())
			{
				String unit = periodUnitAtt.getValue().toString();
				lblMsgTransPeriod.setText(StringUtils.concat(transPeriodAtt.getValue(), StringUtils.SPACE_STRING, unit));
			}

		}

		return lblMsgTransPeriod;
	}

	/**
	 * ��ʾ��Ϣ��Ŀ��IDs
	 * 
	 * @param body
	 * @param spteMsg
	 * @param icdManager
	 * @param testedObjectsOfICD
	 * @return </br>
	 */
	private Label showDestIDs(Composite body, SPTEMsg spteMsg, ClazzManager icdManager)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_DES_NODE")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblDesText = new Label(body, SWT.LEFT);
		ICDMsg msg = spteMsg.getICDMsg();
		if (null != msg)
		{
			if (null != msg.getDestIDs() && msg.getDestIDs().size() != 0)
			{
				lblDesText.setText(spteMsg.getMsg().getDesId());
			} else
			{
				lblDesText.setText(StringUtils.EMPTY_STRING);
			}

		}

		data = new GridData();
		data.widthHint = 175;
		lblDesText.setLayoutData(data);

		return lblDesText;
	}

	/**
	 * ��ʾ��Ϣ��Դ�ڵ��
	 * 
	 * @param body
	 * @param this.spteMsg
	 * @param icdManager
	 * @param testedObjectsOfICD
	 * @return </br>
	 */
	private Label showMsgSrc(Composite body, SPTEMsg spteMsg, ClazzManager icdManager)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_SRC_NODE")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblSrcText = new Label(body, SWT.LEFT);
		lblSrcText.setText(StringUtils.EMPTY_STRING);
		lblSrcText.setText(spteMsg.getMsg().getSrcId());

		data = new GridData();
		data.widthHint = 175;
		lblSrcText.setLayoutData(data);

		return lblSrcText;
	}

	/**
	 * ��ʾ��Ϣ��ID
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showMsgID(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_ID")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblMsgID = new Label(body, SWT.LEFT);
		ICDMsg msg = spteMsg.getICDMsg();
		if (null != msg)
		{
			Attribute msgIDAtt = msg.getAttribute(Constants.ICD_MSG_ID);
			if (null != msgIDAtt && null != msgIDAtt.getValue())
			{
				lblMsgID.setText(msgIDAtt.getValue().toString());
			} else
			{
				lblMsgID.setText(StringUtils.EMPTY_STRING);
			}
		}
		data = new GridData();
		data.widthHint = 175;
		lblMsgID.setLayoutData(data);

		return lblMsgID;
	}

	/**
	 * ��ʾ���������
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showTopicName(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_TOPIC_NAME")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblTopic = new Label(body, SWT.LEFT);
		lblTopic.setFont(body.getFont());
		data = new GridData();
		data.widthHint = 175;
		lblTopic.setLayoutData(data);
		ICDMsg msg = spteMsg.getICDMsg();
		if (null != msg)
		{
			Attribute topicNameAtt = msg.getAttribute(Constants.ICD_MSG_TOPIC_NAME);
			if (null != topicNameAtt && null != topicNameAtt.getValue())
			{
				lblTopic.setText(topicNameAtt.getValue().toString());
			} else
			{
				lblTopic.setText(StringUtils.EMPTY_STRING);
			}
		}

		return lblTopic;
	}

	/**
	 * ��ʾ��Ϣ������
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Combo showMsgName(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_NAME")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Combo cmbMsgName = new Combo(body, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER | SWT.RIGHT);
		cmbMsgName.setEnabled(false);
		cmbMsgName.setFont(body.getFont());
		data = new GridData();
		data.widthHint = 175;
		cmbMsgName.setLayoutData(data);

		if (spteMsg != null)
		{
			String msgName = spteMsg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString();
			cmbMsgName.add(msgName);
			cmbMsgName.select(0);

		}

		return cmbMsgName;
	}

	/**
	 * ��ʾ�㲥����
	 * 
	 * @param body
	 * @param spteMsg
	 * @return </br>
	 */
	private Label showBrocast(Composite body, SPTEMsg spteMsg)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_ICD_MSG_BROCAST")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);
		Label lblBdCast = new Label(body, SWT.LEFT);
		ICDMsg msg = spteMsg.getICDMsg();
		if (null != msg)
		{
			Attribute brocastAtt = msg.getAttribute(Constants.ICD_MSG_BROCAST);
			if (null != brocastAtt && null != brocastAtt.getValue())
			{
				lblBdCast.setText(brocastAtt.getValue().toString());
			} else
			{
				lblBdCast.setText(StringUtils.EMPTY_STRING);
			}

		}
		data = new GridData();
		data.widthHint = 175;
		lblBdCast.setLayoutData(data);

		return lblBdCast;
	}

	/**
	 * ��ʾ����
	 * 
	 * @param body
	 * @return </br>
	 */
	private Text showCycle(Composite body, SPTEMsg spteMsg)
	{
		Label lblCycle = new Label(body, SWT.RIGHT);
		lblCycle.setText(Messages.getString("I18N_PERIOD") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		lblCycle.setLayoutData(data);

		Text txtCycle = new Text(body, SWT.LEFT | SWT.BORDER);
		txtCycle.setEditable(false);
		data = new GridData();
		data.widthHint = 175;
		txtCycle.setLayoutData(data);
		if (null != this.spteMsg.getMsg())
		{
			if (null != this.spteMsg.getMsg().getPeriodDuration())
			{
				txtCycle.setText(this.spteMsg.getMsg().getPeriodDuration().toString());
			} else
			{
				txtCycle.setText(StringUtils.EMPTY_STRING);
			}

		}

		return txtCycle;
	}

}