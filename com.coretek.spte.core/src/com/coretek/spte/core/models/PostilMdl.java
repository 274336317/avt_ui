package com.coretek.spte.core.models;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.common.utils.StringUtils;

/**
 * 标签模型
 * 
 * @author duyisen 2012-3-15
 */
public class PostilMdl extends AbtNode
{

	/**
	 * 图形的缺省显示颜色
	 */
	protected transient Color	defaultColor;

	private static final long	serialVersionUID	= 4395035948444558527L;

	private Point				location			= new Point(100, 100);

	private Rectangle			rectangle			= new Rectangle(100, 100, 1, 1);

	private String				text;

	public static final String	P_CONSTRAINT		= "postil_constraint";

	/**
	 * 每一个标签模型的唯一标识
	 */
	private String				UUID;

	// 右孩子节点
	private PostilChildMdl		rightChildrenMdl;

	private PostilChildMdl		leftChildrenMdl;
	
	private String type;

	public PostilMdl()
	{
		super();
		this.defaultColor = ColorConstants.red;
		this.UUID = StringUtils.getUUID();
	}

	public Point getLocation()
	{
		return this.location;
	}

	public void setLocation(Point location)
	{
		this.location = location;
		setRectangle(new Rectangle(this.location.x, this.location.y, 1, 1));
	}

	public Dimension refreshRegion()
	{
		//return new Dimension(810, 10);  // 设置右边标签的宽度
		return new Dimension(810, 2);  // 设置右边标签的宽度
	}

	public PostilChildMdl getLeftChildrenMdl()
	{
		return leftChildrenMdl;
	}

	public void setLeftChildrenMdl(PostilChildMdl leftChildrenMdl)
	{
		this.leftChildrenMdl = leftChildrenMdl;
	}

	public PostilChildMdl getRightChildrenMdl()
	{
		return rightChildrenMdl;
	}

	public void setRightChildrenMdl(PostilChildMdl childrenMdl)
	{
		this.rightChildrenMdl = childrenMdl;
	}

	public void setRectangle(Rectangle rectangle)
	{
		this.rectangle = rectangle;
	}

	public Rectangle getConstraints()
	{
		return rectangle;
	}

	public void setConstraints(Rectangle rectangle)
	{
		Rectangle old = getConstraints();
		this.rectangle = rectangle;
		setLocation(new Point(rectangle.x, rectangle.y));
		firePropertyChange(P_CONSTRAINT, old, rectangle);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getUUID()
	{
		return UUID;
	}

	public void setUUID(String uUID)
	{
		UUID = uUID;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

}
