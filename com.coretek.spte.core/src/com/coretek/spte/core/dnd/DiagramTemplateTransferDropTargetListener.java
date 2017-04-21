package com.coretek.spte.core.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import com.coretek.spte.core.tools.ElementFactory;

/**
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class DiagramTemplateTransferDropTargetListener extends TemplateTransferDropTargetListener
{

	/*
	 * 拖动的是否是测试对象
	 */

	public static boolean	IS_TEST_OBJ	= false;

	/**
	 * @param viewer
	 */
	public DiagramTemplateTransferDropTargetListener(EditPartViewer viewer)
	{
		super(viewer);
	}

	protected CreationFactory getFactory(Object template)
	{
		return new ElementFactory(template);
	}
}