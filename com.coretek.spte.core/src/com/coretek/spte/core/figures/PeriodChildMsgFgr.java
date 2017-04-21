package com.coretek.spte.core.figures;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;

import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ������Ϣͼ�ε�������ͼ��
 * 
 * @author ���Ρ
 * @date 2010-8-30
 * 
 */
public class PeriodChildMsgFgr extends AbtConnFgr
{

	public PeriodChildMsgFgr(AbtConnMdl model)
	{
		super(model);
		this.add(new MidpointOffsetLocator(this, 0));
		setFigureAttr();
	}

	/**
	 * ע�ⲻҪ���������Լ���ͷ�ı�����ɫ����������CPUռ���ʹ���
	 */
	@Override
	protected void outlineShape(Graphics g)
	{
		g.setForegroundColor(this.model.getColor());
		g.setLineWidth(2);
		g.setLineStyle(Graphics.LINE_SOLID);
		this.getTargetDecoration().setForegroundColor(this.model.getColor());
		super.outlineShape(g);
	}

	private void setFigureAttr()
	{
		PolygonDecoration poly = new PolygonDecoration();
		poly.setScale(6, 6);
		this.setTargetDecoration(poly);
		this.setConnectionRouter(new BendpointConnectionRouter());
	}
}
