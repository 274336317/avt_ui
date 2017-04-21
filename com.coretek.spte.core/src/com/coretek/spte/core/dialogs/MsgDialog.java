/****************************************
 * 北京科银京成技术有限公司版权所有
 * www.coretek.com.cn
 ***************************************/
package com.coretek.spte.core.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Message;

/**
 * 消息展示对话框
 * 
 * @author 孙大巍 2012-3-12
 */
public abstract class MsgDialog extends TitleAreaDialog
{

	protected TableTreeViewer	viewer;

	protected List<SPTEMsg>		spteMsgs	= new ArrayList<SPTEMsg>();

	protected SPTEMsg			spteMsg;

	protected String			type;

	protected ClazzManager		icdManager;

	protected List<Entity>		testedObjectsOfICD;

	protected Message			cloneMsg;

	protected boolean			hasLieError	= false;

	protected Result			result;

	/**
	 * @param parentShell
	 */
	public MsgDialog(Shell shell, Result result, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD)
	{
		super(shell);
		this.spteMsg = spteMsg;
		this.icdManager = icdManager;
		this.testedObjectsOfICD = testedObjectsOfICD;

		this.type = SPTEConstants.MESSAGE_TYPE_SEND;
		List<SPTEMsg> spteMsgList = new ArrayList<SPTEMsg>();
		List<SPTEMsg> msgList;
		try
		{
			msgList = TemplateUtils.filterAllNodeMsg(icdManager, testedObjectsOfICD, true, false);
			spteMsgList = TemplateUtils.filterSpteMsgOfPeriod(msgList, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		spteMsgs = spteMsgList;
		this.result = result;
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
	protected Label showDestIDs(Composite body, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD)
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
				String desName = StringUtils.EMPTY_STRING;
				for (int desID : msg.getDestIDs())
				{
					desName = new StringBuilder(desName).append(TemplateUtils.getFunctionName(icdManager, desID, testedObjectsOfICD.get(0).getClass())).append("、").toString();

				}
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
	protected Label showMsgSrc(Composite body, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD)
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
				int srcID = Integer.parseInt(srcAtt.getValue().toString());
				String srcName = TemplateUtils.getFunctionName(icdManager, srcID, testedObjectsOfICD.get(0).getClass());
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