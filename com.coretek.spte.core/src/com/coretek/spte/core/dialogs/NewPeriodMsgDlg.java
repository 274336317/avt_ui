package com.coretek.spte.core.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDEnum;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.ICDUnit;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.commands.CfgMsgCmd;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;
import com.coretek.testcase.unit.UnitUtils;

public class NewPeriodMsgDlg extends MessageDlg
{

	private Period						clonePeriod;

	// 周期
	private Label						lblCycle;

	private Text						txtCycle;

	// 修正值
	private Label						lblRevise;

	private Text						txtRevise;

	// 发送数量
	private Label						lblNum;

	private Text						txtNum;

	// 发送编号
	private Label						lblSendID;

	private Combo						cmbSendID;

	// 删除按钮
	private Button						delButton;

	// 保存按钮
	private Button						saveButton;

	// 记录发送编号的选择
	private static int					sendIDoldSelect;

	// 保存发送编号和对应Field链表的哈希表
	private Map<String, List<Entity>>	TCFieldMap	= new HashMap<String, List<Entity>>();

	public NewPeriodMsgDlg(Shell parentShell)
	{
		super(parentShell);
	}

	public NewPeriodMsgDlg(Shell shell, AbtConnMdl connectionModel, AbstractConnectionEditPart part, String type)
	{
		super(shell, connectionModel, part, type);
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

	/**
	 * 
	 * @param area
	 */
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

		showMsgName(body);
		this.cmbMsgName.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				if (e.getSource() instanceof Combo)
					handleMsgNameSelect((Combo) e.getSource());
			}
		});

		showIcdTopicName(body);
		showMsgID(body);
		showBrocast(body);
		showSrcNode(body);
		showDesNode(body);
		showTransPeriod(body);
		showTransDelay(body);
		showTransType(body);
		showPeriod(body);
		showRivise(body);
		showSendCount(body);
		showSendID(body);
		showDelButton(body);
		showSaveButton(body);

		for (TableEditor editor : editors)
		{
			if (editor.getEditor() != null)
				editor.getEditor().dispose();
			editor.dispose();
		}

		this.txtNum.addFocusListener(new FocusListener()
		{

			public void focusGained(FocusEvent e)
			{

			}

			public void focusLost(FocusEvent e)
			{

				if (e.getSource() instanceof Text)
				{
					String text = ((Text) e.getSource()).getText();

					int k;
					if (!text.equals(""))
					{// 如果发送次数不为空
						// 验证发送次数是否为整数,不是则禁止删除和保存按钮，modify by xuy
						if (!StringUtils.isPositiveInteger(text))
						{
							setErrorMessage(Messages.getString("I18N_SEND_NUM_MUST_POS_INT"));
							delButton.setEnabled(false);
							saveButton.setEnabled(false);
							return;
						}
						try
						{
							k = Integer.valueOf(text);
						} catch (NumberFormatException e0)
						{
							setErrorMessage(Messages.getString("I18N_SEND_NUM_MUST_POS_INT"));
							return;
						}
						// 当发送次数恒为0时如果当前的消息不是背景消息，则禁止删除按钮，否则根据其他条件来判断删除和保存按钮的状态，modify
						// by xuy
						if (k == 0)
						{
							boolean bl = validate();
							if (isBackgroundMsg())
							{
								delButton.setEnabled(bl);
								saveButton.setEnabled(bl);
							} else
							{
								delButton.setEnabled(false);
								saveButton.setEnabled(bl);
							}
						} else if (k > 0)// 如果发送次数不为0，则根据其他条件是否禁止或使能删除和保存按钮，modify
						// by xuy
						{
							boolean bl = validate();
							delButton.setEnabled(bl);
							saveButton.setEnabled(bl);
							int maxValue = 0;
							List<String> rmList = new ArrayList<String>();
							for (String it : cmbSendID.getItems())
							{
								int v = Integer.parseInt(it);
								if (v > maxValue)
								{
									maxValue = v;
								}
								if (maxValue > k)
								{
									rmList.add(it);
								}
							}
							if (rmList.size() != 0)
							{
								for (String it : rmList)
								{
									cmbSendID.remove(it);
								}
							}
						}

					} else
					{
						k = 1;
						delButton.setEnabled(true);
					}

					cmbSendID.select(0);
					sendIDoldSelect = cmbSendID.getSelectionIndex();

					List<Entity> fields = TCFieldMap.get(cmbSendID.getItem(sendIDoldSelect));

					if (fields.size() != 0)
					{
						if (fields.get(0).getParent() instanceof Period)
						{
							Period pd = (Period) fields.get(0).getParent();
							try
							{
								clonePeriod = (Period) pd.clone();
							} catch (CloneNotSupportedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else
					{
						try
						{
							clonePeriod = (Period) clonePeriod.clone();
						} catch (CloneNotSupportedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					viewer.setInput(clonePeriod.getChildren());
					viewer.expandAll();
					viewer.refresh();

				}
				// 根据其他条件判断是否禁止或使能删除和保存和OK按钮，modify by xuy
				boolean bl = validate();
				getButton(IDialogConstants.OK_ID).setEnabled(bl);

			}

		});

		this.txtCycle.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				boolean bl = validate();
				getButton(IDialogConstants.OK_ID).setEnabled(bl);
				// 如果发送次数恒为0，且不是当前消息不是背景周期消息，则禁止删除按钮，否则禁止删除和保存按钮，modify by xuy
				if (txtNum.getText().equals("0"))
				{
					if (isBackgroundMsg())
					{
						delButton.setEnabled(bl);
						saveButton.setEnabled(bl);
					} else
					{
						delButton.setEnabled(false);
						saveButton.setEnabled(false);
					}
				} else
				// 如果发送次数不为0，则根据其他条件是否禁止或使能删除和保存按钮，modify by xuy
				{
					delButton.setEnabled(bl);
					saveButton.setEnabled(bl);
				}
			}

		});
		this.txtRevise.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				boolean bl = validate();
				getButton(IDialogConstants.OK_ID).setEnabled(bl);
				// 如果发送次数恒为0，且不是当前消息不是背景周期消息，则禁止删除按钮，否则禁止删除和保存按钮，modify by xuy
				if (txtNum.getText().equals("0"))
				{
					if (isBackgroundMsg())
					{
						delButton.setEnabled(bl);
						saveButton.setEnabled(bl);
					} else
					{
						delButton.setEnabled(false);
						saveButton.setEnabled(false);
					}
					;
				} else
				// 如果发送次数不为0，则根据其他条件是否禁止或使能删除和保存按钮，modify by xuy
				{
					delButton.setEnabled(bl);
					saveButton.setEnabled(bl);
				}
			}
		});
		this.cmbSendID.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				getButton(IDialogConstants.OK_ID).setEnabled(validate());
				Combo sendIdCmb = (Combo) e.getSource();
				if (sendIdCmb.getText().equals("1"))
				{
					delButton.setEnabled(false);
				}
			}

		});

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

					if (fields.size() != 0)
					{
						if (fields.get(0).getParent() instanceof Period)
						{
							Period pd = (Period) fields.get(0).getParent();
							try
							{
								clonePeriod = (Period) pd.clone();
							} catch (CloneNotSupportedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else
					{
						try
						{
							clonePeriod = (Period) clonePeriod.clone();
						} catch (CloneNotSupportedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					viewer.setInput(clonePeriod.getChildren());
					viewer.expandAll();
					viewer.refresh();
				}

				for (TableEditor editor : editors)
				{
					if (editor.getEditor() != null)
						editor.getEditor().dispose();
					editor.dispose();
				}

				boolean bl = validate();
				getButton(IDialogConstants.OK_ID).setEnabled(bl);
				// 如果发送次数恒为0，且不是当前消息不是背景周期消息，则禁止删除按钮，否则根据其他条件是否禁止或使能删除和保存按钮，modify
				// by xuy
				if (txtNum.getText().equals("0"))
				{
					if (isBackgroundMsg())
					{
						delButton.setEnabled(bl);
						saveButton.setEnabled(bl);
					} else
					{
						delButton.setEnabled(false);
						saveButton.setEnabled(bl);
					}
				} else
				// 如果发送次数不为0，则根据其他条件是否禁止或使能删除和保存按钮，modify by xuy
				{
					delButton.setEnabled(bl);
					saveButton.setEnabled(bl);
				}
				if (selectIndex == 0)
				{
					delButton.setEnabled(false);
				}
			}
		});
		// 如果发送次数恒为0，且不是当前消息不是背景周期消息，则禁止删除按钮，否则禁止删除和保存按钮，modify by xuy
		boolean bl = validate();
		if (txtNum.getText().equals("0"))
		{
			if (isBackgroundMsg())
			{
				delButton.setEnabled(bl);
				saveButton.setEnabled(bl);
			} else
			{
				delButton.setEnabled(false);
				saveButton.setEnabled(false);
			}
			;
		} else
		// 如果发送次数不为0，则根据其他条件是否禁止或使能删除和保存按钮，modify by xuy
		{
			delButton.setEnabled(bl);
			saveButton.setEnabled(bl);
		}

		if (this.cmbSendID.getText().equals("1"))
		{
			delButton.setEnabled(false);
		}
	}

	protected void showMsgName(Composite body)
	{
		Label label = new Label(body, SWT.RIGHT);
		label.setText(Messages.getString("I18N_ICD_MSG_NAME") + ":");
		GridData data = new GridData();
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
			TCFieldMap = TemplateUtils.getTestCaseFieldsMap(selectedSpteMsg.getMsg());
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
				TCFieldMap = TemplateUtils.getTestCaseFieldsMap(selectedSpteMsg.getMsg());
			}

		}
	}

	/**
	 * 消息周期标签
	 * 
	 * @param body </br> <b>作者</b> duyisen </br> <b>日期</b> 2012-4-1
	 */
	private void showPeriod(Composite body)
	{
		this.lblCycle = new Label(body, SWT.RIGHT);
		this.lblCycle.setText(Messages.getString("I18N_PERIOD") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblCycle.setLayoutData(data);

		this.txtCycle = new Text(body, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 175;
		this.txtCycle.setLayoutData(data);
		if (null != this.selectedSpteMsg.getMsg())
		{
			if (null != this.selectedSpteMsg.getMsg().getPeriodDuration())
			{
				txtCycle.setText(this.selectedSpteMsg.getMsg().getPeriodDuration() + "");
			} else
			{
				txtCycle.setText("");
			}

		}
	}

	private void showRivise(Composite body)
	{
		this.lblRevise = new Label(body, SWT.RIGHT);
		this.lblRevise.setText(Messages.getString("I18N_PERIOD_RIVISE") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblRevise.setLayoutData(data);

		this.txtRevise = new Text(body, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 175;
		this.txtRevise.setLayoutData(data);
		if (null != this.selectedSpteMsg.getMsg())
		{
			if (null != this.selectedSpteMsg.getMsg().getAmendValue())
			{
				txtRevise.setText(this.selectedSpteMsg.getMsg().getAmendValue() + "");
			} else
			{
				txtRevise.setText("");
			}

		}
	}

	private void showSendCount(Composite body)
	{
		this.lblNum = new Label(body, SWT.RIGHT);
		this.lblNum.setText(Messages.getString("I18N_SEND_CONUT") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblNum.setLayoutData(data);

		this.txtNum = new Text(body, SWT.LEFT | SWT.BORDER);
		data = new GridData();
		data.widthHint = 175;
		this.txtNum.setLayoutData(data);
		if (null != this.selectedSpteMsg.getMsg())
		{
			if (null != this.selectedSpteMsg.getMsg().getPeriodCount())
			{
				txtNum.setText(this.selectedSpteMsg.getMsg().getPeriodCount() + "");
				if (this.connModel instanceof BackgroundMsgMdl)
				{
					txtNum.setText("0");
					txtNum.setEditable(false);
				}
			} else
			{
				txtNum.setText("");
			}
		}
		// 只能输入数字
		this.txtNum.addVerifyListener(new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				if (connModel instanceof BackgroundMsgMdl)
				{
					e.doit = e.text.length() == 0 || Character.isDigit(e.text.charAt(0));
				}

			}
		});
	}

	private void showSendID(Composite body)
	{
		this.lblSendID = new Label(body, SWT.RIGHT);
		this.lblSendID.setText(Messages.getString("I18N_SEND_ID") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		this.lblSendID.setLayoutData(data);

		this.cmbSendID = new Combo(body, SWT.DROP_DOWN | SWT.BORDER | SWT.LEFT);
		data = new GridData();
		data.widthHint = 175;
		this.cmbSendID.setLayoutData(data);
		if (null != selectedSpteMsg)
		{
			if (!TCFieldMap.isEmpty())
			{
				String[] items = this.TCFieldMap.keySet().toArray(new String[0]);
				String tem = "";
				for (int i = 0; i < items.length; i++)
				{
					int s = Integer.parseInt(items[i]);
					for (int j = i + 1; j < items.length; j++)
					{
						int v = Integer.parseInt(items[j]);
						if (v < s)
						{
							tem = items[i];
							items[i] = items[j];
							items[j] = tem;
							s = v;
						}
					}
				}
				cmbSendID.setItems(items);
				cmbSendID.select(0);
			} else
			{
				cmbSendID.select(-1);
			}
		} else
		{
			cmbSendID.select(-1);
		}
		// 只能输入数字
		this.cmbSendID.addVerifyListener(new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				e.doit = e.text.length() == 0 || Character.isDigit(e.text.charAt(0));
			}

		});
	}

	private void showDelButton(Composite body)
	{
		this.delButton = new Button(body, SWT.CENTER);
		delButton.setText(Messages.getString("I18N_DELETE"));
		GridData data = new GridData();
		data.widthHint = 80;
		this.delButton.setLayoutData(data);

		this.delButton.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e)
			{
				setDelMessage();
				getButton(IDialogConstants.OK_ID).setEnabled(validate());

			}

			/**
			 * 删除按钮被点击后，更新对话框信息 author 杜一森 2011-5-6
			 */
			private void setDelMessage()
			{
				String num = txtNum.getText();
				if (Integer.valueOf(num) > 1)
				{// 判断周期消息发送次数是否为大于1，如果大于1

					int selected = sendIDoldSelect;
					int index = 0;
					if (selected > 0)
					{
						String[] sendIDitems = cmbSendID.getItems();
						String[] sendItems = new String[sendIDitems.length - 1];
						for (int i = 0; i < sendIDitems.length; i++)
						{
							if (i == selected)
							{// 删除当前选择的记录
								TCFieldMap.remove(cmbSendID.getItem(selected));
								continue;
							} else
							{
								sendItems[index] = sendIDitems[i];
							}
							index++;
						}
						cmbSendID.setItems(sendItems);
						cmbSendID.select(0);
						sendIDoldSelect = cmbSendID.getSelectionIndex();

					} else
					{
						delButton.setEnabled(false);
					}
					// 发送次数减1
					txtNum.setText(String.valueOf(Integer.valueOf(num) - 1));
				} else if (Integer.valueOf(num) == 1)
				{
					// 发送次数减1
					txtNum.setText(String.valueOf(Integer.valueOf(num) - 1));

				} else if (Integer.valueOf(num) == 0)
				{// 发送次数为0时；
					int selected = sendIDoldSelect;
					int index = 0;
					if (selected > 0)
					{
						String[] sendIDitems = cmbSendID.getItems();
						String[] sendItems = new String[sendIDitems.length - 1];
						for (int i = 0; i < sendIDitems.length; i++)
						{
							if (i == selected)
							{// 删除当前选择的记录
								TCFieldMap.remove(cmbSendID.getItem(selected));
								continue;
							} else
							{
								sendItems[index] = sendIDitems[i];
							}
							index++;
						}
						cmbSendID.setItems(sendItems);
						cmbSendID.select(0);
						sendIDoldSelect = cmbSendID.getSelectionIndex();
					}

					delButton.setEnabled(false);
				}

				String key = cmbSendID.getItem(sendIDoldSelect);
				List<Entity> fields = TCFieldMap.get(key);
				if (fields.size() != 0)
				{
					if (fields.get(0).getParent() instanceof Period)
					{
						Period pd = (Period) fields.get(0).getParent();
						try
						{
							clonePeriod = (Period) pd.clone();
						} catch (CloneNotSupportedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else
				{
					try
					{
						clonePeriod = (Period) clonePeriod.clone();
					} catch (CloneNotSupportedException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				viewer.setInput(clonePeriod.getChildren());
				viewer.expandAll();
				viewer.refresh();

				setMessage(Messages.getString("I18N_DELETE_SUCCESS"));
				if (!delButton.isEnabled())
				{
					setMessage(null);
				}
			}

		});
	}

	private void showSaveButton(Composite body)
	{
		this.saveButton = new Button(body, SWT.CENTER);
		saveButton.setText(Messages.getString("I18N_SAVE"));
		GridData data = new GridData();
		data.widthHint = 80;
		this.saveButton.setLayoutData(data);
		this.saveButton.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				String times = txtNum.getText();
				List<String> itemList = new ArrayList<String>();
				String sendID;
				if (!StringUtils.isNull(times))
				{
					sendID = cmbSendID.getText();
					for (String item : cmbSendID.getItems())
					{
						if (!StringUtils.isNull(item))
						{
							itemList.add(item);
						}
					}
					String[] test = null;
					if (!itemList.contains(sendID))
					{
						itemList.add(sendID);
						test = itemList.toArray(new String[0]);
						for (int i = 0; i < test.length; i++)
						{
							String temp = test[i];
							for (int j = i; j < test.length; j++)
							{
								if (Integer.valueOf(test[j]) < Integer.valueOf(test[i]))
								{
									test[i] = test[j];
									test[j] = temp;
									temp = test[i];
								}
							}
						}
					} else
					{
						test = itemList.toArray(new String[0]);
					}

					cmbSendID.setItems(test);
					int index = 0;
					for (String send : test)
					{
						if (send.equals(sendID))
						{
							break;
						}
						index++;
					}
					// 记录发送数据
					TCFieldMap.put(sendID, getFieldList());

					cmbSendID.select(index);
					sendIDoldSelect = index;
					List<Entity> fields = TCFieldMap.get(cmbSendID.getItem(index));
					if (fields.size() != 0)
					{
						if (fields.get(0).getParent() instanceof Period)
						{
							Period pd = (Period) fields.get(0).getParent();
							try
							{
								clonePeriod = (Period) pd.clone();
							} catch (CloneNotSupportedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else
					{
						try
						{
							clonePeriod = (Period) clonePeriod.clone();
						} catch (CloneNotSupportedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					viewer.setInput(clonePeriod.getChildren());
					viewer.expandAll();
					viewer.refresh();

					for (TableEditor editor : editors)
					{
						if (editor.getEditor() != null)
							editor.getEditor().dispose();
						editor.dispose();
					}

					setMessage(Messages.getString("I18N_SAVE_SUCCESS"));
					getButton(IDialogConstants.OK_ID).setEnabled(validate());

				}

			}

		});
	}

	protected void okPressed()
	{
		SPTEMsg tcSpteMsg = null;
		tcSpteMsg = selectedSpteMsg;
		tcSpteMsg.getMsg().setUuid(StringUtils.getUUID());
		if (this.connModel instanceof BackgroundMsgMdl)
		{
			tcSpteMsg.getMsg().setBackground(true);
		} else
		{
			tcSpteMsg.getMsg().setBackground(false);
		}

		tcSpteMsg.getMsg().setPeriodCount(Integer.valueOf(StringUtils.isNull(this.txtNum.getText()) ? "0" : this.txtNum.getText()));
		tcSpteMsg.getMsg().setPeriodDuration(Integer.valueOf(StringUtils.isNull(this.txtCycle.getText()) ? "0" : this.txtCycle.getText()));
		tcSpteMsg.getMsg().setAmendValue(StringUtils.isNull(this.txtRevise.getText()) ? "0" : this.txtRevise.getText());
		ICDMsg icdMsg = tcSpteMsg.getICDMsg();
		List<Integer> destIDs = icdMsg.getDestIDs();
		StringBuilder sb = new StringBuilder("");
		for (Integer destID : destIDs)
		{
			if (destIDs.indexOf(destID) != 0)
			{
				sb.append(",");
			}
			sb.append(destID);
		}
		tcSpteMsg.getMsg().setDesId(sb.toString());

		if (sendIDoldSelect != -1 && !cmbSendID.getText().equals(""))
		{
			String key = cmbSendID.getText();
			boolean has = false;
			List<String> items = new ArrayList<String>();
			TCFieldMap.put(key, getFieldList());
			for (String it : cmbSendID.getItems())
			{
				if (it.equals(key))
				{
					has = true;
					break;
				}
			}
			if (!has)
			{
				String[] sendItems = cmbSendID.getItems();
				for (int i = 0; i < sendItems.length; i++)
				{
					if (i == 0)
					{
						items.add(sendItems[i]);
						continue;
					} else
					{
						int k = Integer.parseInt(key);
						int m = Integer.parseInt(sendItems[i - 1]);
						int n = Integer.parseInt(sendItems[i]);
						if (m < k && k < n)
						{
							items.add(key);
							items.add(sendItems[i]);
						} else
						{
							items.add(sendItems[i]);
							if (i == sendItems.length - 1)
							{
								items.add(key);
							}
						}
					}
				}
				String[] IDs = items.toArray(new String[0]);
				cmbSendID.setItems(IDs);
			}
		}

		String[] periods = cmbSendID.getItems();
		List<Entity> childrenList = new ArrayList<Entity>();
		// 保存period
		for (String period : periods)
		{
			Period tperiod = new Period();
			tperiod.setValue(Integer.parseInt(period));
			tperiod.setChildren(TCFieldMap.get(period));
			tperiod.setParent(tcSpteMsg.getMsg());
			childrenList.add(tperiod);
		}
		tcSpteMsg.getMsg().setChildren(childrenList);

		CfgMsgCmd command = new CfgMsgCmd();
		command.setResultMessage(tcSpteMsg);
		command.setModel(this.connModel);
		this.message = tcSpteMsg.getMsg().getName();

		SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
		editor.getEditDomain().getCommandStack().execute(command);
		this.editors.clear();

		super.okPressed();

	}

	/**
	 * 设置对话框title author 杜一森 2011-5-9
	 */
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		if (this.type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
		{
			if (this.connModel instanceof BackgroundMsgMdl)
			{
				newShell.setText(Messages.getString("I18N_EDIT_BACK_CYLCE_SEND_MSG"));
			} else
			{
				newShell.setText(Messages.getString("I18N_EDIT_CYLCE_SEND_MSG"));
			}
		} else
		{
			if (this.connModel instanceof BackgroundMsgMdl)
			{
				newShell.setText(Messages.getString("I18N_DLG_TITLE_EDIT_BACK_CYCLE_RECV"));
			} else
			{
				newShell.setText(Messages.getString("I18N_DLG_TITLE_EDIT_CYCLE_RECV"));
			}
		}

	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		if (this.type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
		{
			if (this.connModel instanceof BackgroundMsgMdl)
			{
				setTitle(Messages.getString("I18N_EDIT_BACK_CYLCE_SEND_MSG"));
			} else
			{
				setTitle(Messages.getString("I18N_EDIT_CYLCE_SEND_MSG"));
			}

		} else
		{
			if (this.connModel instanceof BackgroundMsgMdl)
			{
				setTitle(Messages.getString("I18N_DLG_TITLE_EDIT_BACK_CYCLE_RECV"));
			} else
			{
				setTitle(Messages.getString("I18N_DLG_TITLE_EDIT_CYCLE_RECV"));
			}
		}

		setMessage(Messages.getString("I18N_SET_ARGS"));
		getButton(IDialogConstants.OK_ID).setEnabled(!isNull());
		getButton(IDialogConstants.OK_ID).setEnabled(validate());
		return control;
	}

	/**
	 * 通过通过tabItem获取TCSignalBean的链表 记录测试用例中TCSignalBean的信息
	 * 
	 * @author 杜一森 2011-6-11
	 * @return
	 */
	public List<Entity> getFieldList()
	{
		List<Entity> tcFieldList = new ArrayList<Entity>();
		tcFieldList.addAll(this.clonePeriod.getChildren());
		return tcFieldList;
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
		List<Entity> Fields = new ArrayList<Entity>();
		SPTEMsg msg = this.selectedSpteMsg;
		for (Entity en : msg.getMsg().getChildren())
		{
			if (en instanceof Period)
			{
				if (((Period) en).getValue() == Integer.parseInt(cmbSendID.getText()))
				{
					try
					{
						this.clonePeriod = (Period) ((Period) en).clone();
					} catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
					}
					Fields.addAll(clonePeriod.getChildren());
				}
			}
		}
		getTabControl(area, Fields);
		viewer.getTableTree().addSelectionListener(new ClientSelectionListener());
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
						if (str.equals(""))
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

	private void handleMsgNameSelect(Combo com)
	{
		Combo cmbMsgName = com;
		int index = cmbMsgName.getSelectionIndex();
		SPTEMsg msg = spteMsgs.get(index);
		if (msg.equals(spteMsg))
		{
			if (null != msg)
			{
				try
				{
					cloneMsg = (Message) spteMsg.getMsg().clone();
				} catch (CloneNotSupportedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				selectedSpteMsg = new SPTEMsg(cloneMsg, msg.getICDMsg());
			}
		} else if (!msg.equals(selectedSpteMsg))
		{
			if (null != msg)
			{
				try
				{
					cloneMsg = (Message) msg.getMsg().clone();
				} catch (CloneNotSupportedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				selectedSpteMsg = new SPTEMsg(cloneMsg, msg.getICDMsg());
			}
		}
		ICDMsg icdMsg = selectedSpteMsg.getICDMsg();
		if (null != icdMsg)
		{
			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TOPIC_NAME))
			{// 主题名称
				lblTopic.setText(icdMsg.getAttribute(Constants.ICD_MSG_TOPIC_NAME).getValue() + "");
			} else
			{
				lblTopic.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_ID))
			{// 消息ID
				lblMsgID.setText(icdMsg.getAttribute(Constants.ICD_MSG_ID).getValue() + "");
			} else
			{
				lblMsgID.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_BROCAST))
			{// 广播属性
				lblBdCast.setText(icdMsg.getAttribute(Constants.ICD_MSG_BROCAST).getValue() + "");
			} else
			{
				lblBdCast.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_SRC_ID))
			{// 源功能ID
				int srcID = Integer.parseInt(icdMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString());
				String srcName = TemplateUtils.getFunctionName(clazzManager, srcID, testedObjectsOfICD.get(0).getClass());
				lblSrcText.setText(srcName);
			} else
			{
				lblSrcText.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TRANS_PERIOD) && isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_PERIOD_UNIT))
			{// 消息传输周期
				String unit = icdMsg.getAttribute(Constants.ICD_MSG_PERIOD_UNIT).getValue().toString();
				lblMsgTransPeriod.setText(StringUtils.concat(icdMsg.getAttribute(Constants.ICD_MSG_TRANS_PERIOD).getValue(), " ", unit));
			} else
			{
				lblMsgTransPeriod.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_MAX_TRANS_DELAY) && isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_DELAY_UNIT))
			{// 消息最大迟延以及单位
				String unit = icdMsg.getAttribute(Constants.ICD_MSG_DELAY_UNIT).getValue().toString();
				lblMaxTrans.setText(StringUtils.concat(icdMsg.getAttribute(Constants.ICD_MSG_MAX_TRANS_DELAY).getValue(), " ", unit));
			} else
			{
				lblMaxTrans.setText("");
			}

			if (isNotNullAttAndValue(icdMsg, Constants.ICD_MSG_TRANS_TYPE))
			{// 消息类型
				lblMsgTransType.setText(icdMsg.getAttribute(Constants.ICD_MSG_TRANS_TYPE).getValue() + "");
			} else
			{
				lblMsgTransType.setText("");
			}
			if (null != icdMsg.getDestIDs() && icdMsg.getDestIDs().size() != 0)
			{// 消息目的
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
		for (TableEditor editor : editors)
		{
			if (editor.getEditor() != null)
				editor.getEditor().dispose();
			editor.dispose();
		}
		editors.clear();
		TCFieldMap = TemplateUtils.getTestCaseFieldsMap(selectedSpteMsg.getMsg());

		List<Entity> fields = TCFieldMap.get(cmbSendID.getItem(sendIDoldSelect));

		if (fields.size() != 0)
		{
			if (fields.get(0).getParent() instanceof Period)
			{
				Period pd = (Period) fields.get(0).getParent();
				try
				{
					clonePeriod = (Period) pd.clone();
				} catch (CloneNotSupportedException e1)
				{
					e1.printStackTrace();
				}
			}
		} else
		{
			try
			{
				clonePeriod = (Period) clonePeriod.clone();
			} catch (CloneNotSupportedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		viewer.setInput(clonePeriod.getChildren());
		viewer.expandAll();
		viewer.refresh();

		for (TableEditor editor : editors)
		{
			Control control = editor.getEditor();
			if (control instanceof Text)
			{
				Text text = (Text) control;
				text.setText("");
			}
		}

		if (!TCFieldMap.isEmpty())
		{
			String[] items = TCFieldMap.keySet().toArray(new String[0]);
			String tem = "";
			for (int i = 0; i < items.length; i++)
			{
				int s = Integer.parseInt(items[i]);
				for (int j = i + 1; j < items.length; j++)
				{
					int v = Integer.parseInt(items[j]);
					if (v < s)
					{
						tem = items[i];
						items[i] = items[j];
						items[j] = tem;
						s = v;
					}
				}
			}
			cmbSendID.setItems(items);
			cmbSendID.select(0);
		} else
		{
			cmbSendID.select(-1);
		}

		if (null != selectedSpteMsg.getMsg())
		{
			if (null != selectedSpteMsg.getMsg().getPeriodCount())
			{// 发送次数
				txtNum.setText(selectedSpteMsg.getMsg().getPeriodCount() + "");
				if (connModel instanceof BackgroundMsgMdl)
				{
					txtNum.setText("0");
					txtNum.setEditable(false);
				}
			} else
			{
				txtNum.setText("");
			}
			if (null != selectedSpteMsg.getMsg().getPeriodDuration())
			{// 周期
				txtCycle.setText(selectedSpteMsg.getMsg().getPeriodDuration() + "");
			} else
			{
				txtCycle.setText("");
			}

			if (null != selectedSpteMsg.getMsg().getAmendValue())
			{// 修正值
				txtRevise.setText(selectedSpteMsg.getMsg().getAmendValue() + "");
			} else
			{
				txtRevise.setText("");
			}

		}

	}

	private class TextModifyListener implements ModifyListener
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
			if (text.getText().equals("") || text.getText().equals("null"))  //无输入时，将部分表格制空
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
				saveButton.setEnabled(false);
				if (null != getButton(IDialogConstants.OK_ID))
				{
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
			}
			// 当输入的预期值不是正整数时，判断是否包含$value,modify by xuy
			else if (!StringUtils.isPositiveInteger(text.getText()))
			{ // 判断预期值是否包含$value时，且格式正确
				String txtValue = text.getText();
				if (StringUtils.isPeriodValueRight(txtValue) || Utils.isSendValueRight(txtValue))
				{
					int sendIDIndex = Integer.parseInt(cmbSendID.getText());
					if (StringUtils.isPeriodValueRight(txtValue) || sendIDIndex > 1)
					{
						// 使对应的二进制和十六进制变为空，modify By xuy
						this.tableItem.setText(2, "");
						this.tableItem.setText(3, "");
						this.field.setValue(text.getText());
						setErrorMessage(null);
						saveButton.setEnabled(true);
						if (null != getButton(IDialogConstants.OK_ID))
						{
							getButton(IDialogConstants.OK_ID).setEnabled(true);
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
						saveButton.setEnabled(false);
						if (null != getButton(IDialogConstants.OK_ID))
						{
							getButton(IDialogConstants.OK_ID).setEnabled(false);
						}
						this.tableItem.setText(2, "");
						this.tableItem.setText(3, "");
						this.field.setValue("");
					}

				} else
				{// 输入不正确时，显示错误信息，使对应的二进制和十六进制变为空，modify By xuy
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
						saveButton.setEnabled(false);
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
							saveButton.setEnabled(false);
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

	/*
	 * 判断是否为背景周期消息 modify by xuy
	 */
	private boolean isBackgroundMsg()
	{
		if (this.connModel instanceof BackgroundMsgMdl)
			return true;
		return false;
	}

	/**
	 * 判断参数设置是否完整 author 杜一森 2011-5-9
	 * 
	 * @return 返回是否为空
	 */
	private boolean isNull()
	{
		String txtNum = this.txtNum.getText();
		String txtCycle = this.txtCycle.getText();
		if (StringUtils.isNull(txtCycle))
		{
			setMessage(Messages.getString("I18N_SET_ARGS"));
			return true;
		}
		if (StringUtils.isNotNull(txtNum))
		{
			int num = Integer.valueOf(txtNum);
			if (num > 0)
			{
				String sendID = this.cmbSendID.getText();
				if (StringUtils.isNull(sendID))
				{
					setMessage(Messages.getString("I18N_SET_ARGS"));
					return true;
				}
			} else
			{
				setErrorMessage(null);
				setMessage(null);
				return false;
			}
		} else
		{
			setMessage(Messages.getString("I18N_SET_ARGS"));
			return true;
		}
		setErrorMessage(null);
		setMessage(null);
		return false;
	}

	/**
	 * 检查输入信息的合法性 author 杜一森 2011-5-8
	 * 
	 * @return 合法性
	 */
	@SuppressWarnings("deprecation")
	public boolean validate()
	{
		String txtNum = this.txtNum.getText();
		String txtCycle = this.txtCycle.getText();
		String txtrevise = this.txtRevise.getText();
		if (StringUtils.isNull(txtCycle))
		{
			setErrorMessage(Messages.getString("I18N_PERIOD_MUST_NOT_NULL"));
			return false;
		} else if (StringUtils.isNotNull(txtCycle))
		{ // 验证周期是否为整数时,modify by xuy
			if (!StringUtils.isPositiveInteger(txtCycle))
			{
				setErrorMessage(Messages.getString("I18N_PERIOD_MUST_POS_INT"));
				return false;
			} else
			{
				int times = Integer.parseInt(txtCycle);
				String str = lblMsgTransPeriod.getText();
				if (StringUtils.isNotNull(str))
				{
					if (str.contains(transPeriodUnit))
					{
						str = str.substring(0, str.indexOf(transPeriodUnit)).trim();
						int transPeriod = Integer.parseInt(str);
						if (times < transPeriod)
						{
							setErrorMessage(Messages.getString("I18N_PERIOD_MUST_GTOE_TRANS"));
							return false;
						}
					}
				}
			}

		}
		if (StringUtils.isNull(txtrevise))
		{
			setErrorMessage(Messages.getString("I18N_REVISE_ILLEGAL"));
			return false;
		} else if (StringUtils.isNotNull(txtrevise))
		{// 验证修正值不为整数时，modify by xuy
			if (!StringUtils.isTxtReviseRight(txtrevise))
			{
				setErrorMessage(Messages.getString("I18N_REVISE_ILLEGAL"));
				return false;
			}

		}
		if (StringUtils.isNotNull(txtNum))
		{
			// 验证发送次数是否为整数
			if (!StringUtils.isPositiveInteger(txtNum))
			{
				setErrorMessage(Messages.getString("I18N_SEND_NUM_MUST_POS_INT"));
				return false;
			}
			int num = Integer.valueOf(txtNum);

			if (this.connModel instanceof BackgroundMsgMdl)
			{
				if (num > 0)
				{
					setErrorMessage(Messages.getString("I18N_BACK_MSG_SEND_NUM_MUST_ZORE"));
					return false;
				}
			} else
			{
				if (num == 0 && !this.type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
				{
					setErrorMessage(Messages.getString("I18N_NOT_BACK_MSG_SEND_NUM_MUST_NOT_ZORE"));
					return false;
				}
			}

			if (num > 0)
			{
				String sendID = this.cmbSendID.getText();
				if (StringUtils.isNotNull(sendID))
				{
					int sendNum = Integer.valueOf(sendID);
					if (sendNum > num)
					{
						setMessage(Messages.getString("I18N_SET_ARGS"));
						setErrorMessage(Messages.getString("I18N_SEND_NUM_MUST_LESS_PERIOD"));
						return false;
					}
				} else
				{
					setMessage(Messages.getString("I18N_SET_ARGS"));
					setErrorMessage(Messages.getString("I18N_SEND_NUM_MUST_NOT_NULL"));
					return false;
				}
			}
		} else
		{
			setMessage(Messages.getString("I18N_SET_ARGS"));
			setErrorMessage(Messages.getString("I18N_PERIOD_NUM_MUST_NOT_NULL"));
			return false;
		}

		// 验证视图集合里面的发送值是否为空,modify by xuy
		if (viewer != null)
		{
			TableTree tableTree = viewer.getTableTree();
			int itemNum = tableTree.getItemCount();
			for (int i = 0; i < itemNum; ++i)
			{
				String text = tableTree.getItem(i).getText(1);
				if (StringUtils.isNull(text) || text.equals("null"))
				{
					if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
					{
						setErrorMessage(Messages.getString("I18N_INPUT_ERR_SEND_VAL"));
					} else
					{
						setErrorMessage(Messages.getString("I18N_ERR_EXP_VAL"));
					}
					return false;
				} else
				{
					String value = this.cmbSendID.getText();
					if (!StringUtils.isNull(value))
					// 判断发送值是否为整数
					{
						if (!(StringUtils.isPeriodValueRight(text) || Utils.isSendValueRight(text)))
						{
							Object inputData = tableTree.getItem(i).getData();
							Field field = (Field) inputData;
							ICDField icdField = TemplateUtils.getICDField(selectedSpteMsg, field);
							if (!(icdField.getIcdEnums() != null && icdField.getIcdEnums().size() > 0 && icdField.getAttribute(Constants.ICD_FIELD_TYPE).getValue().equals("ENUM")))
							{
								if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
								{
									setErrorMessage(Messages.getString("I18N_INPUT_ERR_SEND_VAL"));
								} else
								{
									setErrorMessage(Messages.getString("I18N_ERR_EXP_VAL"));
								}
								return false;
							}

						} else
						{// FIXME:还没添加对最大值最小值的判断
							// String width=table.getItem(i).getText(2);
							// if(StringUtils.isPositiveInteger(width)&&StringUtils.isPositiveInteger(text))
							// { int n=Integer.valueOf(width);
							// int maxLength=StringUtils.numSize(n);
							// try{
							// int currentLength=Integer.valueOf(text);
							// if(currentLength>maxLength)
							// {
							// setErrorMessage("发送值超过了所规定的最大宽度！");
							// return false;
							// }
							// }catch(NumberFormatException e)
							// {
							// setErrorMessage("发送值超过了所规定的最大宽度！");
							// return false;
							// }
							// }
						}
					}
				}
			}
		}
		setErrorMessage(null);
		setMessage(null);
		return true;
	}

}
