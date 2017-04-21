package com.coretek.spte.core.request;

import org.eclipse.gef.Request;

import com.coretek.spte.core.models.TestMdl;

public class ChangeTestedObjectRequest extends Request
{

	private TestMdl	mode;

	private String	name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public TestMdl getNode()
	{
		return mode;
	}

	public void setNode(TestMdl node)
	{
		this.mode = node;
	}

	public ChangeTestedObjectRequest(Object type, TestMdl mode, String name)
	{
		super(type);
		this.mode = mode;
		this.name = name;
	}

}
