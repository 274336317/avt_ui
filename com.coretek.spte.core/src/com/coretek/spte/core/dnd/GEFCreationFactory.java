package com.coretek.spte.core.dnd;

import org.eclipse.gef.requests.CreationFactory;

import com.coretek.common.template.Helper;

/**
 * 消息拖拽时，创建消息模型的工厂类
 * 
 * @author 孙大巍
 * 
 *         2011-4-24
 */
public class GEFCreationFactory implements CreationFactory
{

	private Helper	entity;

	public GEFCreationFactory(Helper entity)
	{
		this.entity = entity;
	}

	public Object getNewObject()
	{
		return entity;
	}

	public Object getObjectType()
	{

		return entity.getClass();
	}
}
