package com.coretek.spte.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 剪贴消息连接线 实现原理：先删除消息连线，然后再复制消息连线放入剪贴板中
 * 
 * @author 孙大巍
 * @date 2010-11-22
 * 
 */
public class CutMsgCmd extends DelCmd
{

	private List<AbtConnMdl>	models	= new ArrayList<AbtConnMdl>();

	private AbtNode				source;

	private AbtNode				target;

	private AbtConnMdl			connection;

	public boolean addElement(AbtConnMdl model)
	{
		if (models.contains(model))
		{
			return false;
		}
		return models.add(model);
	}

	public void setConnection(AbtConnMdl connection)
	{
		this.connection = connection;
		if (connection instanceof MsgConnMdl)
		{
			this.setUpIntervalConnection(Utils.getIncomingInterval(Utils.getTestedLineItem(connection)));
			this.setDownIntervalConnection(Utils.getOutcomingInterval(Utils.getTestedLineItem(connection)));
		}
	}

	public void setSource(AbtNode source)
	{
		this.source = source;
	}

	public void setTarget(AbtNode target)
	{
		this.target = target;
	}

	@Override
	public void execute()
	{
		super.execute();

		List<AbtConnMdl> copiedModels = new ArrayList<AbtConnMdl>(models.size());
		try
		{
			AbtConnMdl copiedModel = (AbtConnMdl) this.connection.clone();
			copiedModel.setSource(this.connection.getSource());
			copiedModel.setTarget(this.connection.getTarget());
			copiedModels.add(copiedModel);
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		this.connection.getTarget().removeInput(this.connection);
		this.connection.getSource().removeOutput(this.connection);

		Clipboard.getDefault().setContents(copiedModels);
	}

	public String getLabel()
	{
		return "";
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		super.undo();
		connection.setSource(source);
		connection.setTarget(target);
		source.addOutput(connection);
		target.addInput(connection);

	}

	@Override
	public boolean canUndo()
	{
		return super.canUndo();
	}
}
