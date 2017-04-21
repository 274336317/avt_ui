package com.coretek.spte.core.models;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.common.utils.StringUtils;

/**
 * ��ǩģ��
 * 
 * @author duyisen 2012-3-15
 */
public class PostilMdl extends AbtNode
{

	/**
	 * ͼ�ε�ȱʡ��ʾ��ɫ
	 */
	protected transient Color	defaultColor;

	private static final long	serialVersionUID	= 4395035948444558527L;

	private Point				location			= new Point(100, 100);

	private Rectangle			rectangle			= new Rectangle(100, 100, 1, 1);

	private String				text;

	public static final String	P_CONSTRAINT		= "postil_constraint";

	/**
	 * ÿһ����ǩģ�͵�Ψһ��ʶ
	 */
	private String				UUID;

	// �Һ��ӽڵ�
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
		//return new Dimension(810, 10);  // �����ұ߱�ǩ�Ŀ��
		return new Dimension(810, 2);  // �����ұ߱�ǩ�Ŀ��
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
