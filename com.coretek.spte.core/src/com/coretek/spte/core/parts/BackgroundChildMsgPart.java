package com.coretek.spte.core.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;

import com.coretek.spte.core.models.AbtCycleMsgMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * ±³¾°ÏûÏ¢
 * 
 * @author Ëï´óÎ¡
 * 
 *         2011-3-30
 */
public class BackgroundChildMsgPart extends PeriodChildMsgPart
{

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);

		if (value != EditPart.SELECTED_NONE)
		{
			((AbtCycleMsgMdl) this.getModel()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setDefaultColor(ColorConstants.orange);
			((AbtCycleMsgMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
		} else
		{
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setDefaultColor(ColorConstants.blue);
			((AbtCycleMsgMdl) this.getModel()).setDefaultColor(ColorConstants.blue);
			((AbtCycleMsgMdl) this.getModel()).setColor(((AbtCycleMsgMdl) this.getModel()).getDefaultColor());
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setColor(((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).getDefaultColor());
		}
	}
}