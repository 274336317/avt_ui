package com.coretek.spte.core.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.Rectangle;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.locators.FKMidpointOffsetLocator;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.router.FKRouter;

/**
 * 消息时间间隔连线图形
 * 
 * @author 孙大巍
 * @date 2010-9-1
 * 
 */
public class TimeSpanFgr extends AbtConnFgr
{

	public TimeSpanFgr(AbtConnMdl model)
	{
		super(model);
		this.lblName.setText(this.model.getName() + " ms");
		if (model.getName() == null || model.getName().trim().length() == 0)
		{
			model.setName(Messages.getString("I18N_INTERVAL")); //时间间隔
		}
		this.add(new FKMidpointOffsetLocator(this, 0));

		setFigureAttr();
	}

	/**
	 * 注意不要设置连线以及箭头的背景颜色，否则会造成CPU占用率过高
	 */
	@Override
	protected void outlineShape(Graphics g)
	{
		g.setForegroundColor(this.model.getColor());
		g.setLineWidth(1); // 设置线条粗细
		this.getTargetDecoration().setForegroundColor(this.model.getColor());
		this.getSourceDecoration().setForegroundColor(this.model.getColor());
		super.outlineShape(g);
	}

	private void setFigureAttr()
	{
		PolygonDecoration sourcePoly = new PolygonDecoration();
		PolygonDecoration targetPoly = new PolygonDecoration();

		this.setTargetDecoration(targetPoly);
		this.setSourceDecoration(sourcePoly);
		this.setConnectionRouter(new FKRouter());
	}

	@Override
	public void setBounds(Rectangle rect)
	{
		super.setBounds(rect);
		Rectangle tmp = new Rectangle(rect.width / 2, rect.y - 25, 10, 10);// 设置标签显示的坐标以及大小
		this.lblName.setBounds(tmp);
		this.lblName.setBackgroundColor(InstanceUtils.getInstance().getIntervalColor());
	}
}
