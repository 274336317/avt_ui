package com.coretek.spte.core.tools;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ������Ϣ���ӣ��ڴ�������Ҫ��FConnectionModel��isTimer��������Ϊfalse ��������Ҫ����һ����Ϣ���Ӷ�����ʱ��������
 * 
 * @author ���Ρ
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