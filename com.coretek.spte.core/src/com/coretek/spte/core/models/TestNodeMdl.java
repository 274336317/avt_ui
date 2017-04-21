package com.coretek.spte.core.models;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 对象节点
 * 
 * @author 孙大巍
 * @date 2010-8-21
 */
public class TestNodeMdl extends AbtNode
{

	private static final long serialVersionUID = 1625166195687972473L;

	private boolean ifroot = false;

	public static final int ITEM_NOCHILD = 0;

	public static final int ITEM_EXPAND = 1;

	public static final int ITEM_COLLAPSED = 2;

	public static final int TYPE_ROOT = 0;

	public static final int TYPE_COLUMN = 2;

	public static final int TYPE_COLUMN2 = 3;

	private int expand = ITEM_EXPAND;

	private int type = TYPE_ROOT;

	/**
	 * TODO 18-1
	 */
	private Dimension region = new Dimension(20, 25);

	private int offset = 0;

	private int depth = 0;

	private boolean ifRefresh = false;

	private TestNodeMdl selected = null;

	protected Dimension fSize = new Dimension(5, 25);

	private boolean isParent;

	private boolean isSelected;

	private TestCaseStatus status = TestCaseStatus.Editing;

	public TestNodeMdl()
	{
	}

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

	public boolean isParent()
	{
		return isParent;
	}

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
		this.firePropertyChange(SPTEConstants.EVENT_PROP_SELECTED, this.isSelected);
	}

	public void setParent(boolean isParent)
	{
		this.isParent = isParent;
	}

	public void setSelected(TestNodeMdl se)
	{
		this.selected = se;
	}

	public TestNodeMdl getSelected()
	{
		return this.selected;
	}

	public boolean getIfRefresh()
	{
		if (this.ifRefresh)
		{
			return true;
		}
		else
		{
			for (int i = 0; i < getChildren().size(); i++)
			{
				TestNodeMdl temp = (TestNodeMdl) getChildren().get(i);
				if (temp.getIfRefresh())
				{
					return true;
				}
			}
		}
		return false;
	}

	public int getExpand()
	{
		return expand;
	}

	public void setExpand(int expand)
	{
		this.expand = expand;
	}

	public boolean isIfroot()
	{
		return ifroot;
	}

	public void setIfroot(boolean ifroot)
	{
		this.ifroot = ifroot;
		if (this.ifroot)
			this.type = TYPE_ROOT;
	}

	public void setOffset(int ost)
	{
		this.offset = ost;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public Dimension refreshRegion()
	{
		int h = this.fSize.height;
		if (this.expand == ITEM_EXPAND)
		{
			for (int i = 0; i < this.getChildren().size(); i++)
			{
				TestNodeMdl ch = (TestNodeMdl) this.getChildren().get(i);
				h += ch.refreshRegion().height;
			}
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
		int rst = 0;
		if (this.expand == ITEM_COLLAPSED || this.getChildren().size() == 0)
		{
			return rst;
		}
		rst++;
		int large = 0;
		if (this.expand == ITEM_EXPAND)
		{
			for (int i = 0; i < this.getChildren().size(); i++)
			{
				TestNodeMdl temp = (TestNodeMdl) this.getChildren().get(i);
				int td = temp.getTotalDepth();
				if (td > large)
				{
					large = td;
				}
			}
		}
		rst += large;
		return rst;
	}

	private Point findPosition(Point p)
	{
		Point rst = new Point();
		if (!this.ifroot)
		{
			rst.x = this.findRoot().getLocation().x + this.offset * this.getDepth();
			TestNodeContainerMdl pa = (TestNodeContainerMdl) this.getParent();
			if (pa.getChildren().indexOf(this) == 0)
			{
				rst.y = pa.getLocation().y + this.fSize.height;
			}
			else
			{
				TestNodeMdl upSibling = (TestNodeMdl) pa.getChildren().get(pa.getChildren().indexOf(this) - 1);
				rst.y = upSibling.getLocation().y + upSibling.getSize().height;
			}
		}
		else
		{
			rst.x = p.x + 2 + 70;
			rst.y = p.y + 30;
		}
		return rst;
	}

	public TestNodeContainerMdl findRoot()
	{
		TestNodeContainerMdl pa = (TestNodeContainerMdl) this.getParent();
		return pa;
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
		return (this.getIncomingConnections());
	}

	public List<AbtConnMdl> getAllOutgoings()
	{
		return (this.getOutgoingConnections());
	}

	@SuppressWarnings("unchecked")
	public List<AbtConnMdl> getShowIncomingConnections()
	{
		if ((this.type == TestNodeMdl.TYPE_COLUMN))
		{
			return this.getIncomingConnections();
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AbtConnMdl> getShowOutgoingConnections()
	{
		if ((this.type == TestNodeMdl.TYPE_COLUMN))
		{
			return this.getOutgoingConnections();
		}
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
		this.getChildren().add(child);
	}

	public TestMdl findStepModel()
	{
		AbtElement step = this.getParent();
		if (step instanceof TestMdl)
		{
			return (TestMdl) step;
		}
		else
		{
			if (step instanceof TestNodeMdl)
			{
				return ((TestNodeMdl) step).findStepModel();
			}
		}
		return null;
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
