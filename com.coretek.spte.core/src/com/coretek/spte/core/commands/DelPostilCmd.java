package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PostilMdl;

/**
 * É¾³ý±êÇ©
 * 
 * @author duyisen 2012-3-15
 */
public class DelPostilCmd extends Command
{

	private ContainerMdl	rootContainerMdl;

	private PostilMdl		postilMdl;

	public DelPostilCmd(ContainerMdl rootContainerMdl, PostilMdl postilMdl)
	{
		this.rootContainerMdl = rootContainerMdl;
		this.postilMdl = postilMdl;
	}

	public void execute()
	{
		rootContainerMdl.removeChild(postilMdl);
		if (postilMdl.getRightChildrenMdl() != null)
		{
			rootContainerMdl.removeChild(postilMdl.getRightChildrenMdl());
		}
		if (postilMdl.getLeftChildrenMdl() != null)
		{
			rootContainerMdl.removeChild(postilMdl.getLeftChildrenMdl());
		}
	}

	public void undo()
	{
		rootContainerMdl.addChild(postilMdl);
		if (postilMdl.getRightChildrenMdl() != null)
		{
			rootContainerMdl.addChild(postilMdl.getRightChildrenMdl());
		}
		if (postilMdl.getLeftChildrenMdl() != null)
		{
			rootContainerMdl.addChild(postilMdl.getLeftChildrenMdl());
		}
	}

}
