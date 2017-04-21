package com.coretek.spte.core.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.coretek.common.utils.StringUtils;

/**
 * 模型基类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 */
public abstract class AbtElement implements Cloneable, Serializable, IPropertySource
{

	private static final long serialVersionUID = 9221190578636776587L;

	public static final String PROP_CHANGE = "PROP_CHANGE";

	public static final String PROP_DESCRIPTION = "DESCRIPTION";

	public static final String PROP_NAME = "NAME";

	public static final String PRO_CHILD = "CHILD";

	protected String name = "";

	protected String description = "";

	protected List<AbtElement> children = new ArrayList<AbtElement>();

	protected AbtElement parent = null;

	protected RootContainerMdl rootModel = null;

	protected String model_name = "";

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	protected String _mesType = "";

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue)
	{
		listeners.firePropertyChange(prop, old, newValue);
	}

	protected void firePropertyChange(String prop, Object newValue)
	{
		listeners.firePropertyChange(prop, null, newValue);
	}

	public void fireChildenChange(AbtElement child)
	{
		this.listeners.firePropertyChange(PRO_CHILD, null, child);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		listeners = new PropertyChangeSupport(this);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		if (StringUtils.isNotNull(this.name) && this.name.equals(name))
		{
			return;
		}
		this.name = name;
		firePropertyChange(PROP_NAME, null, name);
	}

	public void setMesType(String _mesType)
	{
		this._mesType = _mesType;
	}

	public String getMesType()
	{
		return _mesType;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		if (this.description.equals(description))
		{
			return;
		}
		this.description = description;
	}

	public Object getEditableValue()
	{
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		return null;
	}

	public Object getPropertyValue(Object id)
	{
		return null;
	}

	public void setPropertyValue(Object id, Object value)
	{
	}

	public boolean isPropertySet(Object id)
	{
		return true;
	}

	public void resetPropertyValue(Object id)
	{

	}

	/**
	 * @return 返回 parent.
	 */
	public AbtElement getParent()
	{
		return parent;
	}

	/**
	 * @param parent 设置 parent
	 */
	public void setParent(AbtElement parent)
	{
		this.parent = parent;
	}

	/**
	 * @return 返回 children.
	 */
	public List<AbtElement> getChildren()
	{
		if (children == null)
			children = new ArrayList<AbtElement>();
		return children;
	}

	/**
	 * @param children 设置 children
	 */
	public void setChildren(List<AbtElement> children)
	{
		this.children = children;
	}

	public void addChild(AbtElement child)
	{
		addChild(-1, child);
	}

	public void addChild(int index, AbtElement child)
	{
		if (index == -1)
		{
			getChildren().add(child);
		}
		else
		{
			getChildren().add(index, child);
		}
		child.setParent(this);
		child.setRootModel(this.rootModel);
		this.fireChildenChange(child);
	}

	public void removeChild(AbtElement child)
	{
		child.setParent(null);
		getChildren().remove(child);
		this.fireChildenChange(child);
	}

	public RootContainerMdl getFtransmodel()
	{
		return rootModel;
	}

	public void setRootModel(RootContainerMdl root)
	{
		this.rootModel = root;
	}

	public String getModelName()
	{
		return model_name;
	}
}
