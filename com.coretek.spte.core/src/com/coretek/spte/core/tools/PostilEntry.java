package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.models.AbtConnMdl;

public class PostilEntry extends CreationToolEntry
{

	public PostilEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
		this.setToolClass(PostilTool.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Tool createTool()
	{
		PostilTool tool = new PostilTool();
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}

}
