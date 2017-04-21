package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 创建并行消息的入口
 * 
 * @author 孙大巍
 * 
 */
public class ParallelToolEntry extends CreationToolEntry
{

	public ParallelToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
	}

	@Override
	public Tool createTool()
	{
		ParallelTool tool = new ParallelTool();
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}
}