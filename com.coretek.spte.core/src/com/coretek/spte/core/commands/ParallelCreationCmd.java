package com.coretek.spte.core.commands;

/**
 * ����������Ϣ����
 * 
 * @author ���Ρ
 * 
 */
public class ParallelCreationCmd extends MsgCreationCmd
{

	public ParallelCreationCmd()
	{
		super();
		this.creationMessage = new ParallelCreation();
	}

	@Override
	public String getLabel()
	{
		return "����������Ϣ";
	}
}
