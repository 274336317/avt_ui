package com.coretek.spte.core.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.image.IconFactory;
import com.coretek.spte.core.image.ImageConstants;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class ContainerFgr extends Figure
{

	private Label				name			= new Label();

	private ContainerHeaderFgr	containerFigure	= new ContainerHeaderFgr();

	private List<Color>			list			= new ArrayList<Color>();

	private Image				logo;

	private Font				font;

	private Image				icon;

	public ContainerFgr()
	{
		ToolbarLayout layout = new ToolbarLayout();
		layout.setVertical(true);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		Color color = new Color(null, 211, 213, 220);
		list.add(color);
		setBorder(new LineBorder(color));
		setOpaque(true);
		logo = IconFactory.getImageDescriptor(ImageConstants.EDITOR_STAUS).createImage();
		name.setIcon(logo);
		name.setIconAlignment(Label.LEFT);
		font = new Font(SPTEPlugin.getActiveWorkbenchShell().getDisplay(), "ËÎÌå", 10, 1);
		name.setFont(font);
		name.setText("");
		name.setIconTextGap(10);
		name.setLabelAlignment(PositionConstants.LEFT);
		add(name);
		add(containerFigure);
		this.setOpaque(false);
	}

	public ContainerHeaderFgr getContainerFigure()
	{
		return containerFigure;
	}

	public void setName(String name)
	{
		this.name.setText(name);
	}

	public void setIcon(Image icon)
	{
		name.setIcon(icon);
	}

	public void setIcon(String icon)
	{
		this.icon = IconFactory.getImageDescriptor(icon).createImage();
		name.setIcon(this.icon);
	}

	public void paint(Graphics graphics)
	{
		graphics.pushState();
		Rectangle bound = this.getBounds().getCopy();
		bound.height -= 2;
		bound.width -= 2;
		Color color = new Color(null, 246, 246, 246);
		list.add(color);
		graphics.setBackgroundColor(color);
		color = new Color(null, 246, 246, 246);
		list.add(color);
		graphics.setForegroundColor(color);
		graphics.fillGradient(bound, false);
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.popState();
		super.paint(graphics);
	}

	public void dispose()
	{
		if (font != null)
		{
			font.dispose();
			font = null;
		}
		for (Color color : list)
		{
			color.dispose();
		}
		this.list.clear();
		this.list = null;
		if (logo != null)
		{
			logo.dispose();
			logo = null;
		}
		if (icon != null)
		{
			this.icon.dispose();
			this.icon = null;
		}
	}
}
