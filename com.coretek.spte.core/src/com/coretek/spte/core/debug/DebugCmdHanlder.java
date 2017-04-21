package com.coretek.spte.core.debug;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.TestResultManager;
import com.coretek.spte.core.debug.actions.DebugActionsGroup;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.editor.actions.toolbar.SPTETBContributor;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.reference.ReferingCase;
import com.coretek.spte.core.util.TestCaseStatus;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.monitor.manager.ExecutorSession;

/**
 * 调试命令响应模块。每个editor都有一个调试命令响应模块的实例。
 * 
 * @author 孙大巍
 */
public class DebugCmdHanlder implements IPropertyChangeListener, PropertyChangeListener
{

	private final static Logger logger = LoggingPlugin.getLogger(DebugCmdHanlder.class);

	// 上一次执行的消息
	private RootContainerMdl rootContainer;

	private MsgConnMdl previousMsg;

	// 正在被debug的editor
	private SPTEEditor currentEditor;

	// 画面debug按钮组
	private DebugActionsGroup actionsGroup;

	// 测试用例
	private ReferingCase refering;

	// debug开始时间
	private String debugStartTime;

	/**
	 * @param currentFile 当前正在被调试的cas文件
	 * @param currentEditor 当前正在被调试的编辑器
	 */
	public DebugCmdHanlder(IFile currentFile, SPTEEditor currentEditor)
	{
		this.rootContainer = currentEditor.getRootContainerMdl();
		this.currentEditor = currentEditor;

		this.refering = new ReferingCase(currentFile.getProject(), currentFile.getProject().getFolder(currentFile.getParent().getName()), currentFile);
	}

	public void reset()
	{
		this.previousMsg = null;
	}

	public RootContainerMdl getRootContainer()
	{

		return rootContainer;
	}

	public MsgConnMdl getPreviousMsg()
	{

		return previousMsg;
	}

	public void propertyChange(PropertyChangeEvent event)
	{
		Object newValue = event.getNewValue();
		if (!(newValue instanceof DebugCmd))
		{
			return;
		}

		if (event.getNewValue() == DebugCmd.Begin)
		{
			// 开启调试
			debugStartTime = StringUtils.getCurrentTime();

			// 得到debug的按键组
			SPTETBContributor contributor = (SPTETBContributor) currentEditor.getEditorSite().getActionBarContributor();
			actionsGroup = contributor.getActionsGroup();

			this.rootContainer.setStatus(TestCaseStatus.Debug);
			if (this.currentEditor.isDirty())
			{
				// 开启调试的时候，如果editor未保存，则执行一次保存
				this.currentEditor.doSave(null);
			}

			// 渲染下一组将被调试的消息
			MsgConnMdl nextMsg = Utils.getNextMsg(previousMsg, rootContainer, true); // 获取下一组消息的第一条消息
			if (nextMsg != null)
			{
				// 得到将实同时处理的所有消息
				List<MsgConnMdl> list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}
		}
		else
		{
			// 如果测试用例不是调试状态，则不对任何调试命令做出响应
			if (rootContainer.getStatus() != TestCaseStatus.Debug)
			{
				return;
			}
			DebugCmd cmd = (DebugCmd) event.getNewValue();
			if (this.previousMsg != null)
			{
				int offset = this.previousMsg.getSource().getLocation().y;
				if (offset >= this.currentEditor.getClientAreaHeight())
					this.currentEditor.scrollToY(offset);
			}
			else
			{
				this.currentEditor.scrollToY(0);
			}
			if (cmd == DebugCmd.StepInto)
			{
				// F5
				this.handleStepInto();
			}
			else if (cmd == DebugCmd.StepOver)
			{
				// F6
				this.handleStepOver();
			}
			else if (cmd == DebugCmd.StepReturn)
			{
				// F7
				this.handleStepReturn();
			}
			else if (cmd == DebugCmd.Terminate)
			{
				// F8
				this.handleTerminate();
			}
		}

	}

	/**
	 * 如果测试用例没有引用者，则应当保存执行结果到历史记录里—— F7按键功能
	 */
	private void handleStepReturn()
	{
		Utils.addMsgToStatusLine(DebugCmd.StepReturn.getCmd());
		this.rootContainer.setStatus(TestCaseStatus.Editing);
		this.actionsGroup.setEnable(false);
		Utils.addWarningToStatusLine(Messages.getString("I18N_ALL_MSGS_HAS_BEEN_DEBUGED"));
		this.currentEditor.getRootContainerMdl().setStatus(TestCaseStatus.Editing);
		this.currentEditor.getDebugResposne().reset();
		this.previousMsg = null;
	}

