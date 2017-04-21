package com.coretek.spte.core.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 被测对象
 * 
 * @author 孙大巍
 * @date 2011-2-19
 */
public class TestedObjectFgr extends Shape
{

	private Label label;

	private Font font;

	private List<Color> list = new ArrayList<Color>();

	private TestedObjectMdl model = null;

	public Label getLabel()
	{
		return label;
	}

	public TestedObjectFgr(TestedObjectMdl m)
	{
		this.label = new Label();
		this.model = m;
		this.label.setText(model.getName());
		this.label.setTextAlignment(Label.LEFT);// 设置文本或者图片在容器上显示。对齐方式为：SWT.LEFT、SWT.CENTER、SWT.RIGHT
		this.label.setIconTextGap(5);
		this.font = new Font(null, new FontData("宋体", 10, SWT.BOLD));
		this.label.setFont(font);
		this.add(label);
		this.setBorder(null);
	}

	public String getName()
	{
		return this.label.getText();
	}

	public Rectangle getTextBounds()
	{
		return this.label.getTextBounds();
	}

	public void setName(String name)
	{
		this.label.setText(name);
		this.repaint();
	}

	public Point getLeftAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		rst.x = this.getBounds().x;
		rst.y = this.getBounds().y + 15;
		return rst;
	}

	public Point getRightAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		rst.x = this.getBounds().x + this.getBounds().width;
		rst.y = this.getBounds().y + 15;
		return rst;
	}

	/**
	 * @see Shape#fillShape(Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
	}

	/**
	 * @see Shape#outlineShape(Graphics)
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.pushState();
		Rectangle bound = this.getBounds().getCopy();

		bound.height -= 2;
		bound.width -= 2;
		Color m_EditorObjectColor = new Color(null, 246, 246, 246);
		this.list.add(m_EditorObjectColor);
		graphics.setBackgroundColor(m_EditorObjectColor);
		m_EditorObjectColor = new Color(null, 246, 246, 246);
		this.list.add(m_EditorObjectColor);
		graphics.setForegroundColor(m_EditorObjectColor);
		graphics.fillGradient(bound, false);

		// bound.height = 9;
		bound.height = 3;
		m_EditorObjectColor = new Color(null, 128, 191, 255);
		graphics.setBackgroundColor(m_EditorObjectColor);
		graphics.setForegroundColor(m_EditorObjectColor);
		graphics.fillGradient(bound, false);

		// bound.y += 9;
		// bound.height = 10;
		bound.y += 3;
		bound.height = 3;
		graphics.setBackgroundColor(m_EditorObjectColor);
		graphics.setForegroundColor(m_EditorObjectColor);
		graphics.fillGradient(bound, false);
		// bound.y += 10;
		// bound.height = 29;
		bound.y += 3;
		bound.height = 20;
		graphics.setForegroundColor(m_EditorObjectColor);
		graphics.setBackgroundColor(m_EditorObjectColor);
		graphics.fillGradient(bound, false);
		// bound.height = 48;
		// bound.y -= 19;
		bound.height = 40;
		bound.y -= 20;
		graphics.setForegroundColor(m_EditorObjectColor);
		graphics.setLineWidth(2);// 设置边框宽度
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.drawRoundRectangle(bound, 0, 0);
		graphics.popState();
	}

	/*
	 * __________________________________________________________________________________
	 * @Class TestedObjectFgr
	 * @Function setBounds
	 * @Description 设置被测节点字体位置
	 * @Auther MENDY
	 * @param rect
	 * @Date 2016-5-17 下午05:24:10
	 */
	@Override
	public void setBounds(Rectangle rect)
	{
		if (this.getBounds() == null || this.getBounds().height == 0)
		{
			rect.height = SPTEConstants.INIT_LIFELINE_HEIGHT;
		}
		if (rect.height >= SPTEConstants.MAX_HEIGHT_OF_LIFELINE)
		{
			super.setBounds(rect);
			return;
		}
		else if (this.model.getSize().height > SPTEConstants.INIT_LIFELINE_HEIGHT && this.model.getSize().height < SPTEConstants.MAX_HEIGHT_OF_LIFELINE)
		{
			rect.height = this.model.getSize().height;
		}
		super.setBounds(rect);
		Rectangle tmp = new Rectangle(rect.x + 25, rect.y + 2, 100, 30);
		this.label.setBounds(tmp);
	}

	public void dispose()
	{
		this.font.dispose();
		this.font = null;

		for (Color color : list)
		{
			color.dispose();
		}

		this.list.clear();
		this.list = null;
	}
}
