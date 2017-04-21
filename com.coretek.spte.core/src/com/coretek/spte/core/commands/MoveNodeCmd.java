package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtNode;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class MoveNodeCmd extends Command
{
	private AbtNode	node;

	private Point	oldPos;

	private Point	newPos;

	public void setLocation(Point p)
	{
		this.newPos = p;
	}

	public void setNode(AbtNode node)
	{
		this.node = node;
	}

	public void execute()
	{
		oldPos = this.node.getLocation();
		node.setLocation(newPos);
	}

	public String getLabel()
	{
		return "";
	}

	public void redo()
	{
		this.node.setLocation(newPos);
	}

	public void undo()
	{
		this.node.setLocation(oldPos);
	}
}