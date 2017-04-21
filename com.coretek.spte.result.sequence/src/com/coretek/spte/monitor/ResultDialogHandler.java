/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 用于处理结果展示对话框打开操作
 * 
 * @author 孙大巍 2012-3-8
 */
public class ResultDialogHandler
{

	/**
	 * 根据调用者传入的参数，由此函数决定打开哪一种结果展示对话框
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
		{// 消息为发送消息
			if (msg.isPeriodMessage())
			{// 消息为周期发送消息
				TRSendPeriodMsgDlg dlg = new TRSendPeriodMsgDlg(shell, spteMsg, icdManager, testedObjects, compareResult, result);
				dlg.open();
			} else
			{// 消息为非周期发送消息
				TRSendMsgDlg dlg = new TRSendMsgDlg(shell, spteMsg, icdManager, testedObjects);
				dlg.open();
			}
		} else if (msg.isRecv())
		{// 接收消息
			if (msg.isPeriodMessage())
			{// 消息为周期接收消息
				TRRecPeriodMsgDlg dlg = new TRRecPeriodMsgDlg(shell, spteMsg, icdManager, testedObjects, compareResult, result);
				dlg.open();
			} else
			{// 消息为非周期接收消息
				TRRecMsgDlg dlg = new TRRecMsgDlg(shell, result, spteMsg, icdManager, testedObjects);
				dlg.open();
			}
		}
	}

	/**
	 * 用于展示监控的消息，因为监控的消息不需要区分发送消息和接收消息。
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
