package com.coretek.spte.core.commands;

import java.util.List;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 画出被拖放到编辑器的周期消息
 * 
 * @author 孙大巍
 * 
 *         2011-3-9
 */
public class DropPeriodCreationCmd extends AbstractCreationConnCmd
{

	protected PeriodParentMsgMdl	connection;

	protected PeriodChildMsgMdl		childConnection;

	/**
	 * 父连线的连接源点
	 */
	protected TestNodeMdl			source;

	/**
	 * 子连线的连接源
	 */
	protected TestNodeMdl			childSource;

	/**
	 * 父连线的连接目标点
	 */
	protected TestNodeMdl			target;

	/**
	 * 子连线的连接目标
	 */
	protected TestNodeMdl			childTarget;
	protected String				versionId;			// icd文件的版本号

	public DropPeriodCreationCmd(String versionId)
	{
		this.versionId = versionId;
	}

	public PeriodParentMsgMdl getConnection()
	{
		return connection;
	}

	public void setSource(TestNodeMdl source)
	{
		this.source = source;
		this.childSource = (TestNodeMdl) source.getParent().getChildren().get(source.getParent().getChildren().indexOf(this.source) + 2);
	}

	public void setConnection(PeriodParentMsgMdl connection)
	{
		this.connection = connection;
	}

	public void setTarget(TestNodeMdl target)
	{
		this.target = target;
		this.childTarget = (TestNodeMdl) target.getParent().getChildren().get(target.getParent().getChildren().indexOf(this.source) + 2);

	}

	public void execute()
	{
		PeriodParentMsgMdl conn = new PeriodParentMsgMdl(this.source, this.target);
		conn.setTcMsg(this.connection.getTcMsg());
		conn.setName(this.connection.getName() + this.connection.getMesType()); // 消息消息名称
		conn.setMesType(this.connection.getMesType());

		this.childTarget = (TestNodeMdl) target.getParent().getChildren().get(target.getParent().getChildren().indexOf(this.target) + 2);
		this.childConnection = new PeriodChildMsgMdl(this.childSource, this.childTarget);

		this.childConnection.setName(conn.getName());
		this.source.setMesType(this.connection.getMesType());
		this.connection.setTarget(this.target);
		this.connection.setSource(this.source);
		this.childConnection.setParent(conn);
		conn.setFixedChild(this.childConnection);
		this.childConnection.setTcMsg(this.getConnection().getTcMsg());

		this.connection = conn;
	}

	public String getLabel()
	{
		return "拖放创建周期消息";
	}

	public void redo()
	{
		this.source.addOutput(this.connection);
		this.target.addInput(this.connection);
		this.childSource.addOutput(this.childConnection);
		this.childTarget.addInput(this.childConnection);
	}

	public void undo()
	{
		this.source.removeOutput(this.connection);
		this.target.removeInput(this.connection);
		this.childSource.removeOutput(this.childConnection);
		this.childTarget.removeInput(this.childConnection);
	}

	@Override
	public boolean canExecute()
	{
		// 禁止在同一个节点创建多条周期连线
		List<AbtConnMdl> outGoing = this.source.getOutgoingConnections();
		if (outGoing != null && outGoing.size() > 0)
		{
			return false;
		}

		if (this.source.getIncomingConnections() != null && this.source.getIncomingConnections().size() > 0)
		{
			return false;
		}
		if (this.target != null)
		{
			outGoing = this.target.getOutgoingConnections();

			if (outGoing != null && outGoing.size() > 0)
			{
				return false;
			}

			if (this.target.getIncomingConnections() != null && this.target.getIncomingConnections().size() > 0)
			{
				return false;
			}

			if (source.getParent() == target.getParent())
			{
				return false;
			}
		}
		// 不能将连接线画在时间间隔之间
		if (Utils.isBetweenTimeredConnections(this.source))
		{
			return false;
		}

		// 禁止将周期连线创建在已经包含了其它连线的item上
		if (Utils.hasConnection(this.source))
		{
			return false;
		}

		// 禁止将周期消息连线创建在相邻item拥有消息连线上的item上
		if (Utils.hasConnection(Utils.getNextNode(this.source)))
		{
			return false;
		}

		if (Utils.hasConnection(Utils.getNextNode(Utils.getNextNode(this.source))))
		{
			return false;
		}
		return true;
	}
}
