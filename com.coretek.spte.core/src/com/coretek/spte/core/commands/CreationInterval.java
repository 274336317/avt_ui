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
 * ��֤����ʱ����ģ��
 * 
 * @author ���Ρ
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
		// ������һ��item��Ϊ���timer������item
		if (Utils.isSourceOfTimer(source))
		{
			return false;
		}

		// source item ��������Ϣ����
		if (!Utils.hasConnection(source))
		{
			return false;
		}

		// ������timer�������Թ�����
		TestMdl lifeline = (TestMdl) source.getParent().getParent();
		if (lifeline instanceof TestToolMdl)
		{
			return false;
		}

		// ������ʱ�������ߴ�������Ϣ�ĸ���������������
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
				this.source.setMesType("ʱ����");
				// ��source����
				/*
				 * ���ʱ������Ϣ������item������������Ϣ�ĸ����ӣ� ����Ҫ��source��Ϊ����������Ϣ�����ӵ�item
				 */
				if (Utils.getFixedParentMessageModel(source.getIncomingConnections()) != null || Utils.getFixedParentMessageModel(source.getOutgoingConnections()) != null)
				{
					this.source = (TestNodeMdl) this.source.getParent().getChildren().get(this.source.getParent().getChildren().indexOf(this.source) + 2);
				}

				TestNodeContainerMdl item = (TestNodeContainerMdl) source.getParent();
				int index = item.getChildren().indexOf(source);
				/*
				 * Ѱ�Ҵ�source���µĵ�һ��ӵ����Ϣ���ߵ�lineItem
				 */
				for (int i = index + 1; i < item.getChildren().size(); i++)
				{
					TestNodeMdl model = (TestNodeMdl) item.getChildren().get(i);
					// Ŀ��item��������Ϣ���Ӳſ��Ի�ʱ��������
					if (model.getOutgoingConnections() != null && model.getOutgoingConnections().size() > 0)
					{
						PeriodChildMsgMdl childModel = Utils.getFixedChildMessageModel(model.getOutgoingConnections());
						/*
						 * ���ʱ����������Ŀ����ӵ��������Ϣ�����ӵ�item��
						 * ��Ӧ����Ŀ��item��Ϊ����������Ϣ�����ӵ�item
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
						 * ���ʱ����������Ŀ����ӵ��������Ϣ�����ӵ�item��
						 * ��Ӧ����Ŀ��item��Ϊ����������Ϣ�����ӵ�item
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
				// ��source���ϣ���source Ϊtarget,target��Ϊsource
				TestNodeMdl item = (TestNodeMdl) source.getParent();
				/*
				 * ���ʱ�������ߵ�����item������������Ϣ�����ߣ� ����Ҫ������item��Ϊ�����丸���ߵ�item
				 */
				if (Utils.getFixedChildMessageModel(source.getIncomingConnections()) != null || Utils.getFixedChildMessageModel(source.getOutgoingConnections()) != null)
				{
					this.source = (TestNodeMdl) this.source.getParent().getChildren().get(this.source.getParent().getChildren().indexOf(this.source) - 2);
				}
				int index = item.getChildren().indexOf(source);
				// Ѱ�Ҵ�source���ϵĵ�һ��ӵ����Ϣ���ߵ�lineItem
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
