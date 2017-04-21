package com.coretek.spte.core.dnd;

import org.eclipse.gef.requests.CreationFactory;

import com.coretek.common.template.Helper;

/**
 * ��Ϣ��קʱ��������Ϣģ�͵Ĺ�����
 * 
 * @author ���Ρ
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
