package com.coretek.spte.core.tools;

import org.eclipse.gef.requests.SimpleFactory;

import com.coretek.spte.core.models.TestMdl;

/**
 * 测试工具/被测对象工厂类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public class LifelineFactory extends SimpleFactory
{

	public String	step_name	= null;

	public LifelineFactory(Class<?> aClass, String name, boolean isTester)
	{
		super(aClass);
		step_name = name;
	}

	public Object getNewObject()
	{
		TestMdl obj = null;
		try
		{
			obj = (TestMdl) TestMdl.class.newInstance();
			if (obj != null)
			{
				if (step_name != null)
				{
					obj.setName(step_name);
				}
				obj.initModel();
			}

		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return obj;
	}
}
