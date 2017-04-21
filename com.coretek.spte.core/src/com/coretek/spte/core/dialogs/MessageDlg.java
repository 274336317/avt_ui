package com.coretek.spte.core.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.Helper;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.ICDUnit;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.testcase.unit.UnitUtils;

public abstract class MessageDlg extends TitleAreaDialog
{

	/**
	 * 消息主题名称
	 */
	protected Label							lblTopic;

	/**
	 * 消息名称
	 */
	protected Combo							cmbMsgName;

	/**
	 * 广播属性
	 */
	protected Label							lblBdCast;

	/**
	 * 目标节点
	 */
	protected Label							lblDesText;

	/**
	 * 源节点
	 */
	protected Label							lblSrcText;

	/**
	 * 消息传输类型
	 */
	protected Label							lblMsgTransType;

	/**
	 * 消息传输周期
	 */
	protected Label							lblMsgTransPeriod;

	/**
	 * 最大传输延迟
	 */
	protected Label							lblMaxTrans;

	protected Label							lblMsgID;

	protected String						message;

	/**
	 * 连线模型
	 */
	protected MsgConnMdl					connModel;

	protected TableTreeViewer				viewer;

	protected AbstractConnectionEditPart	part;

	protected List<SPTEMsg>					spteMsgs		= new ArrayList<SPTEMsg>();

	protected SPTEMsg						selectedSpteMsg;

	protected String						srcNodeId		= "";							// 目标节点号也就是测试工具节点号

	protected String						msgType			= "";

	protected SPTEMsg						spteMsg;

	/**
	 * 保存editor(s)
	 */
	protected List<TableEditor>				editors			= new ArrayList<TableEditor>();

	protected String						type;
	/**
	 * 记录消息的选择
	 */
	protected static int					messageNameSelectIndex;

	protected ClazzManager					clazzManager;

	protected List<Entity>					testedObjectsOfICD;

	protected Message						cloneMsg;

	protected boolean						hasLieError		= false;

	protected String						transPeriodUnit	= "";

	public MessageDlg(Shell parentShell)
	{
		super(parentShell);

	}