	/**
	 * 进入调试 —— F5按键功能
	 */
	private void handleStepInto()
	{
		Utils.addMsgToStatusLine(DebugCmd.StepInto.getCmd());
		// 获取将要被执行的消息
		MsgConnMdl currentMsg = Utils.getNextMsg(previousMsg, rootContainer, true);
		if (currentMsg == null)
		{
			// 此用例中的所有消息已经执行完毕
			this.handleDebugOver();
		}
		else
		{
			List<MsgConnMdl> list = getWillBeDebugedMsgs(currentMsg);

			// 取出最后一条消息
			MsgConnMdl lastMsgOfGrp = list.get(list.size() - 1);
			IFile ifile = EclipseUtils.getInputOfEditor(this.currentEditor);

			DebugExcutor excutor = new DebugExcutor(ifile, actionsGroup);
			excutor.excute(currentMsg.getTcMsg().getMsg().getUuid(), lastMsgOfGrp.getTcMsg().getMsg().getUuid());

			this.restore(list);

			// 渲染下一组将被调试的消息
			MsgConnMdl nextMsg = Utils.getNextMsg(lastMsgOfGrp, rootContainer, true); // 获取下一组消息的第一条消息
			if (nextMsg != null)
			{
				// 得到将同时处理的所有消息
				list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}

			this.previousMsg = lastMsgOfGrp;
		}
	}

	/**
	 * 处理测试用例中的所有消息已经执行完毕
	 */
	private void handleDebugOver()
	{
		actionsGroup.setEnable(false);
		Utils.addWarningToStatusLine(Messages.getString("I18N_ALL_MSGS_HAS_BEEN_DEBUGED"));
		this.currentEditor.getRootContainerMdl().setStatus(TestCaseStatus.ViewResult);
		this.rootContainer.setStatus(TestCaseStatus.Editing);

		this.currentEditor.getDebugResposne().reset();
		this.previousMsg = null;
	}

	/**
	 * 步骤调试 —— F6按键功能
	 */
	private void handleStepOver()
	{
		MsgConnMdl currentMsg = Utils.getNextMsg(previousMsg, rootContainer, true);
		if (currentMsg == null)
		{
			// 此用例中的所有消息已经执行完毕
			this.handleDebugOver();
		}
		else
		{
			// 得到将实同时处理的所有消息
			List<MsgConnMdl> list = getWillBeDebugedMsgs(currentMsg);

			// 取出最后一条消息
			MsgConnMdl lastMsgOfGrp = list.get(list.size() - 1);

			IFile ifile = EclipseUtils.getInputOfEditor(this.currentEditor);

			DebugExcutor excutor = new DebugExcutor(ifile, actionsGroup);

			excutor.excute(currentMsg.getTcMsg().getMsg().getUuid(), lastMsgOfGrp.getTcMsg().getMsg().getUuid());

			this.restore(list);
			// 渲染下一组将被调试的消息
			MsgConnMdl nextMsg = Utils.getNextMsg(lastMsgOfGrp, rootContainer, true); // 获取下一组消息的第一条消息
			if (nextMsg != null)
			{
				// 得到将实同时处理的所有消息
				list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}

			this.previousMsg = lastMsgOfGrp;
		}
	}

