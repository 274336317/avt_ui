package com.coretek.spte.core.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.commands.AddingPostilCmd;
import com.coretek.spte.core.commands.BackgroundMsgCreationCmd;
import com.coretek.spte.core.commands.IntervalAutoCreationCmd;
import com.coretek.spte.core.commands.MsgCreationCmd;
import com.coretek.spte.core.commands.ParallelCreationCmd;
import com.coretek.spte.core.commands.PeriodMsgCreationCmd;
import com.coretek.spte.core.commands.SetEmulatorCmd;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.MessageBlock;
import com.coretek.spte.testcase.Postil;
import com.coretek.spte.testcase.PostilChild;
import com.coretek.spte.testcase.Postils;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TimeSpan;

/**
 * 注册到.cas文件解析器上，用于画出各种消息图元
 * 
 * @author 孙大巍
 * @date 2010-12-8
 */
public class Painter implements PropertyChangeListener
{

	private RootContainerMdl rootContainer;

	private Painter(RootContainerMdl transmodel)
	{
		this.rootContainer = transmodel;
	}

	public void setRootContainer(RootContainerMdl transmodel)
	{
		this.rootContainer = transmodel;
	}

	public static Painter getInstance(RootContainerMdl transmodel)
	{
		assert transmodel != null;
		return new Painter(transmodel);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (SPTEConstants.PARSER_EVENT_TESTED_MODULE.equals(evt.getPropertyName()))
		{// 解析出了testedModule
			List<AbtElement> list = this.rootContainer.getChildren().get(0).getChildren();
			TestMdl model = null;
			for (AbtElement element : list)
			{
				TestMdl life = (TestMdl) element;
				if (life instanceof TestedObjectMdl)
				{
					model = life;
				}
			}
			SetEmulatorCmd command = new SetEmulatorCmd(model);
			Entity emulator = (Entity) evt.getNewValue();// 获取了新的测试对象
			command.setEmulator(emulator);
			SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(command);
			return;
		}

		String property = evt.getPropertyName();
		Object[] contents = (Object[]) evt.getNewValue();// 一个元素为IProject，第二个元素为生成的连线模型
		// 解析出了普通消息
		if (SPTEConstants.PARSER_EVENT_MESSAGE.equals(property))
		{
			MsgConnMdl messageModel = (MsgConnMdl) contents[1];
			MsgCreationCmd command = null;
			if (messageModel instanceof ParallelMsgMdl)
			{// 并行消息
				command = new ParallelCreationCmd();
				messageModel.setMesType("(并行消息)");
				InstanceUtils.getInstance().setMesType("并行消息");
			}
			else
			{// 普通消息
				command = new MsgCreationCmd();
				messageModel.setMesType("(普通消息)");
				InstanceUtils.getInstance().setMesType("普通消息");
			}
			// 检查剩余的节点是否满足要求
			command.checkNodes(false, this.rootContainer);
			command.setConnection(messageModel);
			ContainerMdl sub = (ContainerMdl) rootContainer.getChildren().get(0);
			// 发送消息
			if (Utils.isSendMessage(messageModel))
			{
				command.setSource(Utils.getFirstIdleItem(Utils.getTester(sub)));
				command.setTarget(Utils.getFirstIdleItem(Utils.getTested(sub)));
			}
			else
			{// 接收消息
				command.setSource(Utils.getFirstIdleItem(Utils.getTested(sub)));
				command.setTarget(Utils.getFirstIdleItem(Utils.getTester(sub)));
			}

			command.setConnection(messageModel);
			SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(command);
		}
		else if (SPTEConstants.PARSER_EVENT_CYCLE.equals(property))
		{// 解析出了周期消息
			MsgConnMdl messageModel = (MsgConnMdl) contents[1];
			messageModel.setMesType("(周期消息)");
			InstanceUtils.getInstance().setMesType("周期消息");
			if (!messageModel.getTcMsg().getMsg().isBackground())
			{// 判断是一般周期消息还是背景周期消息
				PeriodParentMsgMdl cycleModel = (PeriodParentMsgMdl) contents[1];
				PeriodMsgCreationCmd command = new PeriodMsgCreationCmd(true, rootContainer);
				ContainerMdl sub = (ContainerMdl) rootContainer.getChildren().get(0);
				// 检查剩余的节点是否满足要求
				command.checkNodes(true, this.rootContainer);
				// 发送消息
				if (Utils.isSendMessage(cycleModel))
				{
					command.setSource(Utils.getFirstIdleItem(Utils.getTester(sub)));
					command.setTarget(Utils.getFirstIdleItem(Utils.getTested(sub)));
				}
				else
				{// 接收消息
					command.setSource(Utils.getFirstIdleItem(Utils.getTested(sub)));
					command.setTarget(Utils.getFirstIdleItem(Utils.getTester(sub)));
				}
				command.setConnection(cycleModel);
				SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(command);
			}
			else
			{
				BackgroundMsgMdl backgroundMsgMdl = (BackgroundMsgMdl) contents[1];
				backgroundMsgMdl.setMesType("(背景周期消息)");
				InstanceUtils.getInstance().setMesType("背景周期消息");
				BackgroundMsgCreationCmd command = new BackgroundMsgCreationCmd(true, rootContainer);
				ContainerMdl sub = (ContainerMdl) rootContainer.getChildren().get(0);
				// 发送消息
				if (Utils.isSendMessage(backgroundMsgMdl))
				{
					command.setSource(Utils.getFirstIdleItem(Utils.getTester(sub)));
					command.setTarget(Utils.getFirstIdleItem(Utils.getTested(sub)));
				}
				else
				{// 接收消息
					command.setSource(Utils.getFirstIdleItem(Utils.getTested(sub)));
					command.setTarget(Utils.getFirstIdleItem(Utils.getTester(sub)));
				}
				command.setConnection(backgroundMsgMdl);
				SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(command);
			}
		}
		else if (SPTEConstants.PARSER_EVENT_INTERVAL.equals(property))
		{
			// 解析出了时间间隔
			TestCase testCase = (TestCase) contents[1];
			MessageBlock messageBlock = testCase.getMsgBlockofTestCase();
			Postils postils = testCase.getPostilsOfTestCase();
			if (null == messageBlock && null == postils)
			{
				return;
			}
			if (null != messageBlock)
			{
				for (int i = 0; i < messageBlock.getChildren().size(); i++)
				{
					Object obj = messageBlock.getChildren().get(i);
					if (obj instanceof TimeSpan)
					{
						TimeSpan span = (TimeSpan) obj;
						IntervalConnMdl interval = new IntervalConnMdl();
						interval.setResultInterval(span);
						IntervalAutoCreationCmd command = new IntervalAutoCreationCmd();
						ContainerMdl sub = (ContainerMdl) rootContainer.getChildren().get(0);
						TestNodeMdl item = null;
						Object object = messageBlock.getChildren().get(i - 1);
						if (object instanceof Message)
						{
							Message m1 = (Message) object;
							item = Utils.getNode(Utils.getTested(sub), m1);
						}
						if (item != null)
						{
							command.setSource(item);
							command.setTarget(Utils.getNextNode(item));
							SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(command);
							IntervalConnMdl model = command.getIntervalModel();
							model.setName(span.getValue());
							model.setResultInterval(interval.getResultInterval());
						}
					}
				}
			}

			if (null != postils)
			{
				for (int i = 0; i < postils.getChildren().size(); i++)
				{
					Object m_Obj = postils.getChildren().get(i);
					if (m_Obj instanceof Postil)
					{
						PostilMdl m_PostilMdl = new PostilMdl();
						Postil m_Postil = (Postil) m_Obj;
						Point m_Point = new Point(m_Postil.getX(), m_Postil.getY());
						m_PostilMdl.setLocation(m_Point);
						for (int j = 0; j < m_Postil.getChildren().size(); j++)
						{
							Object m_ObjPostilChild = m_Postil.getChildren().get(j);
							if (m_ObjPostilChild instanceof PostilChild)
							{
								PostilChild m_PostilChild = (PostilChild) m_ObjPostilChild;
								int x = m_PostilChild.getX();
								int y = m_PostilChild.getY();
								int w = m_PostilChild.getWidth();
								int h = m_PostilChild.getHigh();
								Point location = new Point(x, y);
								Rectangle rectangle = new Rectangle(x, y, w, h);
								PostilChildMdl postilChildMdl = new PostilChildMdl();
								postilChildMdl.setLocation(location);
								postilChildMdl.setRectangle(rectangle);
								postilChildMdl.setText(m_PostilChild.getText());
								if (x < 460)
								{
									m_PostilMdl.setRightChildrenMdl(postilChildMdl);
								}
								else
								{
									m_PostilMdl.setLeftChildrenMdl(postilChildMdl);
								}
								AddingPostilCmd addingPostilCmd = new AddingPostilCmd((ContainerMdl) rootContainer.getChildren().get(0), m_PostilMdl);
								SPTEEditor.getDefault().getEditDomain().getCommandStack().execute(addingPostilCmd);
							}
						}
					}
				}
			}

		}
	}
}
