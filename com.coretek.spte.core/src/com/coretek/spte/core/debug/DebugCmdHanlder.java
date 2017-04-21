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
 * ����������Ӧģ�顣ÿ��editor����һ������������Ӧģ���ʵ����
 * 
 * @author ���Ρ
 */
public class DebugCmdHanlder implements IPropertyChangeListener, PropertyChangeListener
{

	private final static Logger logger = LoggingPlugin.getLogger(DebugCmdHanlder.class);

	// ��һ��ִ�е���Ϣ
	private RootContainerMdl rootContainer;

	private MsgConnMdl previousMsg;

	// ���ڱ�debug��editor
	private SPTEEditor currentEditor;

	// ����debug��ť��
	private DebugActionsGroup actionsGroup;

	// ��������
	private ReferingCase refering;

	// debug��ʼʱ��
	private String debugStartTime;

	/**
	 * @param currentFile ��ǰ���ڱ����Ե�cas�ļ�
	 * @param currentEditor ��ǰ���ڱ����Եı༭��
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
			// ��������
			debugStartTime = StringUtils.getCurrentTime();

			// �õ�debug�İ�����
			SPTETBContributor contributor = (SPTETBContributor) currentEditor.getEditorSite().getActionBarContributor();
			actionsGroup = contributor.getActionsGroup();

			this.rootContainer.setStatus(TestCaseStatus.Debug);
			if (this.currentEditor.isDirty())
			{
				// �������Ե�ʱ�����editorδ���棬��ִ��һ�α���
				this.currentEditor.doSave(null);
			}

			// ��Ⱦ��һ�齫�����Ե���Ϣ
			MsgConnMdl nextMsg = Utils.getNextMsg(previousMsg, rootContainer, true); // ��ȡ��һ����Ϣ�ĵ�һ����Ϣ
			if (nextMsg != null)
			{
				// �õ���ʵͬʱ�����������Ϣ
				List<MsgConnMdl> list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}
		}
		else
		{
			// ��������������ǵ���״̬���򲻶��κε�������������Ӧ
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
	 * �����������û�������ߣ���Ӧ������ִ�н������ʷ��¼��� F7��������
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
	 * ������� ���� F5��������
	 */
	private void handleStepInto()
	{
		Utils.addMsgToStatusLine(DebugCmd.StepInto.getCmd());
		// ��ȡ��Ҫ��ִ�е���Ϣ
		MsgConnMdl currentMsg = Utils.getNextMsg(previousMsg, rootContainer, true);
		if (currentMsg == null)
		{
			// �������е�������Ϣ�Ѿ�ִ�����
			this.handleDebugOver();
		}
		else
		{
			List<MsgConnMdl> list = getWillBeDebugedMsgs(currentMsg);

			// ȡ�����һ����Ϣ
			MsgConnMdl lastMsgOfGrp = list.get(list.size() - 1);
			IFile ifile = EclipseUtils.getInputOfEditor(this.currentEditor);

			DebugExcutor excutor = new DebugExcutor(ifile, actionsGroup);
			excutor.excute(currentMsg.getTcMsg().getMsg().getUuid(), lastMsgOfGrp.getTcMsg().getMsg().getUuid());

			this.restore(list);

			// ��Ⱦ��һ�齫�����Ե���Ϣ
			MsgConnMdl nextMsg = Utils.getNextMsg(lastMsgOfGrp, rootContainer, true); // ��ȡ��һ����Ϣ�ĵ�һ����Ϣ
			if (nextMsg != null)
			{
				// �õ���ͬʱ�����������Ϣ
				list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}

			this.previousMsg = lastMsgOfGrp;
		}
	}