	public MessageDlg(Shell shell, AbtConnMdl connectionModel, AbstractConnectionEditPart part, String type)
	{
		this(shell);
		this.connModel = (MsgConnMdl) connectionModel;
		this.message = this.connModel.getName();
		this.part = part;
		this.type = type;
		this.spteMsg = this.connModel.getTcMsg();

		List<SPTEMsg> spteMsgList = new ArrayList<SPTEMsg>();
		// 获取与当前工程相关的所有节点
		TestedObjectMdl testedMdl = Utils.getTestedObject(connModel);
		SPTEEditor activeEditor = (SPTEEditor) EclipseUtils.getActiveEditor();
		Entity fighter = activeEditor.getFighter();

		testedObjectsOfICD = TemplateUtils.getTestedObjectsOfICD(fighter, testedMdl.getEmulator());
		clazzManager = activeEditor.getFighterClazzManager();

		try
		{
			List<SPTEMsg> msgList = null;
			if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
			{
				msgList = TemplateUtils.filterAllNodeMsg(clazzManager, testedObjectsOfICD, true, false);
			} else
			{
				msgList = TemplateUtils.filterAllNodeMsg(clazzManager, testedObjectsOfICD, false, false);
			}
			if (connModel instanceof PeriodParentMsgMdl || connModel instanceof PeriodChildMsgMdl)
			{
				spteMsgList = TemplateUtils.filterSpteMsgOfPeriod(msgList, true);
			} else
			{
				spteMsgList = TemplateUtils.filterSpteMsgOfPeriod(msgList, false);
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spteMsgs = spteMsgList;

	}

	public int open()
	{
		if (this.spteMsgs.size() == 0)
		{
			MessageDialog.openWarning(Utils.getShell(), Messages.getString("I18N_WARNING"), Messages.getString("I18N_WARNING_NO_MATCHED_MSG"));
			return Window.CANCEL;
		}
		return super.open();
	}

	/**
	 * 消息名标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showMsgName(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_NAME"), ":"));
		GridData data = new GridData();
		data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.cmbMsgName = new Combo(body, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER | SWT.RIGHT);
		this.cmbMsgName.setFont(body.getFont());
		data = new GridData();
		data.widthHint = 175;
		this.cmbMsgName.setLayoutData(data);
		String[] itemMsgNames = new String[this.spteMsgs.size()];
		for (int i = 0; i < spteMsgs.size(); i++)
		{
			itemMsgNames[i] = spteMsgs.get(i).getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString();
		}
		cmbMsgName.setItems(itemMsgNames);
		if (spteMsg != null)
		{
			String msgName = spteMsg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString();
			int i = 0;
			for (String name : itemMsgNames)
			{
				if (msgName.equals(name))
				{
					cmbMsgName.select(i);
					break;
				}
				i++;
			}
			try
			{
				cloneMsg = (Message) spteMsg.getMsg().clone();
			} catch (CloneNotSupportedException e1)
			{
				e1.printStackTrace();
			}
			selectedSpteMsg = new SPTEMsg(cloneMsg, spteMsg.getICDMsg());
		} else
		{
			cmbMsgName.select(0);
			int index = cmbMsgName.getSelectionIndex();
			SPTEMsg msg = spteMsgs.get(index);
			if (null != msg)
			{
				try
				{
					cloneMsg = (Message) msg.getMsg().clone();
				} catch (CloneNotSupportedException e1)
				{
					e1.printStackTrace();
				}
				selectedSpteMsg = new SPTEMsg(cloneMsg, msg.getICDMsg());
			}

		}
	}

	/**
	 * icd主题名标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showIcdTopicName(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_TOPIC_NAME"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblTopic = new Label(body, SWT.LEFT);
		this.lblTopic.setFont(body.getFont());
		data = new GridData();
		data.widthHint = 175;
		this.lblTopic.setLayoutData(data);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TOPIC_NAME))
			{
				lblTopic.setText(icdMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue().toString());
			} else
			{
				lblTopic.setText("");
			}
		}
	}

	/**
	 * 消息id标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showMsgID(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_ID"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		lblMsgID = new Label(body, SWT.LEFT);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_ID))
			{
				lblMsgID.setText(icdMsg.getAttribute(Constants.ICD_MSG_ID).getValue().toString());
			} else
			{
				lblMsgID.setText("");
			}
		}
		data = new GridData();
		data.widthHint = 175;
		lblMsgID.setLayoutData(data);
	}

	/**
	 * 广播属性标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showBrocast(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_BROCAST"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		lblBdCast = new Label(body, SWT.LEFT);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_BROCAST))
			{
				lblBdCast.setText(icdMsg.getAttribute(Constants.ICD_MSG_BROCAST).getValue().toString());
			} else
			{
				lblBdCast.setText("");
			}

		}
		data = new GridData();
		data.widthHint = 175;
		lblBdCast.setLayoutData(data);
	}

	/**
	 * 源节点标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showSrcNode(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_SRC_NODE"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblSrcText = new Label(body, SWT.LEFT);
		this.lblSrcText.setText(srcNodeId);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_SRC_ID))
			{
				int srcID = Integer.parseInt(icdMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString());
				String srcName = TemplateUtils.getFunctionName(clazzManager, srcID, testedObjectsOfICD.get(0).getClass());
				lblSrcText.setText(srcName);
			} else
			{
				lblSrcText.setText("");
			}

		}
	}

	/**
	 * 目的节点标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showDesNode(Composite body)
	{
		GridData data = new GridData();
		data.widthHint = 175;
		this.lblSrcText.setLayoutData(data);

		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_DES_NODE"), ":"));
		data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblDesText = new Label(body, SWT.LEFT);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (null != icdMsg.getDestIDs() && icdMsg.getDestIDs().size() != 0)
			{
				String desName = "";
				for (int desID : icdMsg.getDestIDs())
				{
					desName = StringUtils.concat(desName, TemplateUtils.getFunctionName(clazzManager, desID, testedObjectsOfICD.get(0).getClass()), "、");

				}
				lblDesText.setText(desName);
			} else
			{
				lblDesText.setText("");
			}
		}
		data = new GridData();
		data.widthHint = 175;
		this.lblDesText.setLayoutData(data);
	}

	/**
	 * 传输周期标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showTransPeriod(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_TRANS_PERIOD"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblMsgTransPeriod = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		this.lblMsgTransPeriod.setLayoutData(data);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TRANS_PERIOD) && isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_PERIOD_UNIT))
			{
				String unit = icdMsg.getAttribute(Constants.ICD_MSG_PERIOD_UNIT).getValue().toString();
				transPeriodUnit = unit;
				lblMsgTransPeriod.setText(StringUtils.concat(icdMsg.getAttribute(Constants.ICD_MSG_TRANS_PERIOD).getValue(), " ", unit));
			}
		}
	}

	/**
	 * 传输延迟标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showTransDelay(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_MAX_TRANS_DELAY"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblMaxTrans = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		this.lblMaxTrans.setLayoutData(data);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_MAX_TRANS_DELAY) && isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_DELAY_UNIT))
			{
				String unit = icdMsg.getAttribute(Constants.ICD_MSG_DELAY_UNIT).getValue().toString();
				lblMaxTrans.setText(StringUtils.concat(icdMsg.getAttribute(Constants.ICD_MSG_MAX_TRANS_DELAY).getValue(), " ", unit));
			} else
			{
				lblMaxTrans.setText("");
			}

		}
	}

	/**
	 * 传输类型标签 </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-3-31
	 */
	protected void showTransType(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(StringUtils.concat(Messages.getString("I18N_ICD_MSG_TRANS_TYPE"), ":"));
		GridData data = new GridData();
		data.widthHint = 80;
		label.setLayoutData(data);

		this.lblMsgTransType = new Label(body, SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		this.lblMsgTransType.setLayoutData(data);
		ICDMsg icdMsg = this.selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TRANS_TYPE))
			{
				lblMsgTransType.setText(icdMsg.getAttribute(Constants.ICD_MSG_TRANS_TYPE).getValue().toString());
			} else
			{
				lblMsgTransType.setText("");
			}

		}
	}

