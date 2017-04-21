/****************************************
 * �����������ɼ������޹�˾��Ȩ����
 * www.coretek.com.cn
 ***************************************/
package com.coretek.spte.core.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * ��ʾ������Ϣ��ִ�н��
 * 
 * @author ���Ρ 2012-3-12
 */
public abstract class PeriodResultDialog extends MsgDialog
{

	/** �������� */
	protected Label						lblNum;

	protected Text						txtNum;

	/** ���ͱ�� */
	protected Label						lblSendID;

	protected Combo						cmbSendID;

	/** ���淢�ͱ�źͶ�ӦField����Ĺ�ϣ�� */
	protected Map<String, List<Entity>>	TCFieldMap	= new HashMap<String, List<Entity>>();

	/** ��¼���ͱ�ŵ�ѡ�� */
	protected static int				sendIDoldSelect;

	protected Message					cloneMsg;

	protected Period					clonePeriod;

	protected CompareResult				compareResult;

	protected SPTEMsg					spteMsgFormDB;

	public PeriodResultDialog(Shell shell, Result result, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjectsOfICD, CompareResult compareResult)
	{
		super(shell, result, spteMsg, icdManager, testedObjectsOfICD);

	}

	/**
	 * ��ʾ����ֵ
	 * 
	 * @param body
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-12
	 */
	protected Text showRevise(Composite body, SPTEMsg selectedSpteMsg)
	{
		Label lblRevise = new Label(body, SWT.RIGHT);
		lblRevise.setText(Messages.getString("I18N_PERIOD_RIVISE") + ":");
		GridData data = new GridData();
		data.widthHint = 80;
		lblRevise.setLayoutData(data);

		Text txtRevise = new Text(body, SWT.LEFT | SWT.BORDER);
		txtRevise.setEditable(false);
		data = new GridData();
		data.widthHint = 175;
		txtRevise.setLayoutData(data);
		if (null != selectedSpteMsg.getMsg())
		{
			if (null != selectedSpteMsg.getMsg().getAmendValue())
			{
				txtRevise.setText(selectedSpteMsg.getMsg().getAmendValue().toString());
			} else
			{
				txtRevise.setText(StringUtils.EMPTY_STRING);
			}

		}

		return txtRevise;
	}

	/**
	 * ��ʾ����
	 * 
	 * @param body
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-12
	 */
	protected Text showCycle(Composite body, SPTEMsg selectedSpteMsg)
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
		if (null != selectedSpteMsg.getMsg())
		{
			if (null != selectedSpteMsg.getMsg().getPeriodDuration())
			{
				txtCycle.setText(selectedSpteMsg.getMsg().getPeriodDuration().toString());
			} else
			{
				txtCycle.setText(StringUtils.EMPTY_STRING);
			}

		}

		return txtCycle;
	}

}