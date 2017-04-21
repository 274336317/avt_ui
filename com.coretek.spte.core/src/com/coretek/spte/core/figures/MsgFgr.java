package com.coretek.spte.core.figures;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 消息连线
 * 
 * @author 孙大巍
 * @date 2010-8-18
 * 
 */
public class MsgFgr extends AbtConnFgr
{

	public MsgFgr(AbtConnMdl model)
	{
		super(model);
		this.init();
	}

	protected void init()
	{
		if (model.getName() == null || model.getName().trim().length() == 0)
		{
			if (Utils.isSendMessage(model))
			{
				model.setName(Messages.getString("I18N_SEND_MSG"));
			} else
			{
				model.setName(Messages.getString("I18N_RECV_MSG"));
			}
		}
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
		g.setLineStyle(Graphics.LINE_SOLID);
		g.setLineWidth(2);

		this.getTargetDecoration().setForegroundColor(this.model.getColor());
		super.outlineShape(g);
	}

	protected void setFigureAttr()
	{
		PolygonDecoration poly = new PolygonDecoration();
		poly.setScale(6, 6);
		this.setTargetDecoration(poly);
		this.setConnectionRouter(new BendpointConnectionRouter());
	}
}