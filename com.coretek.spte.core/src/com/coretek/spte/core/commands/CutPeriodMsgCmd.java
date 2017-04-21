package com.coretek.spte.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;

/**
 * ����������Ϣ���� ʵ��ԭ���Ƚ���������ɾ����Ȼ���ٽ���ɾ����������Ϣģ�͸��Ʒ����������
 * 
 * @author ���Ρ
 * @date 2010-11-23
 * 
 */
public class CutPeriodMsgCmd extends DelPeriodMsgCmd
{

	@Override
	public void execute()
	{
		super.execute();
		List<AbtConnMdl> copiedModels = new ArrayList<AbtConnMdl>(1);
		try
		{
			PeriodParentMsgMdl copiedModel = (PeriodParentMsgMdl) this.fixedParent.clone();
			copiedModel.setSource(this.fixedParent.getSource());
			copiedModel.setTarget(this.fixedParent.getTarget());
			copiedModel.setFixedChild((PeriodChildMsgMdl) this.fixedParent.getFixedChild().clone());
			copiedModel.getFixedChild().setParent(copiedModel);
			copiedModels.add(copiedModel);

		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		this.fixedParent.getTarget().removeInput(this.fixedParent);
		this.fixedParent.getSource().removeOutput(this.fixedParent);

		Clipboard.getDefault().setContents(copiedModels);
	}
}
