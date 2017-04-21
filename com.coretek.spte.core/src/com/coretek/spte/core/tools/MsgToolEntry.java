package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 创建消息连接，在此类中需要将FConnectionModel的isTimer属性设置为false ，表明需要创建一个消息连接而不是时间间隔连接
 * 
 * @author 孙大巍
 * @date 2010-8-23
 * 
 */
public class MsgToolEntry extends CreationToolEntry
{

	public MsgToolEntry(String label, String shortDesc, CreationFactory factory, ImageDescriptor iconSmall, ImageDescriptor iconLarge)
	{
		super(label, shortDesc, factory, iconSmall, iconLarge);
		this.setToolClass(MsgConnTool.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Tool createTool()
	{
		MsgConnTool tool = new MsgConnTool((AbtConnMdl) this.factory.getNewObject(), false);
		tool.setUnloadWhenFinished(true);
		tool.setDefaultCursor(SharedCursors.CROSS);
		return tool;
	}

}