	/**
	 * 终止调试
	 */
	private void handleTerminate()
	{
		// 当用户点击Terminate按钮之后，应该结束debug，并且也将被引用的测试用例也结束debug—— F8按键功能
		actionsGroup.setEnable(false);
		// 保存测试用例
		TestResultManager.getInstance().saveTestCases(refering, debugStartTime, ".debugResult");

		// 设置状态并重设消息颜色
		this.rootContainer.setStatus(TestCaseStatus.Editing);
		this.previousMsg = null;

		ExecutorSession mm = ExecutorSession.getInstance();
		if (mm != null && ExecutorSession.isRunning())
		{
			try
			{
				if (!ExecutorSession.shutDown())
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "关闭执行器失败！");
				}
			}
			catch (TimeoutException e1)
			{
				LoggingPlugin.logException(logger, e1);
			}
		}
		else
		{
			logger.config("检测到执行器未运行，所以不需要关闭执行器。");
		}
	}

	/**
	 * 渲染将要被执行的消息连线
	 * 
	 * @param msg
	 */
	private void render(List<MsgConnMdl> list)
	{
		for (MsgConnMdl msg : list)
		{
			if (msg == null)
			{
				continue;
			}
			TestNodeMdl pSource = (TestNodeMdl) msg.getSource();
			TestNodeMdl pTarget = (TestNodeMdl) msg.getTarget();

			msg.setStatus(TestCaseStatus.Debug);
			pSource.setStatus(TestCaseStatus.Debug);
			pTarget.setStatus(TestCaseStatus.Debug);

			if (msg instanceof PeriodParentMsgMdl)
			{
				// 周期消息
				PeriodParentMsgMdl cycle = (PeriodParentMsgMdl) msg;
				TestNodeMdl sourceNode = (TestNodeMdl) cycle.getSource();
				TestNodeMdl targetNode = (TestNodeMdl) cycle.getTarget();
				cycle.setStatus(TestCaseStatus.Debug);
				sourceNode.setStatus(TestCaseStatus.Debug);
				targetNode.setStatus(TestCaseStatus.Debug);

				TestNodeMdl nextSourceNode = Utils.getNextNode(sourceNode);
				nextSourceNode.setStatus(TestCaseStatus.Debug);

				TestNodeMdl nextTargetNode = Utils.getNextNode(targetNode);
				nextTargetNode.setStatus(TestCaseStatus.Debug);

				nextSourceNode = Utils.getNextNode(nextSourceNode);
				nextSourceNode.setStatus(TestCaseStatus.Debug);

				nextTargetNode = Utils.getNextNode(nextTargetNode);
				nextTargetNode.setStatus(TestCaseStatus.Debug);
			}
		}
	}

	/**
	 * 恢复被渲染的消息
	 * 
	 * @param list
	 */
	private void restore(List<MsgConnMdl> list)
	{
		for (MsgConnMdl msg : list)
			restore(msg);
	}

	/**
	 * 恢复被渲染的消息
	 * 
	 * @param msg
	 */
	private void restore(MsgConnMdl msg)
	{
		if (msg == null)
		{
			return;
		}
		msg.setStatus(TestCaseStatus.Editing);
		msg.setColor(msg.getDefaultColor());

		TestNodeMdl pSource = (TestNodeMdl) msg.getSource();
		TestNodeMdl pTarget = (TestNodeMdl) msg.getTarget();
		pSource.setStatus(TestCaseStatus.Editing);
		pTarget.setStatus(TestCaseStatus.Editing);

		if (msg instanceof PeriodParentMsgMdl)
		{
			// 周期消息
			PeriodParentMsgMdl cycle = (PeriodParentMsgMdl) msg;
			TestNodeMdl sourceNode = (TestNodeMdl) cycle.getSource();
			TestNodeMdl targetNode = (TestNodeMdl) cycle.getTarget();

			TestNodeMdl nextSourceNode = Utils.getNextNode(sourceNode);
			nextSourceNode.setStatus(TestCaseStatus.Editing);

			TestNodeMdl nextTargetNode = Utils.getNextNode(targetNode);
			nextTargetNode.setStatus(TestCaseStatus.Editing);

			nextSourceNode = Utils.getNextNode(nextSourceNode);
			nextSourceNode.setStatus(TestCaseStatus.Editing);

			nextTargetNode = Utils.getNextNode(nextTargetNode);
			nextTargetNode.setStatus(TestCaseStatus.Editing);
		}
	}

	/**
	 * 找出相邻的将被一起处理的所有消息
	 * 
	 * @param fromMsg
	 * @return </br>
	 */
	private List<MsgConnMdl> getWillBeDebugedMsgs(MsgConnMdl fromMsg)
	{
		MsgConnMdl currentMsg = fromMsg; // 当前光标，标记满足条件的最后一条消息
		MsgConnMdl nextMsg = (Utils.getNextMsg(currentMsg, rootContainer, true) == null) ? currentMsg : Utils.getNextMsg(currentMsg, rootContainer, true);

		// 背景消息判断
		if (currentMsg instanceof BackgroundMsgMdl)
		{
			while (true)
			{
				if (nextMsg instanceof BackgroundMsgMdl)
				{
					MsgConnMdl temp = Utils.getNextMsg(nextMsg, rootContainer, true);
					if (temp != null)
					{
						nextMsg = temp;
					}
					else
					{
						break;
					}
				}
				else
				{
					break;
				}

			} // 取得的nestMsg 为第一条非背景消息

			currentMsg = nextMsg;
			nextMsg = Utils.getNextMsg(currentMsg, rootContainer, true);
		}

		// 并行消息判断
		if ((currentMsg instanceof ParallelMsgMdl) && (nextMsg instanceof ParallelMsgMdl))
		{

			// 获取下一条，看是否也为并行消息
			while (true)
			{
				MsgConnMdl temp = Utils.getNextMsg(nextMsg, rootContainer, true);
				if (temp != null)
				{
					if (temp instanceof ParallelMsgMdl)
					{
						// 并行消息，继续查找下一条
						nextMsg = temp;

					}
					else if (Utils.isRecvMessage(temp))
					{
						// 接收消息，也满足条件。不过得跳出并行消息判断的循环
						nextMsg = temp;
						break;

					}
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
			}

			currentMsg = nextMsg;
			nextMsg = Utils.getNextMsg(currentMsg, rootContainer, true);
		}

		// 接收消息判断
		if (Utils.isRecvMessage(nextMsg))
		{
			// 获取返回消息直到遇到非返回消息
			while (true)
			{
				MsgConnMdl temp = Utils.getNextMsg(nextMsg, rootContainer, true);
				if (temp != null)
				{
					if (Utils.isRecvMessage(temp))
					{
						// 接收消息
						nextMsg = temp;
					}
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
			}

			currentMsg = nextMsg;
		}

		List<MsgConnMdl> list = Utils.getIntervalMsgs(fromMsg, currentMsg, this.rootContainer);

		return list;
	}

	public void propertyChange(java.beans.PropertyChangeEvent evt)
	{
		PropertyChangeEvent event2 = new PropertyChangeEvent(evt.getSource(), evt.getPropertyName(), null, evt.getNewValue());
		this.propertyChange(event2);
	}
}