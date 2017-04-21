package com.coretek.spte.core.figures;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;

import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 周期消息图形的子连线图形
 * 
 * @author 孙大巍
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
	 * 注意不要设置连线以及箭头的背景颜色，否则会造成CPU占用率过高
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
