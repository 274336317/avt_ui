/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorPart;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.BackgroundChildMsgMdl;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.TimeSpan;

/**
 * ���������Ϣ
 * 
 * @author SunDawei 2012-5-22
 */
public class AddingRefMsgsCmd extends Command
{

	private TestNodeMdl			node;

	private List<XMLBean>		list;

	private int					beginIndex	= -1;

	private TestedObjectMdl		testedObjectMdl;

	private ClazzManager		icdManager;

	private List<AbtConnMdl>	added		= new ArrayList<AbtConnMdl>();

	public AddingRefMsgsCmd(TestNodeMdl node, List<XMLBean> list, ClazzManager icdManager)
	{
		this.node = node;
		this.list = new ArrayList<XMLBean>(list);
		this.icdManager = icdManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo() <br/> <b>Author</b> SunDawei
	 * </br> <b>Date</b> 2012-5-23
	 */
	@Override
	public void undo()
	{
		for (AbtConnMdl conn : this.added)
		{
			conn.getSource().removeOutput(conn);
			conn.getSource().fireConnectionChange(AbtNode.PROP_OUTPUTS, conn);
			conn.getTarget().removeInput(conn);
			conn.getTarget().fireConnectionChange(AbtNode.PROP_INPUTS, conn);
			conn.setSource(null);
			conn.setTarget(null);
		}
		this.added.clear();
	}

	private int getMaxIdle(int index, int sum, TestNodeContainerMdl tcm)
	{
		int maxIdle = 0;
		for (int i = index, length = index + sum; i < length; i++)
		{
			TestNodeMdl tnm = (TestNodeMdl) tcm.getChildren().get(i);
			if (tnm.getIncomingConnections().size() == 0 || tnm.getOutgoingConnections().size() == 0)
			{
				++maxIdle;
			}
		}
		return maxIdle;
	}

	/**
	 * ת��Ϊ��Ӧ�Ĳ��Թ��߽ڵ�
	 * 
	 * @param index </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private void toTestTool(int index)
	{
		ContainerMdl container = (ContainerMdl) node.getParent().getParent().getParent();
		for (AbtElement element : container.getChildren())
		{
			if (element instanceof TestToolMdl)
			{
				TestNodeContainerMdl tnc = (TestNodeContainerMdl) element.getChildren().get(0);
				node = (TestNodeMdl) tnc.getChildren().get(index);
			}
		}
	}

	private void addNodes(int sum, int maxIdle, TestNodeContainerMdl tcm, RootContainerMdl rootContainerMdl)
	{
		// ��Ҫ���ӵĽڵ�����
		int counter = sum - maxIdle;
		TestNodeMdl begin = (TestNodeMdl) tcm.getChildren().get(beginIndex);

		AddingTestNodeCmd cmd = new AddingTestNodeCmd(counter, rootContainerMdl, begin);
		cmd.execute();
	}

	private RootContainerMdl getRootContainerMdl()
	{
		IEditorPart part = EclipseUtils.getActiveEditor();
		SPTEEditor editor = (SPTEEditor) part;
		return editor.getRootContainerMdl();
	}

	/**
	 * ��ӷ�����Ϣ
	 * 
	 * @param index
	 * @param msg
	 * @param source
	 * @param target </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private int addSendMessage(int index, Message msg, TestNodeMdl source, TestNodeMdl target)
	{
		// ������Ϣ
		if (msg.isBackground() || msg.isPeriodMessage())
		{
			index = index + 2;
			TestNodeMdl source2 = (TestNodeMdl) node.getParent().getChildren().get(index);
			TestNodeMdl target2 = (TestNodeMdl) this.testedObjectMdl.getChildren().get(0).getChildren().get(index);
			if (msg.isBackground())
			{// ������Ϣ
				this.addBackgroundMessage(source, target, source2, target2, msg);

			} else if (msg.isPeriodMessage())
			{// ������Ϣ
				this.addPeriodMessage(source, target, source2, target2, msg);
			}
		} else if (msg.isParallel())
		{// ������Ϣ
			this.addParallel(source, target, msg);
		} else
		{// ��ͨ��Ϣ
			this.addMessage(source, target, msg);
		}

		return index;
	}

	/**
	 * ��ӽ�����Ϣ
	 * 
	 * @param index
	 * @param msg
	 * @param source
	 * @param target
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private int addRecvMessage(int index, Message msg, TestNodeMdl source, TestNodeMdl target)
	{
		// ������Ϣ
		if (msg.isBackground() || msg.isPeriodMessage())
		{
			index = index + 2;
			TestNodeMdl target2 = (TestNodeMdl) node.getParent().getChildren().get(index);
			TestNodeMdl source2 = (TestNodeMdl) this.testedObjectMdl.getChildren().get(0).getChildren().get(index);
			if (msg.isBackground())
			{// ������Ϣ
				this.addBackgroundMessage(target, source, source2, target2, msg);
			} else if (msg.isPeriodMessage())
			{// ������Ϣ
				this.addPeriodMessage(target, source, source2, target2, msg);
			}
		} else if (msg.isParallel())
		{// ������Ϣ
			this.addParallel(target, source, msg);
		} else
		{// ��ͨ��Ϣ
			this.addMessage(target, source, msg);
		}

		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute() <br/> <b>Author</b>
	 * SunDawei </br> <b>Date</b> 2012-5-22
	 */
	@Override
	public void execute()
	{
		this.added.clear();
		RootContainerMdl rootContainerMdl = this.getRootContainerMdl();
		// �ж���Ҫ���ٸ�node
		int sum = this.getSum();
		TestNodeContainerMdl tcm = (TestNodeContainerMdl) node.getParent();
		// ��ѡ�еĽڵ������
		int index = tcm.getChildren().indexOf(node);
		// �ӱ�ѡ�еĽڵ㿪ʼ�������������Ŀ��нڵ���
		int maxIdle = this.getMaxIdle(index, sum, tcm);
		beginIndex = index + (sum - maxIdle);

		if (beginIndex > index)
		{
			// ��Ҫ���ӵĽڵ�����
			this.addNodes(sum, maxIdle, tcm, rootContainerMdl);
		}

		// �����ѡ�еĽڵ㲻�ǲ��Թ����ϵĽڵ㣬��ת��Ϊ���Թ����ϵĽڵ�
		if (!this.isTestTool(node))
		{
			this.toTestTool(index);
		}
		this.testedObjectMdl = Utils.getTestedObject(rootContainerMdl);
		int msgCounter = 0;
		for (int i = index, length = index + sum; i < length; i++)
		{
			XMLBean xmlBean = this.list.get(msgCounter++);
			if (xmlBean instanceof Message)
			{
				Message msg = (Message) xmlBean;
				TestNodeMdl source = (TestNodeMdl) node.getParent().getChildren().get(i);
				TestNodeMdl target = (TestNodeMdl) this.testedObjectMdl.getChildren().get(0).getChildren().get(i);
				if (msg.isSend())
				{
					// ������Ϣ
					i = this.addSendMessage(i, msg, source, target);
				} else if (msg.isRecv())
				{
					// ������Ϣ
					i = this.addRecvMessage(i, msg, source, target);
				}
			} else if (xmlBean instanceof TimeSpan)
			{
				--i;
				this.addTimeSpan((TimeSpan) xmlBean, i);
			}
		}
		System.gc();
	}

	/**
	 * ��ӱ���������Ϣ
	 * 
	 * @param source
	 * @param target
	 * @param source2
	 * @param target2
	 * @param msg </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private void addBackgroundMessage(TestNodeMdl source, TestNodeMdl target, TestNodeMdl source2, TestNodeMdl target2, Message msg)
	{
		BackgroundMsgMdl bg = new BackgroundMsgMdl(source, target);
		BackgroundChildMsgMdl bgChild = new BackgroundChildMsgMdl(source2, target2);
		SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdManager, (Message) msg);
		spteMsg.setMsg(msg);
		bgChild.setTcMsg(spteMsg);
		bg.setTcMsg(spteMsg);
		bg.setName(spteMsg.getMsg().getName());
		this.added.add(bg);
		this.added.add(bgChild);
	}

	/**
	 * ���������Ϣ
	 * 
	 * @param source ���Թ��߽ڵ�
	 * @param target �������ڵ�
	 * @param source2 ���Թ��߽ڵ�
	 * @param target2 �������ڵ�</br> <b>Author</b> SunDawei </br> <b>Date</b>
	 *            2012-5-23
	 */
	private void addPeriodMessage(TestNodeMdl source, TestNodeMdl target, TestNodeMdl source2, TestNodeMdl target2, Message msg)
	{
		PeriodParentMsgMdl pp = new PeriodParentMsgMdl(source, target);
		PeriodChildMsgMdl pc = new PeriodChildMsgMdl(source2, target2);
		pp.setFixedChild(pc);
		pc.setParent(pc);
		SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdManager, (Message) msg);
		pp.setName(spteMsg.getMsg().getName());
		spteMsg.setMsg(msg);
		pc.setTcMsg(spteMsg);
		pp.setTcMsg(spteMsg);
		this.added.add(pp);
		this.added.add(pc);
	}

