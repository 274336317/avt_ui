package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.RootContainerMdl;

/**
 * ɾ�����Թ���/��������������
 * 
 * @author ���Ρ
 * @date 2010-8-21
 * 
 */
public class DelNodeCmd extends Command
{

	private RootContainerMdl	transmodel;

	private ContainerMdl		sub;

	public void setTransModel(RootContainerMdl transmodel)
	{
		this.transmodel = transmodel;
	}

	public void setNode(ContainerMdl sub)
	{
		this.sub = sub;
	}

	public void execute()
	{

		transmodel.removeChild(sub);
	}

	public String getLabel()
	{
		return "";
	}

	public void redo()
	{
		execute();
	}

	public void undo()
	{

		transmodel.addChild(sub);
	}
}