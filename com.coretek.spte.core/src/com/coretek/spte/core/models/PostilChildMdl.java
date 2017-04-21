package com.coretek.spte.core.models;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.spte.core.InstanceUtils;

/**
 * 子标签模型
 * 
 * @author duyisen 2012-3-15
 */
public class PostilChildMdl extends AbtNode
{

	private static final long serialVersionUID = -7932950665663212712L;

	public static final String P_TEXT = "p_text";

	public static final String P_CONSTRAINT = "p_constraint";

	/**
	 * 图形的缺省显示颜色
	 */
	protected transient Color defaultColor;

	private PostilMdl parentMdl;

	private Point location = new Point(100, 100);

	private Rectangle rectangle = new Rectangle(100, 100, 50, 12);

	private String text;

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Function PostilChildMdl
	 * @Description 构造函数设置标签容器名称及颜色
	 * @Auther MENDY
	 * @Date 2016-5-16 下午03:55:22
	 */
	public PostilChildMdl()
	{
		super();
		this.defaultColor = InstanceUtils.getInstance().getLableColor();
		this.text = "注释";
	}

	public Point getLocation()
	{
		return location;
	}

	/*
	 * __________________________________________________________________________________
	 * @Class PostilChildMdl
	 * @Function setLocation
	 * @Description 设置标签输入字符串的长度
	 * @Auther MENDY
	 * @param location
	 * @Date 2016-5-17 下午03:57:27
	 */
	public void setLocation(Point location)
	{
		this.location = location;
		if (this.location.x < 460)
		{
			setRectangle(new Rectangle(this.location.x, this.location.y, 250, 12)); 
		}
		else
		{
			setRectangle(new Rectangle(this.location.x, this.location.y, 450, 12));
		}
	}

	public Dimension refreshRegion()
	{
		return new Dimension(160, 50);
	}

	public void setRectangle(Rectangle rectangle)
	{
		this.rectangle = rectangle;
	}

	public Rectangle getConstraints()
	{
		return rectangle;
	}

	public PostilMdl getParentMdl()
	{
		return parentMdl;
	}

	public void setParentMdl(PostilMdl parentMdl)
	{
		this.parentMdl = parentMdl;
	}

	public void setConstraints(Rectangle rectangle)
	{
		Rectangle old = getConstraints();
		this.rectangle = rectangle;
		this.location = new Point(rectangle.x, rectangle.y);
		firePropertyChange(P_CONSTRAINT, old, rectangle);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		String old = this.text;
		this.text = text;
		firePropertyChange(P_TEXT, old, text);
	}
}
