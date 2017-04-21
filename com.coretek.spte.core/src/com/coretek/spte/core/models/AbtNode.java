package com.coretek.spte.core.models;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * 节点基类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public abstract class AbtNode extends AbtElement
{

	private static final long	serialVersionUID	= 5962912085768915256L;

	public static final String	PRO_FIGURE			= "FIGURE";

	public static final String	PROP_LOCATION		= "LOCATION";

	public static final String	PROP_VISIBLE		= "VISIBLE";

	public static final String	PROP_INPUTS			= "INPUTS";

	public static final String	PROP_OUTPUTS		= "OUTPUTS";

	public static final String	PROP_SIZE			= "SIZE";

	protected Dimension			size				= new Dimension(100, 150);

	protected Point				location			= new Point(0, 0);

	protected boolean			visible				= true;

	protected List<AbtConnMdl>	outputs				= new ArrayList<AbtConnMdl>();

	protected List<AbtConnMdl>	inputs				= new ArrayList<AbtConnMdl>();

	public void addInput(AbtConnMdl connection)
	{
		this.inputs.add(connection);
		fireConnectionChange(PROP_INPUTS, connection);
	}

	public void addOutput(AbtConnMdl connection)
	{
		this.outputs.add(connection);
		fireConnectionChange(PROP_OUTPUTS, connection);
	}

	public List<AbtConnMdl> getIncomingConnections()
	{
		return this.inputs;
	}

	public List<AbtConnMdl> getOutgoingConnections()
	{
		return this.outputs;
	}

	public void removeInput(AbtConnMdl connection)
	{
		this.inputs.remove(connection);
		fireConnectionChange(PROP_INPUTS, connection);
	}

	public void removeOutput(AbtConnMdl connection)
	{
		this.outputs.remove(connection);
		fireConnectionChange(PROP_OUTPUTS, connection);
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		if (this.visible == visible)
		{
			return;
		}
		this.visible = visible;
		firePropertyChange(PROP_VISIBLE, null, Boolean.valueOf(visible));
	}

	public void setLocation(Point p)
	{
		if (this.location.equals(p))
		{
			return;
		}
		this.location = p;
		firePropertyChange(PROP_LOCATION, null, p);
	}

	public Point getLocation()
	{
		return location;
	}

	public void setSize(Dimension d)
	{
		if (this.size.equals(d))
		{
			return;
		}
		this.size = d;
		firePropertyChange(PROP_SIZE, null, d);
	}

	public Dimension getSize()
	{
		return size;
	}

	public void fireConnectionChange(String prop, Object child)
	{
		listeners.firePropertyChange(prop, null, child);
	}

}
