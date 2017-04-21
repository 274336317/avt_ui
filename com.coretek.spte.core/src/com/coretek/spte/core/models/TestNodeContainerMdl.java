package com.coretek.spte.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 测试节点的容器模型
 * 
 * @author 孙大巍
 */
public class TestNodeContainerMdl extends AbtNode
{

	private static final long serialVersionUID = 545493766429592572L;

	/**
	 * 区域大小 h=25,w=20
	 */
	private Dimension region = new Dimension(20, 25);

	private int offset = 0;

	private int depth = 0;

	/**
	 * h=25,w=5
	 */
	protected Dimension fSize = new Dimension(5, 25);

	private TestCaseStatus status = TestCaseStatus.Editing;

	public TestCaseStatus getStatus()
	{
		return status;
	}

	public void setStatus(TestCaseStatus status)
	{
		if (this.status != status)
		{
			this.status = status;
			this.firePropertyChange(SPTEConstants.EVENT_STATUS_CHANGED, this.status);
		}
	}

	public void setOffset(int ost)
	{
		this.offset = ost;
	}

	public Dimension refreshRegion()
	{
		int h = this.fSize.height;
		for (int i = 0; i < this.getChildren().size(); i++)
		{
			TestNodeMdl ch = (TestNodeMdl) this.getChildren().get(i);
			h += ch.refreshRegion().height;
		}
		this.region.height = h;
		int width = this.fSize.width;
		width += this.getTotalDepth() * this.offset;
		this.region.width = width;
		this.size = this.region.getCopy();
		return this.getSize();
	}

	public int getTotalDepth()
	{
		int rst = 1;
		int large = 0;
		for (int i = 0; i < this.getChildren().size(); i++)
		{
			TestNodeMdl temp = (TestNodeMdl) this.getChildren().get(i);
			int td = temp.getTotalDepth();
			if (td > large)
			{
				large = td;
			}
		}
		rst += large;
		return rst;
	}

	private Point findPosition(Point p)
	{
		Point rst = new Point();
		rst.x = p.x + 2 + 70; // 生命线x
		rst.y = p.y + 15; // 生命线y
		return rst;
	}

	public int getDepth()
	{
		return this.depth;
	}

	public void setDepth(int d)
	{
		this.depth = d;
		for (int i = 0; i < this.getChildren().size(); i++)
		{
			TestNodeMdl temp = (TestNodeMdl) this.getChildren().get(i);
			temp.setDepth(this.depth + 1);
		}
	}

	public void setLocation(Point p)
	{
		Dimension rgn = this.refreshRegion();
		this.location = this.findPosition(p);
		this.setSize(rgn);
		if (this.getChildren() != null && this.getChildren().size() > 0)
		{
			Point temp = this.getLocation().getCopy();
			for (int i = 0; i < this.getChildren().size(); i++)
			{
				TestNodeMdl ti = (TestNodeMdl) this.getChildren().get(i);
				ti.setLocation(temp);
				temp = ti.getLocation().getCopy();
			}
		}
	}

	public List<AbtConnMdl> getAllIncomings()
	{
		List<AbtConnMdl> l = new ArrayList<AbtConnMdl>();
		for (Iterator<AbtElement> iter = this.getChildren().iterator(); iter.hasNext();)
		{
			TestNodeMdl child = (TestNodeMdl) iter.next();
			l.addAll(child.getAllIncomings());
		}

		return l;
	}

	public List<AbtConnMdl> getAllOutgoings()
	{
		List<AbtConnMdl> list = new ArrayList<AbtConnMdl>();
		for (Iterator<AbtElement> iter = this.getChildren().iterator(); iter.hasNext();)
		{
			TestNodeMdl child = (TestNodeMdl) iter.next();
			list.addAll(child.getAllOutgoings());
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AbtConnMdl> getShowIncomingConnections()
	{
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	public List<AbtConnMdl> getShowOutgoingConnections()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public void addChild(AbtElement child)
	{
		super.addChild(-1, child);
	}

	@Override
	public void addChild(int index, AbtElement child)
	{
		child.setParent(this);
		child.setRootModel(this.rootModel);
		((TestNodeMdl) child).setDepth(this.depth + 1);
		if (index < 0)
		{
			this.getChildren().add(child);
		}
		else
		{
			this.getChildren().add(index, child);
		}
	}

	public void removeAllConnections()
	{
		List<AbtConnMdl> incomings = this.getIncomingConnections();
		List<AbtConnMdl> outgoing = this.getOutgoingConnections();
		AbtConnMdl con = null;
		TestNodeMdl item = null;
		if (incomings != null && incomings.size() > 0)
		{
			for (Iterator<AbtConnMdl> iter = incomings.iterator(); iter.hasNext();)
			{
				con = (AbtConnMdl) iter.next();
				item = (TestNodeMdl) con.getSource();
				item.removeOutput(con);
			}
		}
		if (outgoing != null && outgoing.size() > 0)
		{
			for (Iterator<AbtConnMdl> iter = outgoing.iterator(); iter.hasNext();)
			{
				con = (AbtConnMdl) iter.next();
				item = (TestNodeMdl) con.getTarget();
				item.removeInput(con);
			}
		}
		if (this.children != null && this.children.size() > 0)
		{
			for (Iterator<AbtElement> iter = this.children.iterator(); iter.hasNext();)
			{
				TestNodeMdl child = (TestNodeMdl) iter.next();
				child.removeAllConnections();
			}
		}
	}

	public Object getPropertyValue(Object id)
	{
		if (PROP_NAME.equals(id))
			return getName();

		return null;
	}

	public void setPropertyValue(Object id, Object value)
	{
		if (PROP_NAME.equals(id))
			setName((String) value);
	}

	public boolean isPropertySet(Object id)
	{
		return true;
	}

	public void resetPropertyValue(Object id)
	{

	}
}