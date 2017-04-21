/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

import org.eclipse.swt.widgets.Shell;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.core.dialogs.TRRecMsgDlg;
import com.coretek.spte.core.dialogs.TRRecPeriodMsgDlg;
import com.coretek.spte.core.dialogs.TRSendMsgDlg;
import com.coretek.spte.core.dialogs.TRSendPeriodMsgDlg;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.dialogs.MonitorMsgDlg;
import com.coretek.spte.monitor.dialogs.MonitorPeriodMsgDlg;
import com.coretek.spte.testcase.Message;

/**
 * ���ڴ�����չʾ�Ի���򿪲���
 * 
 * @author ���Ρ 2012-3-8
 */
public class ResultDialogHandler
{

	/**
	 * ���ݵ����ߴ���Ĳ������ɴ˺�����������һ�ֽ��չʾ�Ի���
	 * 
	 * @param shell
	 * @param spteMsg
	 * @param icdManager
	 * @param testedObjects </br>
	 */
	public static void open(Shell shell, Result result, SPTEMsg spteMsg, ClazzManager icdManager, List<Entity> testedObjects, CompareResult compareResult)
	{
		Message msg = spteMsg.getMsg();
		if (msg.isSend())
		{// ��ϢΪ������Ϣ
			if (msg.isPeriodMessage())
			{// ��ϢΪ���ڷ�����Ϣ
				TRSendPeriodMsgDlg dlg = new TRSendPeriodMsgDlg(shell, spteMsg, icdManager, testedObjects, compareResult, result);
				dlg.open();
			} else
			{// ��ϢΪ�����ڷ�����Ϣ
				TRSendMsgDlg dlg = new TRSendMsgDlg(shell, spteMsg, icdManager, testedObjects);
				dlg.open();
			}
		} else if (msg.isRecv())
		{// ������Ϣ
			if (msg.isPeriodMessage())
			{// ��ϢΪ���ڽ�����Ϣ
				TRRecPeriodMsgDlg dlg = new TRRecPeriodMsgDlg(shell, spteMsg, icdManager, testedObjects, compareResult, result);
				dlg.open();
			} else
			{// ��ϢΪ�����ڽ�����Ϣ
				TRRecMsgDlg dlg = new TRRecMsgDlg(shell, result, spteMsg, icdManager, testedObjects);
				dlg.open();
			}
		}
	}

	/**
	 * ����չʾ��ص���Ϣ����Ϊ��ص���Ϣ����Ҫ���ַ�����Ϣ�ͽ�����Ϣ��
	 * 
	 * @param shell
	 * @param result
	 * @param icdManager
	 */
	public static void open(Shell shell, Result result, ClazzManager icdManager)
	{
		Message msg = result.getSpteMsg().getMsg();
		if (msg.isPeriodMessage())
		{
			MonitorPeriodMsgDlg dlg = new MonitorPeriodMsgDlg(shell, icdManager, result);
			dlg.open();
		} else
		{
			MonitorMsgDlg dlg = new MonitorMsgDlg(shell, result.getSpteMsg(), icdManager);
			dlg.open();
		}

	}

}
