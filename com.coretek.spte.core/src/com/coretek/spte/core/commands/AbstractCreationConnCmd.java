package com.coretek.spte.core.commands;

import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;

/**
 * �������ߵĳ��࣬�ڴ�������������ӽڵ�ķ���
 * 
 * @author ���Ρ
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
	 * ���ʣ��Ľڵ������Ƿ������������ߵ�Ҫ�������������Ϣ����Ӧ������3���ڵ㣬 �������ͨ��Ϣ����Ӧ������1���ڵ㡣
	 * 
	 * @param isCycle ���Ϊtrue��������ǻ�������Ϣ
	 * @param root �ײ�����
	 */
	public void checkNodes(boolean isCycle, RootContainerMdl root)
	{
		TestToolMdl tool = (TestToolMdl) root.getChildren().get(0).getChildren().get(0);
		TestNodeContainerMdl node = (TestNodeContainerMdl) tool.getChildren().get(0);
		if (isCycle)
		{
			// ������Ϣ����Ҫ3���ڵ�
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
			// ��ͨ��Ϣ����Ҫ1���ڵ�
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
