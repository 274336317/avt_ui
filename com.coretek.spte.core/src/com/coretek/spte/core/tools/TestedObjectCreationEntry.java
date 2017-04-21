package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 被测对象
 * 
 * @author 孙大巍
 * @date 2010-11-25
 * 
 */
public class TestedObjectCreationEntry extends CombinedTemplateCreationEntry
{

	public TestedObjectCreationEntry(String label, String shortDesc, Object template, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, template, factory, iconSmall, iconLarge);
		this.setToolClass(TestedObjectTool.class);
	}

	public Tool createTool()
	{
		TestedObjectTool tool = new TestedObjectTool();
		tool.setDefaultCursor(SharedCursors.CURSOR_PLUG);
		tool.setUnloadWhenFinished(true);
		tool.setProperties(getToolProperties());
		return tool;
	}
}
