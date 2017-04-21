package com.coretek.spte.core.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;

import com.coretek.spte.core.image.IconFactory;
import com.coretek.spte.core.image.ImageConstants;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.PostilMdl;

/**
 * 调色板工厂类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class PaletteFactory
{
	public static PaletteRoot createPalette()
	{
		PaletteRoot paletteRoot = new PaletteRoot(); // 新建选项板
		paletteRoot.addAll(createCategories(paletteRoot));
		return paletteRoot;
	}

	private static List<PaletteContainer> createCategories(PaletteRoot root)
	{
		List<PaletteContainer> categories = new ArrayList<PaletteContainer>();
		categories.add(createControlGroup(root));
		categories.add(createComponentsDrawer());

		return categories;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root)
	{
		PaletteGroup controlGroup = new PaletteGroup("工具");
		ToolEntry tool = new SelectionToolEntry();
		root.setDefaultEntry(tool); // 设置默认选项
		controlGroup.add(tool);
		return controlGroup;
	}
	
	private static PaletteContainer createComponentsDrawer()
	{
		PaletteDrawer drawer = new PaletteDrawer("");  // 名称时序图元素
		
		List<ToolEntry> entries = new ArrayList<ToolEntry>();

		ToolEntry tool = new MsgToolEntry("", "创建消息", new SimpleFactory(MsgConnMdl.class), IconFactory.getImageDescriptor(ImageConstants.MESSAGE), null);
		entries.add(tool);

		tool = new ParallelToolEntry("", "创建并行消息", new SimpleFactory(ParallelMsgMdl.class), IconFactory.getImageDescriptor(ImageConstants.PARALLEL), null);
		entries.add(tool);
		//drawer.add(new PaletteSeparator()); // 设置分离

		tool = new BackgroundMsgToolEntry("", "创建背景消息", new SimpleFactory(BackgroundMsgMdl.class), IconFactory.getImageDescriptor(ImageConstants.BACKGROUND), null);
		entries.add(tool);

		tool = new PeriodMsgToolEntry("", "创建一个周期消息", new SimpleFactory(PeriodParentMsgMdl.class), IconFactory.getImageDescriptor(ImageConstants.CYLCE), null);
		entries.add(tool);

		tool = new IntervalConnToolEntry("", "创建消息间的时间间隔", new TestNodeFactory(IntervalConnMdl.class, true), IconFactory.getImageDescriptor(ImageConstants.INTERVAL), null);
		entries.add(tool);

		tool = new PostilEntry("", "创建标签", new SimpleFactory(PostilMdl.class), IconFactory.getImageDescriptor(ImageConstants.POSTIL), null);
		entries.add(tool);

		drawer.addAll(entries);
		ImageDescriptor imageDescriptor = IconFactory.getImageDescriptor(ImageConstants.TOOL_MENU);
		drawer.setSmallIcon(imageDescriptor);
		drawer.setLargeIcon(imageDescriptor);
		
		return drawer;
	}
}