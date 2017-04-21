package com.coretek.spte.core.figures;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.util.Utils;

/**
 * ������Ϣ
 * 
 * @author ���Ρ
 * 
 */
public class ParallelFgr extends MsgFgr
{

	public ParallelFgr(AbtConnMdl model)
	{
		super(model);
	}

	@Override
	protected void init()
	{
		if (model.getName() == null || model.getName().trim().length() == 0)
		{
			if (Utils.isSendMessage(model))
			{
				model.setName(Messages.getString("I18N_PARALLEL_SEND_MSG"));
			} else
			{
				model.setName(Messages.getString("I18N_PARALLEL_RECV_MSG"));
			}
		}
		this.add(new MidpointOffsetLocator(this, 0));
		setFigureAttr();
	}

}
