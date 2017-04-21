package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;

/**
 * 创建连线的超类，在此类中添加了增加节点的方法
 * 
 * @author 孙大巍
 */
public class AbstractCreationConnCmd extends ConnCommand
{

	public AbstractCreationConnCmd(boolean isCycle, RootContainerMdl root)
	{
		this.checkNodes(isCycle, root);
	}

	public AbstractCreationConnCmd()
	{

	}

	/**
	 * 检查剩余的节点数量是否满足所画连线的要求，如果是周期消息，则应该增加3个节点， 如果是普通消息，则应该增加1个节点。
	 * 
	 * @param isCycle 如果为true，则表明是画周期消息
	 * @param root 底层容器
	 */
	public void checkNodes(boolean isCycle, RootContainerMdl root)
	{
		TestToolMdl tool = (TestToolMdl) root.getChildren().get(0).getChildren().get(0);
		TestNodeContainerMdl node = (TestNodeContainerMdl) tool.getChildren().get(0);
		if (isCycle)
		{
			// 周期消息，需要3个节点
			for (int i = node.getChildren().size() - 1; i >= node.getChildren().size() - 4; i--)
			{
				TestNodeMdl kid = (TestNodeMdl) node.getChildren().get(i);
				if (kid.getIncomingConnections() != null && kid.getIncomingConnections().size() != 0)
				{
					this.add(3, root, node.getChildren().size() - 1);
					break;
				}
				else if (kid.getOutgoingConnections() != null && kid.getOutgoingConnections().size() != 0)
				{
					this.add(3, root, node.getChildren().size() - 1);
					break;
				}
			}
		}
		else
		{
			// 普通消息，需要1个节点
			TestNodeMdl kid = (TestNodeMdl) node.getChildren().get(node.getChildren().size() - 2);
			if (kid.getIncomingConnections() != null && kid.getIncomingConnections().size() != 0)
			{
				this.add(1, root, node.getChildren().size() - 1);
			}
			else if (kid.getOutgoingConnections() != null && kid.getOutgoingConnections().size() != 0)
			{
				this.add(1, root, node.getChildren().size() - 1);
			}
		}
	}

	private void add(int sum, RootContainerMdl root, int index)
	{
		TestToolMdl testTool = (TestToolMdl) root.getChildren().get(0).getChildren().get(0);
		TestedObjectMdl testedObject = (TestedObjectMdl) root.getChildren().get(0).getChildren().get(1);
		testTool.addGrandson(index, sum);
		testedObject.addGrandson(index, sum);
	}

}
