package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.MsgConnMdl;

/**
 * 创建消息连线的父类，方便统计出测试工具中包含的节点Id
 * 
 * @author 孙大巍
 * 
 *         2011-3-29
 */
public abstract class ConnCommand extends Command
{

	/**
	 * 更新测试工具的Id集合，以方便显示测试工具的Id
	 * 
	 * @param id
	 * @param add 如果为true，则为添加操作，为false，则为删除操作
	 */
	public void updateIdSet(MsgConnMdl model, String id, boolean add)
	{

	}
}