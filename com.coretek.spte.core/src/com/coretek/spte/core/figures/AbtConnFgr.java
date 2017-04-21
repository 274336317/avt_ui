package com.coretek.spte.core.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ���߻���
 * 
 * @author ���Ρ
 * @date 2010-8-21
 */
public abstract class AbtConnFgr extends PolylineConnection
{

	/**
	 * ��������ʾ�����ֱ�ǩ
	 */
	protected Label lblName;

	protected AbtConnMdl model;

	private Color color;

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public AbtConnFgr(AbtConnMdl model)
	{
		this.model = model;
		this.lblName = new Label();
		this.lblName.setText(this.model.getName());
		this.lblName.setTextAlignment(Label.LEFT);
		this.lblName.setIconTextGap(5);
	}

	/**
	 * ��ӱ�ǩ��ʾ��λ��Լ������
	 * 
	 * @param constraint
	 */
	public void add(Object constraint)
	{
		this.add(this.lblName, constraint);
	}

	public Label getLabel()
	{
		return this.lblName;
	}

	public String getName()
	{
		return this.model.getName();
	}

	public Rectangle getTextBounds()
	{
		return this.lblName.getTextBounds();
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class AbtConnFgr
	 * @Function setName
	 * @Description ������Ϣ������Ϣ����ɫ
	 * @Auther MENDY
	 * @param name
	 *        void
	 * @Date 2016-5-17 ����04:17:45
	 */
	public void setName(String name)
	{
		this.lblName.setText(name);
		if (this.model.getName().contains("��ͨ") || this.model.getName().contains("����"))
		{
			this.lblName.setForegroundColor(InstanceUtils.getInstance().getMesOrBackgroudDefaultColor());
		}
		if (this.model.getName().contains("����") || this.model.getName().contains("����"))
		{
			this.lblName.setForegroundColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor());
		}
		this.model.setName(name);
		this.repaint();
	}

	@Override
	protected void outlineShape(Graphics g)
	{
		super.outlineShape(g);
		g.setLineWidth(3);
	}
}
