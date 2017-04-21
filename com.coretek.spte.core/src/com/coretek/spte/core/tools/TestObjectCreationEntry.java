package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 创建测试对象的入口
 * 
 * @author 孙大巍
 * @date 2010-11-25
 * 
 */
public class TestObjectCreationEntry extends CombinedTemplateCreationEntry
{

	public TestObjectCreationEntry(String label, String shortDesc, Object template, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, template, factory, iconSmall, iconLarge);
		this.setToolClass(TestObjectTool.class);
	}

	public Tool createTool()
	{
		TestObjectTool tool = new TestObjectTool();
		tool.setDefaultCursor(SharedCursors.CURSOR_PLUG);
		tool.setUnloadWhenFinished(true);
		tool.setProperties(getToolProperties());
		return tool;
	}

}
