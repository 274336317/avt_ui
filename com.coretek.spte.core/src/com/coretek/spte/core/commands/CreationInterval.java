package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.util.ICreation;
import com.coretek.spte.core.util.Utils;

/**
 * 验证创建时间间隔模型
 * 
 * @author 孙大巍
 * @date 2010-9-21
 */
public class CreationInterval implements ICreation
{

	private TestNodeMdl source;

	private TestNodeMdl target;

	public TestNodeMdl getSource()
	{
		return source;
	}

	public void setSource(TestNodeMdl source)
	{
		this.source = source;
	}

	public TestNodeMdl getTarget()
	{
		return target;
	}

	public void setTarget(TestNodeMdl target)
	{
		this.target = target;
	}

	public CreationInterval()
	{
	}

	public boolean validate()
	{
		if (this.source == null)
		{
			return false;
		}
		// 不允许将一个item作为多个timer的连出item
		if (Utils.isSourceOfTimer(source))
		{
			return false;
		}

		// source item 必须有消息连线
		if (!Utils.hasConnection(source))
		{
			return false;
		}

		// 不允许将timer画到测试工具上
		TestMdl lifeline = (TestMdl) source.getParent().getParent();
		if (lifeline instanceof TestToolMdl)
		{
			return false;
		}

		// 不允许时间间隔连线从周期消息的父连线连到子连线
		if (source != null && target != null)
		{
			AbtConnMdl parentFixed = Utils.getMessageOfItem(source);
			if (parentFixed != null && parentFixed instanceof PeriodParentMsgMdl)
			{
				AbtConnMdl childFixed = Utils.getMessageOfItem(target);
				if (childFixed != null && childFixed instanceof PeriodChildMsgMdl)
				{
					return false;
				}
			}

			if (!Utils.hasMessageBetweenTargetAndSource(source, target))
			{
				return false;
			}
		}
		return true;
	}

	public Object execute()
	{
		AbtConnMdl connection = null;
		if (this.source.getParent() == this.target.getParent() && this.source.getLocation().x == this.target.getLocation().x && this.source.getLocation().y != this.target.getLocation().y)
		{
			if (source.getLocation().y < target.getLocation().y)
			{
				this.source.setMesType("时间间隔");
				// 从source往下
				/*
				 * 如果时间间隔消息的连出item包含有周期消息的父连接， 则需要将source改为包含周期消息子连接的item
				 */
				if (Utils.getFixedParentMessageModel(source.getIncomingConnections()) != null || Utils.getFixedParentMessageModel(source.getOutgoingConnections()) != null)
				{
					this.source = (TestNodeMdl) this.source.getParent().getChildren().get(this.source.getParent().getChildren().indexOf(this.source) + 2);
				}

				TestNodeContainerMdl item = (TestNodeContainerMdl) source.getParent();
				int index = item.getChildren().indexOf(source);
				/*
				 * 寻找从source往下的第一个拥有消息连线的lineItem
				 */
				for (int i = index + 1; i < item.getChildren().size(); i++)
				{
					TestNodeMdl model = (TestNodeMdl) item.getChildren().get(i);
					// 目标item必须有消息连接才可以画时间间隔连线
					if (model.getOutgoingConnections() != null && model.getOutgoingConnections().size() > 0)
					{
						PeriodChildMsgMdl childModel = Utils.getFixedChildMessageModel(model.getOutgoingConnections());
						/*
						 * 如果时间间隔的连接目标是拥有周期消息子连接的item，
						 * 则应当将目标item改为包含周期消息父连接的item
						 */
						if (childModel != null)
						{
							model = (TestNodeMdl) item.getChildren().get(i + 2);
						}
						this.target = model;
						break;
					}
					else if (model.getIncomingConnections() != null && model.getIncomingConnections().size() > 0)
					{
						PeriodChildMsgMdl childModel = Utils.getFixedChildMessageModel(model.getOutgoingConnections());
						/*
						 * 如果时间间隔的连接目标是拥有周期消息子连接的item，
						 * 则应当将目标item改为包含周期消息父连接的item
						 */
						if (childModel != null)
						{
							model = (TestNodeMdl) item.getChildren().get(i + 2);
						}
						this.target = model;
						break;
					}
				}

			}
			else
			{
				// 从source往上，则source 为target,target变为source
				TestNodeMdl item = (TestNodeMdl) source.getParent();
				/*
				 * 如果时间间隔连线的连出item包含有周期消息子连线， 则需要将连出item改为包含其父连线的item
				 */
				if (Utils.getFixedChildMessageModel(source.getIncomingConnections()) != null || Utils.getFixedChildMessageModel(source.getOutgoingConnections()) != null)
				{
					this.source = (TestNodeMdl) this.source.getParent().getChildren().get(this.source.getParent().getChildren().indexOf(this.source) - 2);
				}
				int index = item.getChildren().indexOf(source);
				// 寻找从source往上的第一个拥有消息连线的lineItem
				for (int i = index - 1; i >= 0; i--)
				{
					TestNodeMdl model = (TestNodeMdl) item.getChildren().get(i);
					if (model.getOutgoingConnections() != null && model.getOutgoingConnections().size() > 0)
					{
						this.target = model;
						break;
					}
					else if (model.getIncomingConnections() != null && model.getIncomingConnections().size() > 0)
					{
						this.target = model;
						break;
					}
				}
				TestNodeMdl temp = this.target;
				this.target = this.source;
				this.source = temp;

			}
			connection = new IntervalConnMdl(source, target);
		}
		return connection;
	}
}