	/**
	 * ������������е�������Ϣ�Ѿ�ִ�����
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
	 * ������� ���� F6��������
	 */
	private void handleStepOver()
	{
		MsgConnMdl currentMsg = Utils.getNextMsg(previousMsg, rootContainer, true);
		if (currentMsg == null)
		{
			// �������е�������Ϣ�Ѿ�ִ�����
			this.handleDebugOver();
		}
		else
		{
			// �õ���ʵͬʱ�����������Ϣ
			List<MsgConnMdl> list = getWillBeDebugedMsgs(currentMsg);

			// ȡ�����һ����Ϣ
			MsgConnMdl lastMsgOfGrp = list.get(list.size() - 1);

			IFile ifile = EclipseUtils.getInputOfEditor(this.currentEditor);

			DebugExcutor excutor = new DebugExcutor(ifile, actionsGroup);

			excutor.excute(currentMsg.getTcMsg().getMsg().getUuid(), lastMsgOfGrp.getTcMsg().getMsg().getUuid());

			this.restore(list);
			// ��Ⱦ��һ�齫�����Ե���Ϣ
			MsgConnMdl nextMsg = Utils.getNextMsg(lastMsgOfGrp, rootContainer, true); // ��ȡ��һ����Ϣ�ĵ�һ����Ϣ
			if (nextMsg != null)
			{
				// �õ���ʵͬʱ�����������Ϣ
				list = getWillBeDebugedMsgs(nextMsg);
				render(list);
			}

			this.previousMsg = lastMsgOfGrp;
		}
	}

	/**
	 * ��ֹ����
	 */
	private void handleTerminate()
	{
		// ���û����Terminate��ť֮��Ӧ�ý���debug������Ҳ�������õĲ�������Ҳ����debug���� F8��������
		actionsGroup.setEnable(false);
		// �����������
		TestResultManager.getInstance().saveTestCases(refering, debugStartTime, ".debugResult");

		// ����״̬��������Ϣ��ɫ
		this.rootContainer.setStatus(TestCaseStatus.Editing);
		this.previousMsg = null;

		ExecutorSession mm = ExecutorSession.getInstance();
		if (mm != null && ExecutorSession.isRunning())
		{
			try
			{
				if (!ExecutorSession.shutDown())
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", "�ر�ִ����ʧ�ܣ�");
				}
			}
			catch (TimeoutException e1)
			{
				LoggingPlugin.logException(logger, e1);
			}
		}
		else
		{
			logger.config("��⵽ִ����δ���У����Բ���Ҫ�ر�ִ������");
		}
	}

	/**
	 * ��Ⱦ��Ҫ��ִ�е���Ϣ����
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
				// ������Ϣ
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
	 * �ָ�����Ⱦ����Ϣ
	 * 
	 * @param list
	 */
	private void restore(List<MsgConnMdl> list)
	{
		for (MsgConnMdl msg : list)
			restore(msg);
	}

	/**
	 * �ָ�����Ⱦ����Ϣ
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
			// ������Ϣ
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
	 * �ҳ����ڵĽ���һ�����������Ϣ
	 * 
	 * @param fromMsg
	 * @return </br>
	 */
	private List<MsgConnMdl> getWillBeDebugedMsgs(MsgConnMdl fromMsg)
	{
		MsgConnMdl currentMsg = fromMsg; // ��ǰ��꣬����������������һ����Ϣ
		MsgConnMdl nextMsg = (Utils.getNextMsg(currentMsg, rootContainer, true) == null) ? currentMsg : Utils.getNextMsg(currentMsg, rootContainer, true);

		// ������Ϣ�ж�
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

			} // ȡ�õ�nestMsg Ϊ��һ���Ǳ�����Ϣ

			currentMsg = nextMsg;
			nextMsg = Utils.getNextMsg(currentMsg, rootContainer, true);
		}

		// ������Ϣ�ж�
		if ((currentMsg instanceof ParallelMsgMdl) && (nextMsg instanceof ParallelMsgMdl))
		{

			// ��ȡ��һ�������Ƿ�ҲΪ������Ϣ
			while (true)
			{
				MsgConnMdl temp = Utils.getNextMsg(nextMsg, rootContainer, true);
				if (temp != null)
				{
					if (temp instanceof ParallelMsgMdl)
					{
						// ������Ϣ������������һ��
						nextMsg = temp;

					}
					else if (Utils.isRecvMessage(temp))
					{
						// ������Ϣ��Ҳ��������������������������Ϣ�жϵ�ѭ��
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

		// ������Ϣ�ж�
		if (Utils.isRecvMessage(nextMsg))
		{
			// ��ȡ������Ϣֱ�������Ƿ�����Ϣ
			while (true)
			{
				MsgConnMdl temp = Utils.getNextMsg(nextMsg, rootContainer, true);
				if (temp != null)
				{
					if (Utils.isRecvMessage(temp))
					{
						// ������Ϣ
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