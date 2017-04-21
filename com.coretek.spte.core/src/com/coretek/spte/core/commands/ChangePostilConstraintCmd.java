package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.PostilMdl;

/**
 * �ƶ���ǩ������,���ܰ���y���ƶ�
 * 
 * @author duyisen 2012-3-14
 */
public class ChangePostilConstraintCmd extends Command
{
	private Rectangle	oldCons;

	private Rectangle	newCons;

	private PostilMdl	postilMdl;

	public ChangePostilConstraintCmd(PostilMdl postilMdl, Rectangle rectangle)
	{
		super();
		this.postilMdl = postilMdl;
		oldCons = postilMdl.getConstraints();
		// ֻ����y�����ƶ�
		this.newCons = new Rectangle(rectangle.x, rectangle.y + 15, oldCons.width, oldCons.height);
	}

	@Override
	public boolean canExecute()
	{

		return true;
	}

	@Override
	public void execute()
	{
		postilMdl.setConstraints(newCons);
	}

	@Override
	public void undo()
	{
		newCons = postilMdl.getConstraints();
		postilMdl.setConstraints(oldCons);
	}
}