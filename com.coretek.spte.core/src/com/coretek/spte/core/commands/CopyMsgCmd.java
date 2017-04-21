package com.coretek.spte.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ������Ϣ����
 * 
 * @author ���Ρ
 * @date 2010-9-6
 * 
 */
public class CopyMsgCmd extends Command
{

	private List<AbtConnMdl>	models	= new ArrayList<AbtConnMdl>();

	public boolean addElement(AbtConnMdl model)
	{
		if (models.contains(model))
		{
			return false;
		}
		return models.add(model);
	}

	@Override
	public boolean canExecute()
	{

		return !models.isEmpty();
	}

	@Override
	public boolean canUndo()
	{
		return false;
	}

	@Override
	public void execute()
	{
		Clipboard.getDefault().setContents(this.models);
	}
}
