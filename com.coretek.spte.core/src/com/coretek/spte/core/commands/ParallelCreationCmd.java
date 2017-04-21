package com.coretek.spte.core.commands;

/**
 * 创建并行消息命令
 * 
 * @author 孙大巍
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
		return "创建并行消息";
	}
}
