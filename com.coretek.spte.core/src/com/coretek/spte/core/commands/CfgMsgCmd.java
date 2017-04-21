package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.Message;

/**
 * ������Ϣ
 * 
 * @author ���Ρ
 * @date 2010-12-23
 */
public class CfgMsgCmd extends Command
{
	private MsgConnMdl	model;

	private SPTEMsg		newMessage;

	private SPTEMsg		oldMessage;

	public MsgConnMdl getModel()
	{
		return model;
	}

	public void setModel(MsgConnMdl model)
	{
		this.model = model;
	}

	public SPTEMsg getResultMessage()
	{
		return newMessage;
	}

	public void setResultMessage(SPTEMsg resultMessage)
	{
		this.newMessage = resultMessage;
	}

	@Override
	public void execute()
	{
		this.oldMessage = model.getTcMsg();
		if (this.oldMessage != null)
		{
			String uuid = this.oldMessage.getMsg().getUuid();
			try
			{
				Message oldMsg = (Message) this.oldMessage.getMsg().clone();
				oldMsg.setUuid(uuid);
				this.oldMessage = new SPTEMsg(oldMsg, this.oldMessage.getICDMsg());
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}

		model.setTcMsg(this.newMessage);

		if (this.model instanceof ParallelMsgMdl)
		{
			this.newMessage.getMsg().setParallel(true);
		}
	}

	@Override
	public void redo()
	{
		model.setTcMsg(this.newMessage);
		model.setName(this.newMessage.getMsg().getName());
		if (this.model instanceof ParallelMsgMdl)
		{
			this.newMessage.getMsg().setParallel(true);
		}
	}

	@Override
	public void undo()
	{
		model.setTcMsg(this.oldMessage);
		if (this.oldMessage != null)
			model.setName(this.oldMessage.getMsg().getName());
		else
		{
			if (Utils.isSendMessage(model))
			{
				if (model instanceof PeriodParentMsgMdl)
				{
					model.setName("���ڷ�����Ϣ");
				} else if (model instanceof BackgroundMsgMdl)
				{
					model.setName("�������ڷ�����Ϣ");
				} else if (model instanceof ParallelMsgMdl)
				{
					model.setName("���з�����Ϣ");
				} else
				{
					model.setName("������Ϣ");
				}

			} else if (Utils.isRecvMessage(model))
			{
				if (model instanceof PeriodParentMsgMdl)
				{
					model.setName("���ڽ�����Ϣ");
				} else if (model instanceof BackgroundMsgMdl)
				{
					model.setName("�������ڽ�����Ϣ");
				} else if (model instanceof ParallelMsgMdl)
				{
					model.setName("���н�����Ϣ");
				} else
				{
					model.setName("������Ϣ");
				}
			}
		}
	}

	@Override
	public String getLabel()
	{
		return Messages.getString("I18N_SET_MSG");
	}
}