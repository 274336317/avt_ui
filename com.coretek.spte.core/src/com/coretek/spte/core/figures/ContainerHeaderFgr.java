package com.coretek.spte.core.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class ContainerHeaderFgr extends Figure
{
	public ContainerHeaderFgr()
	{
		XYLayout layout = new XYLayout();
		setLayoutManager(layout);
		setBorder(new ContainerBorder());
		setOpaque(false);
	}

}