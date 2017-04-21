package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.RootContainerMdl;

/**
 * 创建时序图容器
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class CreateContainerCmd extends Command
{

	protected RootContainerMdl	rootContainer;

	protected AbtNode			node;

	protected Point				location;

	private boolean				isTester;

	public boolean isTester()
	{
		return isTester;
	}

	public void setTester(boolean isTester)
	{
		this.isTester = isTester;
	}

	public void setDiagram(RootContainerMdl rootContainer)
	{
		this.rootContainer = rootContainer;
	}

	public void setNode(AbtNode node)
	{
		this.node = node;
	}

	public void setLocation(Point location)
	{
		this.location = location;
	}

	public void execute()
	{
		if (this.location != null)
		{
			this.node.setLocation(this.location);
		}

		this.rootContainer.addChild(this.node);
	}

	public String getLabel()
	{
		return "";
	}

	public void redo()
	{
		this.execute();
	}

	public void undo()
	{
		rootContainer.removeChild(node);
	}
}