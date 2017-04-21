package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.PostilChildMdl;

/**
 * ֱ�ӱ༭��ǩ������
 * 
 * @author duyisen 2012-3-2
 */
public class DirectPostilCommand extends Command
{

	private PostilChildMdl	model;

	private String			text;

	private String			old;

	public DirectPostilCommand(PostilChildMdl model, String text)
	{
		super();
		this.model = model;
		this.text = text;
	}

	@Override
	public void execute()
	{
		old = model.getText();
		String txt = text;
		model.setText(txt);

	}

	@Override
	public void undo()
	{
		text = model.getText();
		model.setText(old);

	}
}