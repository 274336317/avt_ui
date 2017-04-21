package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.parts.TestPart;

/**
 * 改变生命线的高度和位置的命令
 * 
 * @author 孙大巍
 * @date 2010-8-20
 * 
 */
public class ChangeNodeConstraintCmd extends Command
{
	private TestMdl		node;

	private Point		oldPos;

	private Point		newPos;

	private Dimension	oldSize;

	private Dimension	newSize;

	public ChangeNodeConstraintCmd(TestPart part)
	{

	}

	public void setLocation(Point p)
	{
		this.oldPos = this.node.getLocation();
		p.y = oldPos.y;
		this.newPos = p;
	}

	public void setDimension(Dimension d)
	{
		this.oldSize = this.node.getSize();
		this.newSize = d;
		this.newSize.width = this.oldSize.width;
	}

	public void setNode(TestMdl node)
	{
		this.node = node;
	}

	@Override
	public void execute()
	{
		node.setLocation(newPos);
		this.node.setSize(newSize);
		AbtElement parent = this.node.getParent();
		for (Object element : parent.getChildren())
		{
			if (element == this.node || element instanceof PostilMdl || element instanceof PostilChildMdl)
			{
				continue;
			}
			((TestMdl) element).setSize(newSize);
		}
	}

	@Override
	public String getLabel()
	{
		return "";
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		this.node.setLocation(oldPos);
		this.node.setSize(this.oldSize);
	}

	@Override
	public boolean canExecute()
	{

		return true;
	}
}