package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.models.PostilMdl;

/**
 * 添加标签命令
 * 
 * @author duyisen 2012-2-28
 */
public class AddingPostilCmd extends Command
{

	private ContainerMdl root;

	private Point point;

	private PostilMdl postilModel = null;

	public AddingPostilCmd(Point point, ContainerMdl root)
	{
		this.point = point;
		this.root = root;
	}

	public AddingPostilCmd(ContainerMdl root, PostilMdl postilMdl)
	{
		this.root = root;
		this.postilModel = postilMdl;
	}

	@Override
	public boolean canExecute()
	{
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
		if (null == this.postilModel)
		{
			PostilMdl postilMdl = new PostilMdl();
			Point postilLinePoint = new Point(10, this.point.y);
			postilMdl.setLocation(postilLinePoint);
			if (this.point.x <=300)
			{
				// 以标签为基准，所有标签容器在右边
				Point postilPoint = new Point(10, this.point.y - 17);
				PostilChildMdl rightPostilChildMdl = new PostilChildMdl();
				rightPostilChildMdl.setLocation(postilPoint);
				postilMdl.setRightChildrenMdl(rightPostilChildMdl);
				rightPostilChildMdl.setParentMdl(postilMdl);
				this.postilModel = postilMdl;
				root.addChild(rightPostilChildMdl);
			}
			else
			{
				// 以标签为基准，所有标签容器在左边
				Point postilPoint2 = new Point(460, this.point.y + 2);
				PostilChildMdl leftPostilChildMdl = new PostilChildMdl();
				leftPostilChildMdl.setLocation(postilPoint2);
				postilMdl.setLeftChildrenMdl(leftPostilChildMdl);
				leftPostilChildMdl.setParentMdl(postilMdl);
				this.postilModel = postilMdl;
				root.addChild(leftPostilChildMdl);
			}
			root.addChild(postilMdl);
		}
		else
		{
			root.addChild(postilModel);
			postilModel.getLocation().y -= 15;
			if (null != postilModel.getRightChildrenMdl())
				root.addChild(postilModel.getRightChildrenMdl());
			if (null != postilModel.getLeftChildrenMdl())
				root.addChild(postilModel.getLeftChildrenMdl());
		}

	}
}
