package com.coretek.spte.core.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.PostilChildMdl;

public class ResizePostilChildCommand extends Command
{
	private PostilChildMdl	model;
	private Rectangle		constraints;
	private Rectangle		old;

	public ResizePostilChildCommand(PostilChildMdl model, Rectangle constraints)
	{
		super();
		this.model = model;
		this.constraints = constraints;
	}

	@Override
	public void execute()
	{
		old = model.getConstraints();
		model.setConstraints(constraints);
	}

	@Override
	public void undo()
	{
		constraints = model.getConstraints();
		model.setConstraints(old);
	}
}
