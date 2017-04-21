package com.coretek.spte.core.figures;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.swt.SWT;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.core.locators.MidpointOffsetLocatorForDot;
import com.coretek.spte.core.models.AbtConnMdl;
/**
 * __________________________________________________________________________________
 * @Class PeriodParentMsgFgr.java
 * @Description ������Ϣ�����м�..ͼ��
 *
 * 
 * @Auther MENDY
 * @Date 2016-5-17 ����03:11:28
 */
public class PeriodParentMsgFgr extends AbtConnFgr
{

	private Label	lblDot;

	public PeriodParentMsgFgr(AbtConnMdl model)
	{
		super(model);
		this.add(new MidpointOffsetLocator(this, 0));
		setFigureAttr();
		for (int i = 2; i < 32; i = i + 2)
		{
			this.lblDot = new Label();
			this.lblDot.setTextAlignment(SWT.CENTER);
			this.lblDot.setText("..");
			this.lblDot.setForegroundColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor()); // ����������Ϣ�е�...��ɫ
			this.add(this.lblDot, new MidpointOffsetLocatorForDot(this, 0, i));
		}
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
