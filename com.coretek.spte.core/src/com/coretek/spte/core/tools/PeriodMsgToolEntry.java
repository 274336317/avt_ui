package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * 创建周期消息的入口
 * 
 * @author 孙大巍
 * 
 *         2011-4-27
 */
public class PeriodMsgToolEntry extends CreationToolEntry
{

	public PeriodMsgToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
	}

	@Override
	public Tool createTool()
	{
		PeriodMsgTool tool = new PeriodMsgTool((PeriodParentMsgMdl) this.factory.getNewObject());
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}

}
