package com.coretek.spte.core.policies;

import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Helper;
import com.coretek.common.template.ICDFunctionObjMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.commands.AddingPostilCmd;
import com.coretek.spte.core.commands.DropMsgCreationCmd;
import com.coretek.spte.core.commands.DropPeriodCreationCmd;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.TestedObjects;

/**
 * __________________________________________________________________________________
 * 
 * @Class AbstractXYLayoutPolicy.java
 * @Description 消息视图中拖拽消息到编辑界面功能
 * @Auther MENDY
 * @Date 2016-5-12 上午11:08:15
 */

public abstract class AbstractXYLayoutPolicy extends XYLayoutEditPolicy
{

	/*
	 * __________________________________________________________________________________
	 * @Class AbstractXYLayoutPolicy
	 * @Function getCreateCommand
	 * @Description TODO(消息拖拽编辑界面)
	 * 检查编辑器中的节点是否满足所画图形的要求，如果不满足，则新增加节点
	 * @Auther MENDY
	 * @param request
	 * @return
	 * @Date 2016-5-12 上午09:52:19
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request)
	{
		Object newObj = request.getNewObject();
		CompoundCommand cCmd = new CompoundCommand();

		SPTEEditor activeEditor = (SPTEEditor) EclipseUtils.getActiveEditor();
		Entity testedObjects = activeEditor.getTestedObject();
		Entity fighter = activeEditor.getFighter();
		List<Entity> testedObjectsOfICD = TemplateUtils.getTestedObjectsOfICD(fighter, testedObjects);
		// icd的管理器
		ClazzManager clazzManager = activeEditor.getFighterClazzManager();

		RootContainerMdl root = activeEditor.getRootContainerMdl();
		String version = TemplateUtils.getVersionIDOfFighter(fighter);

		if (newObj instanceof Helper)
		{
			Object functionMsg = newObj;
			// 判断是发送消息还是接收消息
			TestMdl tested = Utils.getTested((ContainerMdl) root.getChildren().get(0));
			TestMdl tester = Utils.getTester((ContainerMdl) root.getChildren().get(0));

			// FIXME:为了实现拖拽消息时；只能拖拽与被测对象在同一层级的消息添加此代码
			boolean canDrag = false;
			if (!(functionMsg instanceof ICDFunctionObjMsg))
			{
				String level = ((TestedObjects) testedObjects).getLevel();
				String lev = TemplateUtils.getParentLevelOfMsg((Helper) functionMsg);
				if (lev.equals(level))
					canDrag = true;
			}
			if (canDrag)
				try
				{
					List<SPTEMsg> spteMsgs = TemplateUtils.filter(clazzManager, functionMsg, testedObjectsOfICD, false);

					for (SPTEMsg msg : spteMsgs)
					{

						if (msg.getMsg().isPeriodMessage())
						{
							// 周期消息
							InstanceUtils.getInstance().setMesType("周期消息");
							PeriodParentMsgMdl cycle = new PeriodParentMsgMdl();
							cycle.setTcMsg(msg);
							cycle.setName(msg.getMsg().getName());
							cycle.setMesType("(周期消息)");

							DropPeriodCreationCmd cmd = new DropPeriodCreationCmd(version);
							cmd.checkNodes(true, root);
							cmd.setConnection(cycle);
							if (msg.getMsg().isSend())
							{
								// 发送消息
								TestNodeMdl testedItem = Utils.getFirstIdleItem(tested);
								TestNodeMdl testerItem = Utils.getFirstIdleItem(tester);
								testerItem.setMesType("周期消息");
								cmd.setSource(testerItem);
								cmd.setTarget(testedItem);
								cCmd.add(cmd);
							}
							else if (msg.getMsg().isRecv())
							{
								// 接收消息
								TestNodeMdl testedItem = Utils.getFirstIdleItem(tested);
								TestNodeMdl testerItem = Utils.getFirstIdleItem(tester);
								testerItem.setMesType("周期消息");
								cmd.setSource(testedItem);
								cmd.setTarget(testerItem);
								cCmd.add(cmd);
							}

						}
						else
						{
							// 普通消息
							InstanceUtils.getInstance().setMesType("普通消息");
							MsgConnMdl conn = new MsgConnMdl();
							conn.setTcMsg(msg);
							conn.setName(msg.getMsg().getName());
							conn.setMesType("(普通消息)");

							DropMsgCreationCmd cmd = new DropMsgCreationCmd(version);
							cmd.checkNodes(false, root);
							cmd.setConnection(conn);
							if (msg.getMsg().isSend())
							{
								// 发送消息
								TestNodeMdl testedItem = Utils.getFirstIdleItem(tested);
								TestNodeMdl testerItem = Utils.getFirstIdleItem(tester);
								testerItem.setMesType("普通消息");
								cmd.setSource(testerItem);
								cmd.setTarget(testedItem);
								cCmd.add(cmd);
							}
							else if (msg.getMsg().isRecv())
							{
								// 接收消息
								TestNodeMdl testedItem = Utils.getFirstIdleItem(tested);
								TestNodeMdl testerItem = Utils.getFirstIdleItem(tester);
								testerItem.setMesType("普通消息");
								cmd.setSource(testedItem);
								cmd.setTarget(testerItem);
								cCmd.add(cmd);
							}
							else
								throw new RuntimeException("无法判断消息是接收消息还是发送消息");

						}
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
					return cCmd;
				}
		}
		else if (newObj instanceof PostilMdl)
		{
			// 创建标签命令
			FigureCanvas canvas = (FigureCanvas) activeEditor.getGraphicalViewer().getControl();
			int offsetY = canvas.getViewport().getViewLocation().y; 
			int offsetX = canvas.getViewport().getViewLocation().x;
			Point point = new Point(request.getLocation().x + offsetX, request.getLocation().y + offsetY);
			AddingPostilCmd addPostilCmd = new AddingPostilCmd(point, (ContainerMdl) root.getChildren().get(0));
			cCmd.add(addPostilCmd);
		}

		return cCmd;

	}
}
