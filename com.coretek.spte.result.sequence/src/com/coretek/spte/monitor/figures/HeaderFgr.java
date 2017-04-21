/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * ��ʾ���ܶ����ͼ��
 * 
 * @author ���Ρ
 * @date 2012-1-5
 */
public class HeaderFgr extends Shape
{

	private int				functionId;							// ���ܱ��

	private String			functionName;							// ������

	private List<NodeFgr>	nodeFgrs	= new ArrayList<NodeFgr>();

	public HeaderFgr(int functionId, String functionName)
	{
		this.functionId = functionId;
		this.functionName = functionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.pushState();
		graphics.fillRectangle(this.getBounds());
		graphics.drawRectangle(new Rectangle(this.getBounds().x + 5, this.getBounds().y + 5, this.getBounds().width - 10, this.getBounds().height - 10));
		graphics.drawString(String.valueOf(this.functionName), this.getBounds().x + 25, this.getBounds().y + 15);
		graphics.popState();

	}

	/**
	 * ��ȡͼ������ʾ�Ĺ��ܶ����id
	 * 
	 * @return the functionId <br/>
	 * 
	 */
	public int getFunctionId()
	{
		return functionId;
	}

	/**
	 * ��ȡͼ������ʾ�Ĺ��ܶ��������
	 * 
	 * @return the functionName <br/>
	 * 
	 */
	public String getFunctionName()
	{
		return functionName;
	}

	public void addNode(NodeFgr node)
	{
		this.nodeFgrs.add(node);
	}

	public List<NodeFgr> getNodes()
	{

		return this.nodeFgrs;
	}
}