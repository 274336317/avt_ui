package com.coretek.spte.core.dialogs;

import java.util.List;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDMsg;
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
import com.coretek.spte.testcase.Message;

public class NewMsgDlg extends MessageDlg
{

	public NewMsgDlg(Shell parentShell)
	{
		super(parentShell);
	}

	public NewMsgDlg(Shell shell, AbtConnMdl connectionModel, AbstractConnectionEditPart part, String type)
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
	 * 消息编辑tab
	 * 
	 * @author duys
	 * @date 2011-6-8
	 * @param area
	 */
	private void createMessageInfoTabItems(Composite area)
	{
		List<Entity> icdFieldList = cloneMsg.getChildren();
		getTabControl(area, icdFieldList);
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
				// TODO Auto-generated method stub

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

	}

	/**
	 * 设置对话框title author 杜一森 2011-5-9
	 */
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
		{
			newShell.setText(Messages.getString("I18N_EDIT_SEND_MSG"));
		} else
		{
			newShell.setText(Messages.getString("I18N_DLG_TITLE_EDIT_RECV"));
		}

	}

	@Override
	protected Control createContents(Composite parent)
	{
		Control control = super.createContents(parent);
		if (type.equals(SPTEConstants.MESSAGE_TYPE_SEND))
		{
			setTitle(Messages.getString("I18N_EDIT_SEND_MSG"));
		} else
		{
			setTitle(Messages.getString("I18N_DLG_TITLE_EDIT_RECV"));
		}
		if (hasLieError)
		{
			if (null != getButton(IDialogConstants.OK_ID))
			{
				getButton(IDialogConstants.OK_ID).setEnabled(false);
			}
		}
		return control;
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
		CfgMsgCmd command = new CfgMsgCmd();
		command.setResultMessage(tcSpteMsg);
		command.setModel(this.connModel);
		this.message = tcSpteMsg.getMsg().getName();

		SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
		editor.getEditDomain().getCommandStack().execute(command);
		this.editors.clear();

		super.okPressed();
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
		viewer.setInput(cloneMsg.getChildren());
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
	}

}
