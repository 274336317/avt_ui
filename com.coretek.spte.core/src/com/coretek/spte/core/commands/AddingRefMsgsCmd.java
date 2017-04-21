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
 * 添加引用消息
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
	 * 转换为对应的测试工具节点
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
		// 需要增加的节点数量
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
	 * 添加发送消息
	 * 
	 * @param index
	 * @param msg
	 * @param source
	 * @param target </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private int addSendMessage(int index, Message msg, TestNodeMdl source, TestNodeMdl target)
	{
		// 发送消息
		if (msg.isBackground() || msg.isPeriodMessage())
		{
			index = index + 2;
			TestNodeMdl source2 = (TestNodeMdl) node.getParent().getChildren().get(index);
			TestNodeMdl target2 = (TestNodeMdl) this.testedObjectMdl.getChildren().get(0).getChildren().get(index);
			if (msg.isBackground())
			{// 背景消息
				this.addBackgroundMessage(source, target, source2, target2, msg);

			} else if (msg.isPeriodMessage())
			{// 周期消息
				this.addPeriodMessage(source, target, source2, target2, msg);
			}
		} else if (msg.isParallel())
		{// 并行消息
			this.addParallel(source, target, msg);
		} else
		{// 普通消息
			this.addMessage(source, target, msg);
		}

		return index;
	}

	/**
	 * 添加接收消息
	 * 
	 * @param index
	 * @param msg
	 * @param source
	 * @param target
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	private int addRecvMessage(int index, Message msg, TestNodeMdl source, TestNodeMdl target)
	{
		// 接收消息
		if (msg.isBackground() || msg.isPeriodMessage())
		{
			index = index + 2;
			TestNodeMdl target2 = (TestNodeMdl) node.getParent().getChildren().get(index);
			TestNodeMdl source2 = (TestNodeMdl) this.testedObjectMdl.getChildren().get(0).getChildren().get(index);
			if (msg.isBackground())
			{// 背景消息
				this.addBackgroundMessage(target, source, source2, target2, msg);
			} else if (msg.isPeriodMessage())
			{// 周期消息
				this.addPeriodMessage(target, source, source2, target2, msg);
			}
		} else if (msg.isParallel())
		{// 并行消息
			this.addParallel(target, source, msg);
		} else
		{// 普通消息
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
		// 判断需要多少个node
		int sum = this.getSum();
		TestNodeContainerMdl tcm = (TestNodeContainerMdl) node.getParent();
		// 被选中的节点的索引
		int index = tcm.getChildren().indexOf(node);
		// 从被选中的节点开始，往下数，最大的空闲节点数
		int maxIdle = this.getMaxIdle(index, sum, tcm);
		beginIndex = index + (sum - maxIdle);

		if (beginIndex > index)
		{
			// 需要增加的节点数量
			this.addNodes(sum, maxIdle, tcm, rootContainerMdl);
		}

		// 如果被选中的节点不是测试工具上的节点，则转换为测试工具上的节点
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
					// 发送消息
					i = this.addSendMessage(i, msg, source, target);
				} else if (msg.isRecv())
				{
					// 接收消息
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
	 * 添加背景周期消息
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
	 * 添加周期消息
	 * 
	 * @param source 测试工具节点
	 * @param target 被测对象节点
	 * @param source2 测试工具节点
	 * @param target2 被测对象节点</br> <b>Author</b> SunDawei </br> <b>Date</b>
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
		// 获取被测对象
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
	 * 添加普通消息
	 * 
	 * @param source 测试工具节点
	 * @param target 被测对象节点</br> <b>Author</b> SunDawei </br> <b>Date</b>
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
	 * 添加并行消息
	 * 
	 * @param source 测试工具节点
	 * @param target 被测对象节点
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
	 * 判断被选中的节点是测试工具还是被测对象节点
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

		return "引用测试用例";
	}

}