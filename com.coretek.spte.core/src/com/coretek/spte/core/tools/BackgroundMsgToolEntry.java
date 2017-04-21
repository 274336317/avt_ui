package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * ±³¾°ÏûÏ¢
 * 
 * @author Ëï´óÎ¡
 * 
 *         2011-3-29
 */
public class BackgroundMsgToolEntry extends CreationToolEntry
{

	public BackgroundMsgToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
		this.setToolClass(BackgroundMsgTool.class);
	}

	@Override
	public Tool createTool()
	{
		BackgroundMsgTool tool = new BackgroundMsgTool();
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}
}