package com.coretek.spte.core.editor.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.coretek.spte.core.commands.DelConnCmd;
import com.coretek.spte.core.commands.DelIntervalCmd;
import com.coretek.spte.core.commands.DelPeriodMsgCmd;
import com.coretek.spte.core.commands.DelPostilCmd;
import com.coretek.spte.core.commands.TestedObjectDeletionCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.parts.IntervalConnPart;
import com.coretek.spte.core.parts.MsgConnPart;
import com.coretek.spte.core.parts.PeriodChildMsgPart;
import com.coretek.spte.core.parts.PeriodParentMsgPart;
import com.coretek.spte.core.parts.PostilPart;
import com.coretek.spte.core.parts.TestPart;

/**
 * 删除操作的超类
 * 
 * @author 孙大巍
 * 
 */
public abstract class AbstractDeletionAtn extends SelectionAction
{

	public AbstractDeletionAtn(IWorkbenchPart part)
	{
		super(part);
	}

	protected Command createCommand(Object object)
	{
		if (object == null)
		{
			return null;
		}
		if (!(object instanceof EditPart))
		{
			return null;
		}
		EditPart selectedPart = (EditPart) object;

		// 删除周期消息父连接
		if (selectedPart instanceof PeriodParentMsgPart)
		{
			Command command = new DelPeriodMsgCmd();
			PeriodParentMsgMdl parentModel = (PeriodParentMsgMdl) selectedPart.getModel();
			((DelPeriodMsgCmd) command).setFixedParent(parentModel);
			((DelPeriodMsgCmd) command).setSource(parentModel.getSource());
			((DelPeriodMsgCmd) command).setTarget(parentModel.getTarget());
			return command;
		} else if (selectedPart instanceof PeriodChildMsgPart)
		{// 周期消息子连接
			DelPeriodMsgCmd command = new DelPeriodMsgCmd();
			PeriodChildMsgMdl childModel = (PeriodChildMsgMdl) selectedPart.getModel();
			PeriodParentMsgMdl parentModel = (PeriodParentMsgMdl) childModel.getParent();
			command.setFixedParent(parentModel);
			command.setSource(parentModel.getSource());
			command.setTarget(parentModel.getTarget());
			return command;
		} else if (selectedPart instanceof IntervalConnPart)
		{// 删除时间间隔
			DelIntervalCmd command = new DelIntervalCmd();
			IntervalConnMdl model = (IntervalConnMdl) selectedPart.getModel();
			command.setTimer(model);
			command.setTargetNode(model.getTarget());
			command.setSourceNode(model.getSource());
			return command;

		} else if (selectedPart instanceof MsgConnPart)
		{// 删除消息
			DelConnCmd command = new DelConnCmd();
			EditPart ep = (EditPart) object;
			if (ep.getModel() instanceof AbtConnMdl)
			{
				AbtConnMdl model = (AbtConnMdl) ep.getModel();
				command.setConnection(model);
				command.setTarget(model.getTarget());
				command.setSource(model.getSource());
			}

			return command;

		} else if (selectedPart instanceof TestPart)
		{// 删除lifeline
			TestedObjectDeletionCmd command = new TestedObjectDeletionCmd();
			TestMdl lifelineModel = (TestMdl) selectedPart.getModel();
			command.setNode(lifelineModel);
			ContainerMdl transmodel = (ContainerMdl) lifelineModel.getParent();
			command.setSubTransModel(transmodel);
			return command;
		} else if (selectedPart instanceof PostilPart)
		{// 删除标签
			ContainerMdl rootContainerMdl = (ContainerMdl) selectedPart.getParent().getModel();
			PostilMdl postilMdl = (PostilMdl) selectedPart.getModel();
			DelPostilCmd deleteCommand = new DelPostilCmd(rootContainerMdl, postilMdl);

			return deleteCommand;

		}

		return null;
	}
}
