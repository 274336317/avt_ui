package com.coretek.spte.core.dnd;

import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

/**
 * 为了方便在创建command的时候明确被测对象的节点号和测试工具的节点而创建这个类
 * 
 * @author 孙大巍
 * 
 *         2011-4-24
 */
public class DNDCreateRequest extends CreateRequest
{

	@Override
	public CreationFactory getFactory()
	{
		return super.getFactory();
	}
}
