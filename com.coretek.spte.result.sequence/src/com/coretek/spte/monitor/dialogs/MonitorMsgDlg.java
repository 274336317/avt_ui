/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

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
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Field;

/**
 * @author SunDawei
 * 
 * @date 2012-9-25
 */
public class MonitorMsgDlg extends TitleAreaDialog
{
	protected TableTreeViewer	viewer;

	protected List<SPTEMsg>		spteMsgs	= new ArrayList<SPTEMsg>();

	protected SPTEMsg			spteMsg;

	protected String			type;

	protected ClazzManager		icdManager;

	protected Result			result;

	public MonitorMsgDlg(Shell shell, SPTEMsg spteMsg, ClazzManager icdManager)
	{
		super(shell);
		this.spteMsg = spteMsg;
		this.icdManager = icdManager;

		this.type = SPTEConstants.MESSAGE_TYPE_SEND;
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

		this.showTopicName(body, spteMsg);

		this.showMsgID(body, spteMsg);

		this.showBrocast(body, spteMsg);

		this.showMsgSrc(body, spteMsg, icdManager);

		this.showDestIDs(body, spteMsg, icdManager);

		this.showTransPeriod(body, spteMsg);

		this.showTransDelay(body, spteMsg);

		this.showTransType(body, spteMsg);

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
		List<Entity> icdFieldList = this.spteMsg.getMsg().getChildren();
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

			ICDField icdField = TemplateUtils.getICDField(spteMsg, field);

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

	/**
	 * 显示消息的传输类型
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showTransType(Composite body, SPTEMsg spteMsg)
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
	 * 显示消息的传输延迟属性
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showTransDelay(Composite body, SPTEMsg spteMsg)
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
	 * 显示消息的传输间隔时间
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showTransPeriod(Composite body, SPTEMsg spteMsg)
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
	 * 显示消息的目的IDs
	 * 
	 * @param body
	 * @param spteMsg
	 * @param icdManager
	 * @param testedObjectsOfICD
	 * @return
	 */
	protected Label showDestIDs(Composite body, SPTEMsg spteMsg, ClazzManager icdManager)
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
				String desName = spteMsg.getMsg().getDesId();
				lblDesText.setText(desName);
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
	 * 显示消息的源节点号
	 * 
	 * @param body
	 * @param selectedSpteMsg
	 * @param icdManager
	 * @param testedObjectsOfICD
	 * @return
	 */
	protected Label showMsgSrc(Composite body, SPTEMsg spteMsg, ClazzManager icdManager)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(new StringBuilder(Messages.getString("I18N_SRC_NODE")).append(":").toString());
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		Label lblSrcText = new Label(body, SWT.LEFT);
		lblSrcText.setText(StringUtils.EMPTY_STRING);
		ICDMsg msg = spteMsg.getICDMsg();
		if (null != msg)
		{
			Attribute srcAtt = msg.getAttribute(Constants.ICD_MSG_SRC_ID);
			if (null != srcAtt && null != srcAtt.getValue())
			{
				String srcName = spteMsg.getMsg().getSrcId();
				lblSrcText.setText(srcName);
			} else
			{
				lblSrcText.setText(StringUtils.EMPTY_STRING);
			}

		}

		data = new GridData();
		data.widthHint = 175;
		lblSrcText.setLayoutData(data);

		return lblSrcText;
	}

	/**
	 * 显示消息的ID
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showMsgID(Composite body, SPTEMsg spteMsg)
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
	 * 显示主题的名字
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showTopicName(Composite body, SPTEMsg spteMsg)
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
	 * 显示消息的名字
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Combo showMsgName(Composite body, SPTEMsg spteMsg)
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
	 * 显示广播属性
	 * 
	 * @param body
	 * @param spteMsg
	 * @return
	 */
	protected Label showBrocast(Composite body, SPTEMsg spteMsg)
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
}