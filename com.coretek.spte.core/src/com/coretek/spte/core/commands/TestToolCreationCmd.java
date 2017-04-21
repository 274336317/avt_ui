package com.coretek.spte.core.commands;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.ContainerMdl;

public class TestToolCreationCmd extends Command
{
	private ContainerMdl	parent	= null;
	private AbtNode			child	= null;
	private int				index	= -1;
	protected Point			location;

	public void setLocation(Point location)
	{
		this.location = location;
	}

	@SuppressWarnings("unchecked")
	public void execute()
	{
		assert parent != null;
		assert child != null;
		if (this.location != null)
		{
			this.child.setLocation(this.location);
		}
		parent.addChild(index, child);
		try
		{
			List children = this.child.getParent().getChildren();

			if (children != null && children.size() > 0)
			{
				this.child.setLocation(new Point(this.location.x, ((AbtNode) children.get(0)).getLocation().y));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
		parent.removeChild(child);
	}

	/**
	 * @return 返回 parent.
	 */
	public ContainerMdl getParent()
	{
		return parent;
	}

	/**
	 * @param parent 设置 parent
	 */
	public void setParent(ContainerMdl parent)
	{
		assert parent != null;
		this.parent = parent;
	}

	/**
	 * @return 返回 child.
	 */
	public AbtNode getChild()
	{
		return child;
	}

	/**
	 * @param child 设置 child
	 */
	public void setChild(AbtNode child)
	{
		assert child != null;
		this.child = child;
	}

	/**
	 * @return 返回 index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index 设置 index
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
}
