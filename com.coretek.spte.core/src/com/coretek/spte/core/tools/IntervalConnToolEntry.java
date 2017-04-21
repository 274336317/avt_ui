package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 创建时间间隔的入口
 * 
 * @author 孙大巍
 * @date 2010-11-24
 * 
 */
public class IntervalConnToolEntry extends CreationToolEntry
{

	public IntervalConnToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
		this.setToolClass(IntervalConnTool.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Tool createTool()
	{
		IntervalConnTool tool = new IntervalConnTool((AbtConnMdl) this.factory.getNewObject(), true);
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}

}
