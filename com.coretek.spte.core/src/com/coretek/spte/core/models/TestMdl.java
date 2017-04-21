package com.coretek.spte.core.models;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.coretek.common.template.XMLBean;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.core.image.ImageConstants;
import com.coretek.spte.core.tools.STextPropertyDescriptor;

/**
 * ����ģ��
 * 
 * @author ���Ρ
 * @date 2010-8-20
 */
public class TestMdl extends AbtNode
{

	private static final long serialVersionUID = 8785611659434157441L;

	protected String icon = ImageConstants.STEP_DB_INPUT_ICON;

	protected TestNodeContainerMdl basicRoot = null;

	/**
	 * ���ñ����ģ���С
	 */
	private Dimension headSize = new Dimension(149, 100);

	private transient Entity emulator;

	/**
	 * ��ȡ��������б�
	 * 
	 * @return
	 */
	public Entity getEmulator()
	{
		return emulator;
	}

	public void setEmulator(Entity emulator/* �������ڵ��б� */)
	{
		this.emulator = emulator;
		if (this.emulator != null)
		{
			StringBuilder sb = new StringBuilder();
			if (this.emulator instanceof XMLBean)
			{
				List<Entity> list = this.emulator.getChildren();
				for (Entity en : list)
				{
					String name = (String) en.getFieldValue("name");
					if (null != name)
					{
						sb.append(name);
					}
				}
				this.setName(sb.toString());
			}
		}

	}

	public void initModel()
	{
		if (this.basicRoot != null)
			this.removeChild(this.basicRoot);
		this.basicRoot = initTreeItem();
		Dimension reg = this.basicRoot.refreshRegion().getCopy();
		this.setSize(reg);
		this.addChild(this.basicRoot);
	}

	/**
	 * ����µĽڵ㵽TestNodeContainerMdl��
	 * 
	 * @param index ��ѡ�еĽڵ����ڲ��Խڵ㼯���е�������
	 * @param sum ��Ҫ���node������
	 */
	public void addGrandson(int index, int sum)
	{
		for (int i = 0; i < sum; i++)
		{
			TestNodeMdl node = new TestNodeMdl();
			node.setExpand(TestNodeMdl.ITEM_NOCHILD);
			node.setType(TestNodeMdl.TYPE_COLUMN);
			this.basicRoot.addChild(index, node);
		}
		Dimension reg = this.basicRoot.refreshRegion().getCopy();
		this.setSize(reg);
		this.basicRoot.fireChildenChange(this.basicRoot);
	}

	/**
	 * ��������ģ����Ĭ�ϼ���50���ڵ�
	 * 
	 * @return
	 */
	private TestNodeContainerMdl initTreeItem()
	{
		TestNodeContainerMdl root = new TestNodeContainerMdl();
		if (this instanceof TestToolMdl)
		{
			root.setName("TestTool---Basic Root");
		}
		else
		{
			root.setName("TestedObject---Basic Root");
		}

		for (int i = 0; i < 100; i++)
		{
			TestNodeMdl column = new TestNodeMdl();
			column.setName("column_" + i);
			column.setExpand(TestNodeMdl.ITEM_NOCHILD);
			column.setType(TestNodeMdl.TYPE_COLUMN);
			root.addChild(column);
		}
		return root;
	}

	public void setRootLocation(Point p)
	{
		basicRoot.setLocation(p);
	}

	public TestNodeContainerMdl getRoot()
	{
		return basicRoot;
	}

	public void removeAllConnections()
	{
		this.basicRoot.removeAllConnections();
	}

	public void setLocation(Point p)
	{
		super.setLocation(p);
	}

	protected IPropertyDescriptor[] descriptors = new IPropertyDescriptor[]
	{ new STextPropertyDescriptor(PROP_NAME, "name"), new STextPropertyDescriptor(PROP_DESCRIPTION, "description") };

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		return descriptors;
	}

	public Object getPropertyValue(Object id)
	{
		if (PROP_NAME.equals(id))
			return getName();
		if (PROP_DESCRIPTION.equals(id))
			return getDescription();
		return null;
	}

	public void setPropertyValue(Object id, Object value)
	{
		if (PROP_NAME.equals(id))
			setName((String) value);
		if (PROP_DESCRIPTION.equals(id))
			setDescription((String) value);
	}

	public void setSize(Dimension d)
	{
		if (149 < d.width)
		{
			this.size.width = d.width;
		}
		else
		{
			this.size.width = headSize.width;
		}
		this.size.height = d.height + headSize.height;
		firePropertyChange(PROP_SIZE, null, d);
	}
}