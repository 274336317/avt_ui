package com.coretek.spte.core.tools;

import org.eclipse.gef.requests.SimpleFactory;

import com.coretek.spte.core.models.AbtConnMdl;

/**
 * ���߽ڵ㴴������
 * 
 * @author ���Ρ
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
	 * @param isTimer true:������������ʱ��������false:��������������Ϣ���߶���
	 */
	public TestNodeFactory(Class<?> aClass, boolean isTimer)
	{
		super(aClass);
	}
}
