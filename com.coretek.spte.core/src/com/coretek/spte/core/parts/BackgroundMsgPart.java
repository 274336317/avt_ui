package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.figures.PeriodParentMsgFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtCycleMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.tools.ConnMouseMotionListener;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * __________________________________________________________________________________
 * 
 * @Class BackgroundMsgPart.java
 * @Description 背景消息连线布局
 * @Auther MENDY
 * @Date 2016-5-12 下午06:37:28
 */
public class BackgroundMsgPart extends PeriodParentMsgPart
{
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
		String pName = evt.getPropertyName();
		if (SPTEConstants.EVENT_VISIBLE_CHANGED.equals(pName))
		{

		}
	}

	@Override
	public void activate()
	{
		super.activate();
	}

	@Override
	public IFigure createFigure()
	{
		PeriodParentMsgMdl model = (PeriodParentMsgMdl) this.getModel();
		if (model.getName() == null || model.getName().trim().length() == 0)
		{
			model.setName(Messages.getString("I18N_BACKGROUND_MSG"));
		}
		PeriodParentMsgFgr figure = new PeriodParentMsgFgr((PeriodParentMsgMdl) this.getModel());
		figure.addMouseMotionListener(new ConnMouseMotionListener((AbtConnMdl) this.getModel()));

		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
		{
			((AbtCycleMsgMdl) this.getModel()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setDefaultColor(ColorConstants.orange);
		} else
		{
			((PeriodParentMsgMdl) this.getModel()).setDefaultColor(InstanceUtils.getInstance().getMesOrBackgroudDefaultColor());
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setDefaultColor(InstanceUtils.getInstance().getMesOrBackgroudDefaultColor());
			((AbtCycleMsgMdl) this.getModel()).setColor(((AbtCycleMsgMdl) this.getModel()).getDefaultColor());
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setColor(((PeriodParentMsgMdl) this.getModel()).getFixedChild().getDefaultColor());
		}
	}
}
