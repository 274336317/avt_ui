package com.coretek.spte.core.request;

import org.eclipse.gef.Request;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 选择消息后，发送request
 * 
 * @author lifs
 * @date 2010-08-22
 * 
 */
public class ChangeLineNameRequest extends Request
{

	private AbtConnMdl	mode;

	private String		name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public AbtConnMdl getNode()
	{
		return mode;
	}

	public void setNode(AbtConnMdl node)
	{
		this.mode = node;
	}

	public ChangeLineNameRequest()
	{

	}

	public ChangeLineNameRequest(Object type, AbtConnMdl mode, String name)
	{
		super(type);
		this.mode = mode;
		this.name = name;
	}
}
