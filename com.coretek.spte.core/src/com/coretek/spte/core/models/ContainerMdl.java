package com.coretek.spte.core.models;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.tools.STextPropertyDescriptor;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * Ê±ÐòÍ¼ÈÝÆ÷
 * 
 * @author Ëï´óÎ¡
 * @date 2010-8-21
 * 
 */
public class ContainerMdl extends AbtNode
{

	private static final long		serialVersionUID	= -1253242308001873459L;

	protected IPropertyDescriptor[]	descriptors			= new IPropertyDescriptor[] { new STextPropertyDescriptor(PROP_NAME, "name"), new STextPropertyDescriptor(PROP_DESCRIPTION, "description") };

	public ContainerMdl()
	{
		initModel();
	}

	private void initModel()
	{
		setSize(new Dimension(800, SPTEConstants.INIT_LIFELINE_HEIGHT));
		setName(Messages.getString("I18N_EDITING_STATUS")); //±à¼­×´Ì¬
	}

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
}