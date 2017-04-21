package com.coretek.spte.core.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.BackgroundChildMsgMdl;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;

/**
 * 控制器工厂类，将模型注入到控制器中
 * 
 * @author 孙大巍
 * @date 2010-8-30
 */
public class SPTEEditPartFactory implements EditPartFactory
{

	public EditPart createEditPart(EditPart context, Object model)
	{
		EditPart part = null;
		if (model instanceof RootContainerMdl)
		{
			part = new RootContainerPart();
		}
		else if (model instanceof BackgroundMsgMdl)
		{
			part = new BackgroundMsgPart();// 背景消息
		}
		else if (model instanceof BackgroundChildMsgMdl)
		{
			part = new BackgroundChildMsgPart();// 背景子消息
		}
		else if (model instanceof PeriodParentMsgMdl)
		{
			part = new PeriodParentMsgPart();
		}
		else if (model instanceof PeriodChildMsgMdl)
		{
			part = new PeriodChildMsgPart();
		}
		else if (model instanceof IntervalConnMdl && ((AbtConnMdl) model).getSource().getParent() == ((AbtConnMdl) model).getTarget().getParent())
		{
			part = new IntervalConnPart(); // 时间间隔布局
		}
		else if (model instanceof ParallelMsgMdl)
		{
			part = new ParallelMsgPart(); // 并行消息
		}
		else if (model instanceof MsgConnMdl)
		{
			part = new MsgConnPart();
		}
		else if (model instanceof ContainerMdl)
		{
			part = new ContainerPart();
		}
		else if (model instanceof TestedObjectMdl)
		{
			part = new TestedObjectPart();
		}
		else if (model instanceof TestToolMdl)
		{
			part = new TestToolPart(); // 测试工具布局
		}
		else if (model instanceof TestNodeMdl)
		{
			part = new TestNodePart();
		}
		else if (model instanceof TestNodeContainerMdl)
		{
			part = new TestNodeContainerPart();
		}
		else if (model instanceof PostilMdl)
		{
			part = new PostilPart(); // 标签布局
		}
		else if (model instanceof PostilChildMdl)
		{
			part = new PostilChildPart(); // 子标签布局
		}
		if (part != null)
		{
			part.setModel(model);
		}
		return part;
	}
}