	private boolean addTimeSpan(TimeSpan timeSpan, int index)
	{
		// ��ȡ�������
		ContainerMdl container = (ContainerMdl) node.getParent().getParent().getParent();
		for (AbtElement element : container.getChildren())
		{
			if (element instanceof TestedObjectMdl)
			{
				TestNodeContainerMdl tnc = (TestNodeContainerMdl) element.getChildren().get(0);
				TestNodeMdl sourceNode = (TestNodeMdl) tnc.getChildren().get(index);
				TestNodeMdl targetNode = (TestNodeMdl) tnc.getChildren().get(index + 1);
				IntervalConnMdl icm = new IntervalConnMdl(sourceNode, targetNode);
				icm.setResultInterval(timeSpan);
				icm.setName(timeSpan.getValue());
				this.added.add(icm);
				return true;
			}
		}

		return false;
	}

	/**
	 * �����ͨ��Ϣ
	 * 
	 * @param source ���Թ��߽ڵ�
	 * @param target �������ڵ�</br> <b>Author</b> SunDawei </br> <b>Date</b>
	 *            2012-5-23
	 */
	private void addMessage(TestNodeMdl source, TestNodeMdl target, Message msg)
	{
		MsgConnMdl conn = new MsgConnMdl(source, target);
		SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdManager, (Message) msg);
		conn.setName(spteMsg.getMsg().getName());
		spteMsg.setMsg(msg);
		conn.setTcMsg(spteMsg);
		conn.setName(spteMsg.getMsg().getName());
		this.added.add(conn);
	}

	/**
	 * ��Ӳ�����Ϣ
	 * 
	 * @param source ���Թ��߽ڵ�
	 * @param target �������ڵ�
	 * @param msg </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private void addParallel(TestNodeMdl source, TestNodeMdl target, Message msg)
	{
		ParallelMsgMdl parallel = new ParallelMsgMdl(source, target);
		SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdManager, (Message) msg);
		spteMsg.setMsg(msg);
		parallel.setTcMsg(spteMsg);
		parallel.setName(spteMsg.getMsg().getName());
		this.added.add(parallel);
	}

	/**
	 * �жϱ�ѡ�еĽڵ��ǲ��Թ��߻��Ǳ������ڵ�
	 * 
	 * @param node
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-22
	 */
	private boolean isTestTool(TestNodeMdl node)
	{
		AbtElement element = node.getParent().getParent();
		if (element instanceof TestToolMdl)
		{
			return true;
		}

		return false;
	}

	private int getSum()
	{
		int sum = 0;
		for (XMLBean bean : list)
		{
			if (bean instanceof Message)
			{
				Message msg = (Message) bean;
				if (msg.isPeriodMessage() || msg.isBackground())
				{
					sum = sum + 3;
				} else
				{
					sum++;
				}
			}
		}

		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#getDebugLabel() <br/> <b>Author</b>
	 * SunDawei </br> <b>Date</b> 2012-5-22
	 */
	@Override
	public String getDebugLabel()
	{

		return "���ò�������";
	}

}