	/**
	 * 判断属性以及属性的值是否为空 如果两个都不为空则返回true
	 * 
	 * @param helper 需要判断的对象
	 * @param attName 属性名称
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-4-1
	 */
	protected boolean isNotNullAttAndValue(Helper helper, String attName)
	{
		if (null != helper.getAttribute(attName) && null != helper.getAttribute(attName).getValue())
		{
			return true;
		}
		return false;
	}

	/**
	 * 设置消息名
	 */
	public void setName()
	{
		((AbtConnMdl) this.part.getModel()).setName(message);

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
	@SuppressWarnings("deprecation")
	protected void getTabControl(Composite com, List<Entity> fields)
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
		viewer.getTableTree().addSelectionListener(new ClientSelectionListener());
		viewer.setInput(fields);
		viewer.expandAll();

	}

	protected static class ContentProvider implements ITreeContentProvider
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

	protected class LabelProvider implements ITableLabelProvider
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

			// TODO :掉用孙大魏的接口，根据selectedSpteMsg和field获取对应的ICDField
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
					return "";
				}
				case 2:
				{// 二进制

					String text = this.getColumnText(element, 1);
					if (StringUtils.isNull(text))
					{
						return "";
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
						return "";
					}
				}
				case 3:
				{// 十六进制
					String text = this.getColumnText(element, 1);
					if (text.equals(""))
					{
						return "";
					} else if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
					{
						int index = Integer.parseInt(field.getValue());
						for (ICDEnum literal : icdField.getIcdEnums())
						{
							if (literal.getValue() == index)
							{
								return StringUtils.concat("0x", Integer.toHexString(literal.getValue()));
							}
						}
					} else if (StringUtils.isNumber(text))
					{
						return StringUtils.concat("0x", Integer.toHexString(Integer.valueOf(text)));
					} else
					{
						return "";
					}

				}
				case 4:
				{// 单位
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_UNIT_CODE) && null != icdField.getIcdUnit() && null != icdField.getIcdUnitType() && null != icdField.getIcdUnit().getDisplayName())
					{
						return icdField.getIcdUnit().getDisplayName();
					}
					return "";
				}
				case 5:
				{// 起始字
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_START_WORD))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_WORD).getValue().toString();
					}
					return "";
				}
				case 6:
				{// 起始位
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_START_BIT))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_START_BIT).getValue().toString();
					}
					return "";
				}
				case 7:
				{// 宽度
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_LENGTH))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_LENGTH).getValue().toString();
					}
					return "";
				}
				case 8:
				{// LSB
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_LSB))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_LSB).getValue().toString();
					}
					return "";
				}
				case 9:
				{// 类型
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_TYPE))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().toString();
					}
					return "";
				}
				case 10:
				{// 最大值
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_MAX_VALUE))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_MAX_VALUE).getValue().toString();
					}
					return "";
				}
				case 11:
				{// 最小值
					if (isNotNullAttAndValue(icdField, Constants.ICD_FIELD_MIN_VALUE))
					{
						return icdField.getAttribute(Constants.ICD_FIELD_MIN_VALUE).getValue().toString();
					}
					return "";
				}
				case 12:
				{
					return "";
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

	private class ClientSelectionListener implements SelectionListener
	{
		private TableTreeItem	item;

		private void setEditor(TableTreeItem item)
		{

			if (this.item == item)
				return;
			this.item = item;
			Object inputData = item.getData();
			Control[] controls = viewer.getTableTree().getTable().getChildren();
			for (Control control : controls)
			{
				control.dispose();
			}
			if (inputData instanceof Field)
			{
				Field field = (Field) inputData;
				if (field.getChildren().size() == 0 || null == field.getChildren())
				{
					// TODO: 通过孙大魏接口获取到ICDField
					final ICDField icdField = TemplateUtils.getICDField(selectedSpteMsg, field);

					final TableItem[] tableItems = item.getParent().getTable().getSelection();

					TableEditor editor = new TableEditor(viewer.getTableTree().getTable());
					editor.grabHorizontal = true;
					editors.add(editor);
					if (icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM"))
					{// 枚举类型
						CCombo alias = new CCombo(viewer.getTableTree().getTable(), SWT.RIGHT);
						String txt = tableItems[0].getText(1);
						int i = 0;
						int index = 0;
						for (ICDEnum literal : icdField.getIcdEnums())
						{
							alias.add(literal.getSymbol());
							if (literal.getSymbol().equals(txt))
							{
								index = i;
							} else
							{
								i++;
							}
						}
						editor.setEditor(alias, tableItems[0], 1);
						final CCombo combo = (CCombo) editor.getEditor();
						combo.setEditable(false);
						combo.setFocus();
						combo.select(index);
						combo.addModifyListener(new ValueListener(tableItems[0], field));
						combo.addKeyListener(new KeyListener()
						{

							public void keyPressed(KeyEvent e)
							{
								switch (e.keyCode)
								{
									case 16777217:// 上移
										int up = viewer.getTableTree().getTable().getSelectionIndex();
										int conut = viewer.getTableTree().getItemCount();
										if (up <= 0)
										{
											up = conut;
										}
										combo.forceFocus();
										combo.dispose();
										TableTreeItem upItem = viewer.getTableTree().getItem(up - 1);
										TableTreeItem[] upItems = { upItem };
										viewer.getTableTree().setSelection(upItems);
										setEditor(upItem);
										break;
									case 16777218:// 下移
										int i = viewer.getTableTree().getTable().getSelectionIndex();
										conut = viewer.getTableTree().getItemCount();
										if (i >= conut - 1)
										{
											i = -1;
										}
										combo.forceFocus();
										combo.dispose();
										TableTreeItem ttItem = viewer.getTableTree().getItem(i + 1);
										TableTreeItem[] items = { ttItem };
										viewer.getTableTree().setSelection(items);
										setEditor(ttItem);
										break;
								}

							}

							public void keyReleased(KeyEvent e)
							{
								// TODO Auto-generated method stub

							}
						});

					} else
					{// 非枚举类型

						final Text text = new Text(viewer.getTableTree().getTable(), SWT.NONE | SWT.LEFT);
						String txt = tableItems[0].getText(1);
						text.setText(txt);
						text.selectAll();
						text.addModifyListener(new TextModifyListener(tableItems[0], field, icdField));

						editor.setEditor(text, tableItems[0], 1);
						String str = text.getText();
						if (StringUtils.isNull(str))
						{
							if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
							{
								setErrorMessage(Messages.getString("I18N_NO_SEND_VAL"));
							} else
							{
								setErrorMessage(Messages.getString("I18N_NO_EXP_VAL"));
							}
							hasLieError = true;
						}
						text.setFocus();
						text.addKeyListener(new KeyListener()
						{

							public void keyPressed(KeyEvent e)
							{
								switch (e.keyCode)
								{
									case 16777217:// 上移
										int up = viewer.getTableTree().getTable().getSelectionIndex();
										int conut = viewer.getTableTree().getItemCount();
										if (up <= 0)
										{
											up = conut;
										}
										text.forceFocus();
										text.dispose();
										TableTreeItem upItem = viewer.getTableTree().getItem(up - 1);
										TableTreeItem[] upItems = { upItem };
										viewer.getTableTree().setSelection(upItems);
										setEditor(upItem);
										break;
									case 16777218:// 下移
										int i = viewer.getTableTree().getTable().getSelectionIndex();
										conut = viewer.getTableTree().getItemCount();
										if (i >= conut - 1)
										{
											i = -1;
										}
										text.forceFocus();
										text.dispose();
										TableTreeItem ttItem = viewer.getTableTree().getItem(i + 1);
										TableTreeItem[] items = { ttItem };
										viewer.getTableTree().setSelection(items);
										setEditor(ttItem);
										break;
								}

							}

							public void keyReleased(KeyEvent e)
							{
								// TODO Auto-generated method stub

							}
						});
					}
					TableEditor unitEditor = new TableEditor(viewer.getTableTree().getTable());
					unitEditor.grabHorizontal = true;
					editors.add(unitEditor);
					if (null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE) && null != icdField.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue() && null != icdField.getIcdUnitType() && null != icdField.getIcdUnit())
					{
						CCombo alias = new CCombo(viewer.getTableTree().getTable(), SWT.RIGHT);
						String txt = tableItems[0].getText(4);
						int i = 0;
						int index = 0;
						for (ICDUnit icdUnit : icdField.getIcdUnitType().getUnits())
						{
							alias.add(icdUnit.getDisplayName());
							if (icdUnit.getDisplayName().equals(txt))
							{
								index = i;
							} else
							{
								i++;
							}
						}
						unitEditor.setEditor(alias, tableItems[0], 4);
						CCombo combo = (CCombo) unitEditor.getEditor();
						combo.setEditable(false);
						combo.select(index);
						combo.addFocusListener(new FocusListener()
						{

							public void focusGained(FocusEvent e)
							{

								String fieldValue = tableItems[0].getText(1);
								if (!StringUtils.isDouble(fieldValue))
								{
									CCombo cm = (CCombo) e.getSource();

									ICDUnit fromUnit = null;
									for (ICDUnit unit : icdField.getIcdUnitType().getUnits())
									{
										if (unit.getDisplayName().equals(tableItems[0].getText(4)))
										{
											fromUnit = unit;
											break;
										}
									}
									String converValue = UnitUtils.conversion(icdField.getIcdUnitType(), fromUnit, icdField.getIcdUnit(), fieldValue);
									if (StringUtils.isDouble(converValue))
									{
										if (StringUtils.isDmsFormat(fieldValue) || StringUtils.isHmsFormat(fieldValue))
										{
											cm.setEnabled(true);
										} else
										{
											cm.setEnabled(false);
										}

									} else
									{
										cm.setEnabled(false);
									}

								} else
								{
									if (e.getSource() instanceof CCombo)
									{
										CCombo cm = (CCombo) e.getSource();
										cm.setEnabled(true);
									}
								}
							}

							public void focusLost(FocusEvent e)
							{
								if (e.getSource() instanceof CCombo)
								{
									CCombo cm = (CCombo) e.getSource();
									cm.setEnabled(true);
								}
							}
						});
						combo.addModifyListener(new UnitsListener(tableItems[0], field, icdField));
					}
				}
			}
		}

		public void widgetDefaultSelected(SelectionEvent e)
		{
			// TODO Auto-generated method stub

		}

		@SuppressWarnings("deprecation")
		public void widgetSelected(SelectionEvent e)
		{

			if (e.item instanceof TableTreeItem)
			{
				setEditor((TableTreeItem) e.item);
			}
		}

	}

	/**
	 * 
	 * 
	 * @author 孙大巍
	 * @date 2010-9-26
	 * 
	 */
	protected static class ValueListener implements ModifyListener
	{

		private TableItem	item;

		private Field		field;

		public ValueListener(TableItem item, Field field)
		{
			this.item = item;
			this.field = field;
		}

		public void modifyText(ModifyEvent e)
		{
			CCombo valueCombo = (CCombo) e.getSource();
			this.item.setText(1, valueCombo.getText());
			int index = 0;
			for (String str : valueCombo.getItems())
			{
				if (str.equals(valueCombo.getText()))
				{
					break;
				}
				index++;
			}
			field.setValue(Integer.toString(index));
			this.item.setText(2, Integer.toBinaryString(index));
			this.item.setText(3, StringUtils.concat("0x", Integer.toHexString(index)));
		}

	}

	protected class TextModifyListener implements ModifyListener
	{

		private TableItem	tableItem;

		private Field		field;

		private ICDField	icdField;

		public TextModifyListener(TableItem tableItem, Field field, ICDField icdField)
		{
			this.tableItem = tableItem;
			this.field = field;
			this.icdField = icdField;
		}

		public void modifyText(ModifyEvent e)
		{
			Text text = (Text) e.getSource();

			this.tableItem.setText(1, text.getText());
			for (TableEditor editor : editors)
			{
				if (tableItem == editor.getItem())
				{
					Control control = editor.getEditor();
					if (control instanceof CCombo)
					{
						CCombo com = (CCombo) control;
						if (!com.isDisposed())
						{
							if (!StringUtils.isDouble(text.getText()))
							{
								ICDUnit fromUnit = null;
								for (ICDUnit unit : icdField.getIcdUnitType().getUnits())
								{
									if (unit.getDisplayName().equals(this.tableItem.getText(4)))
									{
										fromUnit = unit;
										break;
									}
								}
								String converValue = UnitUtils.conversion(icdField.getIcdUnitType(), fromUnit, icdField.getIcdUnit(), text.getText());
								if (StringUtils.isDouble(converValue))
								{
									com.setEnabled(true);
								} else
								{
									com.setEnabled(false);
								}

							} else
							{
								com.setEnabled(true);
							}
						}
					}
				}
			}
			if (text.getText().equals("") || text.getText().equals("null"))
			{
				if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
				{
					setErrorMessage(Messages.getString("I18N_NO_SEND_VAL"));
				} else
				{
					setErrorMessage(Messages.getString("I18N_NO_EXP_VAL"));
				}
				// 使对应的二进制和十六进制变为空，modify By xuy
				this.tableItem.setText(2, "");
				this.tableItem.setText(3, "");
				this.field.setValue("");
				if (null != getButton(IDialogConstants.OK_ID))
				{
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
			}
			// 当输入的预期值不是正整数时，判断是否包含$value,modify by xuy
			else if (!StringUtils.isPositiveInteger(text.getText()))
			{ // 判断预期值是否包含$value时，且格式正确
				if (StringUtils.isPeriodValueRight(text.getText()))
				{
					// 使对应的二进制和十六进制变为空，modify By xuy
					this.tableItem.setText(2, "");
					this.tableItem.setText(3, "");
					this.field.setValue(text.getText());
					setErrorMessage(null);
					if (null != getButton(IDialogConstants.OK_ID))
					{
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				} else
				// 输入不正确时，显示错误信息，使对应的二进制和十六进制变为空，modify By xuy
				{
					if (null != icdField.getIcdUnit() && !this.tableItem.getText(4).equals(icdField.getIcdUnit().getDisplayName()))
					{
						this.tableItem.setText(2, "");
						this.tableItem.setText(3, "");
						ICDUnit fromUnit = null;
						for (ICDUnit unit : icdField.getIcdUnitType().getUnits())
						{
							if (unit.getDisplayName().equals(this.tableItem.getText(4)))
							{
								fromUnit = unit;
								break;
							}
						}
						if (StringUtils.isDmsFormat(text.getText()) || StringUtils.isHmsFormat(text.getText()))
						{
							String value = UnitUtils.conversion(icdField.getIcdUnitType(), fromUnit, icdField.getIcdUnit(), text.getText());
							String value1 = String.valueOf((int) Utils.div(Double.parseDouble(value), 0));
							this.field.setValue(value1);
							setErrorMessage(null);
						} else
						{
							if (!StringUtils.isDouble(text.getText()))
							{
								if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
								{
									setErrorMessage(Messages.getString("I18N_INPUT_ERR_SEND_VAL"));
								} else
								{
									setErrorMessage(Messages.getString("I18N_ERR_EXP_VAL"));
								}
								if (null != getButton(IDialogConstants.OK_ID))
								{
									getButton(IDialogConstants.OK_ID).setEnabled(false);
								}
							}

						}

					} else
					{
						if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
						{
							setErrorMessage(Messages.getString("I18N_INPUT_ERR_SEND_VAL"));
						} else
						{
							setErrorMessage(Messages.getString("I18N_ERR_EXP_VAL"));
						}
						if (null != getButton(IDialogConstants.OK_ID))
						{
							getButton(IDialogConstants.OK_ID).setEnabled(false);
						}
						this.tableItem.setText(2, "");
						this.tableItem.setText(3, "");
						this.field.setValue("");
					}

				}
			} else
			{
				String value = text.getText();
				if (StringUtils.isPositiveInteger(value))
				{
					if (null != icdField.getIcdUnit() && StringUtils.isNotNull(this.tableItem.getText(4)) && !this.tableItem.getText(4).equals(icdField.getIcdUnit().getDisplayName()))
					{

						this.tableItem.setText(2, "");
						this.tableItem.setText(3, "");
						ICDUnit fromUnit = null;
						for (ICDUnit unit : icdField.getIcdUnitType().getUnits())
						{
							if (unit.getDisplayName().equals(this.tableItem.getText(4)))
							{
								fromUnit = unit;
								break;
							}
						}
						String converValue = UnitUtils.conversion(icdField.getIcdUnitType(), fromUnit, icdField.getIcdUnit(), text.getText());
						String value1 = String.valueOf((int) Utils.div(Double.parseDouble(converValue), 0));
						this.field.setValue(value1);

					} else
					{
						try
						{
							this.tableItem.setText(2, Integer.toBinaryString(Integer.valueOf(value)));
							this.tableItem.setText(3, "0x" + Integer.toHexString(Integer.valueOf(value)));
							this.field.setValue(value);
							setErrorMessage(null);
							if (null != getButton(IDialogConstants.OK_ID))
							{
								getButton(IDialogConstants.OK_ID).setEnabled(true);
							}
						} catch (NumberFormatException e1)
						{
							// e1.printStackTrace();
							// 输入不正确时，显示错误信息，使对应的二进制和十六进制变为空，modify By xuy
							if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
							{
								setErrorMessage(Messages.getString("I18N_INPUT_ERR_SEND_VAL"));
							} else
							{
								setErrorMessage(Messages.getString("I18N_ERR_EXP_VAL"));
							}
							this.tableItem.setText(2, "");
							this.tableItem.setText(3, "");
							this.field.setValue("");
							if (null != getButton(IDialogConstants.OK_ID))
							{
								getButton(IDialogConstants.OK_ID).setEnabled(false);
							}
						}
					}

				}
			}
		}

	}

	protected class UnitsListener implements ModifyListener
	{

		private TableItem	item;

		private Field		field;

		private ICDField	icdField;

		public UnitsListener(TableItem item, Field field, ICDField icdField)
		{
			this.item = item;
			this.field = field;
			this.icdField = icdField;
		}

		public void modifyText(ModifyEvent e)
		{
			String oldValue = this.item.getText(4);
			CCombo valueCombo = (CCombo) e.getSource();
			this.item.setText(4, valueCombo.getText());
			String newValue = this.item.getText(4);

			if (StringUtils.isNotNull(oldValue) && StringUtils.isNotNull(newValue) && !oldValue.equals(newValue))
			{

				String strValue = field.getValue();
				String value1 = "0";

				ICDUnit toUnit = null;
				ICDUnit fromUnit = icdField.getIcdUnit();
				for (ICDUnit unit : icdField.getIcdUnitType().getUnits())
				{
					if (unit.getDisplayName().equals(newValue))
					{
						toUnit = unit;
						break;
					}
				}
				// 单位转换
				String value = UnitUtils.conversion(icdField.getIcdUnitType(), fromUnit, toUnit, strValue);
				// 四舍五入
				if (StringUtils.isDouble(value) && toUnit.getID() == fromUnit.getID())
				{
					value1 = String.valueOf((int) Utils.div(Double.parseDouble(value), 0));
				} else
				{
					value1 = value;
				}

				for (TableEditor editor : editors)
				{
					if (item == editor.getItem())
					{
						Control control = editor.getEditor();
						if (control instanceof Text)
						{
							Text text = (Text) control;
							if (!text.isDisposed())
								text.setText(value1);
						}
					}
				}
			}
		}

	}

}