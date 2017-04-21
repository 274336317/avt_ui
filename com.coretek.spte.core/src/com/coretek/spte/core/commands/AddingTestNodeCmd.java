package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 添加测试节点
 * 
 * @author 孙大巍
 * 
 *         2011-4-6
 */
public class AddingTestNodeCmd extends Command
{

	private int					sum;

	private RootContainerMdl	root;

	private TestNodeMdl			node;

	public AddingTestNodeCmd(int sum, RootContainerMdl root)
	{
		this.sum = sum;
		this.root = root;
	}

	public AddingTestNodeCmd(int sum, RootContainerMdl root, TestNodeMdl node)
	{
		this.sum = sum;
		this.root = root;
		this.node = node;
	}

	@Override
	public boolean canExecute()
	{
		if (this.node == null)
		{
			return true;
		}
		TestNodeMdl targetNode = this.node;
		if (this.node.getParent() instanceof TestToolMdl)
		{// 将被选中的node转换为被测对象内包含的对应的node
			targetNode = (TestNodeMdl) Utils.getTestedObject(this.node).getChildren().get(this.node.getParent().getChildren().indexOf(this.node));
		}
		// 禁止在周期消息之间 及 子节点的node上执行添加节点的操作
		if (Utils.isInsideCycleMessage(targetNode) || Utils.isCycleChildMessage(targetNode))
		{
			return false;
		}
		// 禁止在拥有时间间隔的node上执行添加节点的操作
		if (Utils.isBetweenTimeredConnections(targetNode))
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean canUndo()
	{

		return false;
	}

	@Override
	public void execute()
	{
		TestToolMdl testTool = (TestToolMdl) root.getChildren().get(0).getChildren().get(0);
		TestedObjectMdl testedObject = (TestedObjectMdl) root.getChildren().get(0).getChildren().get(1);
		int index = -1;
		if (this.node != null)
		{
			index = this.node.getParent().getChildren().indexOf(this.node);
		}
		testTool.addGrandson(index, sum);
		testedObject.addGrandson(index, sum);
	}
}