package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * 重命名命令
 * 
 * @author 孙大巍
 * @date 2010-8-18
 * 
 */
public class RenameCmd extends Command
{

	private AbtElement	node;

	private String		newName;

	private String		oldName;

	public void setName(String name)
	{
		this.newName = name;
	}

	public void setNode(AbtElement node)
	{
		this.node = node;
	}

	public String getLabel()
	{
		return "重命名";
	}

	public void execute()
	{
		oldName = this.node.getName();
		this.node.setName(newName);
		if (node instanceof PeriodParentMsgMdl)
		{
			PeriodParentMsgMdl parent = (PeriodParentMsgMdl) node;
			parent.getFixedChild().setName(newName);
		}
	}

	public void undo()
	{
		this.node.setName(oldName);
		if (node instanceof PeriodParentMsgMdl)
		{
			PeriodParentMsgMdl parent = (PeriodParentMsgMdl) node;
			parent.getFixedChild().setName(oldName);
		}
	}

	public void redo()
	{
		this.node.setName(newName);
	}
}