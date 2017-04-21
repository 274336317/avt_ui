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

import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 测试工具
 * 
 * @author 孙大巍
 * @date 2011-2-19
 */
public class TestToolFgr extends Shape
{

	private Label label;

	private Font font;

	private List<Color> list = new ArrayList<Color>();

	private TestToolMdl model = null;

	public Label getLabel()
	{
		return label;
	}

	public TestToolFgr(TestToolMdl m)
	{
		this.label = new Label();
		this.model = m;
		this.label.setText(model.getName());
		this.label.setTextAlignment(Label.LEFT);
		this.label.setIconTextGap(5);
		// TODO[0803]修改测试工具字体
		this.font = new Font(null, new FontData("宋体", 10, SWT.BOLD));
		//this.font = new Font(null, new FontData("华文行楷", 18, SWT.BOLD));
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
		rst.y = this.getBounds().y + 10;
		return rst;
	}

	public Point getRightAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		rst.x = this.getBounds().x + this.getBounds().width;
		rst.y = this.getBounds().y + 10;
		return rst;
	}

	/**
	 * @see Shape#fillShape(Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{

	}

	// 设置编辑用户测试时序图
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
		Color m_EditorSimuObjectColor = new Color(null, 246, 246, 246);
		this.list.add(m_EditorSimuObjectColor);
		graphics.setBackgroundColor(m_EditorSimuObjectColor);
		m_EditorSimuObjectColor = new Color(null, 246, 246, 246);
		this.list.add(m_EditorSimuObjectColor);
		graphics.setForegroundColor(m_EditorSimuObjectColor);
		graphics.fillGradient(bound, false);

		// bound.height = 9;
		bound.height = 3;
		m_EditorSimuObjectColor = new Color(null, 238, 85, 0);
		graphics.setBackgroundColor(m_EditorSimuObjectColor);
		graphics.setForegroundColor(m_EditorSimuObjectColor);
		graphics.fillGradient(bound, false);

		// bound.y += 9;
		// bound.height = 10;
		bound.y += 3;
		bound.height = 3;
		graphics.setBackgroundColor(m_EditorSimuObjectColor);
		graphics.setForegroundColor(m_EditorSimuObjectColor);
		graphics.fillGradient(bound, false);
		// bound.y += 10;
		// bound.height = 29;
		bound.y += 3;
		bound.height = 20;
		graphics.setForegroundColor(m_EditorSimuObjectColor);
		graphics.setBackgroundColor(m_EditorSimuObjectColor);
		graphics.fillGradient(bound, false);
		// bound.height = 48;
		// bound.y -= 19;
		bound.height = 40;
		bound.y -= 20;
		graphics.setForegroundColor(m_EditorSimuObjectColor);
		graphics.setLineWidth(2);// 设置边框宽度
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.drawRoundRectangle(bound, 0, 0);
		graphics.popState();
	}

	/*
	 * __________________________________________________________________________________
	 * @Class TestToolFgr
	 * @Function setBounds
	 * @Description 设置测试工具字体位置
	 * @Auther MENDY
	 * @param rect
	 * @Date 2016-5-17 下午05:22:26
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
		Rectangle tmp = new Rectangle(rect.x + 25, rect.y + 2, 100, 30); // with100是测试工具字体的位置
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
