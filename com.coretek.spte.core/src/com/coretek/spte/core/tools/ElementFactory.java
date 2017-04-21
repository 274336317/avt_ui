package com.coretek.spte.core.tools;

import org.eclipse.gef.requests.CreationFactory;

/**
 * 
 * @author Ëï´óÎ¡
 * @date 2010-9-1
 * 
 */
public class ElementFactory implements CreationFactory
{

	private Object	template;

	public ElementFactory(Object template)
	{
		this.template = template;
	}

	@SuppressWarnings("unchecked")
	public Object getNewObject()
	{
		return template;
	}

	public Object getObjectType()
	{
		return template.getClass();
	}
}