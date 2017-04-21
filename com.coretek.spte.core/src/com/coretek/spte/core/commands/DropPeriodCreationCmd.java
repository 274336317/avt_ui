package com.coretek.spte.core.commands;

import java.util.List;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.Utils;

/**
 * �������Ϸŵ��༭����������Ϣ
 * 
 * @author ���Ρ
 * 
 *         2011-3-9
 */
public class DropPeriodCreationCmd extends AbstractCreationConnCmd
{

	protected PeriodParentMsgMdl	connection;

	protected PeriodChildMsgMdl		childConnection;

	/**
	 * �����ߵ�����Դ��
	 */
	protected TestNodeMdl			source;

	/**
	 * �����ߵ�����Դ
	 */
	protected TestNodeMdl			childSource;

	/**
	 * �����ߵ�����Ŀ���
	 */
	protected TestNodeMdl			target;

	/**
	 * �����ߵ�����Ŀ��
	 */
	protected TestNodeMdl			childTarget;
	protected String				versionId;			// icd�ļ��İ汾��

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
		conn.setName(this.connection.getName() + this.connection.getMesType()); // ��Ϣ��Ϣ����
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
		return "�ϷŴ���������Ϣ";
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
		// ��ֹ��ͬһ���ڵ㴴��������������
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
		// ���ܽ������߻���ʱ����֮��
		if (Utils.isBetweenTimeredConnections(this.source))
		{
			return false;
		}

		// ��ֹ���������ߴ������Ѿ��������������ߵ�item��
		if (Utils.hasConnection(this.source))
		{
			return false;
		}

		// ��ֹ��������Ϣ���ߴ���������itemӵ����Ϣ�����ϵ�item��
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
