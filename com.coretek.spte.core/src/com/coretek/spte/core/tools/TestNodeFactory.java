package com.coretek.spte.core.tools;

import org.eclipse.gef.requests.SimpleFactory;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * 连线节点创建工厂
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class TestNodeFactory extends SimpleFactory
{

	@Override
	public Object getNewObject()
	{
		AbtConnMdl item = (AbtConnMdl) super.getNewObject();
		return item;
	}

	/**
	 * 
	 * @param aClass
	 * @param isTimer true:表明创建的是时间间隔对象，false:表明创建的是消息连线对象
	 */
	public TestNodeFactory(Class<?> aClass, boolean isTimer)
	{
		super(aClass);
	}
}
